package cz.aimtec.enviserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"cz.aimtec.enviserver"}) // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class EnviServerApplication {
	  
	public static void main(String[] args) {				
		Logger logger = LoggerFactory.getLogger(EnviServerApplication.class);
		logger.debug("Starting EnviServer application...");
		
		SpringApplication.run(EnviServerApplication.class, args);
	}
}