package com.kenzo.ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kenzo.ecommerce.dto.CategoriaRequest;
import com.kenzo.ecommerce.dto.CategoriaResponse;
import com.kenzo.ecommerce.model.Categoria;
import com.kenzo.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponse::new)
                .toList();
    }

    public CategoriaResponse criar(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        return new CategoriaResponse(categoriaRepository.save(categoria));
    }

    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}