package com.client.services;

import com.client.model.Client;
import com.client.repository.ClientRepository;
import com.client.request.ClientUpdateRequest;
import com.client.request.CreateClientRequest;
import com.client.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService service;

    @Captor
    ArgumentCaptor<Client> clientCaptorEntity;

    private Client client;

    private final UUID ID = UUID.randomUUID();
    private final String NAME = "Luke";
    private final String EMAIL = "luke@gmail.com";
    private final String CPF = "09876543210";

    @BeforeEach
    void init(){
        service = new ClientService(repository);
        client = new Client(ID, NAME, EMAIL, CPF);

    }

    @Test
    public void shouldReturnIfAClientExists(){
        when(repository.existsById(any())).thenReturn(true);

        boolean exists = service.existsById(ID);

        assertTrue(exists);
        verify(repository, times(1)).existsById(any());
    }

    @Test
    public void shouldCreateClient(){
        when(repository.save(any())).thenReturn(client);
        Client expected = new Client(ID, NAME, EMAIL, CPF);

        CreateClientRequest createRequest = new CreateClientRequest(NAME, EMAIL, CPF);

        var actual = service.create(client);

        verify(repository, times(1)).save(any());
        assertEquals(expected, actual);

    }

    @Test
    public void ShouldFindById(){
        Optional<Client> expected = Optional.of(new Client(ID, NAME, EMAIL, CPF));
        when(repository.findById(ID)).thenReturn(Optional.ofNullable(client));

        var actual = service.findById(ID);

        verify(repository, times(1)).findById(ID);
        assertEquals(expected, actual);

    }

    @Test
    public void ShouldFindAll(){
        List<Client> expected = new ArrayList<>();
        expected.add(new Client(ID, NAME, EMAIL, CPF));

        when(repository.findAll()).thenReturn(List.of(client));

        var actual = service.findAll();

        verify(repository, times(1)).findAll();
        assertEquals(expected, actual);
    }

    @Test
    public void ShouldUpdateClient(){
        Client expected = new Client(ID, "Lu", EMAIL, CPF);
        ClientUpdateRequest request = new  ClientUpdateRequest("Lu", EMAIL, CPF);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(client));
        when(repository.save(any())).thenReturn(expected);

        Client actual = service.update(ID, request);

        verify(repository).save(clientCaptorEntity.capture());

        clientCaptorEntity.getValue();
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDelete(){
        service.delete(ID);

        verify(repository, times(1)).deleteById(any());
    }
}
