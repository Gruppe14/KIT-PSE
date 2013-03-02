function bubblechart(json) {
    var data;
	
    console.log("I read " + json.data.length + " data points.");
	
    var xAxisName = json.attribute1;
    var yAxisName = json.attribute2;
	
    data = json.data;
	console.log(data);
    visualize(data); //then start the visualization

    function getX(d) {
        return d[xAxisName];
    }

    function getY(d) {
        return d[yAxisName];
    }

    function visualize(data) {
        var w = 720;
        var h = 640;
		
		//the space between circles
        var padding = 10;
        //the format of the data
        var format = d3.format(",d");
		//the color palette used for the data.
	    var color = d3.scale.category20();

		//the data has to be sorted somehow
		function comparator(a, b) {
			a = +getY(a);
			b = +getY(b);
			
			if (isNaN(a) || isNaN(b)) {
				//abort?
				console.log("Error, the data doesn't have an numeric attribute");
			}
			return (b - a);
		}
		
		//this contains the scales.
		var bubble = d3.layout.pack()
			.value(getY) //set the accessor function to be the second attribute
		    .sort(comparator)
		    .size([w, h])
		    .padding(padding);

		$("#chart").html("");
		//the big svg container
		var svg = d3.select("#chart").append("svg")
		    .attr("width", w)
		    .attr("height", h)
		    .attr("class", "bubble");
		
		//the pack layout depends on data having a very specific format
		var fakedata = Object;
		//each node has children, and so on and so forth
		fakedata.children = data;

		//add each point, but save to add text on it
		var circle = svg.selectAll(".circle")
			.data(bubble.nodes(fakedata)
			.filter(function(d) { return !d.children; }))
			//don't return the root node as it doesn't have any children
			.enter().append("g")
			.attr("class", "circle")
			.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

		//this is the hovertext
		circle.append("title")
			.text(function(d) { return "(" + xAxisName + ":" + getX(d) + ", " + yAxisName + ":" + getY(d) + ")";});

		//draw the circle
		circle.append("circle")
			.attr("r", function(d) { return d.r; })
			.style("fill", function(d) { return color(getX(d)); });

		//this is the text written over the circle.
		circle.append("text")
		.attr("class", "data-text")
		.attr("dy", ".3em")
		.style("text-anchor", "middle")
		.text(function(d) {
			//calculate how much of the title to write
			//it depends on the radius
		    return getX(d).substring(0, d.r / 3.15);
		});
		
		//Yo ocjojo what happens with the boilerplate down here? :-)
		//add styles
		$("#y-axis-description").attr("transform", "rotate(270)")
			.css({
				"font-family": "kalinga",
				"font-size": "10px"
			})
		$(".axis path, .axis line").css({
			"fill": "none",
			"stroke": "black",
			"shape-rendering": "crispEdges"
		});
		$(".axis text").css({
			"font-family": "kalinga",
			"font-size": "12px"
		});
		/*$(".data-text").css({
			"font-family": "kalinga",
			"font-size": "14px",
			"font-weight": "lighter"
		});*/
		$(".circle").css({
			"stroke": "#405040",
			//"fill": "grey"
		});
	}
}