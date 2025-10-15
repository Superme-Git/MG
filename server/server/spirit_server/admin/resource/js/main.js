/**
 * 
 */

function redirect_confirm(url, message)
{
	Modal.confirm({
		msg: message ? message : "确定执行？"
	}).on( function (e) {

		if (e)
		{
			window.location.href = url;
		}
		
        //alert("返回结果：" + e);
        return e;
	});

	//return false;
}



