var flexGrid;
var initPage=1;
var btns=[{ name: 'Run', displayname: "Run", onpress: toolbarItem_onclick }];
$(document).ready(function() {
    var mainheight = document.documentElement.clientHeight;
    var option = {
        height: mainheight-300, 
        url: 'benchmark.do?method=page', 
        colModel: [
	        { display: 'ID', name: 'ID', width: 30, sortable: true,sorttype:'num', align: 'left' },
	        { display: 'Name', name: 'Name', width: 100, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Descript', name: 'Descript', width: 200, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Result', name: 'Result', width: 500, sortable: true,sorttype:'ascii', align: 'left' }
	    ],
	    buttons:btns,
        page:initPage,
    	sortname: "ID",
        sortorder: "asc",
        title: "Cases",
        usepager: true,
        useRp: true,
        rp: 12, // results per page
        rpOptions: [12, 20, 30, 50, 100],
        showCheckbox: true,
        onAddRow:onAddRowData,
        onRowProp:contextmenu
    };
    flexGrid = $("#casesTB").flexigrid(option);
    flexGrid.flexToggleCol(1,false);
    if($.browser.msie){
    	$("#cases .bbit-grid .nDiv").height(85);
    }
});
/**toolbar function*/
function toolbarItem_onclick(cmd, grid) {
    if (cmd == "Run") {
    	var rows=flexGrid.flexGetCheckedRows();
        runCase(rows);
    }else if(cmd=="Add"){
    	AddCase();
    }
}
/**add popup menu*/
function contextmenu(row) {
    var menu = { width: 150, items: [
         { text: "Run", icon: "css/images/run.png", alias: "contextmenu-run", action: contextMenuItem_click },          
         { text: "View", icon: "css/images/view.png", alias: "contextmenu-view", action: contextMenuItem_click }          
    ],onContextMenu:function(event,e){
    	var target=event.currentTarget;
    	 var id = $(target).attr("id").substr(3);
    	 flexGrid.flexUnCheck();
         flexGrid.flexCheck(id);
         return true;
    }};
    $(row).contextmenu(menu);
}
/**popup menu function*/
function contextMenuItem_click(target) {
    var id = $(target).attr("id").substr(3);
    var cmd = this.data.alias;
    var rows=flexGrid.flexGetRowsByIds([id]);
    var cell = rows[0].cell;
    if (cmd == "contextmenu-run") {
    	runCase(rows);
    }else if(cmd == "contextmenu-view"){
    	viewCase(cell);
    }else {
    	flexGrid.flexReload();
    }
}
/**check data when add*/
function onAddRowData(row){
	for(var i=0;i<row.cell.length;i++){
		row.cell[i]=safeDecodeURI(row.cell[i]);//解码
		row.cell[i]=htm2specil(row.cell[i]);
	}
	return row;
}
/*show run case div*/
function runCase(rows){
	if(!rows || rows.length<=0){
		alert("Please select one or more cases to run!");
		return;
	}
	var htm="",row,names="";
	for(var i=0;i<rows.length;i++){
		row=rows[i];
		htm+="<div id='case_"+row.cell[1]+"'>";
		htm+="<input type='hidden' value='"+row.id+"' />";
		htm+="<h4>ID:"+row.cell[0]+"</h4>";
		htm+="<div>"+tab2space(rn2br(row.cell[3],true))+"</div>";
		htm+="</div>";
		htm+="<hr class='bGreen'/>";
		names+=row.cell[1]+",";
	}
	$("#RunCaseResult").html(htm);
	$("#RunCaseNames").val(names);
	if(!ID("ThreadNum").value)ID("ThreadNum").value=100;
	if(!ID("RepeatNum").value)ID("RepeatNum").value=50;
	if(!ID("RWRate").value)ID("RWRate").value=50;
	artDialog({
		title:"Run Case",
		content:ID('RunDiv'),
		lock:true,
		button:[{name:"OK"},{name:"Stop",callback:function(){stopCase(names);return false;},disabled:true}],
		id:"runcase"
	});
}
/*run case on server*/
function RealRunCase(){
	var caseName=$("#RunCaseNames").val();;
	var threadNum=ID("ThreadNum").value;
	var repeatNum=ID("RepeatNum").value;
	var RWRate=ID("RWRate").value;
	if(caseName=='' || threadNum=='' || repeatNum=='' || RWRate==''){
		alert("Some item(s) is empty,please fill it");
		return;
	}
	$("#RunDiv").mask("Runing");
	art.dialog({id: 'runcase'}).button({name:"OK",disabled:true},{name:"Stop",disabled:false});;
	$.post("benchmark.do",{method:"run","case":caseName,thread:threadNum,repeat:repeatNum,rwrate:RWRate},function(result){
		if(result.status){
			var results=result.data.result;
			for(var i=0;i<results.length;i++){
				$("#case_"+results[i].n+" div").html(strHtmFmt(results[i].r));
				var Id=$("#case_"+results[i].n+" input").val();
				var rows=flexGrid.flexGetRowsByIds([Id]);
				var row=rows[0];row.cell[3]=results[i].r;
				flexGrid.flexUpdateRowData(row);
			}
		}else{
			alert(result.info);
		}
		$("#RunDiv").unmask();
		art.dialog({id: 'runcase'}).button({name:"OK",disabled:false},{name:"Stop",disabled:true});
	},"json");
}
/*view case*/
function viewCase(cell)
{
	$("#ViewCaseName").html(cell[1]);
	$("#ViewCaseDescript").html(tab2space(rn2br(cell[2],true)));
	$("#ViewCaseResult").html(tab2space(rn2br(cell[3],true)));
	artDialog({
		title:"View Case",
		content:ID('ViewDiv'),
		lock:true
	});
}
/*stop run*/
function stopCase(caseNames)
{
	$("#RunDiv").mask("Stopping");
	$.post("benchmark.do",{method:"stop","case":caseNames},function(result){
		if(result.status){
		}else{
			alert(result.info);
		}
	},"json");	
}

/*add Case*/
function AddCase(){
	$("#AddCaseName").val();
	$("#AddCaseDescript").val();
	artDialog({
		title:"Add Case",
		content:ID('AddDiv'),
		lock:true,
		button:[
	        {name:"Add",callback:function(){
	        	$("AddDiv").mask("Adding...");
	        	$.post("benchmark.do",{method:"add","name":$("#AddCaseName").val(),"desp":$("#AddCaseDescript").val()},function(result){
	        		$("AddDiv").unmask();
	        		if(result.status){
	        			art.dialog({id: 'addcase'}).close();
	        			flexGrid.flexReload();
	        		}else{
	        			alert(result.info);
	        		}
	        	},"json");	
	        	return false;
	        }},
	        {name:"Cancel"}
		],
		id:'addcase'
	});
}