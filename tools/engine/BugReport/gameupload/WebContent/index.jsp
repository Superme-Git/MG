<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
	<title>BUGREPORT管理系统</title>
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/main.css" />
</head>
<frameset rows="90,*,28" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="<%=path %>/iframe/top.jsp" name="top" scrolling="No" noresize="noresize" id="top" title="top" />
  <frameset cols="236,*" frameborder="no" border="0" framespacing="0"  id="lfrom">
    <frame src="<%=path %>/left/left.jsp" name="left" scrolling="no" noresize="noresize" id="left" title="left" />
    	<frame src="<%=path %>/jsp/upload.jsp" name="mainFrame" scrolling="auto" id="main" title="main" />
  </frameset>
  <frame src="<%=path %>/iframe/bottom.htm" name="bottom" scrolling="No" noresize="noresize" id="bottom" title="bottom" />
</frameset>
<noframes><body>
您的浏览器不支持框架！
</body></noframes>
</html>
