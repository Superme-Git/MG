$( function() {

	$(".status").ajaxComplete(function(e, xhr, settings){
		$(this).text(xhr.status + " " + xhr.statusText);
	});
	
	
	$(".add").click(function() {
		$.ajax({
			type: "POST",
			url : "/access",
			data: {role: $(".role").val(), user: $(".user").val()},
			success : function(){location.reload();}
		});
	});
	
	$(".remove").click( function() {
		$.ajax({
			type: "DELETE",
			url : "/access/" + $(".role").val() + "/" + $(".user").val(),
			success : function(){location.reload();}
		});
	});
	
});
