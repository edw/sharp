# Sharp

A directed graph library.

## Installation

Add the following dependency to your `project.clj` file:

```
[edw/sharp "0.1.0"]
```

## Usage

Currently, three functions are implemented. Future version of this
library may support incrementally building graphs, but the
representation of graph edges in this implementation--a bitfield--does
not lend itself to a simple implementation of graphs where the number
and identity of vertices is unknown at time of construction.

**graph** [OUT IN]...

The function  takes any number of arguments, each argument being a vector
describing a graph edge, the the two elements describing the "out" and
"in" vertices of each edge, respectively. A graph is returned.

**in-edges** GRAPH VERTEX

The function returns a set of every vertex from which an in-edge
originates that has VERTEX as an out-edge.

**out-edges** GRAPH VERTEX

The function returns a set of every vertex which has an in-edge
originating from VERTEX.


## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
