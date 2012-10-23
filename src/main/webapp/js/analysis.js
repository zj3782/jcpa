var flexTBR,flexTBE;
$(document).ready(function() {
    var option = {
        height: 435,
        colModel: [
            { display: 'package', name: 'package', width: 70, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'class', name: 'class', width: 60, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'method', name: 'method', width: 70, sortable: false, align: 'left' },
	        { display: 'rule', name: 'rule', width: 100, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'description', name: 'description', width: 280, sortable: false, align: 'left' },
	        { display: 'example', name: 'example', width: 200, sortable: false, align: 'left' },
	        { display: 'priority', name: 'priority', width: 50, sortable: true,sorttype:'num', align: 'left' }
		],
//		usepager: true,
//      useRp: true,
        onAddRow:onAddRowData,
        striped:false,
        onRowProp:contextmenu
    };
    flexTBR=$("#reportTB").flexigrid(option);
    option = {
        height: 100,
        colModel: [
           { display: 'file', name: 'file', width: 450, sortable: true,sorttype:'ascii', align: 'left' },
           { display: 'error', name: 'error', width: 450, sortable: false, align: 'left' }
        ],
        onAddRow:onAddRowData
    };
    flexTBE=$("#errorTB").flexigrid(option);
});
/**右键菜单*/
function contextmenu(row) {
	var menu = { width: 150, items: [
         { text: "View", icon: "css/images/view.png", alias: "contextmenu-view", action: contextMenuItem_click }
      ]
    };
    $(row).contextmenu(menu);
}
function contextMenuItem_click(target) {
    var id = $(target).attr("id").substr(3);
    var rows=flexTBR.flexGetRowsByIds([id]);
    var cell = rows[0].cell;
    var cmd = this.data.alias;
    if (cmd == "contextmenu-view") {
    	viewReport(cell);
    }
}
/**检查数据*/
function onAddRowData(row){
	for(var i=0;i<row.cell.length;i++){
		row.cell[i]=decodeURIComponent(row.cell[i]);//解码
		if(i!=4)row.cell[i]=htm2specil(row.cell[i]);//description无需转码
	}
	row.cell[4]="<u class='cHand' onclick='viewReportById("+row.id+");'>"+row.cell[4]+"</u>";
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
	$("#viewRule").html(cell[3]);
	$("#viewDescription").html(rn2br(tab2space(cell[4])));
	$("#viewExample").html(rn2br(tab2space(cell[5])));
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
var steps=['Starting','Svn Logining','Svn Login Ok','Svn CheckOuting',
           'Svn CheckOut Ok','Pmd Analysising','Pmd Analysis Ok','Code Analysis Ok','Code Analysis Fail'];

var TimerID=null;
var rowid=1;
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
			IntervalGetReport();
		}else{//产生错误
			artDialog({content:result.info});
			EndIntervalGetReport();
		}
	},"json");
	//定时获取结果report
	StartIntervalGetReport();
	
	$("#svnTitle").show();
	$("#SvnUrl").html($("#url").val());
	$("#SvnUser").html($("#user").val());
	$("#login").remove();
	$("#reportArea").show();
}
/**
 * 定时获取结果report
 * */
function StartIntervalGetReport(interval){
	if(typeof(interval)!="number")interval=2000;
	if(TimerID)clearInterval(TimerID);
	TimerID=setInterval(IntervalGetReport,interval);
}
function EndIntervalGetReport(){
	if(TimerID)clearInterval(TimerID);
	TimerID=0;
}

/**
 * 定时获取结果report
 */
function IntervalGetReport(){
	var rstart=$("#rline").val();
	var estart=$("#eline").val();
	$.post("Analysis.do",{"method":"report","rstart":rstart,"estart":estart},function(result){
		if(result.status){
			addReportData(result.data);
		}else{
			EndIntervalGetReport();
		}
	},"json");
}
function addReportData(data){
	var report=data.report;
	
	$("#reporting").mask(steps[report.step]);
	if(report.step==STEP_SUCCESSEND || report.step==STEP_FAILEND){
		if(report.rstart==report.rend && report.estart==report.eend){
			$("#reporting").unmask();$("#reporting").hide();
			EndIntervalGetReport();
		}
	}
	
	var rstart=strToInt($("#rline").val());
	if(report.rstart>=rstart){
		$("#rline").val(report.rend);
		var rows=[];
		for(var i=0;i<report.report.length;i++){
			var row={id:rowid,cell:report.report[i]};
			rows.push(row);
			rowid++;
		}
		flexTBR.flexAddRowsData(rows);
	}
	
	var estart=strToInt($("#eline").val());
	if(report.estart>=estart){
		$("#eline").val(report.eend);
		var rows=[];
		for(var i=0;i<report.error.length;i++){
			var row={id:0,cell:report.error[i]};
			rows.push(row);
		}
		flexTBE.flexAddRowsData(rows); 
	}
}
/**
 * 重新分析
 */
function ReAnalysis(){
	if(!confirm("Are You Sure To ReAnalysis?"))return;
	$.post("Analysis.do",{"method":"ClearReport"},function(result){
		document.location.reload();
	});
}
/**
 * 导出结果
 */
function exportResult(){
	var htm="";
	var rows=flexTBR.flexGetRows();
	htm+="[reports]\r\n";
	for(var i=0;i<rows.length;i++){
		for(var j=0;j<rows[i].cell.length;j++){
			htm+=rows[i].cell[j]+"\t";
		}
		htm+="\r\n";
	}
	htm+="\r\n";
	var errors=flexTBE.flexGetRows();
	htm+="[errors]\r\n";
	for(var m=0;m<errors.length;m++){
		for(var n=0;n<errors[m].cell.length;n++){
			htm+=errors[m].cell[n]+"\t";
		}
		htm+="\r\n";
	}
	$("#exportTA").val(htm);
	art.dialog({
		content:ID("exportDiv"),
		title:"Export Analysis Result"
	});
	//全选
	ID("exportTA").focus();         
	ID("exportTA").select();
}