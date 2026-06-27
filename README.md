# sdui-backend

Backend do projeto **SDUI fintech** — construído com Kotlin + Ktor seguindo Clean Architecture (Uncle Bob).

## Pré-requisitos

- JDK 17+
- Gradle 8.x (via wrapper)
- Docker + Docker Compose (opcional)

## Como rodar

### Local

```bash
# Após clonar, inicializar o submodule de schemas
git submodule update --init

./gradlew run
# API disponível em http://localhost:8080
```

### Docker Compose

```bash
git submodule update --init
docker-compose up --build
```

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/health` | Health check |
| GET | `/screens/home` | Tela inicial (saldo + atalhos) |
| GET | `/screens/extract` | Extrato de transações |
| GET | `/screens/transfer` | Seleção de contato + valor |
| GET | `/screens/transfer_confirm?contactId=&amount=` | Tela de confirmação |
| POST | `/transfers` | Executa transferência, retorna `{ result, nextScreen }` |

Header opcional: `X-User-Id: user_1` (padrão: usuário fixo Maria).

## Arquitetura — Clean Architecture (Uncle Bob)

```
domain/          # Entities e exceções puras — sem deps externas
application/     # Use cases + ports (interfaces de repositório)
adapter/         # Screen Builders (montam JSON SDUI) + repositories Exposed + DTOs
infrastructure/  # Ktor routes, SQLite/Exposed, SchemaValidator, Application.kt
```

As dependências sempre apontam para dentro: `infrastructure → adapter → application → domain`.

## Testes

```bash
./gradlew test
```
