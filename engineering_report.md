# Relatório de Engenharia — Projeto Biblioteca

Data: 2025-10-15

## Sumário
- Visão geral
- Estrutura do projeto
- Diagrama de classes (UML)
- Casos de uso
- Padrões e arquitetura
- Componentes e responsabilidades
- Ambiente de desenvolvimento e dependências
- Como compilar, testar e executar
- Testes existentes e recomendações
- Próximos passos sugeridos

---

## 1. Visão geral

Este projeto é um protótipo em Java que implementa um sistema simples de biblioteca. Ele permite cadastrar e listar livros e usuários (Aluno, Professor, Bibliotecário), controlar disponibilidade de livros e gerar um relatório textual dos livros cadastrados.

O objetivo principal é demonstrar princípios de projeto limpos (SOLID), padrão MVC, uso de interfaces para o repositório (DIP), padrões Singleton e Factory, e tornar o código testável sem frameworks externos.

## 2. Estrutura do repositório

Raiz do projeto (resumida):

```
biblioteca/
├── pom.xml
├── README.md
├── engineering_report.md
├── diagrams/
│   ├── classes.puml
│   └── usecases.puml
├── src/
│   ├── main/java/com/fatec/biblioteca/
│   │   ├── Main.java
│   │   ├── controller/BibliotecaController.java
│   │   ├── factories/{LivroFactory, UsuarioFactory}
│   │   ├── model/
│   │   │   ├── interfaces/BibliotecaRepository.java
│   │   │   ├── Biblioteca.java
│   │   │   ├── Livro.java
│   │   │   └── usuarios/{Usuario,Aluno,Professor,Bibliotecario}
│   │   └── view/BibliotecaView.java
└── src/test/java/... (testes unitários)
```

## 3. Diagrama de Classes (UML) — resumo

Segue a representação textual (PlantUML) usada no projeto. Você pode renderizar com PlantUML.

Arquivo: `diagrams/classes.puml`

```
@startuml
package com.fatec.biblioteca.controller {
  class BibliotecaController {
    +BibliotecaController(repo: BibliotecaRepository)
    +cadastrarLivro(l: Livro): void
    +removerLivro(l: Livro): void
    +listarLivros(): List<Livro>
    +cadastrarUsuario(u: Usuario): void
    +listarUsuarios(): List<Usuario>
    +gerarRelatorio(): String
  }
}

package com.fatec.biblioteca.model {
  interface BibliotecaRepository {
    +adicionarLivro(l: Livro): void
    +removerLivro(l: Livro): void
    +listarLivros(): List<Livro>
    +adicionarUsuario(u: Usuario): void
    +listarUsuarios(): List<Usuario>
    +generateReportString(): String
  }

  class Biblioteca <<singleton>> {
    -livros: List<Livro>
    -usuarios: List<Usuario>
    +getInstancia(): Biblioteca
    +adicionarLivro(l: Livro): void
    +removerLivro(l: Livro): void
    +listarLivros(): List<Livro>
    +adicionarUsuario(u: Usuario): void
    +listarUsuarios(): List<Usuario>
    +generateReportString(): String
  }

  class Livro {
    -titulo: String
    -autor: String
    -quantidade: int
    +Livro(titulo:String, autor:String)
    +getTitulo(): String
    +getAutor(): String
    +getQuantidade(): int
    +isDisponivel(): boolean
    +emprestar(): void
    +devolver(): void
  }

  abstract class Usuario {
    -nome: String
    +Usuario(nome: String)
    +getNome(): String
  }
  class Aluno
  class Professor
  class Bibliotecario
}

package com.fatec.biblioteca.factories {
  class LivroFactory { +criarLivro(titulo, autor): Livro }
  class UsuarioFactory { +criarUsuario(tipo, nome): Usuario }
}

BibliotecaController ..> BibliotecaRepository : depende de
Biblioteca ..|> BibliotecaRepository : implements
Usuario <|-- Aluno
Usuario <|-- Professor
Usuario <|-- Bibliotecario
Biblioteca "1" *-- "*" Livro
Biblioteca "1" *-- "*" Usuario
@enduml
```

Observações:
- `Biblioteca` é o repositório em memória (padrão Singleton). Testes usam reflexão para resetar a instância entre casos.
- `BibliotecaController` aplica DIP recebendo `BibliotecaRepository` por construtor.
- `Livro` contém lógica de negócio local (`emprestar` / `devolver`) e validação (lança `IllegalStateException` quando não disponível).

