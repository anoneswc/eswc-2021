package org.apache.edgent.samples.topology;

import com.opencsv.CSVWriter;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class CSVFileExporter {

    private Thread serv;
    private Socket socket;
    private volatile List<String[]> send_list = new LinkedList<>();
    private final Object lock = new Object();

    private long start = System.currentTimeMillis();
    private float total;

    public void run(){
        if(serv == null){
            serv = new Thread(() -> {
                try {
                    Writer out = new FileWriter("output.csv");
                    CSVWriter writer = new CSVWriter(out);
                    String[] header = { "Timestamp", "Valeur", "Ignorer" };
                    writer.writeNext(header);

                    while (true) {
                        synchronized (lock) {
                            if (!send_list.isEmpty()) {
                                writer.writeNext(send_list.remove(0));
                                writer.flush();
                            }
                        }
                    }

                } catch (IOException  e) {
                    e.printStackTrace();
                } finally {

                    try {
                        if(socket != null){
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        serv.start();
    }

    public void stop(){
        if(serv != null){
            serv.interrupt();
        }
    }

    public void send(String[] s) {
        total += 1;
        synchronized (lock) {
            send_list.add(s);
        }
        printRate();
    }

    public void sendWindow(List<String[]> lst) {
        total += lst.size();
        synchronized (lock) {
            send_list.addAll(lst);
        }
        printRate();
    }

    private void printRate(){
        System.out.println(total / ((System.currentTimeMillis() - start) / 1000) + "/s");
    }
}
