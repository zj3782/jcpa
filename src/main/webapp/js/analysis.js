var flexTBR,flexTBE;
$(document).ready(function() {
    var option = {
        height: 310,
        colModel: [
                { display: 'Package', name: 'package', width: 150, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Class', name: 'class', width: 130, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Method/Static', name: 'method', width: 150, sortable: false, align: 'left' },
       	        { display: 'Location', name: 'method', width: 150, sortable: false, align: 'left' },
       	        { display: 'Code', name: 'code', width: 200, sortable: false, align: 'left' },
    	        { display: 'Rule', name: 'rule', width: 220, sortable: true,sorttype:'ascii', align: 'left' },
       	        { display: 'Priority', name: 'priority', width: 50, sortable: true,sorttype:'num', align: 'left' }
		],
		url:"Analysis.do?method=reportlist",
		usepager: true,
		useRp: true,
		rp: 12, // results per page,每页默认的结果数
        rpOptions: [12, 20, 30, 50, 100], //可选择设定的每页结果数
        onAddRow:onAddRowData,
        striped:false,
        autoload:false
    };
    flexTBR=$("#reportTB").flexigrid(option);
    flexTBR.flexToggleCol(4,false);//Code这一列隐藏
    
    option = {
        height: 100,
        colModel: [
           { display: 'file', name: 'file', width: 450, sortable: true,sorttype:'ascii', align: 'left' },
           { display: 'error', name: 'error', width: 450, sortable: false, align: 'left' }
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

/**检查数据*/
function onAddRowData(row){
	row.cell[4]=htm2specil(row.cell[4]);//code
	for(var i=0;i<row.cell.length;i++){
		row.cell[i]=decodeURIComponent(row.cell[i]);//解码
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
		row.cell[i]=decodeURIComponent(row.cell[i]);//解码
	}
	return row;
}
/**
 * 查看单个report
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
//step常量
var STEP_START=0;//开始
var STEP_LOGINING=1;//svn登录中
var STEP_LOGINOK=2;//svn登录成功
var STEP_CHECKOUTING=3;//svn检出中
var STEP_CHECKOUTOK=4;//svn检出成功
var STEP_PMDING=5;//pmd处理过程中
var STEP_PMDOK=6;//pmd处理成功
var STEP_SUCCESSEND=7;//成功结束
var STEP_FAILEND=8;//失败结束
var steps=['Starting','Svn logining','Svn login ok','Svn checking out',
           'Svn check out ok','Pmd analysing','Pmd analyse ok','Code analyse ok','Code analyse fail'];

var TimerID=null;
/**
 * 开始分析
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
		}else{//产生错误
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
 * 定时获取结果report
 * */
function StartIntervalGetStep(interval){
	if(typeof(interval)!="number")interval=2000;
	if(TimerID)clearInterval(TimerID);
	TimerID=setInterval(IntervalGetStep,interval);
}
function EndIntervalGetStep(){
	if(TimerID)clearInterval(TimerID);
	TimerID=0;
}

/**
 * 定时获取step
 */
function IntervalGetStep(){
	$.post("Analysis.do",{"method":"step"},function(result){
		if(result.status){
			$("#reportArea").mask(steps[result.data.step]);
			if(result.data.step==STEP_SUCCESSEND || result.data.step==STEP_FAILEND){
				flexTBR.flexReload();
				flexTBE.flexReload();
				EndIntervalGetStep();
				$("#reportArea").unmask();
			}
		}else{
			EndIntervalGetStep();
			$("#reportArea").unmask();
			alert(result.info);
		}
	},"json");
}
/**
 * 重新分析
 */
function ReAnalysis(ask){
	if(ask!=false && !confirm("Do you want to go back?"))return;
	$.post("Analysis.do",{"method":"ClearReport"},function(result){
		document.location.reload();
	});
}
/**
 *获取ruleset 
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