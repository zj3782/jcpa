var flexTB;
var initPage=1;
$(document).ready(function() {
    var mainheight = document.documentElement.clientHeight;
    var option = {
        height: mainheight-300, //flexigrid插件的高度，单位为px
        url: 'pattern.do?method=page', //ajax url,ajax方式对应的url地址
        colModel: [
	        { display: 'ID', name: 'ID', width: 30, sortable: false,sorttype:'num', align: 'left' },
	        { display: 'Name', name: 'Name', width: 100, sortable: false,sorttype:'ascii', align: 'left' },
	        { display: 'Expression', name: 'Expression', width: 180, sortable: false, align: 'left' },
	        { display: 'Warning', name: 'Warning', width: 100, sortable: false, align: 'left' },
	        { display: 'Category', name: 'Category', width: 100, sortable: false, sorttype:'ascii',align: 'left' },
	        { display: 'Scope', name: 'Scope', width: 100, sortable: true, align: 'left'},
	        { display: 'Example', name: 'Example', width: 170, sortable: false, align: 'left' },
	        { display: 'Priority', name: 'Priority', width: 50, sortable: false,sorttype:'num', align: 'left' }
	    ],
        buttons: [
              { name: 'Add', displayname: "Add", onpress: toolbarItem_onclick },
              { name: 'Delete', displayname: "Delete",  onpress: toolbarItem_onclick },
              { name: 'Rulesets', displayname: "Rulesets",  onpress: toolbarItem_onclick }
        ],
        page:initPage,//初始页数
    	sortname: "ID",
        sortorder: "asc",
        title: "Patterns",
        usepager: true,
        useRp: true,
        showCheckbox: true,
        onAddRow:onAddRowData,
        onRowProp:contextmenu
    };
    flexTB=$("#patternTB").flexigrid(option);
    if($.browser.msie){
    	$("#patterns .bbit-grid .nDiv").height(170);
    	setTimeout('flexTB.flexRefreshMenuPop()',5000);
    }
    /**右键菜单*/
    var menu = { width: 150, items: [
         { text: "View", icon: "css/images/view.png", alias: "contextmenu-view", action: contextMenuItem_click },
         { text: "Edit", icon: "css/images/edit.png", alias: "contextmenu-edit", action: contextMenuItem_click },
         { text: "Delete", icon: "css/images/delete.png", alias: "contextmenu-delete", action: contextMenuItem_click },
      ]
    };
    function contextmenu(row) {
        $(row).contextmenu(menu);
    }
    

    /**工具栏按钮*/
    function toolbarItem_onclick(cmd, grid) {
        if (cmd == "Add") {
            AddPattern();
        }else if (cmd == "Delete") {
        	delPattern(flexTB.flexGetCheckedRows());
        }else if(cmd=="Rulesets"){
        	ManageRulesets();
        }
    }
    /**右键菜单*/
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
    }
    /**检查数据*/
    function onAddRowData(row){
    	for(var i=0;i<row.cell.length;i++){
    		row.cell[i]=decodeURIComponent(row.cell[i]);//解码
    		row.cell[i]=htm2specil(row.cell[i]);
    	}
    	row.cell[1]="<a href='javascript:;' onclick='viewPatternById("+row.id+");'>"+row.cell[1]+"</a>";
    	return row;
    }
});


