package issue.jetcd;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JetcdIssue532Application {
    
    public static void main(String[] args) throws InterruptedException {
        
        SpringApplication app = new SpringApplication(
                JetcdIssue532Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);

        // block forever just for demo purposes
        (new CountDownLatch(1)).await();
    }
}
