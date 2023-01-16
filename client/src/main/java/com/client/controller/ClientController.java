package com.client.controller;

import com.client.model.Client;
import com.client.request.ClientDto;
import com.client.request.ClientUpdateRequest;
import com.client.request.CreateClientRequest;
import com.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping(value = "client")
public class ClientController {

	@Autowired
	private ClientService service;

	@PostMapping
	public ResponseEntity<ClientDto> create(@RequestBody CreateClientRequest request)
			throws HttpClientErrorException.BadRequest {

		Client client = Client.builder()
				.name(request.getName())
				.email(request.getEmail())
				.cpf(request.getCpf()).build();

		service.create(client);

		ClientDto clientDto = new ClientDto(
				client.getId(),
				client.getName(),
				client.getEmail(),
				client.getCpf());

		return ResponseEntity.status(HttpStatus.CREATED).body(clientDto);
	}

	@GetMapping(path = { "/{id}" })
	public ResponseEntity<ClientDto>getById(@PathVariable UUID id){
		if(service.existsById(id)){
			Client client = service.findById(id).get();
			ClientDto clientDto = new ClientDto(
					client.getId(),
					client.getName(),
					client.getEmail(),
					client.getCpf());
			return ResponseEntity.ok().body(clientDto);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<ClientDto>>getAll(){
		var response = service.findAll();

		List<ClientDto> list = response.stream().map(client ->
						new ClientDto(client.getId(), client.getName(), client.getEmail(), client.getCpf()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(list);
	}

	@PutMapping(path = { "/{id}" })
	public ResponseEntity<ClientDto>update(@PathVariable UUID id, @RequestBody ClientUpdateRequest request){
		if(service.existsById(id)){

			service.update(id, request);

			ClientDto clientDto = new ClientDto(
					id,
					request.getName(),
					request.getEmail(),
					request.getCpf());

			return ResponseEntity.ok().body(clientDto);
		}else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<?> delete(@PathVariable UUID id){
		if(service.existsById(id)){
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
