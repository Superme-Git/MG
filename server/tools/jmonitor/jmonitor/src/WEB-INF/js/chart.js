
$( function() {
	$(".status").ajaxComplete(function(e, xhr, settings){
		var txt = xhr.status + " " + xhr.statusText;
		if (xhr.responseText.length > 0){
			txt += " : " + (xhr.responseText.length > 60 ? xhr.responseText.length : xhr.responseText);
		}
		$(this).text( txt );
	});
	
	$(".remove").each( function(){
		$(this).click(function() {
			var id = $(this).attr("chartname");
			if (!confirm("你确定要删除 " + id + " 吗？"))
				return;

			$.ajax({
				type: "DELETE",
				url : "/chart/" + id,
				success : function(){location.reload();}
			});
		});
	});
});

