package com.kenzo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kenzo.ecommerce.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}