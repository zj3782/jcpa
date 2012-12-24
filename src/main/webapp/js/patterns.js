var flexTB;
var initPage=1;
$(document).ready(function() {
    var mainheight = document.documentElement.clientHeight;
    var option = {
        height: mainheight-300, //flexigrid height
        url: 'pattern.do?method=page', //data access url
        colModel: [
	        { display: 'ID', name: 'ID', width: 30, sortable: true,sorttype:'num', align: 'left' },
	        { display: 'Name', name: 'Name', width: 200, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Type', name: 'Type', width: 45, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Class/Expression', name: 'Expression', width: 100, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Auxiliary', name: 'Auxiliary', width: 50, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Warning', name: 'Warning', width: 90, sortable: true,sorttype:'ascii', align: 'left' },
	        { display: 'Category', name: 'Category', width: 90, sortable: true, sorttype:'ascii',align: 'left' },
	        { display: 'Scope', name: 'Scope', width: 75, sortable: true,sorttype:'ascii', align: 'left'},
	        { display: 'Example', name: 'Example', width: 90, sortable: true, align: 'left' },
	        { display: 'Priority', name: 'Priority', width: 45, sortable: true,sorttype:'num', align: 'left' }
	    ],
        buttons: [
              { name: 'Add', displayname: "Add", onpress: toolbarItem_onclick },
              { name: 'Delete', displayname: "Delete",  onpress: toolbarItem_onclick },
              { name: 'Rulesets', displayname: "Rulesets",  onpress: toolbarItem_onclick }
        ],
        page:initPage,
    	sortname: "ID",
        sortorder: "asc",
        title: "Patterns",
        usepager: true,
        useRp: true,
        rp: 12, // results per page
        rpOptions: [12, 20, 30, 50, 100], //options of results per page
        showCheckbox: true,
        onAddRow:onAddRowData,
        onRowProp:contextmenu
    };
    flexTB=$("#patternTB").flexigrid(option);
    if($.browser.msie){
    	$("#patterns .bbit-grid .nDiv").height(170);
    	setTimeout('flexTB.flexRefreshMenuPop()',5000);
    }
   //load javaClass list
    loadJavaClasses();
});

