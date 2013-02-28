
function scatterplot(json ){
	//load js from bubblescatter if not there
	if($("script[src='/charts/bubblescatter/bubblescatter.js']").length < 1) {
		$("head").append('<script type="text/javascript" src="/charts/bubblescatter/bubblescatter.js"></script>');
	}
    bubblescatter(json, 3);
}