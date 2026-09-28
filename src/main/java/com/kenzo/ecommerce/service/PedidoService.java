package com.kenzo.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kenzo.ecommerce.dto.ItemPedidoRequest;
import com.kenzo.ecommerce.dto.PedidoRequest;
import com.kenzo.ecommerce.dto.PedidoResponse;
import com.kenzo.ecommerce.model.Cliente;
import com.kenzo.ecommerce.model.ItemPedido;
import com.kenzo.ecommerce.model.Pedido;
import com.kenzo.ecommerce.model.Produto;
import com.kenzo.ecommerce.model.StatusPedido;
import com.kenzo.ecommerce.repository.ClienteRepository;
import com.kenzo.ecommerce.repository.PedidoRepository;
import com.kenzo.ecommerce.repository.ProdutoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return new PedidoResponse(encontrarPedido(id));
    }

    // @Transactional = "tudo ou nada": se qualquer parte falhar
    // (por exemplo, falta de estoque), NADA é salvo no banco.
    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : request.itens()) {
            Produto produto = produtoRepository.findById(itemRequest.produtoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Produto não encontrado: " + itemRequest.produtoId()));

            if (produto.getEstoque() < itemRequest.quantidade()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Estoque insuficiente para o produto: " + produto.getNome());
            }

            // Tira do estoque
            produto.setEstoque(produto.getEstoque() - itemRequest.quantidade());

            // Monta o item, guardando o preço de agora
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(produto.getPreco());
            pedido.adicionarItem(item);

            total = total.add(item.getSubtotal());
        }

        pedido.setTotal(total);
        return new PedidoResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse pagar(Long id) {
        Pedido pedido = encontrarPedido(id);

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Só é possível pagar um pedido com status CRIADO");
        }

        pedido.setStatus(StatusPedido.PAGO);
        return new PedidoResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = encontrarPedido(id);

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "O pedido já está cancelado");
        }

        // Devolve os produtos para o estoque
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        return new PedidoResponse(pedidoRepository.save(pedido));
    }

    private Pedido encontrarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }
}