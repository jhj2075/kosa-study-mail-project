package futurenet.fullstack.reservation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("futurenet.fullstack.reservation.mapper")
@SpringBootApplication
public class ReservationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReservationServiceApplication.class, args);
  }
}
