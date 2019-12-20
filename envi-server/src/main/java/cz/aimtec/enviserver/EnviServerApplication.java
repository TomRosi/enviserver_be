package cz.aimtec.enviserver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication(scanBasePackages={"cz.aimtec.enviserver"}) // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class EnviServerApplication {

    
	public static void main(String[] args) {				
		Logger logger = LoggerFactory.getLogger(EnviServerApplication.class);
		logger.debug("Starting EnviServer application...");
		
		SpringApplication.run(EnviServerApplication.class, args);
	}
}