$(document).ready(function() {
    //center tiles
    if($(".bigTile").length > 0) {
       onResize();
       $(window).resize(onResize);
    } else {
        $("#content").width("70%");
    }
    
    
	$('#lang a').click(function(event) {
		event.preventDefault();
	    $('body').append('<form action="/language" name="language" method="POST" style="display:none;">' +
			'<input type="text" name="lang" value="' + $(this).attr('href') + '" />' +
			'<input type="text" name="path" value="' + window.location.pathname + '" /></form>');
	
	    document.forms['language'].submit();
	})
})

//function to adjust width of content on resize;
function onResize() {
    //tile width
    var tW = $(".bigTile").parent().width()+5;
    $("#content").width(Math.floor($("body").width()/tW)*tW);
}

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
