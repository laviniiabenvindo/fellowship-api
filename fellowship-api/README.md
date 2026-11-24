# Fellowship API

API REST (Spring Boot 3 + Spring Data JPA + PostgreSQL) para cadastrar personagens de O Senhor dos Anéis.

Entidade `Personagem`: `id`, `nome`, `raca`, `localOrigem`, `idade`, `portadorDoAnel`.

## Requisitos
- Java 21 e Maven
- Docker e Docker Compose

## 1. Rodar os testes
```bash
mvn test
```

## 2. Gerar o JAR
```bash
mvn clean package
```
Gera `target/aplicacao.jar`.

## 3. Rodar com Docker Compose (API + PostgreSQL)
```bash
cp .env.example .env        # edite a senha no .env
mvn clean package
docker compose up --build
```
Para parar: `docker compose down` (os dados continuam no volume).
Para apagar também os dados: `docker compose down -v`.

## 4. Rodar o JAR fora da IDE (banco local)
Com um PostgreSQL local rodando:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/fellowship
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
java -jar target/aplicacao.jar
```
No Windows (PowerShell): `$env:DB_USER="seu_usuario"` e assim por diante.

## 5. Endpoints
| Método | Rota | Descrição |
|---|---|---|
| POST | /personagens | Cadastra |
| GET | /personagens | Lista |
| GET | /personagens/{id} | Busca por id (404 se não existir) |
| PUT | /personagens/{id} | Atualiza |
| DELETE | /personagens/{id} | Exclui |
| GET | /actuator/health | Saúde da aplicação |

Exemplo:
```bash
curl -X POST http://localhost:8080/personagens \
  -H "Content-Type: application/json" \
  -d '{"nome":"Frodo","raca":"Hobbit","localOrigem":"Condado","idade":33,"portadorDoAnel":true}'

curl http://localhost:8080/actuator/health
```
