package ar.edu.itba.pod.tpe2.client;

import ar.edu.itba.pod.tpe2.client.utils.populators.*;
import ar.edu.itba.pod.tpe2.client.utils.populators.keyParsers.CHIInfractionParser;
import ar.edu.itba.pod.tpe2.client.utils.populators.keyParsers.NYCInfractionParser;
import ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories.CHITicketFactory;
import ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories.NYCTicketFactory;
import ar.edu.itba.pod.tpe2.client.utils.queries.Query1;
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

        String cityName = "NYC";
        StringBuilder infractionsPath = new StringBuilder("../TPE2-datasets/");
        StringBuilder ticketsPath = new StringBuilder("../TPE2-datasets/");
        String outPath = "../TPE2-out/";

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
                Query1<Integer, Double, LocalDate, NYCTicket> nycQuery1 = new Query1<>(QUERY1_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                logger.info("Inicio del trabajo map/reduce");
                nycQuery1.run();
                logger.info("Fin del trabajo map/reduce");
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
                Query1<String, Integer, LocalDateTime, CHITicket> chiQuery1 = new Query1<>(QUERY1_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                chiQuery1.run();
            }
        }

        // Shutdown
        HazelcastClient.shutdownAll();
    }
}
