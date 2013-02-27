
function scatterplot(json ){
	//load js from bubblescatter if not there
	if($("script[src='/charts/bubblescatter/bubblescatter.js']").length < 1) {
		$("head").append('<script type="text/javascript" src="/charts/bubblescatter/bubblescatter.js"></script>');
	}
	//load css from bubblescatter if not there
	if($("link[href='/charts/bubblescatter/bubblescatter.css']").length < 1){
		$("head").append('<link href="/charts/bubblescatter/bubblescatter.css" rel="stylesheet" type="text/css">');
	}
    bubblescatter(json, 3);
}