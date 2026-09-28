package com.kenzo.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kenzo.ecommerce.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}