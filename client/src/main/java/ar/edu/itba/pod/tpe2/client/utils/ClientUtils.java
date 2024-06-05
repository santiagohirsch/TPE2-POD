package ar.edu.itba.pod.tpe2.client.utils;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientUtils {

    private static final String GROUP_NAME = "g12";
    private static final String GROUP_PASS = "g12-pass";
    public static final String ADDRESSES = "addresses";
    public static final String CITY = "city";
    public static final String IN_PATH = "inPath";
    public static final String OUT_PATH = "outPath";
    public static final String N = "n";
    public static final String FROM = "from";
    public static final String TO = "to";


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

    public static void checkNullArgument(String arg) {
        if(arg == null) {
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
}