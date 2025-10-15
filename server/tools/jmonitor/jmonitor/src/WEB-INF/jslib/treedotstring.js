/* container： string
 * data		: [string.string.string]
 */
 
 
function treeDotString(container, data) {
	var sv = [];
	for(var i=0; i<data.length; i++){
		sv.push( data[i].split('.').join('.') );//.filter(function(e){return e.length > 0;})
	}
	sv.sort();
	
	var svv = [];
	for(var i=0; i<sv.length; i++){
		svv.push( sv[i].split('.') );
	}
	
	function check(pre, lf, ln){
		if (ln.length > 0){
			var nxtpre = pre ? pre+'.'+lf : lf;
			var head = lf;
			if (ln[0].length == 0){
				head = '<a href="javascript:void(0)" data="' + nxtpre + '">' + lf + '</a>';
				ln = ln.slice(1, ln.length);
			}
			return '<li>' + head + markup(nxtpre, ln) + '</li>';
		}
		return '';
	}
	
	function markup( pre, ss ){
		if (ss.length === 0)
			return '';
			
		var mk = '<ul>';
		
		var lf = '';
		var ln = [];
		
		for(var i=0; i<ss.length; i++){
			var s = ss[i];
			var f = s[0];
			var n = s.slice(1, s.length);
			
			if (f !== lf){
				mk += check(pre, lf, ln);
				lf = f;
				ln = [n];
			}else{
				ln.push(n);
			}
		}
		mk += check(pre, lf, ln);
		
		return mk+'</ul>';
	}
	
	$(container).html( markup(null, svv) );
}