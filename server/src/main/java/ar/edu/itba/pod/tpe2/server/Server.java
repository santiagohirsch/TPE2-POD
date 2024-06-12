package ar.edu.itba.pod.tpe2.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import static ar.edu.itba.pod.tpe2.server.utils.ServerUtils.*;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("hz-config Server Starting ...");

        Map<String, String> argsMap = parseArgs(args);
        String mask = argsMap.get(MASK);
        checkNullArgument(mask, MASK_ERROR_MSG);

        // Config
        Config config = new Config();
        // Group Config
        GroupConfig groupConfig = new
                GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASS);
        config.setGroupConfig(groupConfig);
        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();
        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);
        InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Collections.singletonList(mask)).setEnabled(true);
        NetworkConfig networkConfig = new
                NetworkConfig().setInterfaces(interfacesConfig).setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);
        // Management Center Config
//        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig()
//                .setUrl("http://localhost:8080/mancenter/")
//                .setEnabled(true);
//        config.setManagementCenterConfig(managementCenterConfig);
        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
