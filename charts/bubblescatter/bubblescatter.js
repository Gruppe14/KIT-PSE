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
	var measure;
	
    console.log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
	radius = json.attribute3;
	measure = json.measureAttribute;
	
	
    data = json.data;
	console.log(data);
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
			
        //dimensions
        var margin = {
            top: 100,
            right: 100,
            bottom: 100,
            left: 100
        };
        var w = 1000 - margin.left - margin.right;
        var h = 800 - margin.top - margin.bottom;
		
       // var padding = 30;
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
				.range([0, w ]);
			console.log("X axis was set to be linear.");
		}
		else {
			xScale = d3.scale.ordinal()
				.domain(data.map(getX))
				.rangeRoundBands([0, w ], 0.05);
			console.log("X axis was set to be ordinal.");
			
		}
		
		
		if (yAxisNum) {
	        yScale = d3.scale.linear()
	            .domain([d3.min(data, getY), d3.max(data, getY)])
				.range([h, 0]);
				console.log("Y axis was set to be linear.");
		}
		else {
			yScale = d3.scale.ordinal()
				.domain(data.map(getY))
				.rangeRoundBands([h, 0], 0.05);
				console.log("Y axis was set to be ordinal.");

				
		}
		
		//now a scale that maps the radius, too!
	    var rScale = d3.scale.linear()
			    .domain([d3.min(data, getZ), d3.max(data, getZ)])
	            .range([2, 5]); //when the dimensions gets different, we will make these percentages
		

        //the axes
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient("bottom");

        var yAxis = d3.svg.axis()
            .scale(yScale)
            .orient("left");



        $("#chart").html("");
        //the svg chart!
		var svg = d3.select("#chart").append("svg")
		.attr("width", w + margin.left + margin.right)
		.attr("height", h + margin.top + margin.bottom)
		.append("g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
		
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
				var txt = xAxisName + ":" + getX(d) + "\n";
				
				if (bubble) {
					txt += yAxisName + ":" + getY(d) +"\n" + measure + "of " + radius + ":" + getZ(d);
				}
				else {
					txt += measure + " of " + yAxisName + ":" + getY(d);
				}
				return txt;
			});


        //create the axes, too
        svg.append("g")
            .attr("class", "axis")
			.attr("id", "x-axis")
            .attr("transform", "translate(0," + h + ")")
            .call(xAxis)
			.append("text")
			.attr("id", "x-axis-description")
			.attr("x", w)
			.attr("y", -6)
			.style("text-anchor", "end")
			.text(xAxisName);
			

        svg.append("g")
            .attr("class", "axis")
			.attr("id", "y-axis")
            //.attr("transform", "translate(" + 0 + ",0)")
            .call(yAxis)
			.append("text")
			.attr("id", "y-axis-description")
			.attr("transform", "rotate(-90)")
            .attr("y", 6)
			//.attr("x", -50)
            .attr("dy", ".85em")
            .style("text-anchor", "end")
			.text(yAxisName);


		//position names
		$("#x-axis > g > text")
			.attr("class", "x-axis-text")
			.attr("dy", "")
			.attr("y", "4")
			.attr("x", "3")
			.css("text-anchor", "");
		//rotate text
		$("#x-axis text").attr("transform", "rotate(270)");
    }
}