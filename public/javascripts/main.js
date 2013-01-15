$(document).ready(function() {
	$('#lang a').click(function(event) {
		event.preventDefault();
	    $('body').append('<form action="'+ window.location.pathname + 
	    	'" name="language" method="post" style="display:none;"><input type="text" name="lang" value="' 
	    	+ $(this).attr('href') + '" /></form>');
	
	    document.forms['language'].submit();
	})
})
