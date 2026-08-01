# 🏦 DevBank API 

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento_Contínuo-blue?style=for-the-badge)

Uma RESTful API robusta para simulação de operações bancárias. 

> **Aviso de Evolução:** Este projeto é ativo. Ele não é um sistema estático, mas sim um ambiente de aprendizado contínuo onde aplico conceitos reais de Backend, arquitetura e DevOps à medida que evoluo na minha jornada como desenvolvedor.

---

## 🚀 Funcionalidades Atuais

O *Core Business* do banco já está funcional, garantindo a integridade dos dados:
* **Gerenciamento de Contas:** Criação de usuários com saldo inicial e validações de negócio.
* **Operações Financeiras:** Depósitos e Saques com validação estrita de fundos. A API retorna comprovantes em tempo real com o saldo atualizado dinamicamente.
* **Transferências Seguras (Anti-Race Condition):** Implementação de **Lock Pessimista** (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) no banco de dados para evitar inconsistências caso duas transações ocorram no exato mesmo milissegundo.
* **Extrato Inteligente:** Consulta cronológica de movimentações, com tratamento de dados via Operador Ternário no DTO para operações sem remetente/destinatário.

---

## 🏗️ Arquitetura e Decisões Técnicas

* **Clean Architecture & Padrões:** Separação rígida de responsabilidades entre Controllers, Services e Repositories.
* **Data Transfer Objects (DTOs):** Uso de `records` do Java 17 para blindar as entidades do banco e garantir o tráfego limpo de dados.
* **Tratamento Global de Erros:** Interceptação de exceções (como `EntityNotFoundException` e `IllegalArgumentException`) para devolver Códigos HTTP padronizados (404, 400) sem vazar o Stack Trace da aplicação.
* **Migrations Seguras:** Controle de versão do banco de dados utilizando Flyway, permitindo escalabilidade na estrutura das tabelas.

---

## 🗺️ Roadmap: O que vem por aí?

O DevBank está sempre subindo de nível. Estas são as próximas etapas mapeadas para o projeto:

- [ ] **Bean Validation:** Blindar as entradas da API para impedir que usuários enviem dados em branco ou valores negativos diretamente no payload.
- [ ] **Testes Automatizados (Mockito & JUnit):** Expandir a cobertura de testes unitários para os serviços de Depósito e Saque.
- [ ] **Segurança (Spring Security + JWT):** Implementar autenticação baseada em tokens para proteger as rotas financeiras.
- [ ] **Front-end Desacoplado:** Conectar a API REST a uma interface de usuário real desenvolvida em Angular.

---

## ⚙️ Como Executar o Projeto Localmente

**Pré-requisitos:** JDK 17 instalado e Maven (ou uso do Wrapper `mvnw`).

**1. Clone o repositório e acesse a pasta:**
```bash
git clone [https://github.com/seu-usuario/devbank.git](https://github.com/seu-usuario/devbank.git)
cd devbank
````

**2. Inicie o servidor:**

No Windows: .\mvnw spring-boot:run

No Linux/Mac: ./mvnw spring-boot:run

**3. Acesse o Swagger UI:**
Abra o seu navegador e teste todas as rotas interativamente sem precisar do Postman:
👉 http://localhost:8080/swagger-ui.html

## 🛡️ DevOps e CI/CD
Este repositório possui Integração Contínua (CI) configurada via GitHub Actions. Qualquer novo código empurrado para a branch principal dispara automaticamente um servidor de validação que executa o build e roda os testes unitários, garantindo que o sistema nunca quebre em produção.
