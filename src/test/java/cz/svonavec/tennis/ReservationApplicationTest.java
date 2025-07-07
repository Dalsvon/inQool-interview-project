package cz.svonavec.tennis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest(classes = ReservationApplication.class)
@EnableTransactionManagement
class ReservationApplicationTest {
    @Test
    void contextLoads() {}
}

