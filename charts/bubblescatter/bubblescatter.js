function bubblescatter(json, radius) {
	//TODO: Scale the third dimension
	//TODO: Add a fourth dimension, color
    //console.log("The radius is " + radius);
	var bubble = (radius == undefined); //is it a bubblescatter or a scatterplot?
	//console.log("Bubblechart? " + bubble);
	
	var data;
    var xAxisName;
    var yAxisName;
	
	var zAxisName;
	
    console.log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
	
	radius = json.attribute3;
	
	
    data = json.data;
    visualize(data); //then start the visualization

    function getX(d) {
        return d[xAxisName];
    }

    function getY(d) {
        return d[yAxisName];
    }

    function getZ(d) {
        return d[radius];
    }

    function visualize(data) {
        var w = 720;
        var h = 640;
        var padding = 30;
        //the format of the data
        var format = d3.format(".0");

		
		//first, check whether the data is numeric
		var xAxisNum = !data.some ( function (d) {
			return isNaN(+getX(d));
		})
		var yAxisNum = !data.some ( function(d) {
			return isNaN(+getY(d));
		})
		
		console.log("xAxisNum- the axis x is numeric: " + xAxisNum);
		console.log("yAxisNum- the axis y is numeric: " + yAxisNum);
		//at what point do you use arrays to simplify code?

        //the scales
        var xScale;
		var yScale;
		
		if (xAxisNum) {
			xScale = d3.scale.linear()
	            .domain([d3.min(data, getX), d3.max(data, getX)])
				.range([padding, w - padding]);
		}
		else {
			xScale = d3.scale.ordinal()
				.domain(data.map(getX))
				.rangePoints([padding, w - padding]);
			
		}
		
		
		if (xAxisNum) {
	        yScale = d3.scale.linear()
	            .domain([d3.min(data, getY), d3.max(data, getY)])
				.range([h - padding, padding]);
		}
		else {
			yScale = d3.scale.ordinal()
				.domain(data.map(getY))
				.rangePoints([h - padding, padding]);

				
		}
		
		//now a scale that maps the radius, too!
	    var rScale = d3.scale.linear()
			    .domain([d3.min(data, getZ), d3.max(data, getZ)])
	            .range([0.6, 5]) //when the dimensions gets different, we will make these percentages
		

        //the axes
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient("bottom")
            .ticks(3)
            .tickFormat(format);

        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient("left")
            .ticks(3)
            .tickFormat(format);


        $("#chart").html("");
        //the svg chart!
        var svg = d3.select("#chart")
            .append("svg")
            .attr("width", w)
            .attr("height", h);

        //create the points of the scatterplot
        //well, they are svg circles
        svg.selectAll("circle")
            .data(data)
            .enter()
            .append("circle")
            .attr("cx", function (d) {
				return xScale(getX(d));
			})
            .attr("cy", function (d) {
				return yScale(getY(d));
			})
            .attr("r", function (d) {
				//console.log(getZ(d));
				//console.log(bubble);
				if (bubble) {
					return rScale(getZ(d));
				}
				else {
					return 3;
				}
			})
			.attr("class", "circle")
			.append("svg:title")
			
		    .text(function (d) {
				var txt = "(" + getX(d) + "," + getY(d) + ",";
				if (bubble) {
					txt += getZ(d);
				}
				txt += ")";
				return txt;
			});


        //create the axes, too
        svg.append("g")
            .attr("class", "axis")
			.attr("id", "x-axis")
            .attr("transform", "translate(0," + (h - padding) + ")")
            .call(xAxis)
			.append("text")
			.attr("id", "x-axis-description")
			.attr("x", (w -  2 * padding))
			.text(xAxisName);
			

        svg.append("g")
            .attr("class", "axis")
			.attr("id", "y-axis")
            .attr("transform", "translate(" + padding + ",0)")
            .call(yAxis)
			.append("text")
			.attr("id", "y-axis-description")
			.attr("transform", "rotate(-90)")
            .attr("y", 6)
			.attr("x", -30)
            .attr("dy", ".85em")
            .style("text-anchor", "end")
			.text(yAxisName);


		//add the attribute names.
    }
}