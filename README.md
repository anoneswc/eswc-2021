# Commands to Run Edgent

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

./SuccinctEdge_cata ./test_data/sensor_test.nt ./test_query/sensor_test.sparql ./store/sensor_test/ ./simu_tbox/ false true true

if you want to run Edgent with option -b, you must run SuccinctEdge with the following command:
./SuccinctEdge_cata ./test_data/sensor_test.nt ./test_query/sensor_test.sparql ./store/sensor_test/ ./simu_tbox/ false true true true
