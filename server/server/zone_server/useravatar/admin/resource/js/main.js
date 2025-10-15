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

function alert_message(message)
{
	Modal.alert({
		msg: message ? message : "确定执行？"
			
	}).on( function (e) {
		
        //alert("返回结果：" + e);
        return e;
	});

	//return false;
}

function alert_img(url)
{
	Modal.alert({
		msg: '<img src="'+url+'" />'
	}).on( function (e) {
		
        //alert("返回结果：" + e);
        return e;
	});

	//return false;
}


function check_all(input_name)
{
    $("input[name='"+input_name+"']").each(function () {
        $(this).prop("checked", 'checked');
    });
}

function uncheck_all(input_name)
{
    $("input[name='"+input_name+"']").each(function () {
        $(this).prop("checked", false);
    });
}