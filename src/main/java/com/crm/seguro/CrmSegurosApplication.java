package com.crm.seguro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CrmSegurosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmSegurosApplication.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin";
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println(hashedPassword);
	}

}