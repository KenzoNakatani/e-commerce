package com.kenzo.ecommerce.dto;

import java.math.BigDecimal;

import com.kenzo.ecommerce.model.Produto;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        String categoria) {

    public ProdutoResponse(Produto produto) {
        this(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.getCategoria().getNome());
    }
}