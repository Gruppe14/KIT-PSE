function barchart(json, sorted) {
    
    function getX(d) {
        return d[xAxisName];
    }

    function getY(d) {
        return d[yAxisName];
    }
    
    var data;
    var xAxisName;
    var yAxisName;
    var measure;

   
    console.log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
    measure = json.measureAttribute;
    //console.log("yAxisName=" + yAxisName);
    data = json.data;
    data.map(function(d) { console.log(getX(d));});
    //  data.map(function(i) {console.log(i);});
    visualize(data); //then start the visualization

    function getRandomColor() {
        var color = "#";
        for (var i = 0; i < 6; i ++) {
            var rd = Math.floor(Math.random() * 16);
            if (rd < 2) {
            	i--;
            	continue;
            }
            color += rd.toString(16);
        }
        return color;
    }

    function visualize(data) {
        
        //let's calculate how many types we have
        var t = d3.values(data.map(getY)).length;
        //console.log("Different y values is " + t);
        var yMin = d3.min(data, getY);
		
		var yMax = d3.max(data, getY);
		var ratio = yMax / yMin;
		
		var n = 0;
		for(var tmp = yMax; tmp > 1; tmp = tmp / 10) {
			n++;
		}

        //dimensions
        var margin = {
            top: 30,
            right: 20,
            bottom: 20,
            left: d3.max([12 * (n + Math.floor(n/3)-1), 50]) 
        };
        
        //console.log("xMax :" + margin.top);
        var l = (data.length > 5) ? data.length : 5;
        var w = l * 30 - margin.left - margin.right;
        if (w < 40) {
            w = t * 30;
        }
        //console.log("The width is: " + w);
        var h = 480 - margin.top - margin.bottom;

        $("#chart").html("");
        //the svg
        var svg = d3.select("#chart")
            .insert("svg")
            .attr("class", "chart")
            .attr("width", w + margin.left + margin.right)
            .attr("height", h + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            
        //some way to arrange the data
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
        
        //let's sort the data...
        if (sorted != false) {
			data.sort(comparator);
		}

        //the scales
        var xScale = d3.scale.ordinal()
            //.sort(comparator)
            .domain(data.map(getX))
            .rangeRoundBands([0, w], 0.04);

        var yScale = d3.scale.linear();
        yScale.domain([ yMax, yMin]).range([0, h]);
			

        //the axes
        var xAxis = d3.svg.axis().scale(xScale).orient("bottom");
        var yAxis = d3.svg.axis().scale(yScale).orient("left");
		

        //add the data and bars
        svg.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("fill", getRandomColor)
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
            .attr("class", "bar")
            //add hovertext
            .append("title")
            .text(function(d) {
                return xAxisName + ":" + getX(d) + "\n " + measure + " of " + yAxisName + ":" + getY(d);
            });
            
            
        //create axes
        //x axis
        svg.append("g").attr("class", "axis")
            .attr("transform", "translate(0," + h + ")")
            .attr("id", "x-axis")
            .call(xAxis)
            .append("text") //here comes its description
            .attr("id", "x-axis-description")
			//x and y are interchanged because it all is transformed by 90 degree
			//see barchart.css
			.attr("x", 5)
            .attr("y", w + 5)
			.text(xAxisName);
			
		
        //y axis
        svg.append("g")
            .attr("class", "axis")
            .attr("id", "y-axis")
            .call(yAxis)
            .append("text")
   			.attr("id", "y-axis-description")
            .attr("y", 6)
            .attr("dy", ".71em")
			.attr("transform", "rotate(270)")
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
		$("#x-axis text").attr("transform", "rotate(270)")
    }
}
