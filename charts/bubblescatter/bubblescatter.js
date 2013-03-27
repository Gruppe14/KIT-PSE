function bubblescatter(json, radius) {

    var data;
    var xAxisName;
    var yAxisName;
    var zAxisName;
    var measure;
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

    log("The radius is " + radius);
    var bubble = (radius === undefined); //is it a bubblescatter or a scatterplot?
    log("Bubblechart? " + bubble);
    log("I read " + json.data.length + " data points.");
    xAxisName = json.attribute1;
    yAxisName = json.attribute2;
    radius = json.attribute3;
    measure = json.measureAttribute;


    data = json.data;
    log(data);
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

        return idxes.map(function (d) {
            return array[d];
        });

    }

    function keyGenerator() {
        //returns a unique, ascending key
        if (this.key == undefined) {
            this.key = 1;
        } else {
            this.key++;
        }
        log("The key was " + this.key);
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
        
        
        //first, check whether the data is numeric
        var xAxisNum = !data.some(function (d) {
            return isNaN(+getX(d));
        });
        var yAxisNum = !data.some(function (d) {
            return isNaN(+getY(d));
        });

        //then, find the element with the maximum length for the y axis
        var maxString = data.map(function (d) {
            return getY(d).toString();
        })
            .reduce(function (x, y) {
            return (x.length >= y.length) ? x : y;
        });

        //this is not guaranteed to have the maximum pixel length, but will have a good enough
        log("The string of maximum length was: " + maxString);
        var sp = $('<span id=\"bob\">' + maxString + "</span>").css("font-size", "11px").css("dy", ".32em");
        log(sp);
        var p = $("#chart").append(sp);
        log($("#bob"));

        //make sure no text is lost
        var bt = (11 / 7) * $("#bob").width();
        log(bt);
        margin.left = bt;

        //clean up
        $("#bob").remove();


        //calculate the width by the # of needed elements
        var elements = getUniqueValues(data.map(getX));
        var t = elements.length;

        //elements.map(function(d) { console.log(d);}); //for debugging until the bug is fixed

        log("# of different x values is " + t);
        log("xMax :" + margin.top);

        var l = (t > 5) ? t : 5;
        var w = l * 17 + margin.right;
        if (w < 50) {
            l = t * 20;
        }
        log("The width was set to: " + w);

        //do the same for the y axis
        var t3 = getUniqueValues(data.map(getY)).length;

        log("# of different y values is " + t3);
        var h = (!bubble && yAxisNum) ? 640 : (t3 * 8 + margin.bottom);

        log("The height was set to: " + h);

        //the format of the data
        var format = d3.format(".0");

        log("xAxisNum- the axis x is numeric: " + xAxisNum);
        log("yAxisNum- the axis y is numeric: " + yAxisNum);

        //the scales
        var xScale;
        var yScale;

        if (xAxisNum) {
            /* This is done to prevent a probable  bug in d3 -
             * it finds different extrema when the d3.min and d3.max
             * functions are called on the raw data and different
             * when they are called on the data after is has been
             * converted to numerical.
             */
            var xPoints = data.map(function (d) {
                return +getX(d);
            });

            var min = d3.min(xPoints);
            var max = d3.max(xPoints);

            /* //Compare:
             * var d3m = d3.min(data, getX)
             * var d3max = d3.max(data, getX)
             * log("d3 thinks: min: " + d3m + " d3 thinks: max: " + d3max);
             * log("The actual minimum is " + min);
             * log("The actual maximum is " + max);
             * log("Here are the points to find out the truth
             * log(xPoints);
             */

            xScale = d3.scale.linear()
                .domain([min, max])
                .range([0, w]);
            log("X axis was set to be linear.");

        } else {
            xScale = d3.scale.ordinal()
                .domain(data.map(getX))
                .rangePoints([0, w]);
            log("X axis was set to be ordinal.");
        }


        if (yAxisNum) {
			
			
			var yValues = data.map(function(d) { return +getY(d);});
			
			var min = d3.min(yValues);
			var max = d3.max(yValues);
			
			log("MIN of y axis WAS: " + min);
			log("MAX  of y axisWAS : " + max);
			
			if(!bubble) {
				yScale = d3.scale.sqrt()
                .domain([min, max])
                .range([h, 0]);
                log("Y axis was set to be a root.");
			}
			
			else {
				
				yScale = d3.scale.linear()
					.domain([min, max])
					.range([h, 0]);
				log("Y axis was set to be linear.");
			}
        }
        else {
            yScale = d3.scale.ordinal()
                .domain(data.map(getY))
                .rangePoints([h, 0]);
            log("Y axis was set to be ordinal.");


        }

        //now a scale that maps the radius, too!
        var rScale = d3.scale.linear()
            .domain([d3.min(data, getZ), d3.max(data, getZ)])
            .range([2, 8]); //when the dimensions gets different, we will make these percentages


        //the axes
        var xAxis = d3.svg.axis()
            .scale(xScale)
            .orient("bottom");
        if (!xAxisNum) {
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
            } else {
                return 3;
            }
        })
            .attr("class", "circle")
        //.style("fill", getRandomColor);
        .append("svg:title")

        .text(function (d) {
            var txt = xAxisName + ":" + getX(d) + "\n";

            if (bubble) {
                txt += yAxisName + ":" + getY(d) + "\n" + measure + " of " + radius + ":" + getZ(d);
            } else {
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
