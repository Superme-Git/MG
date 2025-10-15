$( function() {
	
	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 60 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});
	
	
	var tableinfo = JSON.parse( $(".tableinfo").text() );
	
	var idhtml = "";
	$.each(tableinfo, function(k, v){
		idhtml += "<option>" + k + "</option>";
	});
	$(".identification").html(idhtml);
	
	function fillvalkey(){
		var id = $(".identification").val();
		if ( !id )
			return;
		var valhtml = '';
		$.each(tableinfo[id].valueList, function(index, val){
			valhtml += "<option>" + val + "</option>";
		});
		$(".value").html(valhtml);
		
		var keyhtml = '';
		var wherehtml = "";
		$.each(tableinfo[id].keyList, function(index, key){
			var itype = "number";
			if (key === "time")	{
				keyhtml += "<option selected>" + key + "</option>";
				itype = "date";
			}
			else keyhtml += "<option>" + key + "</option>";
			wherehtml += '<label>' + key + '</label><span id="' + key + '"><input type="'+ itype + '" class="low"/> <input type="' + itype + '" class="up"/>'  + '</span><br>'; 
		});
		$(".key").html(keyhtml);
		$(".where").html(wherehtml);
	}
	
	fillvalkey();
	$(".identification").change(function(){ fillvalkey(); filltime(); });
	
	
	//响应时间快捷选择
	function filltime(  ){
		var bef = +$(".timesel").val();
		var lownode = $("#time .low");
		if (bef !== -1){
			lownode.val("-"+bef);
		}else{
			lownode.val("");
		}
	}
	
	filltime();
	$(".timesel").change( filltime );
	
	
	function getRequest(){
		var data = {};
		data.bean = $(".identification").val();
		data.values = [$(".value").val()];
		data.key = $(".key").val();
		data.ranges = {};
		$.each(tableinfo[data.bean].keyList, function(index, k){
			var low = $("#" + k + " .low").val();
			var up = $("#" + k + " .up").val();
			
			if (low.length > 0 || up.length > 0){
				data.ranges[k] = {};
				if (low.length > 0){
					if (k === "time" && low.charAt(0) !== '-' &&  low.charAt(0) !== '0'){
						low = low.replace("-", "/");
						low = low.replace("-", "/");
						data.ranges[k].low = Date.parse(low);
					}
					else
					 	data.ranges[k].low = +low;
				}
				if (up.length > 0){
					if (k === "time" && up.charAt(0) !== '-' && up.charAt(0) !== '0' ){
						up = up.replace("-", "/");
						up = up.replace("-", "/");
						data.ranges[k].up = Date.parse(up.replace("-", "/"))
					}
					else{
						data.ranges[k].up = +up;
					}
				}
			}
		});
		return data;
	}
	
	//生成图表XML配置
	
	var xmlnode = $(".xml");
	$(".generatexml").click(function() {
		var data = getRequest();
		var xw = new XMLWriter('UTF-8');
		xw.indentation = 4;
		xw.indentChar = ' ';
		
		xw.document()
			.element("line")
				.attr("name", "").attr('bean', data.bean).attr("value", data.values[0]).attr("key", data.key);
		
		for(var k in data.ranges){
			var v = data.ranges[k];
			xw.element("range").attr("key", k);
			if (v.low !== undefined)
				xw.attr("low", v.low);
			if (v.up !== undefined)
				xw.attr("up", v.up);
			xw.endElement();
		}
		xw.endElement().endDocument();
		var xml = xw.flush() + "\n";
		xmlnode.val(xmlnode.val() + xml);
	});
	
	// 增加图表
	
	$(".addChart").click(function() {
		var chartname = $(".chartName").val();
		var xmltext = xmlnode.val();
		if (!chartname || !xmltext ){
			alert("请填写图表名称和图表配置");
			return;
		}
		var xml = '<chart name="' + chartname + '">\n' + xmltext + '</chart>';
		
		$.ajax({
			type: "POST",
			url : "/chart",
			data: {xml: xml},
		});
	});
	
});
