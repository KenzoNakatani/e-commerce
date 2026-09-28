package com.kenzo.ecommerce.dto;

import java.math.BigDecimal;

import com.kenzo.ecommerce.model.ItemPedido;

public record ItemPedidoResponse(
        Long produtoId,
        String produto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal) {

    public ItemPedidoResponse(ItemPedido item) {
        this(
            item.getProduto().getId(),
            item.getProduto().getNome(),
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getSubtotal());
    }
}