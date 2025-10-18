# Projeto: Biblioteca

Relatório resumido do projeto com árvore de arquivos, casos de uso e diagrama UML de classes.

## Estrutura do projeto (árvore)

```
biblioteca/
├── pom.xml
├── engineering_report.html
├── README.md
├── diagrams/
│   ├── classes.puml
│   └── usecases.puml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/fatec/biblioteca/
│   │   │       ├── Main.java
│   │   │       ├── controller/
│   │   │       │   └── BibliotecaController.java
│   │   │       ├── factories/
│   │   │       │   ├── LivroFactory.java
│   │   │       │   └── UsuarioFactory.java
│   │   │       ├── model/
│   │   │       │   ├── interfaces/
│   │   │       │   │   └── BibliotecaRepository.java
│   │   │       │   ├── usuarios/
│   │   │       │   │   ├── Aluno.java
│   │   │       │   │   ├── Bibliotecario.java
│   │   │       │   │   ├── Professor.java
│   │   │       │   │   └── Usuario.java
│   │   │       │   ├── Biblioteca.java
│   │   │       │   ├── Livro.java
│   │   │       │   └── Usuario.java
│   │   │       └── view/
│   │   │           └── BibliotecaView.java
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/fatec/biblioteca/
│               ├── controller/
│               │   └── BibliotecaControllerTest.java
│               ├── factories/
│               │   ├── LivroFactoryTest.java
│               │   └── UsuarioFactoryTest.java
│               └── model/
│                   ├── BibliotecaTest.java
│                   └── LivroTest.java
└── target/
    └── site/
        └── jacoco/
```

## Casos de uso (resumo)

Atores:
- Usuário (pessoa que opera o sistema via console)

Casos de uso principais:
- Cadastrar Livro: inserir título e autor.
- Listar Livros: exibir livros cadastrados.
- Remover Livro: remover um livro existente.
- Cadastrar Usuário: inserir tipo (aluno|professor|bibliotecario) e nome.
- Listar Usuários: exibir usuários cadastrados.

## Diagrama de Casos de Uso (PlantUML)

Arquivo: `diagrams/usecases.puml`

Conteúdo (PlantUML):

```
@startuml
left to right direction
actor Usuario as U
rectangle Sistema {
  usecase "Cadastrar Livro" as UC1
  usecase "Listar Livros" as UC2
  usecase "Remover Livro" as UC3
  usecase "Cadastrar Usuário" as UC4
  usecase "Listar Usuários" as UC5
}
U --> UC1
U --> UC2
U --> UC3
U --> UC4
U --> UC5
@enduml
```

Como renderizar: instale PlantUML e Graphviz, ou use o plugin PlantUML do VS Code. No terminal:

```bash
# gerar PNG do diagrama de casos de uso
plantuml diagrams/usecases.puml
# gerar PNG do diagrama de classes
plantuml diagrams/classes.puml
```

## Diagrama de Classes (PlantUML)

Arquivo: `diagrams/classes.puml` — contém a representação das classes principais, interfaces e relações (Singleton, Factory, herança de Usuário, dependência do Controller no Repository, View → Controller).

Trecho (resumido):

```
@startuml
package com.fatec.biblioteca.controller {
  class BibliotecaController {
    +cadastrarLivro(l: Livro)
    +removerLivro(l: Livro)
    +listarLivros(): List<Livro>
    +cadastrarUsuario(u: Usuario)
    +listarUsuarios(): List<Usuario>
  }
}
package com.fatec.biblioteca.model {
  class Biblioteca {
    -instancia: Biblioteca
    -livros: List<Livro>
    -usuarios: List<Usuario>
    +getInstancia(): Biblioteca
    +adicionarLivro(l: Livro)
    +removerLivro(l: Livro)
    +listarLivros(): List<Livro>
    +adicionarUsuario(u: Usuario)
    +listarUsuarios(): List<Usuario>
  }
  class Livro { ... }
}
package com.fatec.biblioteca.model.usuarios {
  class Usuario <<abstract>> { -nome: String }
  class Aluno
  class Professor
  class Bibliotecario
}
@enduml
```

