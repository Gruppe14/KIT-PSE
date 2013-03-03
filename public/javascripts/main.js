$(document).ready(function() {
    //center tiles
    if($(".bigTile").length > 0) {
       onResize();
       $(window).resize(onResize);
    }
    
    $('#lang a').click(function(event) {
        event.preventDefault();
        $('body').append('<form action="/language" name="language" method="POST" style="display:none;">' +
            '<input type="text" name="lang" value="' + $(this).attr('href') + '" />' +
            '<input type="text" name="path" value="' + window.location.pathname + '" /></form>');
        document.forms.language.submit();
    });
});

//function to adjust width of content on resize;
function onResize() {
    //tile width
    var tW = $(".bigTile").parent().width()+5;
    $("#content").width(Math.floor($("body").width()/tW)*tW);
}

function displayMessage(header, info) {
    //if no additional info
    info = typeof info !== 'undefined' ? info : "";
    //display message
    $("body").append("<div id='message'><div><span>" + header + 
        "</span><br />" + info + "<span>ok</span></div></div>");
    var w = $("#message").children().width() - $("#message span:last").width();
    $("#message span:last").css("left", w/2).click(function() {
        $(this).parents("#message").remove();
    });
    $("#message span:last").focus();
}

function displayError(err, info){
    if (typeof (info !== 'undefined')) {
        displayMessage(err, info);
    }
    else {
        displayMessage(err);
    }
    
}
