	
	//检查用户ID或邮件列表ID是否合法。用户ID只允许为英文字母、数字和小数点的混合,若包含其他符号或前后有空格则为非法用户ID,同时在用户ID中去掉这些非法字符
	function checkUserID(uid){
		var reg = /[^0-9a-zA-Z.]/g;
		var result = uid.replace(reg,'');
		if(result==uid){
			return true;
		}
		else{
			alert("ID只允许为英文字母、数字和小数点\".\"的混合，\n请检查ID中是否包含其他符号或者前后有空格");
			return false;
		}
	}
	
	//检查部门名字是否合法。部门名字不能以逗号结尾，部门间只能有一个逗号即不能出现连续的逗号
	function checkDepartment(department){
		var reg = /([,\uFF0C]+[,\uFF0C]+)|([,\uFF0C]+)$/g;
		return reg.test(department);
	}

	//检查时间格式是否合法。时间格式为："xxxx-xx-xx xx:xx"
	function checkTime(time){
		var reg = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\d|3[0-1])\s(0\d|1\d|2[0-3]):([0-5]\d)$/g;
		return reg.test(time);
	}
	
	//检查日期格式是否合法。时间格式为："xxxx-xx-xx"
	function checkDate(date){
		var reg = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\d|3[0-1])$/g;
		return reg.test(date);
	}
	
	//检查日期格式是否是上下午，时间格式为："xxxx-xx-xx 上午"或"xxxx-xx-xx 下午"
	function checkHalfDay(halfDay){
		var reg = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2]\d|3[0-1])\s(\u4E0A\u5348|\u4E0B\u5348)$/g;
		return reg.test(halfDay);
	}
	
	//判断空值
	function checkNull(value){
	  if(trim(value)==""){
	    return true;
	  }
	  return false;
	}
	
	//判断数字
	function checkNumber(number){
		var reg = /^(\d)+(.([0-9]){0,2}){0,1}$/;
		return reg.test(number);
	}
		
	//是否邮箱
	function checkEmail(email){
	  	var reg = /^[\w-.]+@[\w-]+(\.(\w)+)*(\.(\w){2,3})$/g;
	  	return reg.test(email);
	}
	
	//去字符串左右两边的空格(中间的空格不能去)  
	function trim(str){  
		return str.replace(/(^\s*)|(\s*$)/g, "");  
	}
	
	//将form中textarea字段的多行转换为array
	function multilineToArray(multiLine){
		var array = multiLine.split("\r\n");
		if (array != null && array.length <= 1) {
			array = multiLine.split("\n");
		}
		return array;
	}
	
	function replaceChar(str){
		var test = str.replace(/%/g,"%25");
		test = test.replace(/&/g,"%26");
		test = test.replace(/\//g,"%2F");
		test = test.replace(/\?/g,"%3F");
		test = test.replace(/#/g,"%23");
		test = test.replace(/=/g,"%3D");
		test = test.replace(/\+/g,"%2B");
		return test;
	}
	
	//比较两个数字的大小，包括整数和浮点数,其中sNumber是字符串格式的源数字，sTarget是字符串格式的目标数字
	function compareNumbers(sNumber,sTarget){
		var numberInt = parseInt(sNumber.split(".")[0]);
		var numberFloat = parseInt(sNumber.split(".")[1]);
		var targetInt = parseInt(sTarget.split(".")[0]);
		var targetFloat = parseInt(sTarget.split(".")[1]);
		if(numberInt<targetInt){
			return -1;
		}
		else if(numberInt==targetInt){
			if(numberFloat<targetFloat)
				return -1;
			else if(numberFloat==targetFloat)
				return 0;
			else 
				return 1;
		}
		else
			return 1;
	}
	
	//比较表格的列，限于该列的数据类型为字符串时,iCol代表表格的列，从0开始，isNumber为true代表比较数字，否则为比较字符串
	function generateCompareTRs(iCol,isNumber) {
	
	    return  function compareTRs(oTR1, oTR2) {
	    				var sValue1 = trim(oTR1.cells[iCol].innerHTML);
		                var sValue2 = trim(oTR2.cells[iCol].innerHTML);
		                if(isNumber)
		                	return compareNumbers(sValue1,sValue2);
		                else
	                		return sValue1.localeCompare(sValue2);
	            };
	}
	
	//对表格进行排序,isNumber为true表示比较数字，否则为比较字符串
	function sortTable(sTableID, iCol, isNumber) {
	    var oTable = document.getElementById(sTableID);
	    var oTBody = oTable.tBodies[0];
	    var colDataRows = oTBody.rows;
	    var aTRs = new Array;
	
	    for (var i=0; i < colDataRows.length; i++) {
	        aTRs[i] = colDataRows[i];
	    }
	
	    if (oTable.sortCol == iCol) {
	        aTRs.reverse();
	    } else {
	        aTRs.sort(generateCompareTRs(iCol,isNumber));
	    }
	
	    var oFragment = document.createDocumentFragment();
	    for (var i=0; i < aTRs.length; i++) {
	        oFragment.appendChild(aTRs[i]);
	    }
	
	    oTBody.appendChild(oFragment);
	    oTable.sortCol = iCol;
	}
	
