<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/js/server.js" > </script>
<link rel="stylesheet" href="/css/status.css" />
<style>
	.opened, .fetch, .authcok {color : #89A54E;}
	.closed, .cremove, .authcerr {color: #AA4643;}
	.url {width: 320px;}
</style>
<title>服务器</title>
</head>

<body>

<#include "head.ftl">

<p class="status"></p>

<#if authcok> 
	<p class="authcok">
<#else>
	<p class="authcerr">
</#if>
${authcstatus}</p>


<p>共有${serverlist?size}个服务器 ,新增一个?  serverId: <input class="serverid"/>URL: <input class="url"/>name:<input class="name"/><button class="add">add</button></p>

<ol>
<#list serverlist as s>
<#if s.opened >
	<li class="opened">${s.id?c} -- ${s.description} --  
	 <#list s.extra?keys as k>,${k}=${s.extra[k]}</#list>
	 <br/>
	</li>
	<button class="remove" serverid="${s.id?c}"> remove </button>
	<button class="fetch" serverid="${s.id?c}"> fetch </button> -- ${s.url}
<#else>
	<li class="closed">${s.id?c} -- ${s.description} -- 
	<#list s.extra?keys as k>,${k}=${s.extra[k]}</#list>
	 <br/> </li>
	<button class="remove cremove" serverid="${s.id?c}"> remove </button> -- ${s.url}
</#if>
</#list>
</ol>


<#include "tail.ftl">

</body>
</html>