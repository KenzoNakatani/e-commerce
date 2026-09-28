package com.kenzo.ecommerce.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoRequest(
        @NotNull(message = "O cliente é obrigatório")
        Long clienteId,

        @NotEmpty(message = "O pedido precisa ter pelo menos um item")
        @Valid
        List<ItemPedidoRequest> itens) {
}