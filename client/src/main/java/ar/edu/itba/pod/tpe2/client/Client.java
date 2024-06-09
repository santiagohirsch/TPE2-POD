package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.tpe2.client.utils.populators.*;
import ar.edu.itba.pod.tpe2.client.utils.populators.keyParsers.CHIInfractionParser;
import ar.edu.itba.pod.tpe2.client.utils.populators.keyParsers.NYCInfractionParser;
import ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories.CHITicketFactory;
import ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories.NYCTicketFactory;
import ar.edu.itba.pod.tpe2.client.utils.queries.*;
import ar.edu.itba.pod.tpe2.models.*;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static ar.edu.itba.pod.tpe2.client.utils.ClientUtils.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");
        String addresses = "192.168.1.137:5701";
        HazelcastInstance hazelcastInstance = getHazelcastInstance(parseAddresses(addresses));

        String cityName = "CHI";
        StringBuilder infractionsPath = new StringBuilder("../TPE2-datasets/");
        StringBuilder ticketsPath = new StringBuilder("../TPE2-datasets/");
        String outPath = "../TPE2-out/";
        String query = "query5";
        int n = 10; //query3 -> top n agencies
        //query4 -> from to
        LocalDateTime from = LocalDateTime.of(2004, 1, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2004, 12, 31, 23, 59, 59);

        Runnable queryInstance = null;
        switch (cityName.toUpperCase()) {
            case "NYC" -> {
                IMap<Integer, String> nycInfractionsMap = hazelcastInstance.getMap(INFRACTIONS_MAP_NAME);
                infractionsPath.append(NYC_INFRACTIONS);
                InfractionsPopulator<Integer> nycInfractionsPopulator = new InfractionsPopulator<>(infractionsPath.toString(), nycInfractionsMap, new NYCInfractionParser());
                logger.info("Inicio de la lectura del archivo: " + infractionsPath);
                nycInfractionsPopulator.run();
                logger.info("Fin de la lectura del archivo: " + infractionsPath);

                IMap<Integer, NYCTicket> nycTicketsMap = hazelcastInstance.getMap(TICKETS_MAP_NAME);
                ticketsPath.append(NYC_TICKETS);
                TicketsPopulator<NYCTicket> nycTicketsPopulator = new TicketsPopulator<>(ticketsPath.toString(), nycTicketsMap, new NYCTicketFactory());
                logger.info("Inicio de la lectura del archivo: " + ticketsPath);
                nycTicketsPopulator.run();
                logger.info("Fin de la lectura del archivo: " + ticketsPath);

                switch (query) {
                    case "query1" -> queryInstance = new Query1<>(QUERY1_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                    case "query2" -> queryInstance = new Query2<>(QUERY2_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                    case "query3" -> queryInstance = new Query3<>(QUERY3_JOB_NAME, hazelcastInstance, nycTicketsMap, outPath, n);
                    case "query4" -> queryInstance = new Query4<>(QUERY4_JOB_NAME, hazelcastInstance, nycTicketsMap, outPath, from, to);
                    case "query5" -> queryInstance = new Query5<>(QUERY5_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                    default -> {
                        logger.error("Invalid query");
                        System.exit(1);
                    }

                }
            }
            case "CHI" -> {
                IMap<String, String> chiInfractionsMap = hazelcastInstance.getMap(INFRACTIONS_MAP_NAME);
                infractionsPath.append(CHI_INFRACTIONS);
                InfractionsPopulator<String> chiInfractionsPopulator = new InfractionsPopulator<>(infractionsPath.toString(), chiInfractionsMap, new CHIInfractionParser());
                logger.info("Inicio de la lectura del archivo: " + infractionsPath);
                chiInfractionsPopulator.run();
                logger.info("Fin de la lectura del archivo " + infractionsPath);

                IMap<Integer, CHITicket> chiTicketsMap = hazelcastInstance.getMap(TICKETS_MAP_NAME);
                ticketsPath.append(CHI_TICKETS);
                TicketsPopulator<CHITicket> chiTicketsPopulator = new TicketsPopulator<>(ticketsPath.toString(), chiTicketsMap, new CHITicketFactory());
                logger.info("Inicio de la lectura del archivo: " + ticketsPath);
                chiTicketsPopulator.run();
                logger.info("Fin de la lectura del archivo: " + ticketsPath);

                switch (query) {
                    case "query1" -> queryInstance = new Query1<>(QUERY1_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                    case "query2" -> queryInstance = new Query2<>(QUERY2_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                    case "query3" -> queryInstance = new Query3<>(QUERY3_JOB_NAME, hazelcastInstance, chiTicketsMap, outPath, n);
                    case "query4" -> queryInstance = new Query4<>(QUERY4_JOB_NAME, hazelcastInstance, chiTicketsMap, outPath, from, to);
                    case "query5" -> queryInstance = new Query5<>(QUERY5_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                    default -> {
                        logger.error("Invalid query");
                        System.exit(1);
                    }
                }
            }
        }

        logger.info("Inicio del trabajo map/reduce");
        queryInstance.run();
        logger.info("Fin del trabajo map/reduce");

        // Shutdown
        HazelcastClient.shutdownAll();
    }
}
