function bubblechart(json, sorted) {
    var data;
    var logging = json.logging;

    if (logging != true) {
        logging = false;
    }

    function logger(enabled) {
        //creates a function, that logs when the enabled logging param is true
        //it also reports whether it was logged. Why not?
        self.logOrNot = enabled;

        function p(message) {
            if (logOrNot) {
                console.log(message);
            }
            return logOrNot;
        }

        return p;

    }

    var log = logger(logging); //our logging function. 
    //It logs when logging is enabled. Cleans up syntax.

    log("I read " + json.data.length + " data points.");

    var xAxisName = json.attribute1;
    var yAxisName = json.attribute2;
    var measure = json.measureAttribute;

    data = json.data;
    log(data);
    visualize(data); //then start the visualization

    function getRandomColor() {
        var color = "#";
        for (var i = 0; i < 6; i++) {
            var rd = Math.floor(Math.random() * 16);
            if (rd < 2) {
                i--;
                continue;
            }
            color += rd.toString(16);
        }
        return color;
    }

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
                log("Error, the data doesn't have an numeric attribute");
                chartError();
            }
            return (b - a);
        }

        //this contains the scales.
        var bubble = d3.layout.pack()
            .value(getY) //set the accessor function to be the second attribute
        .sort(comparator)
            .size([w, h])
            .padding(padding);

        if (sorted !== false) {
            bubble.sort(comparator);
        }

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
            .filter(function (d) {
            return !d.children;
        }))
        //don't return the root node as it doesn't have any children
        .enter().append("g")
            .attr("class", "circle")
            .attr("transform", function (d) {
            return "translate(" + d.x + "," + d.y + ")";
        });

        //this is the hovertext
        circle.append("title")
            .text(function (d) {
            return xAxisName + ":" + getX(d) + "\n " + measure + " of " + yAxisName + ": " + getY(d);
        });

        //draw the circle
        circle.append("circle")
            .attr("r", function (d) {
            return d.r;
        })
            .style("fill", getRandomColor);

        //this is the text written over the circle.
        circle.append("text")
            .attr("class", "data-text")
            .attr("dy", ".3em")
            .style("text-anchor", "middle")
            .text(function (d) {
            //calculate how much of the title to write
            //it depends on the radius
            return getX(d).substring(0, d.r / 3.15);
        });

        $("#y-axis-description").attr("transform", "rotate(270)")
    }
}
