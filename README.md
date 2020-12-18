# Abstract 

In  this  paper,  we  present  a  knowledge  graph  managementsystem that handles data streams at the edge of a computing infrastruc-ture. Such systems are needed for anomaly and risk detection in differentkinds  of  plants  or  building  equipped  with  sensors  and  actuators.  Thelimitations,e.g.,  constrained  computing  power  and  storage  space,  andexpectations,e.g., low latency, high throughput and smart data manage-ment, of Edge computing have been considered during the design phase ofour prototype. As a result, our system, named Streaming SuccinctEdge,adopts a compact, in-memory, streaming-enabled RDF management thatsupports continuous SPARQL queries and RDFS reasoning. We demon-strate its correctness, latency and throughput properties on real-worlduse cases originating from an energy management IoT setting.

https://arxiv.org/abs/2012.07108



# Commands to Run Edgent

The jar is found in eswc-2021\Edgent\out\artifacts\edgent_succinct_jar

java -jar edgent-succinct.jar [Options]

## Edgent Options

### Send types
Up to one of these

-s           // Send everything immediatly DEFAULT   

-b           // Send batch containing raw mesures contained in a window      

-a           // Send batch containing aggregated result by max, min or mean

### Aggregate types, only used with option -a
Up to one of these

-max   	     		// DEFAULT            

-min            

-mean

### Scenario for measures as seen in Figure 6
Up to one of these

-0       

-1

-2

-3

-4

### Window types, only used with option -a and -b
Up to one of these

-sliding	 		 // Window type DEFAULT

-tumbling		 // Window type 

### General Options
Up to two of these

-frequency	 // Frequency of measurements in MICROSECONDS DEFAUTLT: 100 000 (100ms/measure)

-window 	   // Window size in MILLISECONDS, only used with option -a or -b DEFAULT! 5000

# Commands to run SuccinctEdge

First you must have the sdsl library installed, found here https://github.com/simongog/sdsl-lite

run these commands to compile the code:

```
cmake ./
make
```

### Run

to run Succinct with Edgent in stream or aggregate mode:

```
./SuccinctEdge_cata ./test_data/sensor_test.nt ./test_query/sensor_test.sparql ./store/sensor_test/ ./simu_tbox/ false true true
```

if you want to run Edgent with option -b, you must run SuccinctEdge with the following command:

```
./SuccinctEdge_cata ./test_data/sensor_test.nt ./test_query/sensor_test.sparql ./store/sensor_test/ ./simu_tbox/ false true true true
```
