//read the data, store them in global variables
function scatterplot(json) {
    json = JSON.parse(json);
    var data;
    var xAxisName;
    var yAxisName;
        console.log("I read " + json.data.length + " data points.");
        xAxisName = json.attribute1;
        yAxisName = json.attribute2;
        data = json.data;
        visualize(data); //then start the visualization

    function visualize(data) {
        //dimensions of the chart
        var w = 720;
        var h = 640;
        //empty area between last element and chart borders
        var padding = 20;

        //the format of the data
        var format = d3.format(".0");

        //will use this one below
        function getX(d) {
            return d[xAxisName];
        };

        function getY(d) {
            return d[yAxisName];
        };

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
        var svg = d3.select("body")
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
            return xScale(d[xAxisName]);
        })
            .attr("cy", function (d) {
            return yScale(d[yAxisName]);
        })
            .attr("r", 2) //arbitrary. big enough? scale automatically?
        .attr("x", getX)
            .attr("x", getY)
            .attr("class", "circle")
            .append("svg:title")
            .text(function (d) {
            return "(" + getX(d) + "," + getY(d) + ")";
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