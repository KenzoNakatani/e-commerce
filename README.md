# 🛒 E-commerce API

API REST de loja virtual, feita com Java e Spring Boot. O projeto simula o fluxo completo de uma loja: cadastro de produtos, clientes e pedidos, com controle de estoque e regras de negócio.

## 📖 Sobre o projeto

Esse foi o terceiro projeto que fiz para aprender Spring Boot na prática, depois de uma API de produtos e uma API de investimentos. A ideia aqui foi dar um passo além: sair de cadastros simples (CRUD) e construir algo com **regras de negócio de verdade**, como controle de estoque e cálculo de total de pedido.

Construí o projeto em camadas, uma de cada vez:

1. Comecei só com **Categoria** e **Produto**, para validar a estrutura base (model, repository, dto, service, controller)
2. Depois adicionei **Cliente**
3. Por último, **Pedido** e **ItemPedido**, a parte mais desafiadora, onde um pedido tem vários itens e precisa calcular o total e descontar do estoque

Cada camada foi testada no Postman antes de eu seguir para a próxima, para garantir que a anterior estava sólida.

## 🧠 O que aprendi construindo isso

- **Separar Model, DTO, Service e Controller.** No começo parecia repetição de código, mas entendi que isso evita expor direto as classes do banco de dados na API, e deixa fácil mudar uma camada sem afetar as outras.
- **Transações (`@Transactional`).** Ao criar um pedido, se um dos produtos não tiver estoque, **nada** pode ser salvo, nem os itens anteriores que já tinham passado. Foi meu primeiro contato real com a ideia de "tudo ou nada".
- **Guardar o preço no momento da compra.** Se eu salvasse só o produto no pedido, e o preço dele mudasse depois, os pedidos antigos ficariam com o valor errado. Por isso o `ItemPedido` guarda o próprio `precoUnitario`.
- **Enum com `@Enumerated(EnumType.STRING)`.** Descobri que, sem isso, o Spring salva o status como número no banco (0, 1, 2), o que fica difícil de ler direto na tabela. Salvando como texto (`CRIADO`, `PAGO`, `CANCELADO`), fica muito mais claro.
- **Erros de configuração são tão comuns quanto erros de código.** [Se você quiser, conte aqui: ex. "Tive um problema com o Lombok que não estava sendo reconhecido pela IDE" ou "A aplicação não subia porque a porta 8080 já estava em uso por outro projeto meu"]. Aprendi a ler o log de baixo para cima e a procurar a mensagem `Caused by:`, que costuma mostrar o motivo real do erro.

## 🚧 Desafios que enfrentei

- **[Conte aqui um desafio real que você teve]:** por exemplo, "O Lombok não estava funcionando na IDE, mesmo com a dependência no pom.xml. Resolvi rodando o comando de limpar o workspace do Java e reiniciando o VS Code."
- **[Outro desafio]:** por exemplo, "A aplicação não subia porque outra instância minha já estava rodando na porta 8080. Aprendi a usar `netstat` para descobrir qual processo estava ocupando a porta."
- **Entender o `@OneToMany` e `@ManyToOne`.** Demorei para entender qual lado da relação é o "dono". No fim, entendi que quem tem a chave estrangeira (`@JoinColumn`) é sempre o lado `@ManyToOne`.

## 🛠️ Tecnologias

- Java [coloque a versão, ex. 17]
- Spring Boot [coloque a versão, ex. 3.3]
- Spring Data JPA
- Bean Validation
- H2 Database (banco em memória, ótimo para desenvolvimento)
- Lombok
- Maven

## 📂 Estrutura do projeto


## ▶️ Como rodar

1. Clone o repositório: https://github.com/KenzoNakatani/ecommerce.git

2. Entre na pasta do projeto: cd commerce


3. Rode:.\mvnw spring-boot:run

(no Mac/Linux, use `./mvnw spring-boot:run`)

4. A API sobe em `http://localhost:8081`

5. Console do banco: `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:ecommerce`, usuário `sa`, senha em branco)

## 📮 Rotas da API

### Categorias
| Método | Rota | Descrição |
|---|---|---|
| GET | `/categorias` | Lista todas as categorias |
| POST | `/categorias` | Cria uma categoria |
| DELETE | `/categorias/{id}` | Remove uma categoria |

### Produtos
| Método | Rota | Descrição |
|---|---|---|
| GET | `/produtos` | Lista todos os produtos |
| GET | `/produtos/{id}` | Busca um produto |
| POST | `/produtos` | Cria um produto |
| PUT | `/produtos/{id}` | Atualiza um produto |
| DELETE | `/produtos/{id}` | Remove um produto |

### Clientes
| Método | Rota | Descrição |
|---|---|---|
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/{id}` | Busca um cliente |
| POST | `/clientes` | Cria um cliente |
| PUT | `/clientes/{id}` | Atualiza um cliente |
| DELETE | `/clientes/{id}` | Remove um cliente |

### Pedidos
| Método | Rota | Descrição |
|---|---|---|
| GET | `/pedidos` | Lista todos os pedidos |
| GET | `/pedidos/{id}` | Busca um pedido |
| POST | `/pedidos` | Cria um pedido (calcula o total e desconta o estoque) |
| PATCH | `/pedidos/{id}/pagar` | Marca o pedido como pago |
| PATCH | `/pedidos/{id}/cancelar` | Cancela o pedido e devolve o estoque |

## 📌 Exemplo de uso

**Criar um pedido:**
```json
POST /pedidos
{
  "clienteId": 1,
  "itens": [
    { "produtoId": 1, "quantidade": 2 }
  ]
}
```

**Resposta:**
```json
{
  "id": 1,
  "cliente": "Maria Silva",
  "status": "CRIADO",
  "total": 399.80,
  "itens": [
    {
      "produto": "Fone de ouvido",
      "quantidade": 2,
      "precoUnitario": 199.90,
      "subtotal": 399.80
    }
  ]
}
```

## 📏 Regras de negócio

- Não é possível criar um pedido sem estoque suficiente
- O preço do produto é guardado no item no momento da compra, então mudanças de preço futuras não afetam pedidos antigos
- Cancelar um pedido devolve automaticamente os produtos ao estoque
- Não é possível pagar um pedido que já foi cancelado, nem cancelar duas vezes
- O email de cada cliente precisa ser único

## 🔭 Próximos passos

- [ ] Trocar o H2 pelo PostgreSQL, para os dados não serem apagados ao reiniciar
- [ ] Adicionar autenticação com Spring Security e JWT
- [ ] Escrever testes automatizados (unitários e de integração)
- [ ] Criar um tratamento de erros padronizado, com respostas JSON mais claras
- [ ] Adicionar paginação nas listagens

## 👤 Autor

Kenzo Nakatani 