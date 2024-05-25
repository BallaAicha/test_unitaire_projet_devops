package org.etutoria.microservice.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder @ToString
public class CustomerDTO {
    private Long id;
    @NotEmpty //pour dire que le champ ne doit pas être vide
    @Size(min = 2)//pour dire que le champ doit avoir au moins 2 caractères
    private String firstName;
    @NotEmpty
    @Size(min = 2)
    private String lastName;
    //tres important pour tester la validation des entités
    @NotEmpty
    @Size(min = 8)
    private String email;
}
