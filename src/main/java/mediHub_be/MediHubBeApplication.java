package mediHub_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MediHubBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediHubBeApplication.class, args);
    }
}
