$( function() {
	
	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 90 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});

	$(".remove").each( function(){
		$(this).click(function() {
			var id = $(this).attr("identification");
			if (false === confirm("删除后数据库将不再记录这些信息。你确定要删除 " + id + "？"))
				return;
			
			$(this).attr("disabled", "disabled");
			
			$.ajax({
				type: 'DELETE',
				url:  '/table/' + id,
			});
		});
	});
	
	$(".add").each( function(){
		$(this).click(function() {
			var xml = $(this).parent().children()[1].value;
			if (false === confirm("注意此操作将drop同名数据库表，然后重建！！！之后数据库将定时记录这些信息到此表中。你确定要增加 " + xml + "？"))
				return;
			
			$(this).attr("disabled", "disabled");
			
			$.ajax({
				type: 'POST',
				url:  '/table',
				data:  {xml : xml},
			});
		});
	});
	
	var includenode = $(".include");
	var excludenode = $(".exclude");
	
	function onchange() {
		var inc = includenode.val();
		var exc = excludenode.val();
		
		$("li").each( function(){
			var head = $(this).find("h5").html();
			if ( (!inc || head.match(inc)) && (!exc || !head.match(exc)) ){
				$(this).removeClass("hide");
			}else{
				$(this).addClass("hide");
			}
		});
	}
	
	onchange();
	includenode.keyup(onchange);
	excludenode.keyup(onchange);
});
