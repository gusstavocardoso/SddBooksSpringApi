# Books API

Este projeto é uma API RESTful para cadastro e gerenciamento de livros. Desenvolvido com **Java 21**, **Spring Boot 3**, e **PostgreSQL**.

## Pré-requisitos
- Java 17
- Maven
- Docker e Docker Compose

## Tecnologias Utilizadas
- Spring Boot (Web, Data JPA, Validation)
- PostgreSQL
- Testcontainers & REST Assured (Testes de Integração)
- Gatling (Testes de Performance)
- Springdoc OpenAPI (Swagger)
- GitHub Actions (CI)

## Como executar localmente

1. Suba o banco de dados via Docker:
```bash
docker-compose up -d
```

2. Execute a aplicação Spring Boot:
```bash
mvn spring-boot:run
```

3. Acesse a documentação da API (Swagger UI):
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Como executar os testes

### Testes Unitários e de Integração
Os testes de integração utilizam o Testcontainers para provisionar um PostgreSQL em Docker automaticamente. Certifique-se de que o Docker esteja rodando.
```bash
mvn clean test
```

### Testes de Performance (Gatling)
Para executar os testes de carga simulando múltiplos acessos:
1. Certifique-se de que a aplicação está rodando localmente (passo 2 acima).
2. Execute o Gatling através do Maven:
```bash
mvn gatling:test
```
Os relatórios detalhados do Gatling serão gerados na pasta `target/gatling/`.

## CI (Continuous Integration)
O projeto conta com um workflow do GitHub Actions (`.github/workflows/ci.yml`) que é executado a cada push na branch `main`. A esteira configura o Java 21, faz o build pelo Maven e executa todos os testes unitários e de integração automaticamente.
