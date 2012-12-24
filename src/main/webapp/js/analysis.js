var flexTBR,flexTBE;
$(document).ready(function() {
    var option = {
        height: 350,
        colModel: [
                { display: 'Package', name: 'package', width: 140, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Class', name: 'class', width: 120, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Method/Static', name: 'method', width: 130, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Location', name: 'location', width: 150, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Code', name: 'code', width: 30, sortable: true,sorttype:'ascii', align: 'left' },
    	        { display: 'Rule', name: 'rule', width: 210, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Priority', name: 'priority', width: 40, sortable: true,sorttype:'num', align: 'left' }
		],
		buttons: [
	              { name: 'Ignore', displayname: "Ignore",  onpress: toolbarItem_onclick },
	    ],
		url:"Analysis.do?method=reportlist",
		usepager: true,
		useRp: true,
		rp: 12, // results per page
        rpOptions: [12, 20, 30, 50, 100], //chooses of results per page
        onAddRow:onAddRowData,
        showCheckbox: true,
        striped:false,
        autoload:false
    };
    flexTBR=$("#reportTB").flexigrid(option);
    flexTBR.flexToggleCol(4,false);//hide the 'Code' column
    
    option = {
        height: 100,
        colModel: [
           { display: 'file', name: 'file', width: 450, sortable: true,sorttype:'ascii', align: 'left' },
           { display: 'error', name: 'error', width: 450, sortable: true,sorttype:'ascii', align: 'left' }
        ],
        url:"Analysis.do?method=errorlist",
        usepager: true,
		useRp: true,
		rp: 12,
        rpOptions: [12, 20, 30, 50, 100],
        autoload:false,
        onAddRow:onAddErrorRowData
    };
    flexTBE=$("#errorTB").flexigrid(option);
});
/**toolbar function*/
function toolbarItem_onclick(cmd, grid) {
   if (cmd == "Ignore") {
    	IgnoreReports(flexTBR.flexGetCheckedRows());
    }
}
/**check data on add*/
function onAddRowData(row){
	row.cell[4]=htm2specil(row.cell[4]);//code column
	for(var i=0;i<row.cell.length;i++){
			row.cell[i]=safeDecodeURI(row.cell[i]);//decode
	}
	row.cell[3]="<a href='javascript:;' onclick='viewReportById("+row.id+")'>"+row.cell[3]+"</a>";
	row.cell[5]="<a href='"+row.cell[7]+"' target='_blank'>"+row.cell[5]+"</a>";
	row.style="color:"+getPriorityColor(row.cell[6]);
	return row;
}
function getPriorityColor(priority){
	priority=parseInt(priority);
	switch(priority){
	case 1:return "red";
	case 2:return "blue";
	case 3:return "green";
	case 4:return "skyblue";
	case 5:return "gray";
	}
	return "#000";
}
function onAddErrorRowData(row){
	for(var i=0;i<row.cell.length;i++){
		row.cell[i]=safeDecodeURI(row.cell[i]);//解码
	}
	return row;
}
/**
 * delete reports
 * */
function IgnoreReports(rows){
	if(!rows || !rows.length){
		alert("Please select one report or more ones to ignore.");
		return;
	}
	var strReports="<table cellpadding='2' cellspacing='2' style='border: dotted 1px;padding: 5px;'>";
	var strIds="";
	var ids=[];
	for(var i=0;i<rows.length;i++){
		strReports+="<tr><td>"+rows[i].cell[0]+"</td><td>"+rows[i].cell[1]+"</td><td>"+rows[i].cell[3]+"</td><td>"+rows[i].cell[5]+"</td>";
		strIds+=","+rows[i].id;
		ids.push(rows[i].id);
	}
	strReports+="</table>";
	
	artDialog({
		title:"Question",
		content:"Are you sure to ignore selected report(s) ?<br>"+strReports,
		button: [
	        {
	            name: 'OK',
	            callback: function(){
	    			$.post("Analysis.do",{"method":"RemoveReport","ids":strIds},function(result){
	    				if(result.status){
	    					flexTBR.flexDelRowsByIds(ids);
	    					artDialog({title:"Ignore success",content:result.info,icon:"succeed",time:2});
	    				}else{
	    					artDialog({title:"Ignore fail",content:result.info,icon:"error"});
	    				}
	    			},"json");
	    		}
	        },
	        {
	            name: 'Cancel'
	        }
	    ],
		lock:true,
		icon:"question"
	});
}
/**
 * view report
 * */
function viewReportById(id){
	var rows=flexTBR.flexGetRowsByIds([id]);
	viewReport(rows[0].cell);
}
function viewReport(cell){
	$("#viewPackage").html(cell[0]);
	$("#viewClass").html(cell[1]);
	$("#viewMethod").html(cell[2]);
	$("#viewLocation").html(cell[3]);
	$("#viewCode").html(tab2space(rn2br(cell[4])));
	$("#viewRule").html(cell[5]);
	$("#viewPriority").html(cell[6]);
	artDialog({content:ID('viewReport'),title:'View Report',lock:true,id:'viewReport'});
}
/////////////////////////////////////////////////////////////
//Constant var of analysis step
var STEP_START=0;//starting
var STEP_LOGINING=1;//svn logining
var STEP_LOGINOK=2;//svn login success
var STEP_CHECKOUTING=3;//svn checking out
var STEP_CHECKOUTOK=4;//svn check out success
var STEP_PMDING=5;//pmd proccessing
var STEP_PMDOK=6;//pmd process success
var STEP_SUCCESSEND=7;//analysis success
var STEP_FAILEND=8;//analysis fail
var steps=['Starting','Svn logining','Svn login ok','Svn checking out',
           'Svn check out ok','Pmd analysing','Pmd analyse ok','Code analyse ok','Code analyse fail'];

var TimerID=null;
/**
 * start analysis
 * */
function Analysis(){
	var data={method:"work"};
	data.url=$("#url").val();
	data.user=$("#user").val();
	data.pwd=$("#pwd").val();
	data.rule=$("#rule").val();
	$.post("Analysis.do",data,function(result){
		if(result.status){
			IntervalGetStep();
		}else{//error
			alert(result.info);
			EndIntervalGetStep();
		}
	},"json");
	StartIntervalGetStep();
	
	$("#codeTitle").show();
	$("#codeUrl").html($("#url").val());
	$("#codeUser").html($("#user").val());
	$("#login").remove();
	$("#reportArea").show();
	$("#reportArea").mask("Analysising...");
}
/**
 * start the interval getting analysis step
 * */
function StartIntervalGetStep(interval){
	if(typeof(interval)!="number")interval=2000;
	if(TimerID)clearInterval(TimerID);
	TimerID=setInterval(IntervalGetStep,interval);
}
/**
 * stop the interval getting analysis step
 * */
function EndIntervalGetStep(){
	if(TimerID)clearInterval(TimerID);
	TimerID=0;
}

/**
 * interval get analysis step
 */
function IntervalGetStep(){
	$.post("Analysis.do",{"method":"step"},function(result){
		if(result.status){
			$("#reportArea").mask(steps[result.data.step]);
			if(result.data.step==STEP_SUCCESSEND || result.data.step==STEP_FAILEND){
				$("#reportArea").unmask();
				flexTBR.flexReload();
				flexTBE.flexReload();
				EndIntervalGetStep();
				flexTBR.flexToggleCol(4,true);
				flexTBR.flexToggleCol(4,false);
			}
		}else{
			EndIntervalGetStep();
			$("#reportArea").unmask();
			alert(result.info);
		}
	},"json");
}
/**
 * reabalysis
 */
function ReAnalysis(ask){
	if(ask!=false && !confirm("Do you want to go back?"))return;
	$.post("Analysis.do",{"method":"ClearReport"},function(result){
		document.location.reload();
	});
}
/**
 *get ruleset files list
 */
function getRulesets(){
	$.post("pattern.do",{method:'rulesets'},function(result){
		if(result.status){
			var rules=result.data.rules;
			var descs=result.data.desc;
			var desc,htm="";
			for(var i=0;i<rules.length;i++){
				desc=rules[i];
				for(var j=0;j<descs.length;j++){
					if(descs[j].f==rules[i]){
						desc=descs[j].d;
						break;
					}
				}
				htm+="<option value='"+rules[i]+"' title='"+desc+"'>"+rules[i]+"</option>";
			}
			$("#rule").html(htm);
		}
	},'json');
}