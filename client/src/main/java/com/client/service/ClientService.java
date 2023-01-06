package com.client.service;

import com.client.model.Client;
import com.client.repository.ClientRepository;
import com.client.request.ClientUpdateRequest;
import com.client.request.CreateClientRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {


    private final ClientRepository repository;


    public boolean existsById(UUID id){
        return repository.existsById(id);
    }

    public Client create(CreateClientRequest request){
        return repository.save(new Client(UUID.randomUUID(), request.getName(), request.getEmail(), request.getCpf()));
    }

    public Optional<Client> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Client> findAll() {
        return repository.findAll();

    }

    public Client update(UUID id, ClientUpdateRequest request) {
        Client OldClient = repository.findById(id).get();
        BeanUtils.copyProperties(request, OldClient, "id");

        return repository.save(OldClient);
    }

    public void delete(UUID id) {
        repository.deleteById(id);

    }
}
