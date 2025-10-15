<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/jslib/xmlwriter.js" > </script>
<script src="/jslib/jquery.js" > </script>
<script src="/jslib/util.js" > </script>

<script src="/jslib/highcharts.js" > </script>
<script src="/js/drawchart.js" > </script>
<script src="/js/chartchainadder.js" > </script>

<link rel="stylesheet" href="/css/status.css" />
<style>
	.hide {display: none;}
	.active {background: #cc0;}
	.xml { width:800px; height:200px; border: solid 1px lightgray; }
</style>
<title>查看图表</title>
</head>

<body>
<#include "head.ftl">


<p class="status"></p>

<p class="chartinfo hide">${chartinfo}</p>
<p class="tableinfo hide">${tableinfo}</p>

<select class="name">
	<optgroup label="数据表">
		<#list tablenames as tn>
		<option value="${tn}">${tn}</option>
		</#list>
	</optgroup>
	<optgroup label="图表">
		<#list chartnames as cn>
		<option value="${cn}">${cn}</option>
		</#list>
	</optgroup>
</select>

<select class="time hide">
	<option value="1">最近1天</option>
	<option value="2">最近2天</option>
	<option value="3">最近3天</option>
	<option value="7">最近7天</option>
	<option value="30">最近1月</option>
	<option value="90">最近3月</option>
	<option value="365">最近1年</option>
	<option value="-1">全部</option>
</select>
<input class="zid hide" size="5" placeholder="zid"/>

<span id="action0"></span>
<br>
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

<hr>


<input class="chartChainName" placeholder="图表链名称"/>
<button class="generatexml">生成配置</button>
<br>

<dl>
<dt>first|next.graph</dt><dd>可选为(line, pie, column)，默认为：横轴为时间为line，只有一条线且key后缀不是id或level则为pie，其他都是column</dd>
<dt>first|next.type</dt><dd>默认无此属性，只有在key为time,graph为line时允许加入此属性，可为counter或rate或counterrate,counter表示绘图时需要用当前值减去前继值.rate表示换算成/s速率</dd>
<dt>first|next.pieslices</dt><dd>默认无此属性，只有在graph为pie时有用，表示允许切割的片数</dd>
</dl>

<textarea spellcheck="false" class="xml" placeholder="图表链配置"></textarea>
<br>
<button class="addChartChain">增加图表链</button>
<br>


<#include "tail.ftl">

</body>

</html>