package org.apache.edgent.samples.topology;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class TCPServerThread {

    private Thread serv;
    private Socket socket;
    private ServerSocket server;
    private volatile List<String[]> send_list = new LinkedList<>();
    private final Object lock = new Object();

    private long start = System.currentTimeMillis();
    private float total;

    public void run(){
        if(serv == null){
            serv = new Thread(() -> {
                try {
                    server = new ServerSocket(25005);
                    System.out.println("Waiting for connection");
                    socket = server.accept();
                    System.out.println("Connected to Succint");

                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    CSVWriter writer = new CSVWriter(out);
                    String[] header = { "Timestamp", "Senseur1", "Senseur2" };
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
        send_list.add(new String[] {
                "end",
                "end"
        });
    }

    private void printRate(){
        System.out.println(total / ((System.currentTimeMillis() - start) / 1000) + "/s");
    }


}
