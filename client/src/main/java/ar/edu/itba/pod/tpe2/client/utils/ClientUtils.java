package ar.edu.itba.pod.tpe2.client.utils;

import ar.edu.itba.pod.tpe2.client.Client;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ClientUtils {

    private final static Logger logger = LoggerFactory.getLogger(ClientUtils.class);
    public static final String GROUP_NAME = "g12";
    public static final String GROUP_PASS = "g12-pass";
    public static final String NYC_INFRACTIONS = "infractionsNYC.csv";
    public static final String CHI_INFRACTIONS = "infractionsCHI.csv";
    public static final String QUERY1_JOB_NAME = "query1";
    public static final String QUERY2_JOB_NAME = "query2";
    public static final String QUERY3_JOB_NAME = "query3";
    public static final String QUERY4_JOB_NAME = "query4";
    public static final String QUERY5_JOB_NAME = "query5";
    public static final String INFRACTIONS_MAP_NAME = "infractions-map";
    public static final String TICKETS_MAP_NAME = "tickets-map";
    public static final String NYC_TICKETS = "ticketsNYC.csv";
    public static final String CHI_TICKETS = "ticketsCHI.csv";
    public static final String ADDRESSES = "addresses";
    public static final String CITY = "city";
    public static final String IN_PATH = "inPath";
    public static final String OUT_PATH = "outPath";
    public static final String N = "n";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String ADDRESSES_ERROR_MSG = "No addresses were specified";
    public static final String CITY_ERROR_MSG = "No city was specified";
    public static final String IN_PATH_ERROR_MSG = "No input path was specified";
    public static final String OUT_PATH_ERROR_MSG = "No output path was specified";
    public static final String N_ERROR_MSG = "No n parameter was specified";
    public static final String FROM_ERROR_MSG = "No beginning date was specified";
    public static final String TO_ERROR_MSG = "No ending date was specified";



    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argsMap = new HashMap<>();

        for(String arg : args) {
            String[] parts = arg.split("=");
            if(parts.length == 2) {
                argsMap.put(parts[0].substring(2), parts[1]);
            }

        }
        return argsMap;
    }

    public static void checkNullArgument(String arg, String msg) {
        if(arg == null) {
            logger.error(msg);
            System.exit(1);
        }
    }

    public static HazelcastInstance getHazelcastInstance(List<String> addresses) {
        // Client Config
        ClientConfig clientConfig = new ClientConfig();
        // Group Config
        GroupConfig groupConfig = new
                GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASS);
        clientConfig.setGroupConfig(groupConfig);
        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.setAddresses(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    public static List<String> parseAddresses(String addresses) {
        return Arrays.asList(addresses.split(";"));
    }

    public static SimpleFormatter getFormatter() {
        return new SimpleFormatter() {
            private final String format = "dd/MM/yyyy HH:mm:ss:SSSS";

            @Override
            public String format(LogRecord record) {
                String message = record.getMessage();
                String level = record.getLevel().toString();
                String thread = Thread.currentThread().getName();
                String className = extractClassName(record.getSourceClassName());
                // TODO: agregar linea de donde se llamo, tiene que quedar asi:
                // TODO: format level [thread] class (class.java:line) - message
                return String.format(
                        "%s %s [%s] %s - %s\n",
                        new SimpleDateFormat(format).format(record.getMillis()),
                        level,
                        thread,
                        className,
                        message
                );
            }
        };
    }

    private static String extractClassName(String sourceClassName) {
        int lastIndex = sourceClassName.lastIndexOf(".");
        if (lastIndex != -1) {
            return sourceClassName.substring(lastIndex + 1);
        }
        return sourceClassName;
    }

}
