$( function() {
	$(".status").ajaxComplete(function(e, xhr, settings){
		$(this).text(xhr.status + " " + xhr.statusText + " " + xhr.responseText);
	});
	
	$(".remove").each( function(){
		$(this).click(function() {
			var id = $(this).attr("chainname");
			if (!confirm("你确定要删除 " + id + " 吗？"))
				return;

			$.ajax({
				type: "DELETE",
				url : "/chartchain/" + id,
				success : function(){location.reload();}
			});
		});
	});
});
