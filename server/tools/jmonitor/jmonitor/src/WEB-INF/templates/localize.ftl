<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/jslib/xmlwriter.js" > </script>

<script src="/js/localize.js" > </script>
<link rel="stylesheet" href="/css/status.css" />
<style>
	.hide {display: none;}
	.xml { width:800px; border: solid 1px lightgray; }
</style>
<title>本地化</title>
</head>

<body>

<#include "head.ftl">


<p class="status"></p>

<p class="beaninfo hide">${beaninfo}</p>

<p>两种类型，一种是localize， 一种是basekey，localize.key可包含属性extends指向basekey.name, 表示继承</p>
<select class="bean">
</select>
<br>

<textarea class="xml" spellcheck="false" >
</textarea>
<br>

<button class="add">增加配置</button>

<p>共有${locals?size} 个图表</p>
<ol>
<#list locals as t>
	<li >
		<h5>${t.beanName}</h5>
		<textarea readonly rows="${t.rows}" cols="160">${t.xmlDescription}</textarea><br>
		<#if t.readOnly >
		<#else>
		<button class="remove" bean="${t.beanName}" mytype="${t.type}"> 删除 </button>
		</#if>
	</li>
</#list>
</ol>


<#include "tail.ftl">


</body>
</html>