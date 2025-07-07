package cz.svonavec.tennis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class ReservationApplication {

    public static void main(String[] args) {
        log.info("Tennis reservation application starting...");
        SpringApplication.run(ReservationApplication.class, args);
        log.info("Application started");
    }

}