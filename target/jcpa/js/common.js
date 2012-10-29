/**
 * 检查变量
 * */
function checkVar(Var){
	if(Var==null || typeof(Var)=="undefined")return false;
	return true;
}
/**
 * 判断变量是否是object，如果是直接返回原值，不是代表id，通过document.getElementById取得控件对象
 * */
function ctrlOrId(value){
	if(typeof(value)!="object")value=ID(value);
	return value;
}
/**
 * 通过id获取控件（document.getElementById）
 */
function ID(id){
	return document.getElementById(id);
}
/**
*通过name获取控件数组
*/
function NAME(name){
	return document.getElementsByName(name);
}

/**
 * 设置控件失去焦点的时候如果内容为空，显示什么内容提示
 * @param select	jQuery选择器（例如"#myID"、".myClass"）
 * @param note		提示文本
 * @param style		失去焦点是否增加input_blur样式
 * */
function setBlurNote(select,note,init,style){
	if(!checkVar(select))return false;
	if(!checkVar(note))note="";
	if(!checkVar(style))style=true;
	if(!checkVar(init))init=true;
	
	if(init){
		$(select).val(note);
		if(style)$(select).addClass("input_blur");
	}
	
	$(select).focus(function(){
		if(style)$(this).removeClass("input_blur");
		if(this.value==note){
			this.value="";
		}
	});
	$(select).blur(function(){
		if(style)$(this).addClass("input_blur");		
		if(this.value==""){
			$(this).val(note);
		}
	});
}

/**
 * 字符串转化为数字(如果str无法转化为数字，以Default代替)
 * */
function strToInt(str,Default){
	if(typeof(Default)!='number')Default=0;
	var num=Default;
	try{
		num=parseInt(str);
	}catch(e){num=Default;}
	if(num==NaN)num=Default;
	return num;
}

function rn2br(str){
	if(typeof(str)!="string")return str;
	str=str.replace(/\r\n/g,'<br>');
	str=str.replace(/\n/g,'<br>');
	return str;
}
function br2rn(str){
	if(typeof(str)!="string")return str;
	str=str.replace(/<br>/g,'\r\n');
	str=str.replace(/<br\/>/g,'\r\n');
	return str;
}
function tab2space(str){
	if(typeof(str)!="string")return str;
	str=str.replace(/\t/g,'&nbsp;&nbsp;&nbsp;&nbsp;');
	return str;
}
function htm2specil(str){
	if(typeof(str)!="string")return str;
	str=str.replace(/>/g,'&gt;');
	str=str.replace(/</g,'&lt;');
	return str;
}
function specil2htm(str){
	if(typeof(str)!="string")return str;
	str=str.replace(/&gt;/g,'>');
	str=str.replace(/&lt;/g,'<');
	return str;
}
function strHtmFmt(str){
	if(typeof(str)!="string")return str;
	str=htm2specil(str);
	str=rn2br(str);
	str=tab2space(str);
	return str;
}
/**
 * jquery.loadmask.min.js
 * Copyright (c) 2009 Sergiy Kovalchuk (serg472@gmail.com)
 * 
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *  
 * Following code is based on Element.mask() implementation from ExtJS framework (http://extjs.com/)
 *
 */
(function(a){a.fn.mask=function(c,b){a(this).each(function(){if(b!==undefined&&b>0){var d=a(this);d.data("_mask_timeout",setTimeout(function(){a.maskElement(d,c)},b))}else{a.maskElement(a(this),c)}})};a.fn.unmask=function(){a(this).each(function(){a.unmaskElement(a(this))})};a.fn.isMasked=function(){return this.hasClass("masked")};a.maskElement=function(d,c){if(d.data("_mask_timeout")!==undefined){clearTimeout(d.data("_mask_timeout"));d.removeData("_mask_timeout")}if(d.isMasked()){a.unmaskElement(d)}if(d.css("position")=="static"){d.addClass("masked-relative")}d.addClass("masked");var e=a('<div class="loadmask"></div>');if(navigator.userAgent.toLowerCase().indexOf("msie")>-1){e.height(d.height()+parseInt(d.css("padding-top"))+parseInt(d.css("padding-bottom")));e.width(d.width()+parseInt(d.css("padding-left"))+parseInt(d.css("padding-right")))}if(navigator.userAgent.toLowerCase().indexOf("msie 6")>-1){d.find("select").addClass("masked-hidden")}d.append(e);if(c!==undefined){var b=a('<div class="loadmask-msg" style="display:none;"></div>');b.append("<div>"+c+"</div>");d.append(b);b.css("top",Math.round(d.height()/2-(b.height()-parseInt(b.css("padding-top"))-parseInt(b.css("padding-bottom")))/2)+"px");b.css("left",Math.round(d.width()/2-(b.width()-parseInt(b.css("padding-left"))-parseInt(b.css("padding-right")))/2)+"px");b.show()}};a.unmaskElement=function(b){if(b.data("_mask_timeout")!==undefined){clearTimeout(b.data("_mask_timeout"));b.removeData("_mask_timeout")}b.find(".loadmask-msg,.loadmask").remove();b.removeClass("masked");b.removeClass("masked-relative");b.find("select").removeClass("masked-hidden")}})(jQuery);