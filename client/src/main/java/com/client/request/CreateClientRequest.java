package com.client.request;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {

    private String name;
    private String email;
    private String cpf;

}
