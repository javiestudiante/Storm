package com.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Values;
import org.apache.storm.tuple.Fields;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
public class TransactionSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private BufferedReader reader;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            reader = new BufferedReader(new FileReader("resources/bank_transactions_data_2.csv"));
            reader.readLine(); // Saltar la cabecera
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo CSV", e);
        }
    }

    @Override
    public void nextTuple() {
        try {
            String line = reader.readLine();
            if (line != null) {
                collector.emit(new Values(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("transaction"));
    }
}
