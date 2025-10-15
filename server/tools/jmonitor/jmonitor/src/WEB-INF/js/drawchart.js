

function drawChart(renderTo, dat, isBean, cond, myclick, localinfo, zlocal, graph, type, firstBeanName, pieslices) {
	
	var ascounter = (type == "counter" || type == "counterrate" );
	var asrate = (type == "rate" || type == "counterrate");
	
	// 使用浏览器本地时区
	Highcharts.setOptions({
		global: {
			useUTC: false
		}
	});
	
	function color(index){
		var colors = [ '#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92' ];
		return colors[index%colors.length];
	}
	
	function local(bean){
		return {
			bean : function(){
				try{
					return localinfo[bean].beanLocalName;
				}catch(e){
					return cond.bean;
				} 
			},
			key: function(key){
				if (key === 'time')
					return '时间';
				if (key === 'zid')
					return '服务器';
				try{
					return localinfo[bean].keyLocals[key].local;
				}catch(e){
					return key;
				}
			},
			keysep: function(key, sep) {
				try{
					if (key === 'zid')
						return zlocal[sep] || sep;
					return localinfo[bean].keyLocals[key].seps[sep] || sep;
				}catch(e){
					return sep;
				}
			},
			value: function(val) {
				try{
					return localinfo[bean].valueLocals[val] || val;
				}catch(e){
					return val;
				}
			}
		};
	}
	
	function getGraph(key, linecount, defaultgraph){
		var g = 'line';
		var graphAvail = {'line' : true, 'area' : true, 'spline' : true, 'areaspline' : true, 'scatter' : true};
		if (key === 'time' ) {
			;
		}else{
			graphAvail.column = true;
			if (linecount === 1)
				graphAvail.pie = true;
			if (linecount === 1 && !/id$/.test(key) && key !== 'level') {
				g = 'pie';
			} else
				g = 'column';
		}
		if (defaultgraph && graphAvail[defaultgraph])
			g = defaultgraph;
		return g;
	}
	
	function setAxisTime(opt){
		opt.type = 'datetime';
		opt.dateTimeLabelFormats = {
			hour : '%H:%M',
			day : '%m/%e',
			week : '%m/%e',
			month : '%Y/%m',
			year : '%Y'
		};
		opt.maxZoom = cond.dayline ? 7 * 24 * 3600000 : 6 * 3600000;
	}
	
	function interpose(seq) {
		if (seq.length <= 2)
			return seq;
		var seq2 = []; 
				
		var seps = {};
		var sep; // 猜测间隔点
		for(var i=2; i<seq.length; i++){
			sep = seq[i] - seq[i-1];
			if (sep in seps)
				seps[sep]++;
			else
				seps[sep] = 1;
		}
		
		var cur = seps[sep];
		$.each(seps, function(k, v){
			if (v > cur){
				cur = v;
				sep = +k;
			}
		});
		
		var last = seq[0];
		seq2.push(last);
		for(var i=1; i<seq.length; i++){
			var cur = seq[i];
			if (cur - last > 3 * sep){ // 插入0值点
				seq2.push(last+sep);
				seq2.push(cur-sep);
			}else if (cur - last > 2 * sep){
				seq2.push(last+sep);
			}
			seq2.push(cur);
			last = cur;
		}
		
		return seq2;
	}
	
	var linecount = 0;
	for ( var name in dat)
		linecount++;
	if (linecount === 0)
		return;
	 
	// 图像类型
	var chartType = isBean ? getGraph(cond.key, linecount, graph) : 'line';
	
	// 标题
	var title = isBean ? local(cond.bean).bean() : cond.chartName;
	if (isBean && cond.sepkey) {
		title += '.' + local(cond.bean).value(cond.values[0]);
		title += '/' + local(cond.bean).key(cond.sepkey.key);
	}
	
	// 副标题
	var now = new Date().getTime();
	var subtitle = '';
	for ( var key in cond.ranges) {
		var low = cond.ranges[key].low;
		var v = low;
		if (key === 'time' ){
			if (v > 0)
				v = Highcharts.dateFormat('%y/%m/%e %H:%M', v);
			var up = cond.ranges[key].up;
			if (up && up != low ){
				if (up > 0){
					v += '~' + Highcharts.dateFormat('%y/%m/%e %H:%M', up);
				}else if (up < 0){
					v += '~' + up;
				}
			}
		}else
			v = local(firstBeanName).keysep(key, v);
		key = local(firstBeanName).key(key);
		subtitle += key + '=' + v + ',';
	}
	if (subtitle.length > 0) {
		subtitle = subtitle.substring(0, subtitle.length - 1);
	}
	
	// XY标题
	var xtitle = isBean ? local(cond.bean).key(cond.key) : null;
	var ytitle = isBean && cond.sepkey ? local(cond.bean).value(cond.values[0]) : null;
	
	// 拍照
	if (typeof FlashCanvas !== 'undefined'){
		var canvas_ctx = document.getElementById('canvas').getContext; 
		window['SAVE_' + renderTo] = function(  ){
			var c = document.getElementById("canvas");
			c.getContext = canvas_ctx; 
			var svg = thischart.getSVG();
			
			canvg(c, svg, {ignoreMouse: true, ignoreAnimation: true});
			setTimeout(function(){
		    	 FlashCanvas.saveImage(c);
			}, 100);
		};
	}else{
		window['SAVE_' + renderTo] = function(){
			var c = document.getElementById("canvas2");
			var svg = $('#' +  renderTo + ' > .highcharts-container').html().replace(/>\s+/g, ">").replace(/\s+</g, "<");
			canvg(c, svg, { renderCallback: function(){
	    		window.open(c.toDataURL("image/png"))
	    	}, ignoreMouse: true, ignoreAnimation: true });
		};
	}
	
	// 参数，按单个Bean，单坐标系配置
	
	var linePlotOpts = {
		fillOpacity : 0.3,
		marker : {
			enabled : false,
			states : {
				hover : {
					enabled : true,
					radius : 4
				}
			}
		},
		shadow : false,
	};
	
	var options = {
		chart : {
			renderTo : renderTo,
			type : chartType,
			zoomType : 'xy'
		},
		credits : {
			href : 'javascript:SAVE_' + renderTo + '()',
			text : '@' + Highcharts.dateFormat('%Y/%m/%e %H:%M', now)
		},
		title : {
			text : title
		},

		subtitle : {
			text : subtitle
		},
		xAxis : {
			tickLength : 0,
			allowDecimals : false,
			title : {
				text : xtitle
			}
		},
		yAxis : {
			min : 0,
			allowDecimals : asrate,
			title : {
				text : ytitle
			}
		},

		plotOptions : {
			column : {
					groupPadding : 0.05,
					shadow : false,
					borderWidth : 0,
			},
			pie : {
				allowPointSelect : true,
				dataLabels : {
					enabled : true,
					formatter : function() {
						return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.y, 0) + ' - ' + ('' + this.percentage).substring(0, 4) + ' %';
					}
				},
			},
			area : linePlotOpts,
			line : linePlotOpts,
			spline: linePlotOpts,
			areaspline: linePlotOpts,
		},
		
		exporting: {
			enabled: false
		}
	};
	
	function setPointClick( clk ){
		var point = {
			events : {
				click : clk
			}
		};
		options.plotOptions.pie.point = point;
		options.plotOptions.column.point = point;
		options.plotOptions.area.point = point;
		options.plotOptions.line.point = point;
		options.plotOptions.spline.point = point;
		options.plotOptions.areaspline.point = point;
	}
	
	
	
	if (isBean)
	{
		
		// 时间横轴格式
		if (cond.key === 'time') 
			setAxisTime(options.xAxis);
		
		// 堆叠
		var isStacking = false;
		if (chartType !== 'pie' && cond.sepkey) {
			isStacking = true;
			options.plotOptions.series = {
				stacking : 'normal'
			};
		}
		
		// 数据
	
		var series = [];
		var keySequences = [];
		if (chartType !== 'pie'){
			var keyset = {};
			$.each(dat, function(kk, vv) {
				$.each(vv, function(k, v) {
					keyset[k] = 1;
				});
			});
			$.each(keyset, function(k) {
				keySequences.push(+k);
			});
			keySequences.sort(function(a, b) {
				return a - b;
			});
		}
		
		if (chartType === 'column') {
			var cats = [];
			var len = 0;
			$.each(keySequences, function(i, key) {
				var cat = ''+local(cond.bean).keysep(cond.key, key);
				len += cat.length;
				cats.push(cat);
			});
			options.xAxis.categories = cats;
			// 估算初始tickInterval
			options.xAxis.tickInterval = Math.ceil( cats.length / ($('#'+renderTo).innerWidth() / (len/cats.length * 10 * 1.8)) );
			
	
			$.each(dat, function(kk, vv) {
				var data = [];
				$.each(keySequences, function(i, key) {
					data.push(vv[key] || 0);
				});
				series.push({
					name : kk,
					data : data
				});
			});
	
		} else if (chartType === 'pie') {
			var data = [];
			var useOthers = false;
			if (pieslices && pieslices > 0){
				var kvs = [];
				$.each(dat, function(kk, vv) {
					$.each(vv, function(k, v) {
						if (v > 0)
							kvs.push( [k, v] );
					});
				});
				if (kvs.length > pieslices){
					useOthers = true;
					kvs.sort(function(a, b){
						return b[1] - a[1];
					});
					
					for(var i=0; i<pieslices; i++){
						data.push([ ''+local(cond.bean).keysep(cond.key, kvs[i][0]), kvs[i][1] ]);
						keySequences.push(+kvs[i][0]);
					}
					
					var other = 0;
					for(var i=pieslices; i<kvs.length; i++)
						other += kvs[i][1];
					
					data.push(['Others', other]);
				}
			}
			
			if (!useOthers){
				$.each(dat, function(kk, vv) {
					$.each(vv, function(k, v) {
						if (v > 0) {
							data.push([ ''+local(cond.bean).keysep(cond.key, k), v ]);
							keySequences.push(+k);
						}
					});
				});
			}
			series.push({
				data : data
			});
	
		} else { // line
			
			var keySeq2 = interpose(keySequences);
			
			$.each(dat, function(kk, vv) {
				var name = kk ;
				var data = [];
				
				if (ascounter || asrate){
					var lastv = undefined;
					var lastk = undefined;
					$.each(keySeq2, function(i, key) {
						var thisv = vv[key];
						if (thisv !== undefined){
							if ( lastv !== undefined && thisv - lastv >= 0){
								var _denominator = asrate ? (key - lastk) / 1000 : 1;
								var _numerator = ascounter? thisv - lastv : thisv;
								data.push([ key, _numerator / _denominator ]);
							}
							lastv = thisv;
							lastk = key;
						}else{
							data.push([ key, 0 ]);
							lastv = undefined;
						}
					});
				}else{
					$.each(keySeq2, function(i, key) {
						data.push([ key, vv[key] || 0 ]);
					});
				}
				
				series.push({
					name : name,
					data : data
				});
			});
	
			if (isStacking) {
				options.chart.type = 'area';
				series.sort(function(a, b) {
					return (+b.name) - (+a.name);
				});
			}
		}
	
		if (chartType !== 'pie') {
			if (cond.sepkey) {
				$.each(series, function(idx, s) {
					s.name = local(cond.bean).keysep(cond.sepkey.key, s.name);
				});
			} else {
				$.each(series, function(idx, s) {
					s.name = local(cond.bean).value(s.name);
				});
			}
			
			if (ascounter || asrate){
				$.each(series, function(idx, s) {
					s.name += asrate ? "速率" : "新增";
				});
			}
		}
	
		options.series = series;
	
		
		// ////////////////////////////////////////
		
		// 点击事件响应
		setPointClick( function(e) {
			var kv = chartType === 'line' ? this.x : keySequences[this.x];
			if (kv != undefined)
				myclick(cond.key, kv);
		});

	
		// 提示
		if (chartType === 'pie') {
			options.tooltip = {
				formatter : function() {
					return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.y, 0) + ' - ' + ('' + this.percentage).substring(0, 4) + ' %';
				}
			};
		} else {
			options.tooltip = {
				shared : true,
				formatter : function() {
					var s = '<b>' + (cond.key === 'time' ? Highcharts.dateFormat('%y/%m/%e %H:%M', this.x) : this.x);
	
					var decimals = asrate ? 2 : 0;
					var first = true;
					$.each(this.points, function(i, point) {
						s += first && isStacking ? ': ' + Highcharts.numberFormat(point.total, decimals) + '</b>' : '</b>';
						first = false;
						s += '<br/>' + point.series.name + ': ' + Highcharts.numberFormat(point.y, decimals);
					});
	
					return s;
				}
			};
		}
		
	
		
	}
	
	else {
		
		options.xAxis = [];
		options.yAxis = [];
		options.series = [];

		var title2KeyAndSequence = {};
		var cnt = 0;
		$.each(dat, function(chartkey, value) {
			var keys = chartkey.split('/');
			var lineName = keys[0];
			var key  = keys[1];
			var bean = keys[2];
			var defaultgraph = keys[3];
			var lineAscounter = keys[4];
			
			var graph = getGraph(key, 2, defaultgraph);
			var legendName = lineName;
			title2KeyAndSequence[legendName] = {key : key};
			
			var clr = color(cnt);
			
			var xopts = {
				tickLength : 0,
				allowDecimals : false,
				title : {
					text : null
				},
				labels : {
					style : {
						color : clr
					}
				}
			}
			if ( 'time' === key )
				setAxisTime(xopts);
			
			options.xAxis.push(xopts);
			
			options.yAxis.push({
				min : 0,
				allowDecimals : asrate,
				opposite : (cnt%2 == 1),
				title : {
					text : null
				},
				labels : {
					style : {
						color : clr
					}
				},
			});
			
			
			var keySequences = [];
			var keyset = {};
			$.each(value, function(k, v) {
				keyset[k] = 1;
			});
			$.each(keyset, function(k) {
				keySequences.push(+k);
			});
			keySequences.sort(function(a, b) {
				return a - b;
			});
			
			
			if (graph === 'column') {
				
				title2KeyAndSequence[legendName].sequence = keySequences;
				
				var cats = [];
				var len = 0;
				$.each(keySequences, function(i, k) {
					var cat = ''+local(bean).keysep(key, k);
					len += cat.length;
					cats.push(cat);
				});
				xopts.categories = cats;
				xopts.tickInterval = Math.ceil( cats.length / ($('#'+renderTo).innerWidth() / (len/cats.length * 10 * 1.8)) );
		
				var data = [];
				$.each(keySequences, function(i, k) {
					data.push(value[k] || 0);
				});
				options.series.push({
					name : legendName,
					data : data,
					color : clr,
					type : graph,
					yAxis : cnt,
					xAxis : cnt,
				});
				
			}else{ //line
				
				var keySeq2 = interpose(keySequences);
				
				var data = [];
				if (lineAscounter){
					var lastv = undefined;
					var lastk = undefined;
					$.each(keySeq2, function(i, k) {
						var thisv = value[k];
						if (thisv !== undefined){
							if ( lastv !== undefined && thisv - lastv >= 0){
								if (lineAscounter == "rate")
									data.push([ k, (thisv - lastv) / (k - lastk) * 1000 ]);
								else
									data.push([ k, thisv - lastv ]);
							}
							lastv = thisv;
							lastk = k;
						}else{
							data.push([ k, 0 ]);
							lastv = undefined;
						}
					});
				}else{
					$.each(keySeq2, function(i, k) {
						data.push([ k, value[k] || 0 ]);
					});
				}
				
				options.series.push({
					name : legendName,
					data : data,
					color : clr,
					type : graph,
					yAxis : cnt,
					xAxis : cnt,
				});
			}
			
			cnt++;
			
		});
		
		// 点击事件响应
		setPointClick( function(e) {
			var ks = title2KeyAndSequence[this.series.name];
			var kv = ks.sequence ?  ks.sequence[this.x] : this.x;
			myclick(ks.key, kv);
		});
		
		//提示
		var YEAR2000 = 946656000000; // new Date("2000-1-1").getTime()
		options.tooltip = {
			shared : true,
			formatter : function() {
				var s = '';
				$.each(this.points, function(i, point) {
					var k = '<b>' + (point.x > YEAR2000 ? Highcharts.dateFormat('%y/%m/%e %H:%M', point.x) : point.x) + '</b>';
					s +=  point.series.name + ' : (' +  k + ' : ' + Highcharts.numberFormat(point.y, 0) + ')<br/>';
				});
				return s;
			}
		};
	}
	
	if (options.xAxis.categories){
		var orig_tickInterval = options.xAxis.tickInterval;
		options.chart.events =  {
            selection: function(e){
                var ex = this.xAxis[0].getExtremes();
                if (e.resetSelection){
                    this.xAxis[0].options.tickInterval = orig_tickInterval;
                }else{
                    var w = e.xAxis[0].max - e.xAxis[0].min;
                    this.xAxis[0].options.tickInterval = Math.ceil( w / orig_width * orig_tickInterval );
                }
            }
        };
	}
	
	var thischart = new Highcharts.Chart(options);
	
	if (options.xAxis.categories){
		var ex = thischart.xAxis[0].getExtremes();
		var orig_width = ex.max - ex.min;
	}
	
}
