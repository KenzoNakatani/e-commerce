package com.kenzo.ecommerce.dto;

import com.kenzo.ecommerce.model.Categoria;

public record CategoriaResponse(Long id, String nome) {

    public CategoriaResponse(Categoria categoria) {
        this(categoria.getId(), categoria.getNome());
    }
}