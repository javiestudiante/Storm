package com.example;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class AlertBolt extends BaseRichBolt {

    @Override
    public void prepare(Map<String, Object> stormConf, TopologyContext context, OutputCollector collector) {
        // No se necesita inicialización específica para este Bolt
    }

    @Override
    public void execute(Tuple input) {
        try {
            // Obtener la transacción sospechosa
            String suspiciousTransaction = input.getStringByField("suspicious_transaction");

            // Imprimir la alerta (o enviarla a un sistema de monitoreo)
            System.out.println("ALERTA: Transacción sospechosa detectada -> " + suspiciousTransaction);

        } catch (Exception e) {
            System.err.println("Error procesando la tupla: " + input);
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // No emite nuevas tuplas, así que no se declaran campos de salida
    }
}