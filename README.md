Markdown
# 🏦 DevBank API

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-003545?style=for-the-badge&logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)

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

---

## 🧠 Explicação Técnica e Arquitetura do Projeto

Para garantir os requisitos de um sistema corporativo e financeiro do mundo real, a API foi desenhada utilizando os seguintes conceitos avançados do ecossistema Java/Spring:

### 1. Tratamento Global de Erros (`TratadorDeErros.java`)
Em vez de espalhar blocos repetitivos de `try-catch` por todos os Controladores da aplicação ou expor *stacktraces* internos para o cliente, foi implementada uma estrutura centralizada utilizando interceptadores do Spring:
* **`@RestControllerAdvice`**: Intercepta de forma global as exceções lançadas em qualquer ponto da camada de controle.
* **`@ExceptionHandler`**: Captura falhas específicas (como erros de validação ou de negócio) e as mapeia para um objeto padronizado (`ErroDTO`), garantindo que o cliente receba sempre uma resposta JSON limpa, amigável e com o status HTTP adequado (ex: `400 Bad Request`).

### 2. Controle Transacional Seguro (`@Transactional`)
Operações financeiras exigem conformidade estrita com as propriedades ACID (Atomicidade, Consistência, Isolamento e Durabilidade). No `TransferenciaService.java`:
* A anotação **`@Transactional`** garante que todo o fluxo de transferência (verificação de saldo, débito da conta de origem e crédito na conta de destino) seja executado como uma **única unidade atômica**.
* Caso ocorra qualquer falha ou erro inesperado no meio do processamento (como perda de conexão ou variações de regra), o Spring executa automaticamente o **`rollback`**, desfazendo todas as alterações e impedindo a inconsistência de dados.

### 3. Evolução de Banco de Dados com Migrations (`Flyway`)
A estrutura do banco de dados não depende de geração automática mágica de ORM em produção. Foi adotado o **Flyway** para versionamento estruturado do esquema do banco de dados através de arquivos SQL nativos (`V1__criar_tabela_usuarios.sql`). Isso assegura rastreabilidade de alterações e padronização entre ambientes de desenvolvimento, testes e produção.

### 4. Imutabilidade e Performance com Java Records (`DTOs`)
Para a transferência de dados entre as camadas da API (Payloads de Entrada e Saída), foram utilizados os modernos **Java Records** (como o `TransferenciaDTO`), eliminando códigos desnecessários (*boilerplate*) de Getters, Setters, `equals()` e `hashCode()`, além de assegurar que os dados trafeguem de forma estritamente imutável e segura.

---

## 🛠️ Como Executar o Projeto Localmente

### Passo 1: Clone este repositório

### Passo 2: Entre na pasta do projeto
Bash
cd devbank

### Passo 3: Execute o projeto usando o Maven
Bash
./mvnw spring-boot:run
(No Windows, utilize mvnw.cmd spring-boot:run)

A aplicação estará a rodar localmente na porta 8080.

# ⚙️ Como Usar a API (Endpoints e Exemplos)
Pode utilizar ferramentas como o Postman ou o Insomnia para testar as funcionalidades abaixo.

### 1. Cadastrar Usuário (POST /usuarios)
Cria um novo utilizador no banco de dados com um saldo inicial.

URL: http://localhost:8080/usuarios

Body (JSON):

JSON
{
  "nome": "Fernando Silva",
  "login": "fernando.dev",
  "senha": "senhaSegura123",
  "saldo": 1000.00
}

### 2. Consultar Usuário (GET /usuarios/{id})
Retorna os dados do utilizador especificado na URL, incluindo o seu saldo atualizado.

URL: http://localhost:8080/usuarios/1

### 3. Realizar Transferência (POST /transferencias)
Realiza a transferência de valores entre dois utilizadores, utilizando os IDs gerados no cadastro.

Regra de Negócio: A API valida automaticamente se o remetente possui saldo suficiente antes de efetuar a dedução e o acréscimo.

Segurança: A operação é blindada pela anotação @Transactional, garantindo o rollback completo caso ocorra qualquer falha no processamento.

URL: http://localhost:8080/transferencias

Body (JSON):

JSON
{
  "idRemetente": 1,
  "idDestinatario": 2,
  "valor": 150.50
}
# 🗄️ Acessando o Banco de Dados (H2 Console)
Como o projeto utiliza o banco em memória H2, pode visualizar as tabelas (criadas automaticamente pelo Flyway) e os dados inseridos diretamente pelo navegador.

Acesse no seu navegador: http://localhost:8080/h2-console

Preencha os campos com as credenciais configuradas:

JDBC URL: jdbc:h2:mem:meubanco

User Name: sa

Password: (deixe em branco)

Clique em Connect para visualizar a tabela usuarios e testar queries SQL.