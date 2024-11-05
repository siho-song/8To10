package show.schedulemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SchedulemanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulemanagementApplication.class, args);
    }

}
