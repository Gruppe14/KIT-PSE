function bubblescatter(json) {
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
        return d[zAxisName];
    }



    function visualize(data) {
        var w = 720;
        var h = 640;
        var padding = 30;

        //the format of the data
        var format = d3.format(".0");


        //the scales
        var xScale = d3.scale.linear()
            .domain([d3.min(data, getX), d3.max(data, getX)])
            .range([padding, w - padding]);
        var yScale = d3.scale.linear()
            .domain([d3.min(data, getY), d3.max(data, getY)])
            .range([h - padding, padding]);

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
            return xScale(d.x);
        })
            .attr("cy", function (d) {
            return yScale(d.y);
        })
            .attr("r", function (d) {
            return d.r;
        }) //arbitrary. big enough? scale automatically?
        .attr("x", getX)
            .attr("x", getY)
            .attr("class", "circle")
            .append("svg:title")
            .text(function (d) {
            return "(" + getX(d) + "," + getY(d) + "," +  getZ(d) +")";
        });


        //create the axes, too
        svg.append("g")
            .attr("class", "axis")
            .attr("transform", "translate(0," + (h - padding) + ")")
            .call(xAxis);

        svg.append("g")
            .attr("class", "axis")
            .attr("transform", "translate(" + padding + ",0)")
            .call(yAxis);





    }

}