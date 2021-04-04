package pet.kozhinov.iron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class Iron {
    public static void main(String[] args) {
        SpringApplication.run(Iron.class, args);
    }
}
