package com.client.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Builder
@Data
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-generator")
    private UUID id;
    private String name;
    private String email;
    private String cpf;

}
