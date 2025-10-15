$(function(){
	
	var DAYLINELIMIT = 7;
	
	$(".status").ajaxComplete(function(e, xhr, settings){
		$(this).text(xhr.status + " " + xhr.statusText + " : " + (xhr.responseText.length > 30 ? xhr.responseText.length : xhr.responseText));
	});
	
	var idnode = $(".identification");
	var timenode = $('.time');
	var timelownode = $('.timelow');
	var timeupnode = $('.timeup');
	var zidnode = $('.zid');
	
	function onTimeChange(){
		var v = +timenode.val();
		if (v === -2){
			timelownode.removeClass('hide');
			timeupnode.removeClass('hide');
		}else{
			timelownode.addClass('hide');
			timeupnode.addClass('hide');
		}
	}
	
	timenode.change(onTimeChange);
	
	function getTimeRange(){
		var r = {};
		var low = timelownode.val();
		if (low){
			low = low.replace("-", "/");
			low = low.replace("-", "/");
			r.low = Date.parse(low);
		}else{
			return r;
		}
		
		var up = timeupnode.val();
		if (up){
			up = up.replace("-", "/");
			up = up.replace("-", "/");
			r.up = Date.parse(up);
		}
		
		return r;
	}
	
	$(".dump").click(function(){
		var src = "/tabledata?table=" + idnode.val() ;
		
		var zid = +zidnode.val();
		if (zid != -1){
			src += "&zid=" + zid;
		}
		
		var tm = +timenode.val();
		var dayline = true;
		var timelow = null;
		var timeup = null;
		if (tm > 0){
			dayline = (tm > DAYLINELIMIT);
			timelow = -tm;
		}else if (tm === -1){
			;
		}else if (tm === -2){
			var r = getTimeRange();
			if (!r.low){
				$(".status").text("time low must be set");
				return;
			}
			var up = r.up;
			if (!up)
				up = Date.now();
			
			if (up <= r.low){
				$(".status").text("time up <= low");
				return;
			}
			
			dayline = (up - r.low) > (DAYLINELIMIT*86400000);
			timelow = r.low;
			if (r.up)
				timeup = r.up;
		}
		
		src += "&dayline=" + dayline;
		if (null !== timelow)
			src += "&timelow=" + timelow;
		if (null !== timeup)
			src += "&timeup=" + timeup;
		
		
		document.getElementById("downloadfrm").src = src; 
	});
	
});