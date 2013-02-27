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
        //dimensions
        var margin = {
            top: 30,
            right: 30,
            bottom: 30,
            left: 50
        };
        var w = 700 - margin.left - margin.right;
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
            .domain([ d3.max(data, getY), 0])
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
                
            return h - yScale(getY(d));
        })
            .attr("width", xScale.rangeBand())
            .attr("height", function (d) {
            return yScale(getY(d));
        })
            .attr("class", "bar");

        //create axes
        svg.append("g").attr("class", "x axis")
            .attr("transform", "translate(0," + h + ")").call(xAxis)
            .append("text")
            .attr("class", "xAxisDescription")
			.attr("x", (w -  margin.right))
			.text(xAxisName);

        svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
   			.attr("class", "yAxisDescription")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text(yAxisName);
            
    }
}