## 4. Casos de Uso

Resumo dos principais casos de uso implementados e quem os executa:

- Ator: Usuário (qualquer pessoa que usa o console)
  - Cadastrar Livro — inserir título e autor (usa `LivroFactory`)
  - Listar Livros — exibir livros cadastrados
  - Remover Livro — remover instância existente
  - Cadastrar Usuário — tipos: `aluno`, `professor`, `bibliotecario` (usa `UsuarioFactory`)
  - Listar Usuários — exibir usuários cadastrados

Arquivo PlantUML exemplo: `diagrams/usecases.puml`

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

## 5. Padrões e Decisões de Arquitetura

- MVC (simplificado): `BibliotecaView` (View) → `BibliotecaController` (Controller) → `Biblioteca` (Model/Repository).
- DIP: Controller depende da interface `BibliotecaRepository` e não da implementação concreta.
- Singleton: `Biblioteca.getInstancia()` fornece instância única padrão do repositório.
- Factory: `LivroFactory`, `UsuarioFactory` para centralizar criação de instâncias.
- Repository: `BibliotecaRepository` abstrai operações de persistência (in-memory no momento).

Design rationale:
- Facilitar testes: controller pode receber um fake repo (testes implementam `FakeRepo`).
- Simplicidade: sem frameworks externos, fácil leitura e extensão.

## 6. Ambiente de desenvolvimento e dependências

- Java: 17 (o `pom.xml` define source/target para 17)
- Build tool: Maven
- Dependências (somente para testes): JUnit Jupiter 5.10.0
- Ferramentas recomendadas: PlantUML + Graphviz para diagramas (ou plugin VSCode PlantUML), IDE (IntelliJ/VSCode)
- Container/devcontainer: há configuração em `.devcontainer/` para uso em ambientes reproduzíveis.

Como verificar versão Java/Maven:

```bash
java -version
mvn -version
```

Instalar Graphviz (para renderizar PlantUML):

Debian/Ubuntu:
```bash
sudo apt update
sudo apt install -y graphviz
```

## 7. Build, testes e execução

Compilar:

```bash
mvn -DskipTests package
```

Executar testes:

```bash
mvn test
```

Executar a aplicação (jar):

```bash
java -cp target/biblioteca-1.0-SNAPSHOT.jar com.fatec.biblioteca.Main
```

Gerar diagramas PlantUML (se PlantUML + Graphviz estiverem instalados):

```bash
plantuml diagrams/usecases.puml
plantuml diagrams/classes.puml
```

## 8. Testes existentes

- `LivroTest` — valida getters, emprestar/devolver e exceções.
- `LivroFactoryTest` — checa criação do livro.
- `UsuarioFactoryTest` — checa criação por tipo e valida erros.
- `BibliotecaTest` — testa singleton, adicionar/listar/remover e imutabilidade das listas retornadas.
- `BibliotecaControllerTest` — testa controller com `FakeRepo` e geração de relatório.

Ferramenta de cobertura: JaCoCo está configurado no `pom.xml`. O relatório é gerado automaticamente na fase de teste.

## 9. Observações de implementação e pontos de atenção

- `Biblioteca` é um singleton com listas internas. Para testes, o singleton é resetado por reflexão — isso é aceitável em protótipos mas não ideal em produção.
- `Livro` expõe métodos `emprestar()` / `devolver()` — a lógica de negócios relacionada a empréstimos é interna à entidade; não há associação entre `Emprestimo` e `Usuario` no modelo atual.
- `BibliotecaRepository` inclui `generateReportString()` (usado por testes e controller) para gerar um relatório textual. Isso acopla uma representação textual ao repositório; uma alternativa seria mover esta responsabilidade para o Controller ou uma classe `ReportService`.

## 10. Recomendações e próximos passos

1. Introduzir entidade `Emprestimo` para relacionar `Usuario` com `Livro` (data de empréstimo, data prevista, status). Isso permitirá rastrear quem pegou qual cópia.
2. Mover geração de relatórios para uma classe separada `ReportService` para manter SRP.
3. Remover o padrão Singleton ou torná-lo opcional (permitir injeção por construtor para facilitar testes e substituições em runtime).
4. Adicionar persistência simples (arquivo JSON ou banco embedded como H2) para manter dados entre execuções.
5. Cobertura de testes: estender testes para o controller/comportamentos de UI (E2E) e configurar um workflow CI para executar testes e gerar relatório de cobertura.
6. Melhorar validações de entrada e mensagens de erro na `View`.
