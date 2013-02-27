
function scatterplot(json ){
    try {
        bubblescatter(json, 3);
    }
    catch(err) {
      $("head").append('<script type="text/javascript" src="/charts/bubblescatter/bubblescatter.js"></script>');
       bubblescatter(json);
    }
}