<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/jslib/jquery.js" > </script>
<script src="/js/table.js" > </script>
<link rel="stylesheet" href="/css/status.css" />
<style>
	.addable textarea { color : #AA4643;  }
	.hide {display: none;}
</style>
<title>数据表</title>
</head>

<body>

<#include "head.ftl">

<p class="status"></p>

<a href="server">若要增加数据表，请在服务器页面选择一个服务器抓取数据</a>

<p>共有${tablelist?size}个数据表 。太多了？那就过滤一下：
<input class="include" placeholder="include"/>
<input class="exclude" placeholder="exclude" value="xdb_Table|java_lang|xio_"/>
可修改属性如下：
</p>
<dl>
<dt>table.fetch_offset_minute</dt><dd>默认为0，系统会在每天的offset + period*[N]出取数据，这里时间为格林威治标准时间，如果要在北京时间8:30取，请设置为30</dd>
<dt>table.fetch_period_minute</dt><dd>默认为10，表示10分钟取一次数据</dd>
<dt>table.peak</dt><dd>默认为false，表示value为两次fetch时间之间的累计值，整天汇总时SUM；设置为true则表示状态值，整天汇总时根据下面的sample再抽样，非时间横轴绘图时取平均状态</dd>
<dt>table.sample</dt><dd>默认为360，表示每隔6小时抽样一次作为当天汇总数据，只在peak为true时起作用</dd>
<dt>table.dynamic_mbean</dt><dd>默认不出现，表示不是动态MBean，当待添加的table里有2个时表示可能为动态MBean,请自己做好选择（主要是xdb.util.Counter）</dd>
<dt>table.tablename</dt><dd>默认不出现，数据表名称由domain_type_name属性值组合而成，跟标题一样。若组合值长度>32则默认生成一个tablename</dd>
<dt>table.[key]column.name</dt><dd>默认不出现，由attr属性生成，如果长度>64则默认生成一个name</dd>
</dl>

<ol>
<#list tablelist as t>
<#if t.isWorking >
	<li class="working">
		<h5>${t.identification}</h5>
		<textarea rows="${t.rows}" cols="160" readonly>${t.xmlDescription}</textarea><br>
		<button class="remove" identification="${t.identification}"> 删除 </button>
	</li>
<#else>
	<li class="addable">
		<h5>${t.identification}</h5>
		<textarea rows="${t.rows}" cols="160" spellcheck="false">${t.xmlDescription}</textarea><br>
		<button class="add"> 增加 </button>
	</li>
</#if>
</#list>
</ol>


<#include "tail.ftl">

</body>
</html>