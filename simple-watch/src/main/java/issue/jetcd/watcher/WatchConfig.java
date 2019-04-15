package issue.jetcd.watcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.etcd.jetcd.Client;

@Configuration
public class WatchConfig {

    private static final String[] ETCD_ENDPOINTS = { 
                "http://etcd0:2379", 
                "http://etcd1:2379", 
                "http://etcd2:2379" 
    };
    
    @Bean public Client etcdClient() {
        return Client.builder().endpoints(ETCD_ENDPOINTS).build();
    }
}
