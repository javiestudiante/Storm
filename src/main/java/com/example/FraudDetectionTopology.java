package com.example;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;

public class FraudDetectionTopology {
    public static void main(String[] args) {
        try {
            // Crear el TopologyBuilder
            TopologyBuilder builder = new TopologyBuilder();

            // Añadir Spout
            builder.setSpout("transaction-spout", new TransactionSpout());

            // Añadir Bolts
            builder.setBolt("filter-bolt", new FilterBolt()).shuffleGrouping("transaction-spout");
            builder.setBolt("alert-bolt", new AlertBolt()).shuffleGrouping("filter-bolt");

            // Configurar la topología
            Config config = new Config();
            LocalCluster cluster = new LocalCluster();

            // Enviar la topología al clúster local
            cluster.submitTopology("FraudDetectionTopology", config, builder.createTopology());

            // Ejecutar por un tiempo definido
            Utils.sleep(10000);
            cluster.shutdown();

        } catch (AlreadyAliveException | InvalidTopologyException e) {
            System.err.println("Error al iniciar la topología: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}