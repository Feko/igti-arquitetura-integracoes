package com.feko.demoacmeap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feko.demoacmeap.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	public Optional<Cliente> findByCpf(String cpf);
	
}