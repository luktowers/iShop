package com.client.request;

import com.client.config.StringOnlyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {

    @NotNull
    @JsonDeserialize(using = StringOnlyDeserializer.class)
    private String name;
    @NotNull
    @JsonDeserialize(using = StringOnlyDeserializer.class)
    private String email;
    @JsonDeserialize(using = StringOnlyDeserializer.class)
    @NotNull
    private String cpf;

}
