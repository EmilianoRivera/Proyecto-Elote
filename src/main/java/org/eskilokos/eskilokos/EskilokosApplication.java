package org.eskilokos.eskilokos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.eskilokos.eskilokos.core.entidades")
public class EskilokosApplication {

    public static void main(String[] args) {
        SpringApplication.run(EskilokosApplication.class, args);
    }
    
}
