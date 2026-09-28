package com.kenzo.ecommerce.dto;

import com.kenzo.ecommerce.model.Cliente;

public record ClienteResponse(Long id, String nome, String email, String telefone) {

    public ClienteResponse(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}