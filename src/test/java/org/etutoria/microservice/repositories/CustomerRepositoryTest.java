package org.etutoria.microservice.repositories;

import org.assertj.core.api.AssertionsForClassTypes;
import org.etutoria.microservice.entities.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        // cette méthode est exécutée avant chaque test
        customerRepository.save(Customer.builder()
                .firstName("Ousmane")
                .lastName("Mbacke")
                .email("usmanembacke@gmail.com")
                .build());
        customerRepository.save(Customer.builder()
                .firstName("Mouhamed")
                .lastName("Mbacke")
                .email("mohamed@gmail.com")
                .build());
        customerRepository.save(Customer.builder()
                .firstName("Moussa")
                .lastName("Mbacke")
                .email("moussa@gmail.com")
                .build());
    }

    @Test
    public void shouldFindCustomerByEmail() {
        String givenEmail = "usmanembacke@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        // AssertionsForClassTypes permet de tester les optionals
        assertThat(result).isPresent(); // je m'attends à ce que le résultat soit présent
    }
    //tester un autre scénario le cas où le client n'existe pas
    @Test
    public void shouldNotFindCustomerByEmail() {
        String givenEmail = "xxx@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(givenEmail);
        assertThat(result).isEmpty(); // je m'attends à ce que le résultat ne soit pas présent
    }

    @Test
    public void shouldFindCustomerByFirstNameContainsIgnoreCase() {
        String keyword = "n";//chercher les clients dont le prénom contient "ou" en ignorant la casse
        //ici on defint ce qu'on s'attend à avoir
        List<Customer> expected = List.of(
                Customer.builder()
                        .firstName("Ousmane")
                        .lastName("Mbacke")
                        .email("usmanembacke@gmail.com")
                        .build()

        );
        List<Customer> result = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);//je m'attends à avoir un seul client
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);//je m'attends à avoir le client Ousmane comme le expectedResult qui est le res attendu
    }
}
