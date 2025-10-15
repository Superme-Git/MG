<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/jslib/json2.js" > </script>
<script src="/js/access.js" > </script>
<link rel="stylesheet" href="/css/status.css" />
<title>访问控制</title>
</head>

<body>

<#include "head.ftl">

<p class="status"></p>

<p>Welcome ${user}</p>

<#list roleForbids?keys as role>
<h5>${role} 禁止访问路径</h5>
<table>
	<#list roleForbids[role] as forbid>
	<tr><td><i>${forbid.methods}</i></td><td>${forbid.path}</td></tr>
	</#list>
</table>
</#list>

<#list roleUsers?keys as role>
<h5>${role} 用户</h5>
<ol>
	<#list roleUsers[role] as u>
	<li>${u}</li>
	</#list>
</ol>
</#list>

<select class="role">
<#list roleUsers?keys as role>
	<option value="${role}">${role}</option>
</#list>
</select>

<input class="user" placeholder="用户">
<button class="add">添加</button>
<button class="remove">删除</button>

<#include "tail.ftl">

</body>
</html>