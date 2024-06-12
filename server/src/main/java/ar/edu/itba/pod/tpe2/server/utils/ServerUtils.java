package ar.edu.itba.pod.tpe2.server.utils;

import ar.edu.itba.pod.tpe2.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ServerUtils {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static final String GROUP_NAME = "g12";
    public static final String GROUP_PASS = "g12-pass";
    public static String MASK = "mask";
    public static String MASK_ERROR_MSG = "No mask was specified";

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
}
