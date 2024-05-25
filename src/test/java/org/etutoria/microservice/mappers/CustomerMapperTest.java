package org.etutoria.microservice.mappers;

import org.assertj.core.api.AssertionsForClassTypes;
import org.etutoria.microservice.dtos.CustomerDTO;
import org.etutoria.microservice.entities.Customer;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {
CustomerMapper underTest = new CustomerMapper();
@Test
    public void shouldMapCustomerToCustomerDTO() {
        //Given
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@gmail.com");
        //customerDTO avec les mêmes valeurs que customer pour pouvoir comparer est ce que le mapping est correct
        CustomerDTO expected = new CustomerDTO();
        expected.setFirstName("John");
        expected.setLastName("Doe");
        expected.setEmail("john@gmail.com");
        //When cad appeler la méthode à tester
        CustomerDTO result = underTest.fromCustomer(customer);//ici on appelle la méthode à tester on lui passe un customer et on s'attend à avoir un customerDTO
        //ce resultat doit être égale à expected pour que le test soit validé
        //Then
    assertThat(expected).usingRecursiveComparison().isEqualTo(result);




    }

    @Test
    public void shouldMapCustomerDTOToCustomer() {
        //Given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setEmail("john1@gmail.com");
        //customer avec les mêmes valeurs que customerDTO pour pouvoir comparer est ce que le mapping est correct
        Customer expected = new Customer();
        expected.setFirstName("John");
        expected.setLastName("Doe");
        expected.setEmail("john1@gmail.com");

        //When cad appeler la méthode à tester
        Customer result = underTest.fromCustomerDTO(customerDTO);//ici on appelle la méthode à tester on lui passe un customerDTO et on s'attend à avoir un customer

        //Then
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);
        assertThat(result).isNotNull();
    }


    @Test
    public void shouldMapListCustomersToListCustomerDTO() {
        //Given
        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john2@gmail.com");

        Customer customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Doe");
        customer2.setEmail("jane2@gmail.com");

        //When
        List<Customer> customers = List.of(customer1, customer2);
      //cree une liste de customerdto expected avec les meme valeurs
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setFirstName("John");
        customerDTO1.setLastName("Doe");
        customerDTO1.setEmail("john2@gmail.com");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setFirstName("Jane");
        customerDTO2.setLastName("Doe");
        customerDTO2.setEmail("jane2@gmail.com");
        List<CustomerDTO> expected = List.of(customerDTO1, customerDTO2);

        List<CustomerDTO> result = underTest.fromListCustomers(customers);
        //Then
        assertThat(expected).usingRecursiveComparison().isEqualTo(result);

}

//teste les autres scenario ici je vais simuler l'attente d'une exception
    //scénario ou il génére des cas exceptionnel
    @Test
    public void shouldNotMapNullCustomerToCustomerDTO() {
        //Given
        Customer customer = null;
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setFirstName("John");
        customerDTO1.setLastName("Doe");
        customerDTO1.setEmail("john2@gmail.com");

        //When
        //ici j'appelle
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.fromCustomer(customer))
                .isInstanceOf(IllegalArgumentException.class);


    }


}