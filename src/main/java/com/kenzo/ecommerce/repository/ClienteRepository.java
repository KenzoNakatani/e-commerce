package com.kenzo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kenzo.ecommerce.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);
}