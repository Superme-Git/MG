<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/json2.js" > </script>
<script src="/jslib/jquery.js" > </script>
<script src="/js/tabledatadumper.js" > </script>

<link rel="stylesheet" href="/css/status.css" />
<style>
	.hide {display: none;}
</style>

<title>数据下载</title>
</head>

<body>

<#include "head.ftl">


<p class="status"></p>


<label>类别</label>
<select class="identification">
	<#list tablenames as tn>
		<option value="${tn}">${tn}</option>
	</#list>
</select>

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

<button class="dump">抓取数据</button>

<iframe id="downloadfrm" class="hide" src=""></iframe>


<#include "tail.ftl">

</body>

</html>