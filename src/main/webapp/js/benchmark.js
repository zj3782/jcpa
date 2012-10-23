var flexGrid;
var initPage=1;
$(document).ready(function() {
    var mainheight = document.documentElement.clientHeight;
    var option = {
        height: mainheight-300, //flexigrid插件的高度，单位为px
        url: 'benchmark.do?method=page', //ajax url,ajax方式对应的url地址
        colModel: [
	        { display: 'ID', name: 'ID', width: 30, sortable: true,sorttype:'num', align: 'left' },
	        { display: 'Name', name: 'Name', width: 100, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Descript', name: 'Descript', width: 200, sortable: false, align: 'left' },
	        { display: 'Result', name: 'Result', width: 500, sortable: false, align: 'left' }
	    ],
	    buttons: [
	        { name: 'Run', displayname: "Run", onpress: toolbarItem_onclick }
	    ],
        page:initPage,//初始页数
    	sortname: "ID",
        sortorder: "asc",
        title: "Cases",
        usepager: true,
        useRp: true,
        showCheckbox: true,
        onAddRow:onAddRowData,
        onRowProp:contextmenu
    };
    flexGrid = $("#casesTB").flexigrid(option);
    flexGrid.flexToggleCol(1,false);
    if($.browser.msie){
    	$("#cases .bbit-grid .nDiv").height(85);
    }
    /**工具栏按钮*/
    function toolbarItem_onclick(cmd, grid) {
        if (cmd == "Run") {
        	var rows=flexGrid.flexGetCheckedRows();
            runCase(rows);
        }
    }
    /**右键菜单*/
    function contextmenu(row) {
        var menu = { width: 150, items: [
             { text: "Run", icon: "css/images/run.png", alias: "contextmenu-run", action: contextMenuItem_click },          
             { text: "View", icon: "css/images/view.png", alias: "contextmenu-view", action: contextMenuItem_click }          
        ]};
        $(row).contextmenu(menu);
    }
    /**右键菜单*/
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
    /**检查数据*/
    function onAddRowData(row){
    	for(var i=0;i<row.cell.length;i++){
    		row.cell[i]=decodeURIComponent(row.cell[i]);//解码
    		row.cell[i]=htm2specil(row.cell[i]);
    	}
    	return row;
    }
});

//运行case
function runCase(rows){
	var htm="",row,names="";
	for(var i=0;i<rows.length;i++){
		row=rows[i];
		htm+="<div id='case_"+row.cell[1]+"'>";
		htm+="<input type='hidden' value='"+row.id+"' />";
		htm+="<h4>"+row.cell[1]+"</h4>";
		htm+="<div>"+tab2space(rn2br(row.cell[3]))+"</div>";
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
		button:[{name:"Cancel"},{name:"Stop",callback:function(){stopCase(names);return false;},disabled:true}],
		id:"runcase"
	});
}
//真正运行case
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
	art.dialog({id: 'runcase'}).button({name:"Cancel",disabled:true},{name:"Stop",disabled:false});;
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
		art.dialog({id: 'runcase'}).button({name:"Cancel",disabled:false},{name:"Stop",disabled:true});;
	},"json");
}
//查看case
function viewCase(cell)
{
	$("#ViewCaseName").html(cell[1]);
	$("#ViewCaseDescript").html(tab2space(rn2br(cell[2])));
	$("#ViewCaseResult").html(tab2space(rn2br(cell[3])));
	artDialog({
		title:"View Case",
		content:ID('ViewDiv'),
		lock:true
	});
}
//停止
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