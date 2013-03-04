function piechart(json, sorted) {
    var data, xAxisName, yAxisName, measure;
    
    console.log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
    measure = json.measureAttribute;
    
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

        function comparator(a, b) {
			a = +getY(a); //the second dimension is always the measure
			b = +getY(b);
			
			if (isNaN(a) || isNaN(b)) {
				//abort?
				console.log("Error, the data doesn't have an numeric attribute");
				chartError();
			}
			return (b - a);
		}
        var pie = d3.layout.pie()
            .value(getY);
            
        if (sorted !== false) {
            pie.sort(comparator);
        }

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

        //This function returns the angle that the text should be
        //written so that it fits greatly in its arc
        function angle(d) {
            var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
            a = (a >= 90) ? a - 180 : a;
            return a;   
        }
        //add text, even
        slices.append("text")
        .attr("class", "data-text")
        //now, not so fast. this will only happen on big enough slices    
        .text(function (d) {
            //angles are in radians...
            var startAngle = d.startAngle * 180 / Math.PI;
            var endAngle = d.endAngle * 180 / Math.PI;
            var text = "";

            //TODO: FIND A GREAT MATHEMATICALLY PRECISE FORMULA INSTEAD OF STUPID HEURISTIC
            //You know, calculate the pixel size by using the radius and the arc
            if((Math.abs(startAngle - endAngle)) > 12) {
                //console.log(getX(d.data));
                text = getX(d.data);
            }
            return text;
        })
        .attr("transform", function (d) {
            return "translate(" + arc.centroid(d) + ")" + "rotate(" + angle(d) + ")";
        });
        
        //hovertext, too!
        slices.append("title")
        .text(function(d) {
            return xAxisName + ":" + getX(d.data) + "\n" + measure + " of " + yAxisName + ":" + format(getY(d.data));
        });
    }
}