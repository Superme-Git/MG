<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="UTF-8"%>
<%
	String result = (String)session.getAttribute("result");
	String path = request.getContextPath();
%>    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>result</title>
</head>
<body>
    <br/>
	<%=result%>
	<br/>
	<hr/>
	<a href="<%=path %>/jsp/upload.jsp" >继续上传</a>
</body>
</html>