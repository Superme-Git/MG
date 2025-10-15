<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js"></script>
<style>
	.url {width: 320px;}
</style>
<title>T</title>
</head>

<body>

<#include "head.ftl">

<a href="cfg/configure">configure</a>
<a href="cfg/jmxport">jmxport</a>
<a href="cfg/access">access</a>
<a href="cfg/lastfetch">lastfetch</a>
<a href="cfg/logging.properties">logging.properties</a>
<br/>

URL: <input class="url"/> <button class="mbeaninfo">mbeaninfo</button>
<br/>

<pre class="result"></pre>

<#include "tail.ftl">

<script>
	$(function(){
		$(".result").ajaxComplete(function(e, xhr, settings){
			$(this).html(xhr.status + " " + xhr.statusText + "\n" + xhr.responseText);
		});
		
		$('.mbeaninfo').click(function(){
			$.post('test/mbeaninfo', {url: $('.url').val()});
		});
	});
</script>

</body>
</html>