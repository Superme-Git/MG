<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/xmlwriter.js" > </script>
<script src="/jslib/json2.js" > </script>
<script src="/jslib/jquery.js" > </script>
<script src="/jslib/util.js" > </script>

<script src="/jslib/treedotstring.js" > </script>
<script src="/jslib/highcharts.js" > </script>

<!--[if lt IE 9]>
	<script charset="UTF-8" src="/jslib/flashcanvas.js" > </script>
	<script charset="UTF-8" src="/jslib/exporting.js" > </script>
<![endif]-->

<script src="/jslib/rgbcolor.js" > </script>
<script src="/jslib/canvg.js" > </script>

<script src="/js/drawchart.js" > </script>
<script src="/js/chartchainviewer.js" > </script>

<link rel="stylesheet" href="/css/status.css" />
<style>
	.hide {display: none;}
	.placeholder {margin-left: 200px;}
	#tree { position: absolute; left: 20px;  width: 180px; border: solid 1px lightgray;}
	#tree .active {background : lightgray;}
	#canvas, #holder0, #holder1, #holder2, #holder3, #holder4, #holder5, #holder6 {height:440px; margin: 0 4px; min-width: 600px;}
</style>



<title>查看图表链</title>
</head>

<body>
<#include "head.ftl">

<div id="chain">

<p class="status"></p>

<p class="chaininfo hide">${chaininfo}</p>
<p class="localinfo hide">${localinfo}</p>

<div id="tree">
</div>

<div class="placeholder">

<div id="action0" class="hide">

<select class="time">
	<option value="1">最近1天</option>
	<option value="2">最近2天</option>
	<option value="3">最近3天</option>
	<option value="7">最近7天</option>
	<option value="30">最近1月</option>
	<option value="90">最近3月</option>
	<option value="365">最近1年</option>
	<option value="-1">全部</option>
	<option value="-2">选择区间</option>
</select>

<input class="timelow hide" type="date" placeholder="起始时间" size="12"/>
<input class="timeup hide" type="date" placeholder="截至时间" size="12"/>

<select class="zid">
	<option value="-1">全部</option>
	<#list servers as sv>
		<option value="${sv.id}">${sv.desc}</option>
	</#list>
</select>

<button class="query">query</button>

</div>
<div id="holder0"></div>

<div id="action1"></div>
<div id="holder1"></div>

<div id="action2"></div>
<div id="holder2"></div>

<div id="action3"></div>
<div id="holder3"></div>

<div id="action4"></div>
<div id="holder4"></div>

<div id="action5"></div>
<div id="holder5"></div>

<div id="action6"></div>
<div id="holder6"></div>

<!--[if lt IE 9]>
	<canvas id="canvas"></canvas>
<![endif]-->
<canvas id="canvas2" class="hide"></canvas>
</div>

<#include "tail.ftl">

</body>

</html>