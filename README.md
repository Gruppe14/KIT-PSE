KIT-PSE [![Build Status](https://travis-ci.org/Gruppe14/KIT-PSE.png)](https://travis-ci.org/Gruppe14/KIT-PSE)
=======

Visualizing and Statistically Analyzing Access Behavior to Scientific Databases:

For many scientific databases, it would be worthwhile to visually and statistically analyze query logs to, e.g., know which parts of the data the user community finds interesting, i.e., accesses frequently with their queries, and which ones are less interesting. However, visualization tools supporting statistical query log analysis are currently missing due to two main reasons. First, the format of query logs, which primarily consists of texts, is not directly suitable for a graphical presentation. It is thus desirable to transform them into a representation that facilitates visual statistical analysis, e.g., multi-dimensional data points. This new form of query logs essentially contains both categorical (e.g., name of table being accessed) and numerical (e.g., access time) attributes. The unavoidable presence of categorical attributes on the other hand constitutes the second reason why it is hard to visualize query logs. In particular, there is no well-established form to graphically display and statistically analyze multi-dimensional categorical data. Such forms, e.g., scatter plot, nevertheless abound for numerical data. One thus expects to have additional techniques to apply the existing rich sets of visual statistical analysis tools for numerical data on categorical data. This does not exclude the possibility of converting categorical data to numerical one.

The objective of this PSE assignment is to build software that takes raw database query logs as input and transform them into multi-dimensional numerical data points, i.e., into a representation that can be readily used for visual statistical analysis. Students working on this assignment will not only be exposed to database technology (to an extent manageable within the PSE) and basic statistics concepts, they will also design and implement technology that is of high practical value.



This program does just that.

To run:
==============
install [java play](http://www.playframework.org/documentation/2.1-RC2/Installing)

git clone http://github.com/Gruppe14/KIT-PSE.git

play run
