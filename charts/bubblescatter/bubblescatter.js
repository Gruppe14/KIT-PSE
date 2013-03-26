function bubblescatter(json, radius) {
    //console.log("The radius is " + radius);
	var bubble = (radius === undefined); //is it a bubblescatter or a scatterplot?
	//console.log("Bubblechart? " + bubble);
	
	var data;
    var xAxisName;
    var yAxisName;
	var zAxisName;
	var measure;
	
   // console.log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
	radius = json.attribute3;
	measure = json.measureAttribute;
	
	
    data = json.data;
	//console.log(data);
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
    
    function getUniqueValues(array) {
		//O(n). I think
		var idxes = []; //the indexes of the original array that are unique
		var objectSet = {};
		
		for (var i = 0; i < array.length; i++) {
			
			if (!objectSet.hasOwnProperty(array[i])) {
				idxes.push(i);
				objectSet[array[i]] = true;
			}
		}
		
		return idxes.map(function(d) { return array[d];});
		
	}
	
	function keyGenerator() {
		//returns a unique, ascending key
		if (this.key == undefined) {
			this.key = 1;
		}
		else {
			this.key++;
		}
		//console.log("The key was " + this.key);
		return this.key;
		
	}

    function visualize(data) {
        
        //dimensions
        var margin = {
            top: 30,
            right: 30,
            bottom: 50,
            left: 40
        };
        
        //first, find the element with the maximum length for the y axis
        var maxString =  data.map(function(d) { return getY(d).toString();})
        .reduce(function(x , y) { return (x.length >= y.length) ? x : y;});
        
        //this is not guaranteed to have the maximum pixel length, but will have a good enough
		console.log(maxString);
		var sp = $('<span id=\"bob\">' + maxString + "</span>").css("font-size", "11px").css("dy", ".32em");
		//console.log(sp);
		var p = $("#chart").append(sp);
		//console.log($("#bob"));
		
		//make sure no text is lost
		var bt = (11 / 7) * $("#bob").width();
        console.log(bt);
        margin.left = bt;
        
        //clean up
        $("#bob").remove();
        
        
		//calculate the width by the # of needed elements
		var elements = getUniqueValues(data.map(getX));
		var t = elements.length;
		
		elements.map(function(d) { console.log(d);}); //for debugging until the bug is fixed
		
        console.log("# of different x values is " + t);
		//console.log("xMax :" + margin.top);		

        var l = (t > 5) ? t : 5;
        var w = l * 17 + margin.right;
        if (w < 50) {
            l = t * 20;
        }
        console.log("The width was set to: " + w);

        //do the same for the y axis
		var t3 = getUniqueValues(data.map(getY)).length;
        
        console.log("# of different y values is " + t3);
        var h = t3 * 8 + margin.bottom;
        console.log("The height was set to: " + h);

        //the format of the data
        var format = d3.format(".0");

		
		//first, check whether the data is numeric
		var xAxisNum = !data.some ( function (d) {
			return isNaN(+getX(d));
		});
		var yAxisNum = !data.some ( function(d) {
			return isNaN(+getY(d));
		});
		
		console.log("xAxisNum- the axis x is numeric: " + xAxisNum);
		console.log("yAxisNum- the axis y is numeric: " + yAxisNum);

        //the scales
        var xScale;
		var yScale;
		
		if (xAxisNum) {
			xScale = d3.scale.linear()
			.domain([d3.min(data, getX), d3.max(data, getX)])
			.range([0, w ]);
			console.log("X axis was set to be linear.");
			
			console.log("The minimum point was " + d3.min(data, getX));
			console.log("The maximum was " + d3.max(data, getX));
			console.log("The bug is here. d3.{min,max} doesn't find the one we want.");
		}
		else {
			xScale = d3.scale.ordinal()
			.domain(data.map(getX))
			.rangePoints([0, w]);
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
			.rangePoints([h, 0]);
			console.log("Y axis was set to be ordinal.");

				
		}
		
		//now a scale that maps the radius, too!
        var rScale = d3.scale.linear()
            .domain([d3.min(data, getZ), d3.max(data, getZ)])
            .range([2, 8]); //when the dimensions gets different, we will make these percentages
		

        //the axes
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient("bottom");
		if(!xAxisNum) {
			xAxis.tickValues(data.map(getX));
			//xAxis.tickPadding(margin.left);
		}

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
            .data(data, keyGenerator) //assigns an id to each data point
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
			//.style("fill", getRandomColor);
			.append("svg:title")
			
            .text(function (d) {
				var txt = xAxisName + ":" + getX(d) + "\n";
				
				if (bubble) {
					txt += yAxisName + ":" + getY(d) +"\n" + measure + " of " + radius + ":" + getZ(d);
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
			//.append("text")
			//.attr("id", "x-axis-description")
			//.attr("x", w)
			//.attr("y", -6)
			//.style("text-anchor", "end")
			//.text(xAxisName);
			

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
			.attr("x", "8")
			.css("text-anchor", "");
		//rotate text
		$("#x-axis text").attr("transform", "rotate(-270)");
    }
}
