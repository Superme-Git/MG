<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.locojoy.pager.*" %>
<%@ page import="com.world2.dao.*,com.world2.utils.*" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html> 
	<head>
		<title>BUGREPORT系统</title>
		<link type="text/css" rel="stylesheet" href="<%=path %>/css/main.css" />
		<script type="text/javascript" src="<%=path %>/js/util.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jq131.js"></script>
		<script type="text/javascript" src="<%=path %>/js/msg.js"></script>
		<script language=JavaScript>
		<!--
		function deleteFile(path,id)
		{	
			if (confirm("真的要删除此记录么？")){
				var form = document.getElementById("gotoPageForm");
				var pt = path + "/deleteFile?id=" + id;
				if(form != null){
					if(form.start != null)
						pt += "&start="+form.start.value;
					if(form.searchName != null)
						pt += "&searchName="+form.searchName.value;
				}
				window.location = pt;
			}
		}
		//-->
		</script>
	</head>
	<body>
<div class="bar"><span class="txt c1">查询</span><img src="<%=path %>/images/bar_11.gif" class="tt" /></div>
<div style="display:none;" id="zxz_msg">
	<span wzid="none" isclo="no" tim="2000">
	<%String tip = (String)request.getAttribute("tip");
		if(tip != null)
		out.print(tip);	
	%>
	</span>
</div>
<div class="bar1">
	<span class="seach">
	<form action="<%=path %>/fileList"> 
	关键字：<input type="text" name="searchName"/><input type="submit" value="查询" class="bt_sm"/>
	</form>
	</span>
</div>	
	
<!-- 以下是显示部分 -->	
<div class="main_m">
<% 
List<Exceptions> list = (List<Exceptions>)request.getAttribute("result");
PageManager<Exceptions> manager = (PageManager<Exceptions>)request.getAttribute("manager");
if(list != null && list.size()>0){
%>	
	<table border="0" cellspacing="0" cellpadding="0" width="100%" class="table" id="table">
		<thead>
			<tr class="tit">
			<th width="10%">fileName</th>
			<th>exceptionCode</th>
			<th>exceptionAddress</th>
			<th>descript</th>
			<th>appVersion</th>
			<th>computerName</th>
			<th>userName</th>
			<th>register</th>
			<th>stackInfo</th>
			<th>osVersion</th>
			<th>graphic</th>
			<th>driver</th>
			<th>uploadTime</th>
			<th>phoneNumber</th>
			<th>QQorMSN</th>
			<th>Email</th>
			<th width="10%">fileName</th>
			</tr>
		</thead>
		<tbody>
		<%for(Exceptions item:list){
			String filePath = "";
			if( item.getRelativePath() != null && !item.getRelativePath().isEmpty()	)
			{
				filePath = filePath + item.getRelativePath() + java.io.File.separator;
			}

			filePath = filePath + item.getUpload();
			boolean validFile = true; // just now, always true. 
		%>
		<tr>
			<td><%if( validFile ) {%>
			<a href="<%=path %>/fileDownload?fileName=<%=filePath%>"><%=filePath%></a><%}else{%><%=filePath%></a><%}%>
			</td>
			<td><%=item.getExceptionCode() %></a></td>
			<td><%=item.getExceptionAddress() %></a></td>
			<%if(item.getDescript() != null){ %>
			<td><textarea rows="" cols="30" readonly><%=item.getDescript() %></textarea></td>
			<%} else{%>
			<td></td>
			<%} %>
			<%if(item.getAppversion() != null){ %>
			<td><%=item.getAppversion() %></a></td>
			<%} else{%>
			<td></td>
			<%} %>
			<%if(item.getComputerName() != null){ %>
			<td><%=item.getComputerName() %></a></td>
			<%} else{%>
			<td></td>
			<%} %>
			<%if(item.getUserName() != null){ %>
			<td><%=item.getUserName() %></a></td>
			<%} else{%>
			<td></td>
			<%} %>
			<%if(item.getRegister() != null){ %>
			<td><%=item.getRegister() %></a></td>
			<%} else{%>
			<td></td>
			<%} %>
			<%if(item.getStackInfo()!= null){ %>
			<td class="zl"><%=item.getStackInfo() %></a></td>
			<%}else{ %>
			<td></td>
			<%} %> 
			<%if(item.getOsVersion()!= null){ %>
			<td class="zl"><%=item.getOsVersion() %></a></td>
			<%}else{ %>
			<td></td>
			<%} %> 
			<%if(item.getGraphic()!= null){ %>
			<td><%=item.getGraphic() %></a></td>
			<%}else{ %>
			<td></td>
			<%} %> 
			<%if(item.getDriver()!= null){ %>
			<td>
			<%=item.getDriver() %></a></td>
			<%}else{ %>
			<td></td>
			<%} %> 
			<%if(item.getUploadTime()!=null){ %>
			<td><%=DateParser.getInstance().getDayTme(DateParser.DATETIME_FORM2,item.getUploadTime()) %></td>
			<%}else{ %>
			<td></td>
			<%} %>
			<%if(item.getPhoneNumber() !=null){ %>
			<td><%=item.getPhoneNumber() %></td>
			<%}else{ %>
			<td></td>
			<%} %>
			<%if(item.getQqOrMsn() !=null){ %>
			<td><%=item.getQqOrMsn() %></td>
			<%}else{ %>
			<td></td>
			<%} %>
			<%if(item.getEmail() !=null){ %>
			<td><%=item.getEmail() %></td>
			<%}else{ %>
			<td></td>
			<%} %> 
			<td><%if( validFile ) {%>
			<a href="<%=path %>/fileDownload?fileName=<%=filePath%>"><%=filePath%></a><%}else{%><%=filePath%></a><%}%></td>
			<td><a href="javascript:deleteFile('<%=path %>','<%=item.getId() %>');">删除</a></td>
		</tr>
		<%} %>
		</tbody>
	</table>
	<div align="right">
			<%
			if (manager != null) {
				out.print(manager.getButtonListPages());
				out.print(manager.getJavaScript());
			}
			%>
	</div>
<%} 
%>
<div class="msg_">
</div>
<div class="tablebon"></div>
</div>
</body>
</html>