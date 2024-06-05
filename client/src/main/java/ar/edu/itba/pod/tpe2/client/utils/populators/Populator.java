package ar.edu.itba.pod.tpe2.client.utils.populators;

import com.hazelcast.core.IMap;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Populator<K, V> implements Runnable {
    private static final int BATCH_SIZE = 1024;
    private static final int LINE_LIMIT = 1000000;
    private final CSVReader reader;
    private final Map<K, V> currentMap;
    private final IMap<K, V> hazelcastIMap;

    public Populator(String csvPath, IMap<K, V> hazelcastIMap) {
        this.reader = InitReader(csvPath);
        this.currentMap = new HashMap<>(BATCH_SIZE);
        this.hazelcastIMap = hazelcastIMap;
    }

    private CSVReader InitReader(String csvPath) {
        FileReader filereader;
        try {
            filereader = new FileReader(csvPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found in " + csvPath);
        }

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        return new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();
    }

    private void flushCurrentMap() {
        hazelcastIMap.putAll(currentMap);
    }

    protected abstract IMap.Entry<K, V> consumeLine(String[] line);

    @Override
    public void run() {
        String[] line;
        int i = 0;

        try {
            while((line = reader.readNext()) != null && i < LINE_LIMIT) {
                if (currentMap.size() == BATCH_SIZE) {
                    flushCurrentMap();
                    currentMap.clear();
                }

                IMap.Entry<K, V> entry = consumeLine(line);
                currentMap.putIfAbsent(entry.getKey(), entry.getValue());
                i++;

                if (i % 100000 == 0) {
                    System.out.println("Read " + i + " lines");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        flushCurrentMap();

        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
