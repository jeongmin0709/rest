package org.zerock.ciub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CiubApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiubApplication.class, args);
    }

}
