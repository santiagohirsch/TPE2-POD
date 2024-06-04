package ar.edu.itba.pod.tpe2.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static ar.edu.itba.pod.tpe2.client.utils.ClientUtils.*;

import java.util.List;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");
        String addresses = "192.168.64.1:5701";
        HazelcastInstance hazelcastInstance = getHazelcastInstance(parseAddresses(addresses));
        String mapName = "testMap";
        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);
        testMapFromMember.set(1, "test1");
        IMap<Integer, String> testMap = hazelcastInstance.getMap(mapName);
        System.out.println(testMap.get(1));
        // Shutdown
        HazelcastClient.shutdownAll();
    }
}
