$( function() {
	
	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 60 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});
	
	function status(txt) { $(".status").text(txt); }
	

	var namenode = $('.name');
	var timenode = $('.time');
	var zidnode = $('.zid');
	
	var DATEFORMAT = "MM/dd/HH:mm";
	var MAXLVL = 6;
	var DAYLINELIMIT = 7;
	
	var chartinfo = JSON.parse( $(".chartinfo").text() );
	var tableinfo = JSON.parse( $(".tableinfo").text() );
	
	var keycharts = {zid: [], time: []};
	$.each(chartinfo, function(cn, keylist){
		
		$.each(keylist, function(i, key){
			if (keycharts[key])
				keycharts[key].push(cn);
			else
				keycharts[key] = [];
		});
		
		if (keycharts.zid.indexOf(cn) == -1)
			keycharts.zid.push(cn);
		if (keycharts.time.indexOf(cn) == -1)
			keycharts.time.push(cn);
	});

	//value字段选择
	function onValueChange( v, lvl ){
		var id = '#action'+lvl;
		
		if (v === '全部'){
			$( id + ' .qk').addClass('hide');
			onSepKeyChange('不分段', lvl);
		}else{
			var n = $( id + ' .qk');
			n.removeClass('hide');
			onSepKeyChange(n.val(), lvl);
		}
	}
	
	//sepkey字段选择
	function onSepKeyChange( v, lvl ){
		var id = '#action'+lvl;
		
		if (v === '不分段'){
			$( id + ' .qkr_min').addClass('hide');
			$( id + ' .qkr_max').addClass('hide');
			$( id + ' .qkr_sep').addClass('hide');
			$(id + ' .qb').each( function(){ 
				$(this).removeAttr('disabled');
			});
			
		}else{
			$( id + ' .qkr_min').removeClass('hide');
			$( id + ' .qkr_max').removeClass('hide');
			$( id + ' .qkr_sep').removeClass('hide');
			
			$(id + ' .qb').each( function(){ 
				if ($(this).text() === v )
					$(this).attr('disabled', 'disabled');
				else
					$(this).removeAttr('disabled');
			});
		}
	}
	
	function beanQueryMarkup( bean, constrainkey, keylist ){
		var mk = '<select class="qv"><option selected>全部</option>';
		for(var i=0; i<bean.valueList.length; i++)
			mk += '<option>' + bean.valueList[i] + '</option>';
		mk += '</select>';
	
		mk += '<select class="qk hide"><option>不分段</option>';
		for(var i=0; i<bean.keyList.length; i++){
			if(constrainkey && bean.keyList[i] == constrainkey)
				continue;
			mk += '<option>'+ bean.keyList[i] + '</option>';
		}
		mk += '</select>';
		mk +='<input class="qkr_min hide" size="5" placeholder="min">';
		mk +='<input class="qkr_max hide" size="5" placeholder="max">';
		mk +='<input class="qkr_sep hide" size="5" placeholder="sep">';
		
		for(var i=0; i<keylist.length; i++){
			if(constrainkey && keylist[i] == constrainkey)
				continue;
			mk += '<button class="qb">' + keylist[i] + '</button>';
		}
			
		return mk;
	}
	
	function  markupAction(lvl, mk){
		var id = '#action' + lvl;
		$(id).html(mk);
		var qv = $(id + ' .qv');
		if (qv.length > 0){
			qv.change(function(){ onValueChange( qv.val(), lvl ); });
		}
		
		var qk = $(id + ' .qk');
		if (qk.length > 0)
			qk.change(function(){ onSepKeyChange( qk.val(), lvl ); });
		
		$(id + ' .qb').each( function(){ 
			$(this).unbind('click');
			$(this).click(function(){ queryClick($(this), lvl, true); });
		});
		
		$(id + ' .qc').each( function(){
			$(this).unbind('click');
			$(this).click(function(){ queryClick($(this), lvl, false); });
		});
	}
	
	
	function getValueAndSepKey(bean, lvl){
		var res = {ok: true};
		
		var qv = $('#action'+lvl + ' .qv').val();
		if (qv === '全部'){
			res.values = bean.valueList;
		}else{
			res.values = [qv];
			
			var qk = $('#action'+lvl + ' .qk').val();
			if (qk !== '不分段'){
				var min = $('#action'+lvl + ' .qkr_min').val();
				var max = $('#action'+lvl + ' .qkr_max').val();
				var sep = $('#action'+lvl + ' .qkr_sep').val();
				
				if (!min || !max || !sep ){
					status("Fill SepKey Param!");
					res.ok = false;
					return res;
				}
				min = +min;
				max = +max;
				sep = +sep;
				
				if (isNaN(min) || isNaN(max) || isNaN(sep)){
					status("SepKey Param must be Number");
					res.ok = false;
					return res;
				}
				
				res.sepkey = { key: qk, min: min, max: max, sep: sep}; 
			}
		}
		return res;
		
	}
	
	// 图表名称选择
	
	function onNameChange(){
		var n = namenode.val(),
			ti = tableinfo[n],
			mk = '';
		if (ti){
			timenode.removeClass("hide");
			zidnode.removeClass("hide");
			mk = beanQueryMarkup(ti, null, ti.keyList);
		}else{
			timenode.addClass("hide");
			zidnode.addClass("hide");
			mk = '<button class="qc">' + n + '</button>'
		}
		
		markupAction(0, mk);
	}
	
	onNameChange();
	namenode.change(onNameChange);
	
	//生成图表链
	
	var chainnamenode = $(".chartChainName");
	
	var xmlnode = $('.xml');
	$('.generatexml').click(function(){
		var base = namenode.val();
		var bean = tableinfo[base];
		
		var n0 = $('#action0 .active');
		if (n0.length === 0){
			status('no active chart or bean');
			return;
		}
		var chainname = chainnamenode.val();
		if ( !chainname ){
			status("请填写图表链名称");
			return;
		}
			
		var xw = new XMLWriter('UTF-8');
		xw.indentation = 4;
		xw.indentChar = ' ';
		xw.document().element('chartchain').attr('name', chainname).endElement();
		
		xw.element('first');
		if (bean){
			
			var res = getValueAndSepKey(bean, 0);
			if ( !res.ok )
				return;
			
			var time = +timenode.val(),
			    zid = zidnode.val();
			xw.attr('bean', base).attr('key', n0.text()).attr('values', res.values.join(','));
			if (time > 0)
				xw.attr('days', time);
			if (zid )
				xw.attr('zid', zid);
			if (res.sepkey)
				xw.attr('sepkey', [res.sepkey.key, res.sepkey.min, res.sepkey.max, res.sepkey.sep].join(','));
		}
		else{
			xw.attr('chart', base);
		}
		xw.endElement();
		
		for(var lvl = 1; lvl <= MAXLVL; lvl++){
			var n = $('#action' + lvl + ' .active');
			if (n.length === 0)
				break;
			var isBean = (bean && n.hasClass('qb'));
			xw.element('next');
			if (isBean){
				xw.attr('key', n.text());
				
				var res = getValueAndSepKey(bean, lvl);
				if (!res.ok)
					return;
				
				xw.attr('values', res.values.join(','));
				if (res.sepkey)
					xw.attr('sepkey', [res.sepkey.key, res.sepkey.min, res.sepkey.max, res.sepkey.sep].join(','));
			}else{
				xw.attr('chart', n.text());
			}
			
			xw.endElement();
		}
		
		xw.endDocument();
		xmlnode.val(xw.flush());
	});
	
	
	//添加图表链
	
	$(".addChartChain").click(function() {
		var xmltext = xmlnode.val();
		if (!xmltext){
			alert("请填写图表链配置");
			return;
		}
		
		$.ajax({
			type: "POST",
			url : "/chartchain",
			data: {xml: xmltext},
		});
	});
	
	//////////////////////////////////////////////
	
	function vanishLevel( lvl ){
		for(var i=lvl; i<=MAXLVL; i++){
			$('#action' + i).html('');
			$('#holder' + i).html('');
		}
	}
	
	function vanishButtonFlag( lvl ){
		$('#action' + lvl + ' .qb').each( function(){ 
			$(this).removeClass('active');
		});
		$('#action' + lvl + ' .qc').each( function(){ 
			$(this).removeClass('active');
		});
	}
	
	function queryClick(node, lvl, isBean){
		var chartName = node.text();
		var base = namenode.val();
		var bean = tableinfo[base];
		
		if (isBean && !bean){
			status("query " + base + " not bean!");
			return;
		}
		
		vanishButtonFlag( lvl );
		
		vanishLevel(lvl+1);
		
		var byKey = chartName;
		if (isBean)
			status( 'query ' + base + ' ' + byKey);
		else
			status( "query " + chartName );
		
		
		
		var dayline;
		var ranges = {};
		var avail_keys = [];
		
		var time = +timenode.val();
		if (time > DAYLINELIMIT || time <= 0)
			dayline = true;
		else
			dayline = false;
		if (time > 0)
			ranges['time'] = {low: -time};
			
		var zid = zidnode.val();
		if (zid && !zid)
			ranges['zid'] = {low: +zid, up: +zid};
		
		if (bean){
			for(var i=0; i<bean.keyList.length; i++)
				avail_keys.push(bean.keyList[i]);
		}
			
		var avail_charts;
		for(var i=1; i<=lvl; i++){
			var kn = $('#action' + i + ' .kn').val();
			var kv = +($('#action' + i + ' .kv').val());
			ranges[kn] = {low : kv, up : kv};
			
			var cc = keycharts[kn];
			if (cc){
				if (i === 1)	avail_charts = cc;
				else			avail_charts = intersect( avail_charts, cc );
			}
			
			if (bean){
				var idx = avail_keys.indexOf(kn);
				if (idx !== -1)
					avail_keys.splice(idx, 1);
			}
		}
		
		var cond = {};
		var url;
		if (isBean){
			cond.bean = base;
			cond.key = byKey;
			
			var res = getValueAndSepKey(bean, lvl);
			if (!res.ok)
				return;

			cond.values = res.values;
			if (res.sepkey)
				cond.sepkey = res.sepkey;
				
			cond.dayline = dayline;
			cond.ranges = ranges;
			
			url = "/chartchaindata"
			
		}else{
			cond = {chartName: chartName, ranges: ranges};
			url = "/chartdata"
		}
		
		node.addClass('active');
		node.attr("disabled", "disabled");
		$.ajax({
			type: "POST",
			url : url,
			data: {condition: JSON.stringify(cond)},
			success: success,
			error: function(){node.removeAttr("disabled");}
		});
		
		
		////////////////////////////////////
		
		function success(dat, textstatus, xhr) {
			node.removeAttr("disabled");
			
			dat = JSON.parse(dat);
			
			function myclick(kn, kv){
				var cc = keycharts[kn];
				if (cc && lvl > 0)
					cc = intersect(cc, avail_charts);
				
				var mk = '<input class="kn" value="' + kn + '" readonly/>' + '<input class="kv" value="' + kv + '" readonly/>';
				
				mk += beanQueryMarkup(bean, kn, avail_keys);
				
				if (cc){
					for(var i=0; i<cc.length; i++)
						mk += '<button class="qc">' + cc[i] + '</button>';
				}

				vanishLevel( lvl+1);
				markupAction(lvl+1, mk);
			}
			
			drawChart("holder"+lvl, dat, isBean, cond, myclick, null, null, null, null, null);
		}
	}
	
	
	
});
