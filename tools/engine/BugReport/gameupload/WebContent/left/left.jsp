<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
<head>
	<title>BUGREPORT管理系统</title>
	<link type="text/css" rel="stylesheet" href="<%=path %>/css/main.css" />
	<script type="text/javascript" src="<%=path %>/js/jq131.js"></script>
	<style type="text/css">
	html{
	scrollbar-face-color:#FAE58D;
	scrollbar-track-color:#f1f1f1;
	scrollbar-arrow-color:#FF0000;
	scrollbar-darkshadow-color:#FDE582;
	scrollbar-shadow-color:#AC7101;
	scrollbar-3dlight-color:#FDE582;
	}
	</style>
	<script language="javascript">
	function switchSysBar(){
		var switchPoint = document.getElementById("on_of_value");
		if (switchPoint.value==3){
			switchPoint.value=4;
			var doc = window.parent.document.getElementById("lfrom");
			doc.cols="15,*";
			var div1 = document.getElementById("hidden");
			div1.style.display="";//显示
			var div2 = document.getElementById("show");
			div2.style.display="none";//隐藏
			document.getElementById('main').className = "lf_m1"; 
		}
		else{
			switchPoint.value=3;
			window.parent.document.getElementById("lfrom").cols="236,*";
			document.getElementById("hidden").style.display="none";//隐藏
			document.getElementById("show").style.display="";//显示
			document.getElementById('main').className = "lf_m"; 
		}
	}
		
	function each_(t){
		var el = document.getElementById('mu').getElementsByTagName('a');
		for(var i=0; i<el.length; i++){
			el[i].className = '';
		}
		t.className = 'zxz';
	}
	</script>
</head>
<body class="lf_m" id="main">
<div id="hidden" class="rt_t" style="display:none">
	<span class="op" id="isop" onclick="switchSysBar();"></span>
</div>
<div id="show">
<div class="bar lf_t">
	<span class="op" id="isop" onclick="switchSysBar();"></span>
	<span class="txt c1">控制面板</span>
	<img src="<%=path %>/images/bar_11.gif" id="on_of" class="tt"/>
	<input type="hidden" id="on_of_value" value="3"/>
</div>
<div class="lf_mu" id="mu">
<a href="<%=path %>/jsp/upload.jsp" target="mainFrame"  onclick="each_(this)"><img src="<%=path %>/images/bar_21.gif" /><span>上传文件测试</span></a>
<a href="<%=path %>/fileList" target="mainFrame"  onclick="each_(this)"><img src="<%=path %>/images/bar_21.gif" /><span>下载文件</span></a>
</div>
</div>
</body>
</html>
