package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.tpe2.client.utils.populators.CHIInfractionsPopulator;
import ar.edu.itba.pod.tpe2.client.utils.populators.CHITicketsPopulator;
import ar.edu.itba.pod.tpe2.client.utils.populators.NYCInfractionsPopulator;
import ar.edu.itba.pod.tpe2.client.utils.populators.NYCTicketsPopulator;
import ar.edu.itba.pod.tpe2.client.utils.queries.Query1;
import ar.edu.itba.pod.tpe2.models.CHITicket;
import ar.edu.itba.pod.tpe2.models.NYCTicket;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static ar.edu.itba.pod.tpe2.client.utils.ClientUtils.*;

import java.util.Map;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");
        String addresses = "192.168.64.1:5701";
        HazelcastInstance hazelcastInstance = getHazelcastInstance(parseAddresses(addresses));

        String nycInfractionsPath = "../TPE2-datasets/infractionsNYC.csv";
        String nycInfractionsMapName = "nyc-infractions";
        IMap<Integer, String> nycInfractions = hazelcastInstance.getMap(nycInfractionsMapName);
        NYCInfractionsPopulator nycInfractionsPopulator = new NYCInfractionsPopulator(nycInfractionsPath, nycInfractions);
        logger.info("Inicio de la lectura del archivo: " + nycInfractionsPath);
        nycInfractionsPopulator.run();
        logger.info("Fin de la lectura del archivo: " + nycInfractionsPath);

//        String chiInfractionsPath = "../TPE2-datasets/infractionsCHI.csv";
//        String chiInfractionsMapName = "chi-infractions";
//        IMap<String, String> chiInfractions = hazelcastInstance.getMap(chiInfractionsMapName);
//        CHIInfractionsPopulator chiInfractionsPopulator = new CHIInfractionsPopulator(chiInfractionsPath, chiInfractions);
//        logger.info("Inicio de la lectura del archivo: " + chiInfractionsPath);
//        chiInfractionsPopulator.run();
//        logger.info("Fin de la lectura del archivo: " + chiInfractionsPath);

        String nycTicketsPath = "../TPE2-datasets/ticketsNYC.csv";
        String nycTicketsMapName = "nyc-tickets";
        IMap<Integer, NYCTicket> nycTickets = hazelcastInstance.getMap(nycTicketsMapName);
        NYCTicketsPopulator nycTicketsPopulator = new NYCTicketsPopulator(nycTicketsPath, nycTickets);
        logger.info("Inicio de la lectura del archivo: " + nycTicketsPath);
        nycTicketsPopulator.run();
        logger.info("Fin de la lectura del archivo: " + nycTicketsPath);

        Query1 query1 = new Query1("query1", hazelcastInstance, nycInfractions, nycTickets, "../TPE2-out/");
        logger.info("Inicio del trabajo map/reduce");
        query1.run();
        logger.info("Fin del trabajo map/reduce");

//        String chiTicketsPath = "../TPE2-datasets/ticketsCHI.csv";
//        String chiTicketsMapName = "chi-tickets";
//        IMap<Integer, CHITicket> chiTickets = hazelcastInstance.getMap(chiTicketsMapName);
//        CHITicketsPopulator chiTicketsPopulator = new CHITicketsPopulator(chiTicketsPath, chiTickets);
//        logger.info("Inicio de la lectura del archivo: " + chiTicketsPath);
//        chiTicketsPopulator.run();
//        logger.info("Fin de la lectura del archivo: " + chiTicketsPath);
//
//        System.out.println("NYC INFRACTIONS");
//        System.out.println(nycInfractions.size());
//        for (Map.Entry<Integer, String> entry : nycInfractions.entrySet()) {
//            System.out.println(entry);
//        }
//        System.out.println("--------------------");
//        System.out.println("CHI INFRACTIONS");
//        for (Map.Entry<String, String> entry : chiInfractions.entrySet()) {
//            System.out.println(entry);
//        }
//        System.out.println("--------------------");
//        System.out.println("NYC TICKETS");
//        System.out.println("Amount: " + nycTickets.size());
//        for (Map.Entry<Integer, NYCTicket> entry : nycTickets.entrySet()) {
//            System.out.println(entry);
//        }
//        System.out.println("--------------------");
//        System.out.println("CHI TICKETS");
//        System.out.println("Amount: " + chiTickets.size());

        // Shutdown
        HazelcastClient.shutdownAll();
    }
}
