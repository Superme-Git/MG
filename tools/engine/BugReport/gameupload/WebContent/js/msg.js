
function win_size(){
	var xs, ys;
	if (window.innerHeight && window.scrollMaxY) { 
	xs = document.body.scrollWidth;
	ys = window.innerHeight + window.scrollMaxY;
	} else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
	xs = document.body.scrollWidth;
	ys = document.body.scrollHeight;
	} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
	xs = document.body.offsetWidth;
	ys = document.body.offsetHeight;
	}
	var de = document.documentElement;
	var w = window.innerWidth || self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
	var h = window.innerHeight || self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight;
	return [xs,ys,w,h];
}

function get_msg(){
	if(checkNull($('#zxz_msg span').html())){return false;}
	var p = $('#zxz_msg span');
	var str = p.html();
	var isclo = p.attr('isclo');
	var wzid = p.attr('wzid');
	var tim = p.attr('tim');
	return [str,isclo,wzid,parseInt(tim)];
	}
function cr_box(){
	$('body').append('<div class="mag_wm" id="msg_wm"><div class="mag_wm_inf" id="mag_wm_inf"></div></div>');
	}

function clos(){
	$('#msg_wm').empty();
	$('#msg_wm').remove();
	}
//z:是否有关闭，b:显示位置
function msg_(str,isclo,wzid,tim){
	if(checkNull(trim(str))) 
		return false;
	
	if($('#msg_wm').length == 0){cr_box();}else{clos();cr_box();}
	if(str){$('#mag_wm_inf').append(str);}
	if(isclo == 'yes'){
		$('#msg_wm').prepend('<div class="mag_wm_t"><span id="mag_close">×</span></div>');
		$('#mag_close').bind('click',function(){clos();});
		}else{if(tim){setTimeout(clos,tim);}else{setTimeout(clos,3000);}}
		
	h = $('#mag_wm').height();
	if(wzid != 'none'){
		if($('#'+wzid).length != 0){
			var k = $('#'+wzid).offset();
			var t = k.top;
			var l = $('#'+wzid).width()+k.left;
			}else{
				l = Math.ceil(win_size()[2]/2 - 150);
				t = Math.ceil(win_size()[3]/2 - h -100);
				}
		}else{
		
			l = Math.ceil(win_size()[2]/2 - 150);
			t = Math.ceil(win_size()[3]/2 - h-100);
			
			}
	$('#msg_wm').css({top:t,left:l});
	}
	
$(document).ready(function(){
			if(checkNull($('#zxz_msg span').html())){return false;}
			var g = get_msg();
			msg_(g[0],g[1],g[2],g[3]);
	});