package org.apache.edgent.samples.topology;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.edgent.function.Supplier;
import org.apache.edgent.providers.direct.DirectProvider;
import org.apache.edgent.topology.TStream;
import org.apache.edgent.topology.TWindow;
import org.apache.edgent.topology.Topology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean stream = true;
        boolean batch = false;
        boolean aggregate = false;

        boolean max = true;
        boolean min = false;
        boolean mean = false;

        int scenario = 0;

        boolean sliding = true;
        boolean tumbling = false;

        int frequency = 100000;
        int window_size = 5000;

        int i = 0;
        while(i < args.length){
            String arg = args[i];
            switch(arg){
                case "-s":      stream = true;  batch = false;  aggregate = false; break;   // Send everything immediatly DEFAULT
                case "-b":      stream = false; batch = true;   aggregate = false; break;   // Send batch containing raw numbers in window
                case "-a":      stream = false; batch = false;  aggregate = true; break;    // Send batch containing aggregated result by max, min or mean

                case "-max":    max = true;     min = false;    mean = false; break;        // DEFAULT
                case "-min":    max = false;    min = true;     mean = false; break;
                case "-mean":   max = false;    min = false;    mean = true; break;

                case "-0": scenario = 0; break;      // no anomaly DEFAULT
                case "-1": scenario = 1; break;
                case "-2": scenario = 2; break;
                case "-3": scenario = 3; break;
                case "-4": scenario = 4; break;

                case "-tumbling":   sliding = false;    tumbling = true; break;             // Window type
                case "-sliding":    sliding = true;     tumbling = false; break;            // DEFAULT

                case "-frequency":      i += 1; frequency = Integer.parseInt((args[i])); break;      // Frequency of measurements in MILLISECONDS
                case "-window":    i += 1; window_size = Integer.parseInt((args[i])); break;    // Window size in MILLISECONDS

                default: throw new IllegalArgumentException("Unknown option " + arg);
            }
            i += 1;
        }
        Supplier<Pair<Double, Long>> sensor;
        if(scenario == 0)
            sensor = new NoAnomalySensor();
        else if(scenario == 1)
            sensor = new SomeAnomalySensor();
        else if(scenario == 2)
            sensor = new SerieAnomalySensor();
        else if(scenario == 3)
            sensor = new GradualAnomalySensor();
        else
            sensor = new AbruptAnomalySensor();

        DirectProvider dp = new DirectProvider();
        Topology topology = dp.newTopology();

        //TCPServerThread sink = new TCPServerThread();
        //CSVFileExporter sink = new CSVFileExporter();
        TCPThread sink = new TCPThread();
        sink.run();

        TStream<Pair<Double, Long>> raw = topology.poll(sensor, frequency, TimeUnit.MICROSECONDS);
        TStream<Pair<Double, Long>> data;

        if(aggregate){
            TWindow<Pair<Double, Long>, Object> window = raw.last(window_size, TimeUnit.MILLISECONDS, k -> 0);
            if(max)
                if(tumbling)
                    data = window.batch((lst,k) -> Collections.max(lst));
                else
                    data = window.aggregate((lst,k) -> Collections.max(lst));
            else if(min)
                if(tumbling)
                    data = window.batch((lst,k) -> Collections.max(lst));
                else
                    data = window.aggregate((lst,k) -> Collections.min(lst));
            else
                if(tumbling)
                    data = window.batch((lst,k) -> Collections.max(lst));
                else
                    data = window.aggregate((lst,k) -> mean(lst));

            data.sink(
                    vals -> sink.send(new String[] {
                            vals.getRight().toString(),
                            vals.getLeft().toString()
                    }));
        }
        else if (batch){
            TWindow<Pair<Double, Long>, Object> window = raw.last(window_size, TimeUnit.MILLISECONDS, k -> 0);
            if(tumbling)
                window.batch((lst,k) -> aggregateWindow(lst)).sink(sink::sendWindow);
            else
                window.aggregate((lst,k) -> aggregateWindow(lst)).sink(sink::sendWindow);
        }

        if(stream)
            raw.sink(
                vals -> sink.send(new String[] {
                        vals.getRight().toString(),
                        vals.getLeft().toString()
                }));

        dp.submit(topology);
    }

    // TODO mean the second sensor
    private static Pair<Double, Long> mean(List<Pair<Double, Long>> lst) {
        if(lst.isEmpty())
            return Pair.of(null,null);
        double res = 0D;
        Pair<Double, Long> t = null;
        for(Pair<Double, Long> triple: lst) {
            t = triple;
            Double d = t.getLeft();
            res += d;
        }
        res =  res / lst.size();
        return Pair.of(res, t.getRight());
    }

    private static List<String[]> aggregateWindow(List<Pair<Double, Long>> source){
        List<String[]> lst = new ArrayList<>();
        for(Pair<Double, Long> pair: source){
            lst.add(new String[]{String.valueOf(System.currentTimeMillis()), pair.getLeft().toString()});
        }

        return lst;
    }
}
