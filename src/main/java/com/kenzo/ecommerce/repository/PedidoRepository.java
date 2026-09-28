package com.kenzo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kenzo.ecommerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}