//删除pattern
function delPattern(rows){
	if(!rows)return;
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
		content:"Are You Sure To Delete Those Patterns ?<br>"+strPattern,
		button: [
	        {
	            name: 'OK',
	            callback: function(){
	    			$.post("Pattern.do",{"method":"delete","ids":strIds},function(result){
	    				if(result.status){
	    					flexTB.flexDelRowsByIds(ids);
	    					artDialog({title:"Delete Success",content:result.info,icon:"succeed",time:2});
	    				}else{
	    					artDialog({title:"Delete Fail",content:result.info,icon:"error"});
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
//添加pattern
function AddPattern()
{
	$("#patternName").val("");
	$("#patternExpression").val("");
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
	    				data.expression=$("#patternExpression").val();
	    				data.warning=$("#patternWarning").val();
	    				data.example=$("#patternExample").val();
	    				data.category=$("#patternCategory").val();
	    				data.scope=$("#patternScope").val();
	    				data.priority=$("#patternPriority").val();
	            		if(data.name=="" || data.expression=="" || data.warning=="" || 
	            				data.example=="" || data.category=="" || data.scope=="" || data.priority=="")
	    				{
	    					artDialog({content:"Some Item(s) is empty,can save!",time:3});
	    					return false;
	    				}
	    				$.post("Pattern.do",data,function(result){
		    				if(result.status){
		    					flexTB.flexAddRowsData([result.data]);
		    					art.dialog({id: 'addpattern'}).close();
		    					artDialog({title:"Add Success",content:result.info,icon:"succeed",time:2});
		    				}else{
		    					artDialog({title:"Add Fail",content:result.info,icon:"error"});
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
//修改pattern
function editPattern(id,fromView){
	var waitDlg=artDialog({title:'Loading Pattern...'});
	$.post("pattern.do?method=get",{'id':id},function(result){
		waitDlg.close();
		if(result.status){
			$("#patternName").val(decodeURIComponent(result.data.name));
			$("#patternExpression").val(decodeURIComponent(result.data.expression));
			$("#patternWarning").val(decodeURIComponent(result.data.warning));
			$("#patternCategory").val(decodeURIComponent(result.data.category));
			$("#patternScope").val(decodeURIComponent(result.data.scope));
			$("#patternExample").val(decodeURIComponent(result.data.example));
			$("#patternPriority").val(decodeURIComponent(result.data.priority));
			artDialog({
				title:"Edit Pattern",
				content:ID('patternAddEdit'),
				lock:true,
				button: [
			        {
			            name: 'Edit',
			            callback: function(){
			    				var data={"method":"update"};
			    				data.id=id;
			    				data.name=$("#patternName").val();
			    				data.expression=$("#patternExpression").val();
			    				data.warning=$("#patternWarning").val();
			    				data.example=$("#patternExample").val();
			    				data.category=$("#patternCategory").val();
			    				data.scope=$("#patternScope").val();
			    				data.priority=$("#patternPriority").val();
			            		if(data.name=="" || data.expression=="" || data.warning=="" || 
			            				data.example=="" || data.category=="" || data.scope=="" || data.priority=="")
			    				{
			    					artDialog({content:"Some Item(s) is empty,can save!",time:3});
			    					return false;
			    				}
			    				$.post("Pattern.do",data,function(result){
				    				if(result.status){
				    					flexTB.flexUpdateRowData(result.data);
				    					art.dialog({id: 'editPattern'}).close();
				    					artDialog({title:"Save Success",content:result.info,icon:"succeed",time:2});
				    				}else{
				    					artDialog({title:"Save Fail",content:result.info,icon:"error"});
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
//查看pattern
function viewPatternById(id){
	var rows=flexTB.flexGetRowsByIds([id]);
	viewPattern(rows[0].cell);
}
function viewPattern(cell){
	var s=cell[1];
	$("#patternNameDiv").html(s).attr("title",cell[1]);
	s=strHtmFmt(cell[2]);
	$("#patternExpressionDiv").html(s).attr("title",cell[2]);
	s=strHtmFmt(cell[3]);
	$("#patternWarningDiv").html(s).attr("title",cell[3]);
	s=cell[4];
	$("#patternCategoryDiv").html(s).attr("title",cell[4]);
	s=cell[5];
	$("#patternScopeDiv").html(s).attr("title",cell[5]);
	s=strHtmFmt(cell[6]);
	$("#patternExampleDiv").html(s).attr("title",cell[6]);
	s=cell[7];
	$("#patternPriorityDiv").html(s).attr("title",cell[7]);
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
 * 管理ruleset
 * */
function ManageRulesets(){
	var dlg=artDialog({
		title:'RuleSets Loading...',
		cancle:true,
		lock:true,
		id:'ManageRulesets'
	});
	$("#rulesetsTB tr").remove();
	$.post("pattern.do",{method:'rulesets'},function(result){
		if(result.status){
			var rules=result.data.rules;
			for(var i=0;i<rules.length;i++){
				addRuleToTB(rules[i]);
			}
			dlg.content(ID('rulesets')).title("RuleSets");
		}
	},'json');
}
/**
 * 删除某个ruleset
 * */
function delRuleset(ruleFile){
	if(!confirm("Do you want to delete ruleset ["+ruleFile+"]?"))return;
	$("#rulesets").mask("Deleting "+ruleFile);
	$.post("pattern.do",{method:"delruleset",rule:ruleFile},function(result){
		$("#rulesets").unmask();
		if(result.status){
			$("#rulesetsTB tr[title='"+ruleFile+"']").remove();
			art.dialog(ruleFile+" Delete Success.").time(3);
		}else{
			art.dialog(result.info);
		}
	},"json");
}
/**
 * 添加ruleset
 * */
function AddRuleSet(){
	artDialog({
		content:ID("addruleset"),
		title:"Add RuleSet",
		lock:true,
		cancel:true,
		ok:function(){
			var filename=$("#AddFileName").val();
			var all=(ID("AddPatternAll").checked)?1:0;
			var cond=$("#AddPattCond").val();
			$("#addruleset").mask("Adding...");
			$.post("pattern.do",{method:'addruleset',fn:filename,'all':all,'cond':cond},function(result){
				$("#addruleset").unmask();
				if(result.status){
					addRuleToTB(filename+".xml");
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
	$("#AddFileName").val(today.getFullYear()+"_"+today.getMonth()+"_"+today.getDay()).focus().select();
}
function addRuleToTB(filename){
	var tb=ID("rulesetsTB"),tr,td;
	tr=tb.insertRow(tb.rows.length);
	tr.title=filename;
	td=tr.insertCell(0);
	td.innerHTML="<div class='w240'>"+filename+"</div>";
	td=tr.insertCell(1);
	var htm="<a href='pattern.do?method=viewRuleset&file="+filename+"' target='_blank'>view</a>&nbsp;&nbsp;";
	htm+="<a href='pattern.do?method=downRuleset&file="+filename+"' target='_blank'>download</a>&nbsp;&nbsp;";
	htm+="<a href='javascript:;' onclick='delRuleset(\""+filename+"\")'>delete</a>";
	td.innerHTML=htm;
}
/**
 *上传ruleset 
 */
function UpRuleSet(){
	artDialog({
		content:ID("upruleset"),
		title:"Upload RuleSet",
		lock:true,
		cancel:true,
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
		url:'pattern.do?method=upRuleset',
		secureuri:false,
		fileElementId:'fileToUpload',
		dataType: 'json',
		data:{},
		success: function (data, status){
			$("#upruleset").unmask();
			if(data.status){
				art.dialog({id:'upruleset'}).close();
				//addRuleToTB(data.data.filename[0]);
				ManageRulesets();//重新加载
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