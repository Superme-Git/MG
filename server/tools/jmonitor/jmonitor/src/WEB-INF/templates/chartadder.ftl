<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/json2.js" > </script>
<script src="/jslib/jquery.js" > </script>
<script src="/jslib/xmlwriter.js" > </script>
<script src="/jslib/util.js" > </script>

<script src="/js/chartadder.js" > </script>

<link rel="stylesheet" href="/css/status.css" />
<style>
	.xml { border: solid 1px lightgray; background: #eee;}
	.xml { width:800px; height:200px;}
	label { position: absolute; text-align: right; width: 100px; }
	span, textarea, .identification, .value, .key, .timesel, .chartName, .generatexml, .addChart { margin-left: 110px; }
	.tableinfo {display: none;}
</style>
<title>跨数据表图表添加</title>
</head>

<body>

<#include "head.ftl">

<p class="status"></p>

<p class="tableinfo">${tableinfo}</p>

<label>类别</label><select class="identification"></select>
<br>
<label>纵坐标</label><select class="value"></select>
<br>
<label>横坐标</label><select class="key"></select>
<br>

<div class="where"></div>

<label>时间快捷选择</label>
<select class="timesel">
	<option value="1">最近1天</option>
	<option value="2">最近2天</option>
	<option value="3">最近3天</option>
	<option value="7">最近7天</option>
	<option value="30">最近1月</option>
	<option value="90">最近3月</option>
	<option value="365">最近1年</option>
	<option value="-1">全部</option>
</select>
<br>

<button class="generatexml">生成配置</button>
<br>

<label>配置内容</label><textarea spellcheck="false" class="xml" placeholder="相同的range应提出到line外面作为公共约束，time字段为负数N时 代表NOW()-INTERVAL [N] DAY。生成后注意填写name属性哦"></textarea>
<br>

<label>配置名称</label><input class="chartName" placeholder="一定要填呢"/>
<br>

<button class="addChart">增加图表</button>
<br>

<#include "tail.ftl">

</body>

</html>