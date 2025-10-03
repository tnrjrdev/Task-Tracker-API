# 📝 Task Tracker (Spring Boot)
https://github.com/tnrjrdev/Task-Tracker-API

Um **Task Tracker** simples, baseado em **Spring Boot**, que permite criar, atualizar, listar e excluir tarefas via **API REST**.  
As tarefas são armazenadas em um arquivo JSON (`tasks.json`) no diretório de execução — **sem banco de dados**.  

## 🚀 Funcionalidades

- Criar novas tarefas (`description`)
- Atualizar descrição de uma tarefa
- Excluir tarefas
- Marcar tarefa como:
  - `todo`
  - `in-progress`
  - `done`
- Listar todas as tarefas ou filtrar por status
- Registro automático de:
  - `id` incremental
  - `createdAt` (data/hora de criação)
  - `updatedAt` (última atualização)

## 📦 Estrutura do Projeto

```
src/main/java/dev/task/tracker
│
├── TrackerApplication.java      # Classe principal
│
├── model
│   ├── Task.java                 # Entidade da tarefa
│   └── Status.java               # Enum (TODO, IN_PROGRESS, DONE)
│
├── repository
│   ├── TaskRepository.java       # Interface do repositório
│   └── FileTaskRepository.java   # Implementação persistindo em tasks.json
│
├── service
│   └── TaskService.java          # Regras de negócio
│
└── web
    ├── TaskController.java       # Endpoints REST
    ├── dto                       # DTOs (Create, Update, Status)
    └── handler                   # Exception handler global
```

## ⚙️ Pré-requisitos

- **Java 22** (ou versão compatível)
- **Maven 3.9+**
- (opcional) **Postman** ou **cURL** para testar

## ▶️ Rodando o Projeto

Clone o repositório e entre na pasta do projeto:

```bash
git clone https://github.com/seu-usuario/task-tracker.git
cd task-tracker
```

Compile e gere o JAR:

```bash
mvn clean package -DskipTests
```

Execute:

```bash
java -jar target/tracker-0.0.1-SNAPSHOT.jar
```

A aplicação sobe por padrão em:  
👉 [http://localhost:8081](http://localhost:8081)

> ⚠️ Se quiser mudar a porta, edite `src/main/resources/application.properties`:
> ```properties
> server.port=8080
> ```

## 🔗 Endpoints da API

### Criar tarefa
```http
POST /tasks
```
```json
{
  "description": "Comprar mantimentos"
}
```

### Listar tarefas
```http
GET /tasks
GET /tasks?status=todo
GET /tasks?status=in-progress
GET /tasks?status=done
```

### Atualizar descrição
```http
PUT /tasks/{id}
```
```json
{
  "description": "Comprar e cozinhar"
}
```

### Atualizar status
```http
PATCH /tasks/{id}/status
```
```json
{
  "status": "in-progress"
}
```

Ou use os atalhos:
```http
POST /tasks/{id}/mark-in-progress
POST /tasks/{id}/mark-done
```

### Excluir tarefa
```http
DELETE /tasks/{id}
```

## 📂 Estrutura do `tasks.json`

O arquivo é criado automaticamente na pasta de execução:

```json
{
  "nextId": 2,
  "tasks": [
    {
      "id": 1,
      "description": "Comprar mantimentos",
      "status": "in-progress",
      "createdAt": "2025-10-03T15:30:00Z",
      "updatedAt": "2025-10-03T15:45:00Z"
    }
  ]
}
```

## 🧪 Testando com Postman

1. Abra o Postman
2. Crie uma **Collection** chamada `Task Tracker`
3. Configure a variável `baseUrl = http://localhost:8081`
4. Adicione os requests:
   - `POST {{baseUrl}}/tasks`
   - `GET {{baseUrl}}/tasks`
   - `GET {{baseUrl}}/tasks?status=done`
   - `PUT {{baseUrl}}/tasks/{id}`
   - `PATCH {{baseUrl}}/tasks/{id}/status`
   - `DELETE {{baseUrl}}/tasks/{id}`

> Também é possível usar **cURL** direto no terminal.

## Melhorias Futuras

- Paginação (`?page=&size=`)
- Busca textual (`?q=`)
- Suporte a múltiplos arquivos de storage (`--file=...`)
- Testes unitários com JUnit + MockMvc
- Documentação da API com Swagger/OpenAPI

---

## 📜 Licença

Este projeto é open-source e distribuído sob a licença **MIT**.
