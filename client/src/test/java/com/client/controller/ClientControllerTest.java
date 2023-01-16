package com.client.controller;


import com.client.model.Client;
import com.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureJsonTesters
@WebMvcTest(value = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest {

	@MockBean
	private ClientService service;
	@Autowired
	private MockMvc mvc;
	@Autowired
	private JacksonTester<List<?>> jsonApiResponse;
	private Client client;

	private final UUID ID = UUID.randomUUID();
	private final String NAME = "Luke";
	private final String EMAIL = "luke@gmail.com";
	private final String CPF = "09876543210";

	@BeforeEach
	public void init(){
		client = new Client(ID, NAME, EMAIL, CPF);
	}
	@Test
	public void ShouldCreateClient() throws Exception {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", NAME);
		requestBody.put("email", EMAIL);
		requestBody.put("cpf", CPF);

		ObjectMapper mapper = new ObjectMapper();

		 mvc.perform(post("/client")
						 .content(mapper.writeValueAsString(requestBody))
						 .contentType(MediaType.APPLICATION_JSON)
						 .accept(MediaType.APPLICATION_JSON)
						 .characterEncoding("UTF-8"))
				 .andExpect(status().isCreated())
				 .andExpect(jsonPath("name").value(client.getName()))
				 .andExpect(jsonPath("email").value(client.getEmail()))
				 .andExpect(jsonPath("cpf").value(client.getCpf()))
				 .andReturn();

		 verify(service, times(1)).create(any());
	}

	@Test
	public void  shouldReturnErrorGivenInvalidInput() throws Exception {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", 10);
		requestBody.put("email", EMAIL);
		requestBody.put("cpf", CPF);

		ObjectMapper mapper = new ObjectMapper();

		mvc.perform(post("/client")
						.content(mapper.writeValueAsString(requestBody))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8"))
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	@Test
	public void shouldGetClientGivenValidId() throws Exception {
		when(service.findById(any())).thenReturn(Optional.ofNullable(client));
		when(service.existsById(any())).thenReturn(true);

		mvc.perform(get("/client/" + ID)).andExpect(status().isOk())
				.andExpect(jsonPath("name").value(client.getName()))
				.andExpect(jsonPath("email").value(client.getEmail()))
				.andExpect(jsonPath("cpf").value(client.getCpf()))
				.andReturn();

		verify(service, times(1)).findById(any());
		verify(service, times(1)).existsById(any());

	}

	@Test
	public void shouldReturnErrorGivenInvalidId() throws Exception {
		when(service.existsById(any())).thenReturn(false);

		mvc.perform(get("/client/" + ID)).andExpect(status().isNotFound());

		verify(service, times(1)).existsById(any());
	}

	@Test
	public void shouldGetAllGivenThereIsClient() throws Exception {
		UUID uuid = UUID.randomUUID();
		List<Client> list = new ArrayList<>();
		list.add(new Client(uuid, NAME, EMAIL, CPF));
		list.add(client);
		when(service.findAll()).thenReturn(list);

		mvc.perform(get("/client/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(uuid.toString()))
				.andExpect(jsonPath("$[0].name").value(NAME))
				.andExpect(jsonPath("$[0].email").value(EMAIL))
				.andExpect(jsonPath("$[0].cpf").value(CPF))
				.andExpect(jsonPath("$[1].id").value(ID.toString()))
				.andExpect(jsonPath("$[1].name").value(NAME))
				.andExpect(jsonPath("$[1].email").value(EMAIL))
				.andExpect(jsonPath("$[1].cpf").value(CPF))
				.andReturn();

		verify(service, times(1)).findAll();
	}

	@Test
	public void shouldReturnEmptyListGivenThereIsNoClient() throws Exception {
		List<Client> list = new ArrayList<>();
		when(service.findAll()).thenReturn(list);

		var actual = mvc.perform(get("/client/")
				.accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

		verify(service, times(1)).findAll();
		assertEquals(jsonApiResponse.write(list).getJson(), actual.getContentAsString());

	}

	@Test
	public void shouldUpdateClient() throws Exception {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "Lucas");
		requestBody.put("email", EMAIL);
		requestBody.put("cpf", CPF);

		when(service.findById(any())).thenReturn(Optional.ofNullable(client));
		when(service.existsById(any())).thenReturn(true);
		when(service.update(any(), any())).thenReturn(new Client(ID, "Lucas", EMAIL, CPF));

		ObjectMapper mapper = new ObjectMapper();

		mvc.perform(put("/client/" + ID)
						.content(mapper.writeValueAsString(requestBody))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").value("Lucas"))
				.andExpect(jsonPath("email").value(client.getEmail()))
				.andExpect(jsonPath("cpf").value(client.getCpf()))
				.andReturn();
	}

	@Test
	public void updateShouldReturnErrorGivenInvalidId() throws Exception {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "Lucas");
		requestBody.put("email", EMAIL);
		requestBody.put("cpf", CPF);

		when(service.existsById(any())).thenReturn(false);
		ObjectMapper mapper = new ObjectMapper();

		mvc.perform(put("/client/" + ID)
						.content(mapper.writeValueAsString(requestBody))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.characterEncoding("UTF-8"))
				.andExpect(status().isNotFound());

		verify(service, times(1)).existsById(any());
	}

	@Test
	public void delete() throws Exception {
		doNothing().when(service).delete(any());
		when(service.existsById(any())).thenReturn(true);

		mvc.perform(MockMvcRequestBuilders
				.delete("/client/" + ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

		verify(service, times(1)).existsById(any());
		verify(service, times(1)).delete(any());
	}

	@Test
	public void deleteShouldReturnErrorGivenInvalidId() throws Exception {
		when(service.existsById(any())).thenReturn(false);

		mvc.perform(MockMvcRequestBuilders
						.delete("/client/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(service, times(1)).existsById(any());
	}
}
