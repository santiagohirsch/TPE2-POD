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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private static final java.util.logging.Logger performanceLogger = java.util.logging.Logger.getLogger("performanceLogger");
    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");

        String query = args[0];
        System.out.println(query);
        Map<String, String> argsMap = parseArgs(args);

        List<String> addresses = parseAddresses(argsMap.get(ADDRESSES));
        String city = argsMap.get(CITY);
        String inPath = argsMap.get(IN_PATH);
        String outPath = argsMap.get(OUT_PATH);

        checkNullArgument(argsMap.get(ADDRESSES), ADDRESSES_ERROR_MSG);
        checkNullArgument(city, CITY_ERROR_MSG);
        checkNullArgument(inPath, IN_PATH_ERROR_MSG);
        checkNullArgument(outPath, OUT_PATH_ERROR_MSG);

        HazelcastInstance hazelcastInstance = getHazelcastInstance(addresses);

        StringBuilder infractionsPath = new StringBuilder(inPath).append("/");
        StringBuilder ticketsPath = new StringBuilder(inPath).append("/");


        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler(outPath + "/time" + query.substring(5) + ".txt");
            fileHandler.setFormatter(getFormatter());
            performanceLogger.addHandler(fileHandler);
        } catch (IOException | SecurityException e) {
            throw new RuntimeException(e);
        }


        Runnable queryInstance = null;
        switch (city.toUpperCase()) {
            case "NYC" -> {
                logger.info("Reading " + city.toUpperCase() + " infractions CSV");
                IMap<Integer, String> nycInfractionsMap = hazelcastInstance.getMap(INFRACTIONS_MAP_NAME);
                infractionsPath.append(NYC_INFRACTIONS);
                InfractionsPopulator<Integer> nycInfractionsPopulator = new InfractionsPopulator<>(infractionsPath.toString(), nycInfractionsMap, new NYCInfractionParser());
                performanceLogger.info("Inicio de la lectura del archivo: " + infractionsPath);
                nycInfractionsPopulator.run();
                performanceLogger.info("Fin de la lectura del archivo: " + infractionsPath);

                logger.info("Reading " + city.toUpperCase() + " tickets CSV");
                IMap<Integer, NYCTicket> nycTicketsMap = hazelcastInstance.getMap(TICKETS_MAP_NAME);
                ticketsPath.append(NYC_TICKETS);
                TicketsPopulator<NYCTicket> nycTicketsPopulator = new TicketsPopulator<>(ticketsPath.toString(), nycTicketsMap, new NYCTicketFactory());
                performanceLogger.info("Inicio de la lectura del archivo: " + ticketsPath);
                nycTicketsPopulator.run();
                performanceLogger.info("Fin de la lectura del archivo: " + ticketsPath);

                switch (query) {
                    case "query1" -> queryInstance = new Query1<>(QUERY1_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                    case "query2" -> queryInstance = new Query2<>(QUERY2_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                    case "query3" -> {
                        String n = argsMap.get(N);
                        checkNullArgument(n, N_ERROR_MSG);
                        queryInstance = new Query3<>(QUERY3_JOB_NAME, hazelcastInstance, nycTicketsMap, outPath, n);
                    }
                    case "query4" -> {
                        String from = argsMap.get(FROM);
                        String to = argsMap.get(TO);
                        checkNullArgument(from, FROM_ERROR_MSG);
                        checkNullArgument(to, TO_ERROR_MSG);
                        queryInstance = new Query4<>(QUERY4_JOB_NAME, hazelcastInstance, nycTicketsMap, outPath, from, to);
                    }
                    case "query5" -> queryInstance = new Query5<>(QUERY5_JOB_NAME, hazelcastInstance, nycInfractionsMap, nycTicketsMap, outPath);
                    default -> {
                        logger.error("Invalid query");
                        System.exit(1);
                    }

                }
            }
            case "CHI" -> {
                logger.info("Reading " + city.toUpperCase() + " infractions CSV");
                IMap<String, String> chiInfractionsMap = hazelcastInstance.getMap(INFRACTIONS_MAP_NAME);
                infractionsPath.append(CHI_INFRACTIONS);
                InfractionsPopulator<String> chiInfractionsPopulator = new InfractionsPopulator<>(infractionsPath.toString(), chiInfractionsMap, new CHIInfractionParser());
                performanceLogger.info("Inicio de la lectura del archivo: " + infractionsPath);
                chiInfractionsPopulator.run();
                performanceLogger.info("Fin de la lectura del archivo " + infractionsPath);

                logger.info("Reading " + city.toUpperCase() + " tickets CSV");
                IMap<Integer, CHITicket> chiTicketsMap = hazelcastInstance.getMap(TICKETS_MAP_NAME);
                ticketsPath.append(CHI_TICKETS);
                TicketsPopulator<CHITicket> chiTicketsPopulator = new TicketsPopulator<>(ticketsPath.toString(), chiTicketsMap, new CHITicketFactory());
                performanceLogger.info("Inicio de la lectura del archivo: " + ticketsPath);
                chiTicketsPopulator.run();
                performanceLogger.info("Fin de la lectura del archivo: " + ticketsPath);

                switch (query) {
                    case "query1" -> queryInstance = new Query1<>(QUERY1_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                    case "query2" -> queryInstance = new Query2<>(QUERY2_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                    case "query3" -> {
                        String n = argsMap.get(N);
                        checkNullArgument(n, N_ERROR_MSG);
                        queryInstance = new Query3<>(QUERY3_JOB_NAME, hazelcastInstance, chiTicketsMap, outPath, n);
                    }
                    case "query4" -> {
                        String from = argsMap.get(FROM);
                        String to = argsMap.get(TO);
                        checkNullArgument(from, FROM_ERROR_MSG);
                        checkNullArgument(to, TO_ERROR_MSG);
                        queryInstance = new Query4<>(QUERY4_JOB_NAME, hazelcastInstance, chiTicketsMap, outPath, from, to);
                    }
                    case "query5" -> queryInstance = new Query5<>(QUERY5_JOB_NAME, hazelcastInstance, chiInfractionsMap, chiTicketsMap, outPath);
                    default -> {
                        logger.error("Invalid query");
                        System.exit(1);
                    }
                }
            }
        }

        performanceLogger.info("Inicio del trabajo map/reduce");
        queryInstance.run();
        performanceLogger.info("Fin del trabajo map/reduce");

        // Shutdown
        HazelcastClient.shutdownAll();
    }
}
