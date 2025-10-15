function intersect(a, b){
	var ai=0, bi=0;
	var result = [];
	while(ai < a.length && bi < b.length){
		if (a[ai] < b[bi]) { ai++; }
		else if (a[ai] > b[bi]) { bi++; }
		else{
			result.push(a[ai]);
			ai++;
			bi++;
		}
	}
	return result;
}

function number2str( num ){
	var v = [];
	function tail(n){
		if (n.length <= 3){
			v.push(n);
		}else{
			v.push(n.slice(n.length-3));
			tail(n.slice(0, n.length-3));
		}
	}
	tail("" + num);
	
	var sv = [];
	for(var i=v.length-1; i>=0; i--){
		sv.push(v[i]);
	}
	var ss = sv.join(',');
	
	return ss;
}