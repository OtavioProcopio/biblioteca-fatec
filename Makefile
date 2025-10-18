# ============================================================================
# Makefile para Sistema de Biblioteca - Java Maven
# Automatiza build, testes, cobertura, limpeza e execução
# ============================================================================

# Variáveis
MAVEN = mvn
JAVA = java
MAIN_CLASS = com.fatec.biblioteca.Main
JAR_FILE = target/biblioteca-1.0-SNAPSHOT.jar
COVERAGE_REPORT = target/site/jacoco/index.html
TEST_REPORT = target/surefire-reports/
BROWSER = $(shell which xdg-open || which open || echo "echo 'Browser não encontrado:'")

# Cores para output
RED = \033[0;31m
GREEN = \033[0;32m
YELLOW = \033[1;33m
BLUE = \033[0;34m
PURPLE = \033[0;35m
CYAN = \033[0;36m
NC = \033[0m # No Color

# ============================================================================
# Targets Principais
# ============================================================================

.PHONY: help
help: ## Mostra esta ajuda
	@echo "$(CYAN)┌─────────────────────────────────────────────────────────────────┐$(NC)"
	@echo "$(CYAN)│                    Sistema de Biblioteca                       │$(NC)"
	@echo "$(CYAN)│                 Comandos Makefile Disponíveis                  │$(NC)"
	@echo "$(CYAN)└─────────────────────────────────────────────────────────────────┘$(NC)"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "$(GREEN)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(YELLOW)Exemplos de uso:$(NC)"
	@echo "  make build          # Compila o projeto"
	@echo "  make test           # Executa todos os testes"
	@echo "  make run            # Executa a aplicação"
	@echo "  make coverage       # Gera relatório de cobertura"
	@echo "  make all            # Build completo + testes + cobertura"

.PHONY: all
all: clean build test coverage ## Build completo: limpa, compila, testa e gera cobertura
	@echo "$(GREEN)✅ Build completo executado com sucesso!$(NC)"
	@echo "$(CYAN)📊 Relatórios disponíveis:$(NC)"
	@echo "  - Cobertura: $(COVERAGE_REPORT)"
	@echo "  - Testes: $(TEST_REPORT)"

# ============================================================================
# Build e Compilação
# ============================================================================

.PHONY: build
build: ## Compila o projeto
	@echo "$(BLUE)🔨 Compilando o projeto...$(NC)"
	$(MAVEN) compile
	@echo "$(GREEN)✅ Compilação concluída!$(NC)"

.PHONY: package
package: ## Cria o JAR executável
	@echo "$(BLUE)📦 Criando JAR executável...$(NC)"
	$(MAVEN) package -DskipTests
	@echo "$(GREEN)✅ JAR criado: $(JAR_FILE)$(NC)"

.PHONY: install
install: ## Instala dependências e compila
	@echo "$(BLUE)📥 Instalando dependências...$(NC)"
	$(MAVEN) clean install
	@echo "$(GREEN)✅ Instalação concluída!$(NC)"

# ============================================================================
# Testes
# ============================================================================

.PHONY: test
test: ## Executa todos os testes
	@echo "$(BLUE)🧪 Executando testes...$(NC)"
	$(MAVEN) test
	@echo "$(GREEN)✅ Testes concluídos!$(NC)"

.PHONY: test-view
test-view: ## Executa apenas testes da View
	@echo "$(BLUE)🧪 Executando testes da View...$(NC)"
	$(MAVEN) test -Dtest=BibliotecaViewTest
	@echo "$(GREEN)✅ Testes da View concluídos!$(NC)"

.PHONY: test-model
test-model: ## Executa apenas testes do Model
	@echo "$(BLUE)🧪 Executando testes do Model...$(NC)"
	$(MAVEN) test -Dtest="*Test" -Dtest.excludes="*ViewTest,*ControllerTest"
	@echo "$(GREEN)✅ Testes do Model concluídos!$(NC)"

.PHONY: test-controller
test-controller: ## Executa apenas testes do Controller
	@echo "$(BLUE)🧪 Executando testes do Controller...$(NC)"
	$(MAVEN) test -Dtest=BibliotecaControllerTest
	@echo "$(GREEN)✅ Testes do Controller concluídos!$(NC)"

.PHONY: test-integration
test-integration: ## Executa testes de integração
	@echo "$(BLUE)🧪 Executando testes de integração...$(NC)"
	$(MAVEN) test -Dtest="**/*IntegrationTest,**/*IT"
	@echo "$(GREEN)✅ Testes de integração concluídos!$(NC)"

# ============================================================================
# Cobertura e Relatórios
# ============================================================================

.PHONY: coverage
coverage: ## Gera relatório de cobertura de código
	@echo "$(BLUE)📊 Gerando relatório de cobertura...$(NC)"
	$(MAVEN) test jacoco:report
	@echo "$(GREEN)✅ Relatório de cobertura gerado!$(NC)"
	@echo "$(CYAN)📖 Abra o arquivo: $(COVERAGE_REPORT)$(NC)"

.PHONY: coverage-open
coverage-open: coverage ## Gera e abre relatório de cobertura no browser
	@echo "$(BLUE)🌐 Abrindo relatório de cobertura...$(NC)"
	@if [ -f "$(COVERAGE_REPORT)" ]; then \
		$(BROWSER) $(COVERAGE_REPORT) 2>/dev/null || echo "$(YELLOW)⚠️  Abra manualmente: $(COVERAGE_REPORT)$(NC)"; \
	else \
		echo "$(RED)❌ Relatório não encontrado. Execute 'make coverage' primeiro.$(NC)"; \
	fi

.PHONY: coverage-summary
coverage-summary: ## Mostra resumo da cobertura no terminal
	@echo "$(BLUE)📊 Resumo da Cobertura:$(NC)"
	@if [ -f "target/jacoco.exec" ]; then \
		$(MAVEN) jacoco:report -q; \
		echo "$(GREEN)✅ Cobertura atualizada$(NC)"; \
		echo "$(CYAN)📖 Veja detalhes em: $(COVERAGE_REPORT)$(NC)"; \
	else \
		echo "$(YELLOW)⚠️  Execute 'make test' primeiro para gerar dados de cobertura$(NC)"; \
	fi

# ============================================================================
# Execução
# ============================================================================

.PHONY: run
run: build ## Executa a aplicação principal
	@echo "$(BLUE)🚀 Executando a aplicação...$(NC)"
	$(MAVEN) exec:java -Dexec.mainClass="$(MAIN_CLASS)" -Dexec.cleanupDaemonThreads=false

.PHONY: run-jar
run-jar: package ## Executa via JAR
	@echo "$(BLUE)🚀 Executando via JAR...$(NC)"
	$(JAVA) -jar $(JAR_FILE)

.PHONY: debug
debug: build ## Executa em modo debug
	@echo "$(BLUE)🔍 Executando em modo debug...$(NC)"
	$(MAVEN) exec:java -Dexec.mainClass="$(MAIN_CLASS)" -Dexec.args="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

# ============================================================================
# Limpeza
# ============================================================================

.PHONY: clean
clean: ## Remove arquivos de build
	@echo "$(YELLOW)🧹 Limpando arquivos de build...$(NC)"
	$(MAVEN) clean
	@echo "$(GREEN)✅ Limpeza concluída!$(NC)"

.PHONY: clean-all
clean-all: clean ## Limpeza completa (build + IDE)
	@echo "$(YELLOW)🧹 Limpeza completa...$(NC)"
	rm -rf .vscode/settings.json
	rm -rf *.log
	find . -name "*.class" -delete
	find . -name "*~" -delete
	@echo "$(GREEN)✅ Limpeza completa concluída!$(NC)"

# ============================================================================
# Desenvolvimento
# ============================================================================

.PHONY: compile-check
compile-check: ## Verifica se o código compila sem executar testes
	@echo "$(BLUE)🔍 Verificando compilação...$(NC)"
	$(MAVEN) compile test-compile
	@echo "$(GREEN)✅ Código compila corretamente!$(NC)"

.PHONY: dependency-tree
dependency-tree: ## Mostra árvore de dependências
	@echo "$(BLUE)🌳 Árvore de dependências:$(NC)"
	$(MAVEN) dependency:tree

.PHONY: dependency-analyze
dependency-analyze: ## Analisa dependências não utilizadas
	@echo "$(BLUE)🔍 Analisando dependências...$(NC)"
	$(MAVEN) dependency:analyze

.PHONY: validate
validate: ## Valida estrutura do projeto
	@echo "$(BLUE)✅ Validando projeto...$(NC)"
	$(MAVEN) validate
	@echo "$(GREEN)✅ Projeto válido!$(NC)"

# ============================================================================
# CI/CD e Qualidade
# ============================================================================

.PHONY: ci
ci: clean compile-check test coverage ## Pipeline CI completo
	@echo "$(GREEN)✅ Pipeline CI executado com sucesso!$(NC)"
	@echo "$(CYAN)📊 Resultados:$(NC)"
	@echo "  - Compilação: ✅"
	@echo "  - Testes: ✅"
	@echo "  - Cobertura: $(COVERAGE_REPORT)"

.PHONY: verify
verify: ## Executa verificações de qualidade
	@echo "$(BLUE)🔍 Executando verificações...$(NC)"
	$(MAVEN) verify
	@echo "$(GREEN)✅ Verificações concluídas!$(NC)"

.PHONY: site
site: ## Gera site de documentação do projeto
	@echo "$(BLUE)📖 Gerando documentação...$(NC)"
	$(MAVEN) site
	@echo "$(GREEN)✅ Documentação gerada em target/site/$(NC)"

# ============================================================================
# Informações do Sistema
# ============================================================================

.PHONY: info
info: ## Mostra informações do projeto e ambiente
	@echo "$(CYAN)┌─────────────────────────────────────────────────────────────────┐$(NC)"
	@echo "$(CYAN)│                 Informações do Sistema                         │$(NC)"
	@echo "$(CYAN)└─────────────────────────────────────────────────────────────────┘$(NC)"
	@echo "$(BLUE)📁 Projeto:$(NC) Sistema de Biblioteca"
	@echo "$(BLUE)📦 Versão:$(NC) 1.0-SNAPSHOT"
	@echo "$(BLUE)☕ Java:$(NC) $$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)"
	@echo "$(BLUE)🔧 Maven:$(NC) $$(mvn -version 2>/dev/null | head -n 1 | cut -d' ' -f3)"
	@echo "$(BLUE)🖥️  SO:$(NC) $$(uname -s) $$(uname -r)"
	@echo "$(BLUE)📂 Diretório:$(NC) $$(pwd)"
	@echo "$(BLUE)🎯 Main Class:$(NC) $(MAIN_CLASS)"
	@echo "$(BLUE)📋 JAR:$(NC) $(JAR_FILE)"
	@echo ""
	@echo "$(YELLOW)📊 Status do Projeto:$(NC)"
	@if [ -f "$(JAR_FILE)" ]; then echo "$(GREEN)✅ JAR compilado$(NC)"; else echo "$(RED)❌ JAR não encontrado$(NC)"; fi
	@if [ -f "$(COVERAGE_REPORT)" ]; then echo "$(GREEN)✅ Relatório de cobertura$(NC)"; else echo "$(YELLOW)⚠️  Relatório de cobertura não gerado$(NC)"; fi
	@if [ -d "$(TEST_REPORT)" ]; then echo "$(GREEN)✅ Relatórios de teste$(NC)"; else echo "$(YELLOW)⚠️  Relatórios de teste não gerados$(NC)"; fi

.PHONY: stats
stats: ## Mostra estatísticas do código
	@echo "$(CYAN)┌─────────────────────────────────────────────────────────────────┐$(NC)"
	@echo "$(CYAN)│                   Estatísticas do Código                       │$(NC)"
	@echo "$(CYAN)└─────────────────────────────────────────────────────────────────┘$(NC)"
	@echo "$(BLUE)📊 Arquivos Java:$(NC) $$(find src -name "*.java" | wc -l)"
	@echo "$(BLUE)📊 Linhas de código:$(NC) $$(find src -name "*.java" -exec cat {} + | wc -l)"
	@echo "$(BLUE)📊 Arquivos de teste:$(NC) $$(find src/test -name "*Test.java" | wc -l)"
	@echo "$(BLUE)📊 Classes principais:$(NC) $$(find src/main -name "*.java" | wc -l)"
	@echo "$(BLUE)📊 Tamanho do projeto:$(NC) $$(du -sh . | cut -f1)"

# ============================================================================
# Targets especiais
# ============================================================================

# Default target
.DEFAULT_GOAL := help

# Evita conflitos com arquivos
.PHONY: all build test clean run help info stats coverage