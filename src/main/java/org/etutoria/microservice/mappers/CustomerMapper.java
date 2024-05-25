package org.etutoria.microservice.mappers;

import org.etutoria.microservice.dtos.CustomerDTO;
import org.etutoria.microservice.entities.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerMapper {
    private ModelMapper modelMapper = new ModelMapper();
    public CustomerDTO fromCustomer(Customer customer){
        return  modelMapper.map(customer,CustomerDTO.class);
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        return modelMapper.map(customerDTO,Customer.class);
    }

    //pour convertir une liste de Customer en liste de CustomerDTO
    public List<CustomerDTO> fromListCustomers(List<Customer> customers){
        //on utilise la méthode map de la classe Stream pour convertir chaque élément de la liste en CustomerDTO
        //le this fait référence à l'instance de la classe CustomerMappe
        return customers.stream().map(this::fromCustomer).collect(Collectors.toList());
    }
}
