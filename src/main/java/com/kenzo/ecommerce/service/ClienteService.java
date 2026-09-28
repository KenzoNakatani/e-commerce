package com.kenzo.ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kenzo.ecommerce.dto.ClienteRequest;
import com.kenzo.ecommerce.dto.ClienteResponse;
import com.kenzo.ecommerce.model.Cliente;
import com.kenzo.ecommerce.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponse::new)
                .toList();
    }

    public ClienteResponse buscarPorId(Long id) {
        return new ClienteResponse(encontrarCliente(id));
    }

    public ClienteResponse criar(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Já existe um cliente com esse email");
        }
        Cliente cliente = new Cliente();
        preencher(cliente, request);
        return new ClienteResponse(clienteRepository.save(cliente));
    }

    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = encontrarCliente(id);
        preencher(cliente, request);
        return new ClienteResponse(clienteRepository.save(cliente));
    }

    public void deletar(Long id) {
        encontrarCliente(id);
        clienteRepository.deleteById(id);
    }

    private Cliente encontrarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private void preencher(Cliente cliente, ClienteRequest request) {
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
    }
}