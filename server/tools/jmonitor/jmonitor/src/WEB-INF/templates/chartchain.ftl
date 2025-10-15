<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/js/chartchain.js" > </script>
<link rel="stylesheet" href="/css/status.css" />
<title>图表链</title>
</head>

<body>

<#include "head.ftl">

<a href="chartchainadder">增加图表链</a><br>
<a href="chart">跨数据表图表配置</a>

<p class="status"></p>

<p>共有${chartchainlist?size} 个图表链</p>

<ol>
<#list chartchainlist as t>
	<li >
		<h5>${t.chartChainName}</h5>
		<textarea readonly rows="${t.rows}" cols="160">${t.xmlDescription}</textarea><br>
		<button class="remove" chainname="${t.chartChainName}"> 删除 </button>
	</li>
</#list>
</ol>

<#include "tail.ftl">

</body>
</html>