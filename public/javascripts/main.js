$(document).ready(function() {
	$('#lang a').click(function(event) {
		event.preventDefault();
	    $('body').append('<form action="'+ window.location.pathname + 
	    	'" name="language" method="post" style="display:none;"><input type="text" name="lang" value="' 
	    	+ $(this).attr('href') + '" /></form>');
	
	    document.forms['language'].submit();
	})
})

function displayError(err, info){
	//if no additional info
	info = typeof info !== 'undefined' ? info : "";
	//display message
	$("body").append("<div id='error'><div><span>" + err + "</span>" 
		+ info + "<div>ok</div></div></div>");
	$("#error").find("div:last").click(function() {
		$(this).parents("#error").remove();
	});
	$("#error").find("div:last").focus();
}
