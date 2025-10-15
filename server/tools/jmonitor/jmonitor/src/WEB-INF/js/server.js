$( function() {
	
	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 60 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});
	
	$(".add").click( function() {	
		$.ajax({
			type: "POST",
			url : "/server",
			data: {serverid: $(".serverid").val(), url: $(".url").val(), name:  $(".name").val()},
			success : function(){location.reload();}
		});
	});
	
	$(".remove").each( function(){
		$(this).click(function() {
			var id = $(this).attr("serverid");
			if (!confirm("你确定要断开与 " + id + " 的连接吗？"))
				return;

			$.ajax({
				type: "DELETE",
				url : "/server/" + id,
				success : function(){location.reload();}
			});
		});
	});
	
	$(".fetch").each( function(){
		$(this).click(function() {
			var id = $(this).attr("serverid");
			var node = $(this);
			node.attr("disabled", "disabled"); node.text("fetching...");
			
			$.ajax({
				type: "POST",
				url : "/server/" + id,
				complete: function(){node.removeAttr("disabled"); node.text("fetch");}
			});
		});
	});
	
});