/**patterns list popup menu*/
var menu = { width: 150, items: [
     { text: "View", icon: "css/images/view.png", alias: "contextmenu-view", action: contextMenuItem_click },
     { text: "Edit", icon: "css/images/edit.png", alias: "contextmenu-edit", action: contextMenuItem_click },
     { text: "Delete", icon: "css/images/delete.png", alias: "contextmenu-delete", action: contextMenuItem_click },
  ],onContextMenu:function(event,e){
  		var target=event.currentTarget;
  		var id = $(target).attr("id").substr(3);
  		flexTB.flexUnCheck();
  		flexTB.flexCheck(id);
        return true;
   }
};
/**add popup menu*/
function contextmenu(row) {
    $(row).contextmenu(menu);
}
/**popup menu function*/
function contextMenuItem_click(target) {
    var id = $(target).attr("id").substr(3);
    var rows=flexTB.flexGetRowsByIds([id]);
    var cell = rows[0].cell;
    var cmd = this.data.alias;
    if (cmd == "contextmenu-view") {
    	viewPattern(cell);
    }
    else if (cmd == "contextmenu-edit") {
    	editPattern(id,false);
    }
    else if (cmd == "contextmenu-delete") {
    	delPattern(rows);
    }
    else {
    	flexTB.flexReload();
    }
    flexTB.flexUnCheck();
    flexTB.flexCheck(id);
}
/**patterns list toolbar function*/
function toolbarItem_onclick(cmd, grid) {
    if (cmd == "Add") {
        AddPattern();
    }else if (cmd == "Delete") {
    	delPattern(flexTB.flexGetCheckedRows());
    }else if(cmd=="Rulesets"){
    	ManageRulesets();
    }
}
/**check data on add*/
function onAddRowData(row){
	if(row.cell[1]){
		var matches=row.cell[1].match(/<a[^>]+>([^<]*)<\/a>/);
		if(matches && matches.length>1){
			row.cell[1]=matches[1];
		}
	}
	for(var i=0;i<row.cell.length;i++){
		row.cell[i]=safeDecodeURI(row.cell[i]);
		row.cell[i]=htm2specil(row.cell[i]);
	}
	row.cell[1]="<a href='javascript:;' onclick='viewPatternById("+row.id+");'>"+row.cell[1]+"</a>";
	return row;
}
/**change pattern type and change visiable*/
function changePatternType(type){
	if(type=='java'){
		$("#patternExpressionBlock").hide();
		$("#patternClassBlock").show();
	}else{
		$("#patternExpressionBlock").show();
		$("#patternClassBlock").hide();
	}
}
function changePatternTypeDiv(type){
	if(type=='java'){
		$("#patternExpressionBlockDiv").hide();
		$("#patternClassBlockDiv").show();
	}else{
		$("#patternExpressionBlockDiv").show();
		$("#patternClassBlockDiv").hide();
	}
}
/**load javaclass list*/
function loadJavaClasses(){
	$.post("pattern.do",{method:'javaClasses'},function(result){
		if(result.status){
			var classes=result.data.classes;
			var htm="";
			for(var i=0;i<classes.length;i++){
				htm+="<option value='"+classes[i]+"'>"+classes[i]+"</option>";
			}
			$("#patternClass").html(htm);
		}
	},'json');
}
/*delete pattern*/
function delPattern(rows){
	if(!rows || !rows.length){
		alert("Please select one pattern or more ones to delete.");
		return;
	}
	var strPattern="";
	var strIds="";
	var ids=[];
	for(var i=0;i<rows.length;i++){
		strPattern+=rows[i].cell[1]+"<br>";
		strIds+=","+rows[i].id;
		ids.push(rows[i].id);
	}
	
	artDialog({
		title:"Question",
		content:"Are you sure to delete selected pattern(s) ?<br>"+strPattern,
		button: [
	        {
	            name: 'OK',
	            callback: function(){
	    			$.post("Pattern.do",{"method":"delete","ids":strIds},function(result){
	    				if(result.status){
	    					flexTB.flexDelRowsByIds(ids);
	    					artDialog({title:"Delete success",content:result.info,icon:"succeed",time:2});
	    				}else{
	    					artDialog({title:"Delete fail",content:result.info,icon:"error"});
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
/*add pattern*/
function AddPattern()
{
	$("#patternName").val("");
	$("#patternExpression").val("");
	$("#patternAuxiliary").val("");
	$("#patternWarning").val("");
	$("#patternExample").val("");
	artDialog({
		title:"Add Pattern",
		content:ID('patternAddEdit'),
		lock:true,
		button: [
	        {
	            name: 'Add',
	            callback: function(){
	    				var data={"method":"add"};
	    				data.name=$("#patternName").val();
	    				data.type=$("#patternType").val();
	    				data.javaclass=$("#patternClass").val();
	    				data.expression=$("#patternExpression").val();
	    				data.aux=$("#patternAuxiliary").val();
	    				data.warning=$("#patternWarning").val();
	    				data.example=$("#patternExample").val();
	    				data.category=$("#patternCategory").val();
	    				data.scope=$("#patternScope").val();
	    				data.priority=$("#patternPriority").val();
	    				
	    				var bOk=true;
	            		if(data.name=="" ||  data.warning=="" || data.example=="" || data.category=="" || data.scope=="" || data.priority==""){
	            			bOk=false;
	    				}else{
	    					if(data.type=='java' && data.javaclass=="")bOk=false;
	    					else if(data.type!='java' && data.expression=="")bOk=false;
	            		}
	            		if(!bOk)
	            		{
	    					artDialog({content:"Some item(s) is empty,can not save!",time:3});
	    					return false;
	    				}
	    				$.post("Pattern.do",data,function(result){
		    				if(result.status){
		    					flexTB.flexReload();
		    					art.dialog({id: 'addpattern'}).close();
		    					artDialog({title:"Add success",content:result.info,icon:"succeed",time:2});
		    				}else{
		    					artDialog({title:"Add fail",content:result.info,icon:"error"});
		    				}
		    			},"json");
	    				return false;
	    			}
	        },{
	        	name:'Cancel'
	        }
	    ],
		id:"addpattern"
	});
}
/*edit pattern*/
function editPattern(id,fromView){
	var waitDlg=artDialog({title:'Loading Pattern...'});
	$.post("pattern.do?method=get",{'id':id},function(result){
		waitDlg.close();
		if(result.status){
			$("#patternName").val(safeDecodeURI(result.data.name));
			$("#patternType").val(safeDecodeURI(result.data.type));
			$("#patternClass").val(safeDecodeURI(result.data.javaclass));
			$("#patternExpression").val(safeDecodeURI(result.data.expression));
			$("#patternAuxiliary").val(safeDecodeURI(result.data.aux));
			$("#patternWarning").val(safeDecodeURI(result.data.warning));
			$("#patternCategory").val(safeDecodeURI(result.data.category));
			$("#patternScope").val(safeDecodeURI(result.data.scope));
			$("#patternExample").val(safeDecodeURI(result.data.example));
			$("#patternPriority").val(safeDecodeURI(result.data.priority));
			
			changePatternType(result.data.type);
			
			artDialog({
				title:"Edit Pattern",
				content:ID('patternAddEdit'),
				lock:true,
				button: [
			        {
			            name: 'Ok',
			            callback: function(){
			    				var data={"method":"update"};
			    				data.id=id;
			    				data.name=$("#patternName").val();
			    				data.type=$("#patternType").val();
			    				data.javaclass=$("#patternClass").val();
			    				data.expression=$("#patternExpression").val();
			    				data.aux=$("#patternAuxiliary").val();
			    				data.warning=$("#patternWarning").val();
			    				data.example=$("#patternExample").val();
			    				data.category=$("#patternCategory").val();
			    				data.scope=$("#patternScope").val();
			    				data.priority=$("#patternPriority").val();
			    				
			    				var bOk=true;
			            		if(data.name=="" ||  data.warning=="" || data.example=="" || data.category=="" || data.scope=="" || data.priority==""){
			            			bOk=false;
			    				}else{
			    					if(data.type=='java' && data.javaclass=="")bOk=false;
			    					else if(data.type!='java' && data.expression=="")bOk=false;
			            		}
			            		if(!bOk)
			            		{
			    					artDialog({content:"Some item(s) is empty,can not save!",time:3});
			    					return false;
			    				}
			    				$.post("Pattern.do",data,function(result){
				    				if(result.status){
				    					flexTB.flexUpdateRowData(result.data);
				    					art.dialog({id: 'editPattern'}).close();
				    					artDialog({title:"Save success",content:result.info,icon:"succeed",time:2});
				    				}else{
				    					artDialog({title:"Save fail",content:result.info,icon:"error"});
				    				}
				    			},"json");
			    				return false;
			    			}
			        },{
			        	name:'Cancel',
			        	callback:function(){if(fromView)viewPatternById(id);}
			        }
			    ],
				id:"editPattern"
			});
		}else{
			alert(result.info);
		}
	},'json');
}
/*view pattern*/
function viewPatternById(id){
	var rows=flexTB.flexGetRowsByIds([id]);
	viewPattern(rows[0].cell);
}
function viewPattern(cell){
	var s=cell[1];
	$("#patternNameDiv").html(s).attr("title",cell[1]);
	s=cell[2];
	$("#patternTypeDiv").html(s).attr("title",cell[2]);
	if(cell[2]=='java'){
		s=cell[3];
		$("#patternClassDiv").html(s).attr("title",cell[3]);
	}else{
		s=strHtmFmt(cell[3]);
		$("#patternExpressionDiv").html(s).attr("title",cell[3]);	
	}
	s=strHtmFmt(cell[4]);
	$("#patternAuxiliaryDiv").html(s).attr("title",cell[4]);
	s=strHtmFmt(cell[5]);
	$("#patternWarningDiv").html(s).attr("title",cell[5]);
	s=cell[6];
	$("#patternCategoryDiv").html(s).attr("title",cell[6]);
	s=cell[7];
	$("#patternScopeDiv").html(s).attr("title",cell[7]);
	s=strHtmFmt(cell[8]);
	$("#patternExampleDiv").html(s).attr("title",cell[8]);
	s=cell[9];
	$("#patternPriorityDiv").html(s).attr("title",cell[9]);
	
	changePatternTypeDiv(cell[2]);
	
	artDialog({
		title:"View Pattern",
		content:ID('patternDetail'),
		lock:true,
		button: [
			{
				name: 'New Window',
				callback:function(){window.open("pattern.jsp?id="+cell[0]);return false;}
			},
			{
	            name: 'Edit',
	            callback: function(){
	            	editPattern(cell[0],true);
	    		}
	        },
			{
				name: 'Close'
			}
	    ],
		id:"viewPattern"
	});
}
/**
 * manage ruleset
 * */
function ManageRulesets(){
	var dlg=artDialog({
		title:'RuleSets loading...',
		cancle:true,
		lock:true,
		id:'ManageRulesets'
	});
	$("#rulesetsTB tr").remove();
	$.post("pattern.do",{method:'rulesets'},function(result){
		if(result.status){
			var rules=result.data.rules;
			var descs=result.data.desc;
			var desc;
			for(var i=0;i<rules.length;i++){
				desc=rules[i];
				for(var j=0;j<descs.length;j++){
					if(descs[j].f==rules[i]){
						desc=descs[j].d;
						break;
					}
				}
				addRuleToTB(rules[i],desc);
			}
			dlg.content(ID('rulesets')).title("RuleSets");
		}
	},'json');
}
/**
 * delete ruleset
 * */
function delRuleset(ruleFile){
	if(!confirm("Do you want to delete ruleset ["+ruleFile+"]?"))return;
	$("#rulesets").mask("Deleting "+ruleFile);
	$.post("pattern.do",{method:"delruleset",rule:ruleFile},function(result){
		$("#rulesets").unmask();
		if(result.status){
			$("#rulesetsTB tr[title='"+ruleFile+"']").remove();
			art.dialog(ruleFile+" Delete success.").time(3);
		}else{
			art.dialog(result.info);
		}
	},"json");
}
/**
 * add ruleset
 * */
function AddRuleSet(){
	artDialog({
		content:ID("addruleset"),
		title:"Add RuleSet",
		lock:true,
		cancel:true,
		ok:function(){
			var filename=$("#AddFileName").val();
			if(filename.slice(filename.length-4)!='.xml')filename+=".xml";
			var desc=$("#AddDescription").val();
			var type=(ID("AddPatternAll").checked)?"all":(ID("AddPatternSelect").checked)?"selected":"custom";
			var cond="";
			if(type=="selected"){
				var rows=flexTB.flexGetCheckedRows();
				if(rows.length<=0){
					alert("There is no pattern selected.");
					return;
				}
				cond+="ID in ( "+rows[0].id;
				for(var i=1;i<rows.length;i++){
					cond+=","+rows[i].id;
				}
				cond+=")";
			}else{
				cond=$("#AddPattCond").val();
			}
			$("#addruleset").mask("Adding...");
			$.post("pattern.do",{method:'addruleset',fn:filename,'type':type,'cond':cond,'desc':desc},function(result){
				$("#addruleset").unmask();
				if(result.status){
					addRuleToTB(filename,desc);
					art.dialog({id:'addruleset'}).close();
				}else{
					alert(result.info);
				}
			},'json');
			return false;
		},
		id:'addruleset'
	});
	var today=new Date();
	$("#AddFileName").val(today.getFullYear()+"_"+(today.getMonth()+1)+"_"+today.getDate()+"_"+
			today.getHours()+"_"+today.getMinutes()+"_"+today.getSeconds()+".xml").focus().select();
}
function addRuleToTB(filename,desc){
	var tb=ID("rulesetsTB"),tr,td;
	tr=tb.insertRow(tb.rows.length);
	tr.title=filename;
	td=tr.insertCell(0);
	td.innerHTML="<div class='w240' title='"+desc+"'>"+filename+"</div>";
	td=tr.insertCell(1);
	var htm="<a href='pattern.do?method=viewRuleset&file="+filename+"' target='_blank'>View</a>&nbsp;&nbsp;";
	htm+="<a href='pattern.do?method=downRuleset&file="+filename+"' target='_blank'>Download</a>&nbsp;&nbsp;";
	htm+="<a href='javascript:;' onclick='delRuleset(\""+filename+"\")'>Delete</a>";
	td.innerHTML=htm;
}

/**
 *upload ruleset 
 */
function UpRuleSet(){
	artDialog({
		content:ID("upruleset"),
		title:"Upload RuleSet",
		lock:true,
		id:'upruleset'
	});
}
function ajaxUpRuleSet(){
	$("#upruleset").ajaxStart(function(){
		$(this).mask("Uploading...");
	}).ajaxComplete(function(){
		$(this).unmask();
	});

	$.ajaxFileUpload({
		url:'pattern.do?method=upRuleset&desc='+encodeURIComponent($("#upDescription").val()),
		secureuri:false,
		fileElementId:'fileToUpload',
		dataType: 'json',
		data:{},
		success: function (data, status){
			$("#upruleset").unmask();
			if(data.status){
				art.dialog({id:'upruleset'}).close();
				ManageRulesets();//reload
			}else{
				alert(data.info);
			}
		},
		error: function (data, status, e){
			$("#upruleset").unmask();
			alert(e);
		}
	});
}