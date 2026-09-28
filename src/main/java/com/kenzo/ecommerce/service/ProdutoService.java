package com.kenzo.ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kenzo.ecommerce.dto.ProdutoRequest;
import com.kenzo.ecommerce.dto.ProdutoResponse;
import com.kenzo.ecommerce.model.Categoria;
import com.kenzo.ecommerce.model.Produto;
import com.kenzo.ecommerce.repository.CategoriaRepository;
import com.kenzo.ecommerce.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponse::new)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {
        return new ProdutoResponse(encontrarProduto(id));
    }

    public ProdutoResponse criar(ProdutoRequest request) {
        Produto produto = new Produto();
        preencher(produto, request);
        return new ProdutoResponse(produtoRepository.save(produto));
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = encontrarProduto(id);
        preencher(produto, request);
        return new ProdutoResponse(produtoRepository.save(produto));
    }

    public void deletar(Long id) {
        encontrarProduto(id);
        produtoRepository.deleteById(id);
    }

    private Produto encontrarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private void preencher(Produto produto, ProdutoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setEstoque(request.estoque());
        produto.setCategoria(categoria);
    }
}