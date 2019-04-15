package issue.jetcd.watcher;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import io.etcd.jetcd.watch.WatchResponse;

@Component public class Watch {

    private static final Logger LOG = LoggerFactory.getLogger(Watch.class);
    
    private static final String ETCD_KEY = "key";

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    
    @Autowired private Client etcd ;
    
    @PostConstruct public void initialize() { 
        
        LOG.info("Initializing etcd watch"); 
        ByteSequence key = ByteSequence.from(ETCD_KEY, UTF_8);
        WatchOption opts = WatchOption
                .newBuilder().withRevision(1).build();
        
        etcd.getWatchClient().watch(key, opts, 
                this::onWatchUpdate, 
                this::onWatchError);
    }
    
    private void onWatchUpdate(WatchResponse update) { 
        
        update.getEvents().stream()
            .map(WatchEvent::getKeyValue)
            .forEach(kv -> LOG.info("KEY: {} - VALUE: {}", 
                 kv.getKey().toString(UTF_8),
                 kv.getValue().toString(UTF_8)));
    }
    
    private void onWatchError(Throwable error) { 
        
        LOG.error("Watch error", error);
    }
}
