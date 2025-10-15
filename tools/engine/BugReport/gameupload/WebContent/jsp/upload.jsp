<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <TITLE>文件上传</TITLE>
    <link type="text/css" rel="stylesheet" href="<%=path %>/css/main.css" />
</head>

<SCRIPT language=javascript>
var count = 0;
function addFile(){
   obj = document.getElementById("temp3");
   if (count == 15) {
   		alert("max allowed count is 15. ");
   		return;
   }
   count ++;
   elm = document.createElement("A");
   elm.innerHTML = "File"+count+": ";
   obj.appendChild(elm);
   elm = document.createElement("input");
   elm.type = "file";
   elm.name = "FILE"+count;
   elm.size = "50";
   obj.appendChild(elm);
   elm = document.createElement("BR");
   obj.appendChild(elm);
}
function cutFile(){
   if(count>0){
      obj = document.getElementById("temp3");
      count--;
      obj1 = obj.getElementsByTagName("A")[count];
      obj2 = obj.getElementsByTagName("input")[count];
      obj3 = obj.getElementsByTagName("BR")[count];
      obj.removeChild(obj1);
      obj.removeChild(obj2);
      obj.removeChild(obj3);
   }
}
</SCRIPT>
<BODY>
<div class="bar"><span class="txt c1">上传文件</span><img src="<%=path %>/images/bar_11.gif" class="tt" /></div>
<div class="main_m">

<div class="from1_">
<div class="bar"><span class="txt c1">请选择要上传的文件</span><img src="<%=path %>/images/bar_11.gif" class="tt" /></div>
<div class="from1_tb">
<form method="POST" action="<%=path %>/FileUploadWithInfoServlet"  ENCTYPE="multipart/form-data">
上传至相对路径:
<input type="text" value="" name="RELATIVEPATH">&nbsp;例: test/game
<TABLE>
<TR>
	<TD>ExceptionCode:</TD>
	<TD><input type="text" value="" name="ExceptionCode"></TD>
</TR>
<TR>
	<TD>ExceptionAddress:</TD>
	<TD><input type="text" value="" name="ExceptionAddress"></TD>
</TR>
<TR>
	<TD>Register:</TD>
	<TD><input type="text" value="" name="Register"></TD>
</TR>
<TR>
	<TD>StackInfo:</TD>
	<TD><input type="text" value="" name="StackInfo"></TD>
</TR>
<TR>
	<TD>Graphic:</TD>
	<TD><input type="text" value="" name="Graphic">
</TD>
</TR>
<TR>
	<TD>Driver:</TD>
	<TD><input type="text" value="" name="Driver"></TD>
</TR>
<TR>
	<TD>Description:</TD>
	<TD><input type="text" value="" name="Descript"></TD>
</TR>
<TR>
	<TD>OSVersion:</TD>
	<TD><input type="text" value="" name="OSVersion"></TD>
</TR>
<TR>
	<TD>AppVersion:</TD>
	<TD><input type="text" value="" name="AppVersion"></TD>
</TR>
<TR>
	<TD>ComputerName:</TD>
	<TD><input type="text" value="" name="ComputerName"></TD>
</TR>
<TR>
	<TD>UserName:</TD>
	<TD>
<input type="text" value="" name="UserName"></TD>
</TR>
<TR>
	<TD>PhoneNo.:</TD>
	<TD>
<input type="text" value="" name="PhoneNumber"></TD>
</TR>
<TR>
	<TD>QQ/MSN</TD>
	<TD>
<input type="text" value="" name="QQorMSN"></TD>
</TR>
<TR>
	<TD>Email</TD>
	<TD><input type="text" value="" name="Email"></TD>
</TR>
</TABLE>
<br/>
<br/>

<input name="addfile" type="button" value="增加" onClick="addFile();">
<input name="cutfile" type="button" value="删除" onClick="cutFile();">
<br/>
<hr/>
<span id="temp3">
</span>
<hr>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="上传">&nbsp;&nbsp;&nbsp;&nbsp;<a href='mailto:liudongxue01238@locojoy.com'>疑问</a>
</form>
<div class="c"></div>
</div>
</div>
<div class="tablebon"></div>
</div>
</BODY>
</html>