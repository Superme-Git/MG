$(function() {
	var MAXLVL = 6;
	var DAYLINELIMIT = 7;
	var DATEFORMAT = '%y/%m/%e %H:%M';

	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 60 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});
	
	
	function status(txt) {
		$(".status").text(txt);
	}

	var chaininfo = JSON.parse($('.chaininfo').html());
	var localinfo = JSON.parse($('.localinfo').html());

	var cns = [];
	for ( var cn in chaininfo) {
		cns.push(cn);
	}
	treeDotString('#tree', cns);



	function vanishLevel(lvl) {
		for ( var i = lvl; i <= MAXLVL; i++) {
			$('#action' + i).html('');
			$('#holder' + i).html('');
		}
	}

	var timenode = $('#action0 .time');
	var timelownode = $('#action0 .timelow');
	var timeupnode = $('#action0 .timeup');
	var zidnode = $('#action0 .zid');
	var querynode = $('#action0 .query');
	
	function getTimeRange() {
		var r = {};
		var low = timelownode.val();
		if (low) {
			low = low.replace("-", "/");
			low = low.replace("-", "/");
			r.low = Date.parse(low);
		} else {
			return r;
		}

		var up = timeupnode.val();
		if (up) {
			up = up.replace("-", "/");
			up = up.replace("-", "/");
			r.up = Date.parse(up);
		}

		return r;
	}
	
	var selected_time = null;
	var selected_zid = null;
	
	function onTimeChange() {
		var v = +timenode.val();
		selected_time = v;
		
		if (v === -2) {
			timelownode.removeClass('hide');
			timeupnode.removeClass('hide');
		} else {
			timelownode.addClass('hide');
			timeupnode.addClass('hide');
		}
	}

	
	function onZidChange(){
		var v = +zidnode.val();
		selected_zid = v;
	}
	
	timenode.change(onTimeChange);
	zidnode.change(onZidChange);

	function setSelectOption(tag, val) {
		var selected = false;
		$('#action0 .' + tag + ' option').each(function(i, e) {
			var n = $(this);
			var v = +n.val();
			n.removeAttr('selected');
			if (v === val || (!val && v === -1)) {
				n.attr('selected', 'selected');
				selected = true;
			}
		});
		if (selected === false && val > 0) {
			var nd = $('#action0 .' + tag);
			nd.html('<option value="' + val + '">' + val + '</option>' + nd.html());
		}
	}
	
	function setTimeOptionAtFirstSeenOrAboveCurrent(val){
		var old = +$('#action0 .time').val();
		if (selected_time === null || (old !== -2 & old !== -1 && (val === -1 || val > old)) ){
			setSelectOption("time", val);
			selected_time = val;
		}
	}
	
	function setZidOptionAtFirstSeen(val){
		if (selected_zid === null){
			setSelectOption("zid", val);
			selected_zid = val;
		}
	}

	$('#tree a').each(function() {
		$(this).click(function() {
			$('#tree .active').removeClass('active');
			$(this).addClass('active');
			var name = $(this).attr('data');
			
			$('#action0').removeClass('hide');
			
			var info = chaininfo[name];
			if (!info){
				$(".status").text('no such chain ' + name);
				return;
			}
			var isBean = !info.first.chart;
			
			if (isBean){
				var time = info.first.days;
				var zid = info.first.zid;
				
				setTimeOptionAtFirstSeenOrAboveCurrent(time);
				setZidOptionAtFirstSeen(zid);
			}
			querynode.text(name);
			queryClick(0);
		});
	});
	
	
	querynode.live("click", function() {
		queryClick(0);
	});

	function queryClick( lvl ) {
		var chainName = querynode.text();
		var info = chaininfo[chainName];

		if (!info) {
			$(".status").text('no such chain ' + chainName);
			return;
		}

		var maxlvl = info.nexts.length;
		if (lvl > maxlvl) {
			$(".status").text('maxlvl到头了');
			return;
		}
		
		vanishLevel(lvl + 1);

		
		//得到基本属性
		
		var isBean = !info.first.chart;
		var chartName = info.first.name;
		var beanName = chartName;
		var firstBeanName = isBean ? beanName : null;
		
		if (isBean){
			var byKey = info.first.key;
			var time = info.first.days;
			var zid = info.first.zid;
			var values = info.first.values;
			var sepkey = info.first.sepkey;
			var graph = info.first.graph;
			var type = info.first.type;
			var pieslices = info.first.pieslices;
		}
		if (lvl > 0){
			var nxt = info.nexts[lvl - 1];
			if (nxt.chart) {
				isBean = false;
				chartName = nxt.name;
			}else{
				byKey = nxt.key;
				sepkey = nxt.sepkey;
				values = nxt.values;
				graph = nxt.graph;
				type = nxt.type;
				pieslices = nxt.pieslices;
			}
		}
			
		
		//计算约束
		var ranges = {};
		var dayline = true;
		
		var tm = +timenode.val();
		if (tm > 0) {
			dayline = (tm > DAYLINELIMIT);
			ranges['time'] = {
				low : -tm
			};
		} else if (tm === -1) {
			dayline = true;
			// 全部时间
		} else if (tm === -2) {
			var r = getTimeRange();
			if (!r.low) {
				status("time low must be set");
				return;
			}
			var up = r.up;
			if (!up)
				up = new Date().getTime();

			if (up <= r.low) {
				status("time up <= low");
				return;
			}

			ranges['time'] = r;
			dayline = (up - r.low) > (DAYLINELIMIT * 86400000);
		}

		var zi = +zidnode.val();
		if (!isNaN(zi) && zi != -1)
			ranges['zid'] = {
				low : zi,
				up : zi
			};
		
		for ( var i = 1; i <= lvl; i++) {
			var kn = $('#action' + i + ' .kn').val();
			var kv = +($('#action' + i + ' .kv').val());
			ranges[kn] = {
				low : kv,
				up : kv
			};
		}
		
		///////////////////////////

		var cond = {};
		var url;
		if (isBean) {
			status('query ' + beanName + ' ' + byKey);
			cond.bean = beanName;
			cond.key = byKey;
			cond.values = values;
			cond.dayline = dayline;
			cond.ranges = ranges;
			
			if (sepkey)
				cond.sepkey = sepkey;

			url = "/chartchaindata"
		} else {
			status("query " + chartName);
			cond = {
				chartName : chartName,
				ranges : ranges
			};
			url = "/chartdata"
		}

		$.ajax({
			type : "POST",
			url : url,
			data : {
				condition : JSON.stringify(cond)
			},
			success : success,
		});

		// //////////////////////////////////
		
		var zlocal = {};
		$('select.zid > option').each(function(){
			zlocal[ $(this).val() ] = $(this).html();
		});

		function success(data, textstatus, xhr) {

			data = JSON.parse(data);

			function myclick(kn, kv) {
				vanishLevel(lvl + 1);
				$('#action' + (lvl + 1)).html('<input class="kn hide" value="' + kn + '"/><input class="kv hide" value="' + kv + '"/>');
				queryClick(lvl + 1);
			}
			
			drawChart("holder"+lvl, data, isBean, cond, myclick, localinfo, zlocal, graph, type, firstBeanName, pieslices);
		}
	}

});