package com.client.repository;

import com.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository <Client, UUID> {

	public Client findByName(String name);
}
