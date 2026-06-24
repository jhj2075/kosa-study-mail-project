package futurenet.fullstack.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("futurenet.fullstack.scheduler.mapper")
@EnableScheduling
@SpringBootApplication
public class SchedulerServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SchedulerServiceApplication.class, args);
  }
}
