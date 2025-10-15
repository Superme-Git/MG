$(function(){
	
	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 60 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});
	
	var beaninfo = JSON.parse($('.beaninfo').html());
	
	var mk = '';
	for(var bn in beaninfo){
		mk += '<option>' + bn + '</option>';
	}
	mk += '<option>--basekey--</option>';

	var beannode = $('.bean');
	var xmlnode = $('.xml');
	beannode.html(mk);
	
	function change(){
		var bn = beannode.val();
		if (!bn) return;
		
		
		var xw = new XMLWriter('UTF-8');
		xw.indentation = 4;
		xw.indentChar = ' ';
		
		if (bn === '--basekey--'){
			xw.document().element('basekey').attr('name', '')
								.element('sep').attr('o', 0).attr('local', '').endElement()
							.endElement().endDocument();
		}else{
			var bean = beaninfo[bn];
			xw.document().element('localize').attr('bean', bn).attr('local', '');
			
			$.each(bean.keyList, function(idx, v){
				if (v === 'time' || v === 'zid')
					return;
				xw.element('key').attr('o', v).attr('local', '').element('sep').attr('o', 0).attr('local', '').endElement().endElement();
			});
			
			$.each(bean.valueList, function(idx, v){
				xw.element('value').attr('o', v).attr('local', '').endElement();
			});
			
			xw.endElement().endDocument();
		}
		var xml = xw.flush();
		xmlnode.val(xml).attr('rows', xml.split('\n').length + 4);
	}
	
	change();
	beannode.change(change);
	
	
	$('.add').click(function(){
		$.ajax({
			type: "POST",
			url : "/localize",
			data: {xml: xmlnode.val()},
			success : function(){location.reload();}
		});
	});
	
	$('.remove').each(function(){
		$(this).click( function(){
			$(this).attr("disabled", "disabled");
			$.ajax({
				type: "DELETE",
				url : "/localize/" + $(this).attr("mytype") + '/' + $(this).attr("bean"),
				success : function(){location.reload();}
			});
		});
	});
});