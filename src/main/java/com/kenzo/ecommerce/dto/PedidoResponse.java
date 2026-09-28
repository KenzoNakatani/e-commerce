package com.kenzo.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.kenzo.ecommerce.model.Pedido;
import com.kenzo.ecommerce.model.StatusPedido;

public record PedidoResponse(
        Long id,
        String cliente,
        LocalDateTime dataCriacao,
        StatusPedido status,
        List<ItemPedidoResponse> itens,
        BigDecimal total) {

    public PedidoResponse(Pedido pedido) {
        this(
            pedido.getId(),
            pedido.getCliente().getNome(),
            pedido.getDataCriacao(),
            pedido.getStatus(),
            pedido.getItens().stream().map(ItemPedidoResponse::new).toList(),
            pedido.getTotal());
    }
}