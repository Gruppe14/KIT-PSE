function barchart(json) {
    
    function getX(d) {
        return d[xAxisName];
    }

    function getY(d) {
        return d[yAxisName];
    }
    
    var data;
    var xAxisName;
    var yAxisName;
    console.log(json);
    console.log(json.data);
   
        //console.log("I read " + json.data.length + " data points.");
        xAxisName = json.attribute1;
        yAxisName = json.attribute2;
        console.log("yAxisName=" + yAxisName);
        data = json.data;
        data.map(function(d) { console.log(getX(d));});
      //  data.map(function(i) {console.log(i);});
        visualize(data); //then start the visualization

 

    function visualize(data) {
		
		var yMax = d3.max(data, getY);
		var n = 0;
		for(var tmp = yMax; tmp > 1; tmp = tmp/10) {
			n++;
		}
		
        //dimensions
        var margin = {
            top: 30,
            right: 20,
            bottom: 20,
            left: d3.max([12*(n+Math.floor(n/3)-1), 50]) 
        };
        var w = data.length * 30 - margin.left - margin.right;
        var h = 400 - margin.top - margin.bottom;

        $("#chart").html("");
        //the svg
        var svg = d3.select("#chart")
            .insert("svg")
            .attr("class", "chart")
            .attr("width", w + margin.left + margin.right)
            .attr("height", h + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        //the scales
        var xScale = d3.scale.ordinal()
            .domain(data.map(getX))
            .rangeRoundBands([0, w], 0.04);

        
        var yScale = d3.scale.linear()
            .domain([ yMax, 0])
            .range([0, h]);
			

        //the axes
        var xAxis = d3.svg.axis().scale(xScale).orient("bottom");
        var yAxis = d3.svg.axis().scale(yScale).orient("left");
		
		
		




        //add the data and bars
        svg.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", function (d, i) {
            return xScale(getX(d));
        })
            .attr("y", function (d) {
                
            return yScale(getY(d));
        })
            .attr("width", xScale.rangeBand())
            .attr("height", function (d) {
            return h - yScale(getY(d));
        })
            .attr("class", "bar");

            
        //create axes
        //x axis
        svg.append("g").attr("class", "axis")
            .attr("transform", "translate(0," + h + ")")
            .attr("id", "x_axis")
            .call(xAxis)
            .append("text") //here comes its description
            .attr("id", "xAxisDescription")
			//x and y are interchanged because it all is transformed by 90 degree
			//see barchart.css
			.attr("x", 5)
            .attr("y", w + 5)
			.text(xAxisName);
			
		
        //y axis
        svg.append("g")
            .attr("class", "axis")
            .attr("id", "y_axis")
            .call(yAxis)
            .append("text")
   			.attr("id", "yAxisDescription")
            .attr("y", 6)
            .attr("dy", ".71em")
			.attr("transform", "rotate(270)")
            .style("text-anchor", "end")
            .text(yAxisName);
        
		//position names
		$("#x_axis > g > text")
			.attr("dy", "")
			.attr("y", "4")
			.attr("x", "3")
			.css("text-anchor", "");
		//rotate text
		$("#x_axis text").attr("transform", "rotate(270)")
        
        //####  all styles####
			.css({
				"font-family": "monospace",
				"font-size": "14px",
				"pointer-events": "none"
			});
		
		$("#yAxisDescription").css({
			"font-family": "monospace",
			"font-size": "14px",
			"font-weight": "bold"
		});
		
		$(".bar").css({
			"fill": "lightblue",
			"stroke": "white"
		});
        
        $(".axis path,.axis line").css({
            "fill": "none",
            "stroke": "black",
            "shape-rendering": "crispEdges"    
        });
        $("#xAxisDescription").css("font-weight", "bold");
    }
}
