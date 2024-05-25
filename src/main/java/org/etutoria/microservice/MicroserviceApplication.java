package org.etutoria.microservice;

import org.etutoria.microservice.entities.Customer;
import org.etutoria.microservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
    @Bean
    @Profile("!test")//pour dire à spring de ne pas exécuter cette méthode si on est en mode test car on a déjà inséré les données dans le test
    CommandLineRunner commandLineRunner(
            CustomerRepository customerRepository
    ){
        return  args -> {
            customerRepository.save(
                    Customer.builder()
                            .firstName("Ousmane").lastName("Mbacke").email("usmanembacke@gmail.com")
                            .build());
            customerRepository.save(
                    Customer.builder()
                            .firstName("Mouhamed").lastName("Mbacke").email("mohamed@gmail.com")
                            .build());
            customerRepository.save(
                    Customer.builder()
                            .firstName("Moussa").lastName("Mbacke").email("moussa@gmail.com")
                            .build());

        };
    }

}
