package org.apache.edgent.samples.topology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private String filePath;
    private List<List<String>> table = new ArrayList<>();

    private CSVReader(String filePath){
        this.filePath = filePath;
    }

    public CSVReader of(String filePath){
        CSVReader reader = new CSVReader(filePath);

        reader.build();

        return reader;
    }

    private void build() {
        try {
            Reader reader = new FileReader(filePath);
            String line;
            //while(line = reader.read)
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}
