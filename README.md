# 🏦 DevBank API

Uma API RESTful desenvolvida em Java e Spring Boot para simular operações bancárias essenciais, com foco em segurança de transações e boas práticas de arquitetura.

## 🎯 Objetivo do Projeto
O DevBank foi construído para solidificar conhecimentos no ecossistema Spring, implementando um fluxo completo de ponta a ponta: desde a modelagem do banco de dados com migrações até o tratamento global de exceções, simulando cenários reais de regras de negócio financeiras.

## 🚀 Tecnologias e Práticas Utilizadas
* **Java 17**
* **Spring Boot** (Web, Data JPA)
* **H2 Database** (Banco de dados em memória para testes ágeis)
* **Flyway** (Versionamento e controle de migrações do banco de dados)
* **Lombok** (Redução de boilerplate de código)
* **Padrão DTO** (Data Transfer Object)
* **Controle de Transações** (`@Transactional` para garantir a integridade do saldo)
* **Tratamento Global de Erros** (`@RestControllerAdvice` para respostas de erro amigáveis)

## ⚙️ Funcionalidades
A API expõe os seguintes endpoints:

### Usuários
* `POST /usuarios` - Cadastra um novo usuário com um saldo inicial.
* `GET /usuarios/{id}` - Consulta os dados e o saldo atual de um usuário específico.

### Transferências
* `POST /transferencias` - Realiza a transferência de valores entre dois usuários.
    * *Regra de Negócio:* Valida se o remetente possui saldo suficiente antes de efetuar a dedução e o acréscimo.
    * *Segurança:* Utiliza `@Transactional` para garantir o *rollback* caso ocorra qualquer falha durante a operação.

## 🛠️ Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone [https://github.com/SEU-USUARIO/devbank.git](https://github.com/SEU-USUARIO/devbank.git)