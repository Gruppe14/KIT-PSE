function piechart(json) {
    var data;
    var xAxisName;
    var yAxisName;
    console.log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
    data = json.data;
    visualize(data); //then start the visualization

    function getX(d) {
        return d[xAxisName];
    }

    function getY(d) {
        return d[yAxisName];
    }



    function visualize(dataset) {
        var w = 620;
        var h = 480;
        
        var radius = Math.min(w, h) / 2; //change 2 to 1.4. It's hilarious.
        var color = d3.scale.category20();
        var format = d3.format(".3r");

        var arc = d3.svg.arc() //each datapoint will create one later.
			.outerRadius(radius - 20)
			.innerRadius(0);
        //well, if you set this to not 0 it becomes a donut chart!

        var pie = d3.layout.pie()
            .sort(getY)
            .value(getY);

        $("#chart").html("");
        var svg = d3.select("#chart").append("svg")
            .attr("width", w)
            .attr("height", h)
            .attr("class", "chart")
            .append("g") //someone to transform. Groups data.
        .attr("transform", "translate(" + w / 2 + "," + h / 2 + ")");
        //transform to the center.

        //create the slices
        var slices = svg.selectAll(".arc")
            .data(pie(dataset))
            .enter().append("g")
            .attr("class", "slice");


        //and fill them
        slices.append("path")
            .attr("d", arc)
            .style("fill", function (d, i) {
            return color(i);
        });

        //add text, even
        slices.append("text")
            .attr("class", "data-text")
            .attr("transform", function (d) {
            return "translate(" + arc.centroid(d) + ")";

        })
        //now, not so fast. this will only happen on big enough slices    
        .text(function (d) {
            var startAngle = d.startAngle;
            var endAngle = d.endAngle;
            var text = "";
            
            //TODO: FIND A GREAT MATHEMATICALLY PRECISE FORMULA INSTEAD OF STUPID HEURISTIC
            if((Math.abs(startAngle - endAngle)) > 0.7) {
                //console.log(getX(d.data));
                text = getX(d.data);
            }
//            console.log(startAngle - endAngle);
            return text;
        });
        
        //hovertext, too!
        slices.append("title")
        .text(function(d) {
            return "(" + xAxisName + ":" + getX(d.data) + ", " + yAxisName + ":" + format(getY(d.data)) + ")";
        });
        
               
        //add non dynamic styles
        $(".data-text").css({
			"font-family": "sans-serif",
            "dy": ".35em",
            "text-anchor": "middle"
        });
    }
}