package com.example;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class FilterBolt extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String line = input.getString(0);
        String[] fields = line.split(","); // Separar los campos del CSV

        try {
            // Extraer datos relevantes
            double amount = Double.parseDouble(fields[2]); // TransactionAmount
            String location = fields[5];                  // Location
            String channel = fields[10];                 // Channel
            int duration = Integer.parseInt(fields[11]); // TransactionDuration
            double balance = Double.parseDouble(fields[13]); // AccountBalance
            String occupation = fields[9];               // CustomerOccupation
            int age = Integer.parseInt(fields[8]);       // CustomerAge

            // Criterios de sospecha
            boolean isSuspicious = 
                amount > 10000 ||                         // Monto alto
                duration < 5 ||                           // Duración rápida
                balance - amount < 100 ||                 // Saldo bajo tras transacción
                (occupation.equals("Student") && amount > 5000) || // Inconsistente con ocupación
                location.equals("Unknown");               // Ubicación desconocida

            if (isSuspicious) {
                // Emitir transacción sospechosa
                collector.emit(new Values(line));
            }
        } catch (Exception e) {
            System.err.println("Error procesando la línea: " + line);
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("suspicious_transaction"));
    }
}