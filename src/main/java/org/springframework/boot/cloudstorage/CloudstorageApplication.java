package org.springframework.boot.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CloudstorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudstorageApplication.class, args);
	}

}
