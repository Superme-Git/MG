<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/js/chart.js" > </script>
<link rel="stylesheet" href="/css/status.css" />
<title>跨数据表图表配置</title>
</head>

<body>

<#include "head.ftl">


<p class="status"></p>

<a href="chartadder">增加跨数据表图表</a>

<p>共有${chartlist?size} 个图表</p>

<ol>
<#list chartlist as t>
	<li >
		<h5>${t.chartName}</h5>
		<textarea readonly rows="${t.rows}" cols="160">${t.xmlDescription}</textarea><br>
		<button class="remove" chartname="${t.chartName}"> 删除 </button>
	</li>
</#list>
</ol>



<#include "tail.ftl">

</body>
</html>