$(function () {
    window.Modal = function () {
        var reg = new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
        
        var _html = '<div id="[ModalID]" class="modal">'
        + '<div class="modal-dialog">'
        + '<div class="modal-content">'
        + '<div class="modal-header">'
        + '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
        + '<h5 class="modal-title"><i class="fa fa-exclamation-circle"></i> [Title]</h5>'
		+ '</div>'
		+ '<div class="modal-body small">'
		+ '<p>[Message]</p>'
		+ '</div>'
		+ '<div class="modal-footer" >'
		+ '<button type="button" class="btn btn-primary ok" data-dismiss="modal">[BtnOk]</button>'
		+ '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>'
		+ '</div>'
		+ '</div>'
		+ '</div>'
        + '</div>';

        //关闭时恢复 modal html 原样，供下次调用时 replace 用
        //var _init = function () {
        //    alr.on("hidden.bs.modal", function (e) {
        //        $(this).html(_html);
        //    });
        //}();

        /* html 复原不在 _init() 里面做了，重复调用时会有问题，直接在 _alert/_confirm 里面做 */


        var _alert = function (options) {
            
        	_html.replace('btn-success', 'btn-primary');
        	//_html.replace('cancel', 'cancel hide');
            var newdialog = _dialog(options, _html);

            return {
                on: function (callback) {
                    if (callback && callback instanceof Function) {
                    	$(newdialog).find('.ok').click(function () { callback(true) });
                    }
                }
            };
        };

        var _confirm = function (options) {

			_html.replace('btn-primary', 'btn-success');
        	//_html.replace('cancel', 'cancel');
			var newdialog = _dialog(options, _html);

            return {
                on: function (callback) {
                    if (callback && callback instanceof Function) {
                    	$(newdialog).find('.ok').click(function () { callback(true) });
                    	$(newdialog).find('.cancel').click(function () { callback(false) });
                    }
                }
            };
        };

        var _dialog = function (options, html) {

        	var id = "modal" + new Date().getTime();

            var ops = {
                msg: "提示内容",
                title: "操作提示",
                btnok: "确定",
                btncl: "取消"
            };

            $.extend(ops, options);

            //console.log(alr);

            var html1 = html.replace(reg, function (node, key) {
                return {
					ModalID : id,
                    Title: ops.title,
                    Message: ops.msg,
                    BtnOk: ops.btnok,
                    BtnCancel: ops.btncl
                }[key];
            });
            
            $("body").append(html1);

            $("#"+id).modal({
                width: 500,
                backdrop: true //'static'
            });
            
            return $("#"+id);
        }

        return {
            alert: _alert,
            confirm: _confirm
        }

    }();
});

/**

// 四个选项都是可选参数
Modal.alert(
    {
        msg: '内容',
        title: '标题',
        btnok: '确定',
        btncl:'取消'
    });

// 如需增加回调函数，后面直接加 .on( function(e){} );
// 点击“确定” e: true
// 点击“取消” e: false
Modal.confirm(
    {
        msg: "是否删除角色？"
    })
    .on( function (e) {
        alert("返回结果：" + e);
});

*/
