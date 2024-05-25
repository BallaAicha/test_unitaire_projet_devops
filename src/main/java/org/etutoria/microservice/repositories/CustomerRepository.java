package org.etutoria.microservice.repositories;

import org.etutoria.microservice.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
     Optional<Customer> findByEmail(String email);
        List<Customer> findByFirstNameContainsIgnoreCase(String keyword);//pour chercher par nom en ignorant la casse



}
