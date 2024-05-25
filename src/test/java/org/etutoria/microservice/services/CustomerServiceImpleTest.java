package org.etutoria.microservice.services;

import org.assertj.core.api.AssertionsForClassTypes;
import org.etutoria.microservice.dtos.CustomerDTO;
import org.etutoria.microservice.entities.Customer;
import org.etutoria.microservice.exceptions.CustomerNotFoundException;
import org.etutoria.microservice.exceptions.EmailAlreadyExistException;
import org.etutoria.microservice.mappers.CustomerMapper;
import org.etutoria.microservice.repositories.CustomerRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)//pour dire à Junit d'utiliser l'extension de mockito cad de faire les tests avec mockito
class CustomerServiceImpleTest {
    //cette classe est pour tester les méthodes de la classe CustomerServiceImple

    @Mock
    private CustomerRepository customerRepository;//on crée un mock de CustomerRepository car on ne veut pas tester les méthodes de cette classe reelle
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImple underTest;//c'est la classe à tester on injecte les mocks dans cette classe cad on injecte les dépendances

    @Test
    void shouldSaveNewCustomer() {//tester la méthode saveNewCustomer
        //Given cad les données d'entrée
        CustomerDTO customerDTO= CustomerDTO.builder()
                .firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer customer= Customer.builder()
                .firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer savedCustomer= Customer.builder()
                .id(1L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        CustomerDTO expected= CustomerDTO.builder()
                .id(1L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());//on simule le comportement de la méthode findByEmail de CustomerRepository
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);//on simule le comportement de la méthode fromCustomerDTO de CustomerMapper
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);//on simule le comportement de la méthode save de CustomerRepository
        Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);//on simule le comportement de la méthode fromCustomer de CustomerMapper
        CustomerDTO result = underTest.saveNewCustomer(customerDTO);//on appelle la méthode à tester
        AssertionsForClassTypes.assertThat(result).isNotNull();//on vérifie que le résultat n'est pas null
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);//on vérifie que le résultat est égale à expected
    }


    @Test
    void shouldNotSaveNewCustomerWhenEmailExist() {//tester le cas où l'email existe déjà

        CustomerDTO customerDTO= CustomerDTO.builder()
                .firstName("Ismail").lastName("Matar").email("xxxxx@gmail.com").build();
        Customer customer= Customer.builder()
                .id(5L).firstName("Ismail").lastName("Matar").email("xxxxx@gmail.com").build();
        Mockito.when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.of(customer));//on simule le comportement de la méthode findByEmail de CustomerRepository
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.saveNewCustomer(customerDTO))
                .isInstanceOf(EmailAlreadyExistException.class);//on vérifie que l'exception levée est de type EmailAlreadyExistException
    }


    @Test
    void shouldGetAllCustomers() {//tester la méthode getAllCustomers
        List<Customer> customers = List.of(
                Customer.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                Customer.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                CustomerDTO.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        Mockito.when(customerRepository.findAll()).thenReturn(customers);//on simule le comportement de la méthode findAll de CustomerRepository
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);//on simule le comportement de la méthode fromListCustomers de CustomerMapper
        List<CustomerDTO> result = underTest.getAllCustomers();//on appelle la méthode à tester
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }


    @Test
    void shouldFindCustomerById() {
        Long customerId = 1L;
        Customer customer=Customer.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build();
        CustomerDTO expected=CustomerDTO.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(expected);
        CustomerDTO result = underTest.findCustomerById(customerId);
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }

    @Test
    void shouldNotFindCustomerById() {
        Long customerId = 8L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.findCustomerById(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage(null);
    }


    @Test
    void shouldSearchCustomers() {
        String keyword="m";
        List<Customer> customers = List.of(
                Customer.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                Customer.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        List<CustomerDTO> expected = List.of(
                CustomerDTO.builder().firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build(),
                CustomerDTO.builder().firstName("Ahmed").lastName("Yassine").email("ahmed@gmail.com").build()
        );
        Mockito.when(customerRepository.findByFirstNameContainsIgnoreCase(keyword)).thenReturn(customers);
        Mockito.when(customerMapper.fromListCustomers(customers)).thenReturn(expected);
        List<CustomerDTO> result = underTest.searchCustomers(keyword);
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }


    @Test
    void updateCustomer() {
        Long customerId= 6L;
        CustomerDTO customerDTO= CustomerDTO.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer customer= Customer.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Customer updatedCustomer= Customer.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        CustomerDTO expected= CustomerDTO.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomerDTO(customerDTO)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        Mockito.when(customerMapper.fromCustomer(updatedCustomer)).thenReturn(expected);
        CustomerDTO result = underTest.updateCustomer(customerId,customerDTO);
        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
    }


    @Test
    void shouldDeleteCustomer() {
        Long customerId =1L;
        Customer customer= Customer.builder()
                .id(6L).firstName("Ismail").lastName("Matar").email("ismail@gmail.com").build();
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);
    }


    @Test
    void shouldNotDeleteCustomerIfNotExist() {
        Long customerId =9L;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.deleteCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }

}