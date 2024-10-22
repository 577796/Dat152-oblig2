package no.hvl.dat152.rest.ws.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("no.hvl.dat152.rest.ws.repository")
@EntityScan("no.hvl.dat152.rest.ws.model")
@ComponentScan(basePackages = {"no.hvl.dat152.rest.ws.service", 
								"no.hvl.dat152.rest.ws.controller", 
								"no.hvl.dat152.rest.ws.main.config",
								"no.hvl.dat152.rest.ws.security"})
public class LibraryApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);

	}

}
