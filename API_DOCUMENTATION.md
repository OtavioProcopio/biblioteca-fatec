# 🌐 REST API Documentation

This document describes the RESTful API endpoints available in the Biblioteca FATEC system.

## Base URL

```
http://localhost:8080
```

## API Documentation UI

Access the interactive Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON specification:
```
http://localhost:8080/api-docs
```

## Authentication

Currently, the API does not require authentication. All endpoints are publicly accessible.

---

## 📚 Livros (Books) Endpoints

### List All Books
- **URL**: `/api/livros`
- **Method**: `GET`
- **Description**: Returns a list of all books in the library
- **Response**: Array of book objects

**Example Response:**
```json
[
  {
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "quantidade": 1,
    "disponivel": true
  }
]
```

### Get Book by Title
- **URL**: `/api/livros/{titulo}`
- **Method**: `GET`
- **Description**: Returns a specific book by its title
- **URL Parameters**: `titulo` (string) - The book title
- **Response**: Book object or 404 if not found

### Create New Book
- **URL**: `/api/livros`
- **Method**: `POST`
- **Description**: Adds a new book to the library
- **Request Body**:
```json
{
  "titulo": "Book Title",
  "autor": "Author Name"
}
```
- **Response**: Created book object with status 201

### Delete Book
- **URL**: `/api/livros/{titulo}`
- **Method**: `DELETE`
- **Description**: Removes a book from the library
- **URL Parameters**: `titulo` (string) - The book title
- **Response**: 204 No Content on success

---

## 👥 Usuários (Users) Endpoints

### List All Users
- **URL**: `/api/usuarios`
- **Method**: `GET`
- **Description**: Returns a list of all users
- **Response**: Array of user objects

**Example Response:**
```json
[
  {
    "nome": "Ana Souza",
    "tipo": "Aluno"
  }
]
```

### Get User by Name
- **URL**: `/api/usuarios/{nome}`
- **Method**: `GET`
- **Description**: Returns a specific user by name
- **URL Parameters**: `nome` (string) - The user name
- **Response**: User object or 404 if not found

### Create New User
- **URL**: `/api/usuarios`
- **Method**: `POST`
- **Description**: Adds a new user to the system
- **Request Body**:
```json
{
  "nome": "User Name",
  "tipo": "Aluno"
}
```
- **Valid Types**: `Aluno`, `Professor`, `Bibliotecario`
- **Response**: Created user object with status 201

---

## 📖 Empréstimos (Loans) Endpoints

### List All Loans
- **URL**: `/api/emprestimos`
- **Method**: `GET`
- **Description**: Returns a list of all loans
- **Response**: Array of loan objects

**Example Response:**
```json
[
  {
    "nomeUsuario": "Ana Souza",
    "tituloLivro": "Clean Code",
    "dataEmprestimo": "2025-11-08",
    "status": "EMPRESTADO"
  }
]
```

### Register New Loan
- **URL**: `/api/emprestimos`
- **Method**: `POST`
- **Description**: Registers a new book loan
- **Request Body**:
```json
{
  "nomeUsuario": "Ana Souza",
  "tituloLivro": "Clean Code"
}
```
- **Response**: Created loan object with status 201
- **Error**: 409 Conflict if book is not available

### Return a Book
- **URL**: `/api/emprestimos/{nomeUsuario}/{tituloLivro}/devolver`
- **Method**: `PUT`
- **Description**: Registers the return of a borrowed book
- **URL Parameters**: 
  - `nomeUsuario` (string) - The user name
  - `tituloLivro` (string) - The book title
- **Response**: Updated loan object with status 200
- **Error**: 404 if active loan not found

---

## 📊 Relatórios (Reports) Endpoints

### Generate Complete Report
- **URL**: `/api/relatorios`
- **Method**: `GET`
- **Description**: Returns a complete library report with books, users, and loans
- **Response**: Plain text report

**Example Response:**
```
Relatório da Biblioteca
======================

Livros:
Livro [titulo=Clean Code, autor=Robert C. Martin, quantidade=1]

Usuários:
Aluno [nome=Ana Souza]
  - Emprestado: Clean Code (em 2025-11-08)

Empréstimos:
Emprestimo[usuario=Ana Souza, livro=Clean Code, status=EMPRESTADO]
```

---

## Error Responses

The API uses standard HTTP status codes and returns error messages in JSON format:

### 400 Bad Request
```json
{
  "titulo": "Título é obrigatório",
  "autor": "Autor é obrigatório"
}
```

### 404 Not Found
```json
{
  "error": "Livro não encontrado: Book Title"
}
```

### 409 Conflict
```json
{
  "error": "Livro indisponível para empréstimo."
}
```

### 500 Internal Server Error
```json
{
  "error": "Erro interno do servidor: [message]"
}
```

---

## Running the API

### Start the Server
```bash
mvn spring-boot:run
```

Or using Make:
```bash
make run
```

The API will be available at `http://localhost:8080`

### Example API Calls

Using cURL:

```bash
# List all books
curl http://localhost:8080/api/livros

# Create a new book
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{"titulo":"New Book","autor":"Author Name"}'

# Register a loan
curl -X POST http://localhost:8080/api/emprestimos \
  -H "Content-Type: application/json" \
  -d '{"nomeUsuario":"Ana Souza","tituloLivro":"Clean Code"}'

# Return a book
curl -X PUT "http://localhost:8080/api/emprestimos/Ana%20Souza/Clean%20Code/devolver"
```

---

## Technology Stack

- **Spring Boot 3.2.0** - Main framework
- **Spring Web** - REST API support
- **Spring Validation** - Request validation
- **Springdoc OpenAPI 2.3.0** - API documentation (Swagger UI)
