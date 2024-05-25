package org.etutoria.microservice.web;
import org.etutoria.microservice.dtos.CustomerDTO;
import org.etutoria.microservice.exceptions.CustomerNotFoundException;
import org.etutoria.microservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.Matchers;
import java.util.List;
@ActiveProfiles("test")
@WebMvcTest(CustomerRestController.class)//cette annotation permet de tester les méthodes de CustomerRestController le webMvcTest permet de tester les controllers
class CustomerRestControllerTest {
    //cette classe permet de tester les méthodes de CustomerRestController cad il fait des tests d'intégration qui consiste à tester les méthodes de CustomerRestController avec les dépendances réelles
    //ici on démarre le contexte spring et on teste les méthodes de CustomerRestController
    @MockBean//on crée un mock de CustomerService car on ne veut pas tester les méthodes de cette classe reelle ici c'est mockbean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;//c'est un objet qui permet de tester les méthodes des controllers
    @Autowired
    private ObjectMapper objectMapper;//c'est un objet qui permet de convertir les objets en json et vice versa

    List<CustomerDTO> customers;


    @BeforeEach
    void setUp() {
        //avant chaque test on initialise les données
        this.customers = List.of(
                CustomerDTO.builder().id(1L).firstName("Mohamed").lastName("Youssfi").email("med@gmail.com").build() ,
                CustomerDTO.builder().id(2L).firstName("Imane").lastName("Tawil").email("imane@gmail.com").build(),
                CustomerDTO.builder().id(3L).firstName("yasmine").lastName("Ibrahimi").email("yasmine@gmail.com").build()
        );
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        //envoie une requête get à l'URL /api/customers le perform permet de déclencher la requête et on aura une reponse http qu'on doit tester
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())//on s'attend à avoir un status 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))//on s'attend à avoir une liste de 3 éléments, avec jsonPath on peut tester le contenu de la réponse cad le nombre d'éléments dans la liste
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));//on s'attend à avoir une liste de customers , on verifie que le contenu de la réponse est égale à la liste de customers , le objectMapper permet de convertir la liste de customers en json
    }

    @Test
    void shouldGetCustomerById() throws Exception {
        Long id = 1L;
        Mockito.when(customerService.findCustomerById(id)).thenReturn(customers.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers.get(0))));
    }

    @Test
    void shouldNotGetCustomerByInvalidId() throws Exception {
        Long id = 9L;
        Mockito.when(customerService.findCustomerById(id)).thenThrow(CustomerNotFoundException.class);//si je te donne un id qui n'existe pas tu dois lever une exception
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}",id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())//on s'attend à avoir un status 404 qui signifie que le client n'existe pas
                .andExpect(MockMvcResultMatchers.content().string(""));//on s'attend à avoir une chaine vide
    }

    @Test
    void searchCustomers() throws Exception {
        String keyword="m";
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers?keyword="+keyword))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customers)));
    }

    @Test
    void shouldSaveCustomer() throws Exception {
        CustomerDTO customerDTO= customers.get(0);//on prend le premier client de la liste
        String expected = """
                {
                  "id":1, "firstName":"Mohamed", "lastName":"Youssfi", "email":"med@gmail.com"
                }
                """;
        Mockito.when(customerService.saveNewCustomer(Mockito.any())).thenReturn(customers.get(0));//on enregistre un nouveau client et on s'attend à avoir le client enregistré
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(expected));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId=1L;
        CustomerDTO customerDTO= customers.get(0);
        Mockito.when(customerService.updateCustomer(Mockito.eq(customerId),Mockito.any())).thenReturn(customers.get(0));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerDTO)));
    }
    @Test
    void shouldDeleteCustomer() throws Exception {
        Long customerId=1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/{id}",customerId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}