# ATIVIDADE - TESTE DE CAIXA BRANCA E REVISÃO DE CÓDIGO-FONTE

## Introdução
Este trabalho tem como objetivo realizar a análise estrutural de um código-fonte desenvolvido em Java. A análise foi conduzida com base nos conceitos de Teste de Caixa Branca, abordando aspectos como fluxo de execução, complexidade ciclomática, caminhos básicos, legibilidade, segurança e boas práticas de desenvolvimento.
Além disso, foi realizada a reconstrução e melhoria do código original, visando corrigir falhas estruturais e vulnerabilidades.

## Análise Estática do Código

### Documentação
O código não possui comentários explicativos nem documentação Javadoc, o que dificulta o entendimento da responsabilidade de cada método e das decisões de implementação.
Em projetos reais, recomenda-se documentar principalmente métodos como conectarBD() e verificarUsuario(), explicando propósito, parâmetros e retorno.

### Nomenclatura
A nomenclatura é parcialmente adequada, porém pode ser melhorada:
- User: adequado, porém poderia ser mais específico como UserDAO ou UsuarioRepository, pois a classe realiza acesso ao banco de dados.
- conectarBD(): compreensível, mas o padrão Java sugere conectarBancoDados().
- verificarUsuario(): nome claro e adequado.
= Variáveis públicas como nome e result: má prática, deveriam ser privadas.

### Legibilidade 
O código apresenta simplicidade, porém alguns pontos prejudicam a legibilidade:
- Uso de concatenação de SQL em múltiplas linhas.
- Strings SQL montadas manualmente dificultam leitura e manutenção.
- Ausência de comentários explicativos no fluxo.
- Uso de variáveis globais públicas afeta a previsibilidade da classe.

### Tratamento de Exceções
O tratamento de exceções não segue boas práticas:
- Os blocos catch (Exception e) { } estão vazios.
- Isso oculta erros importantes e dificulta a depuração.
O ideal seria registrar o erro, por exemplo com e.printStackTrace() ou uso de logger.

### Segurança 
Foi identificada uma grave vulnerabilidade de SQL Injection:

```java
sql += "where login = '" + login + "' ";
sql += " and senha = '" + senha + "'";
```

Isso permite que um atacante injete comandos SQL maliciosos.

Solução recomendada: utilização de PreparedStatement.
Outros problemas de segurança:
- Senhas comparadas em texto puro (sem hash).
- Credenciais do banco expostas diretamente no código-fonte.

### Conexão 
Problemas identificados:
- Driver incorreto:
  
```java
Class.forName("com.mysql.Driver.Manager")
```

O correto seria algo como:
-com.mysql.cj.jdbc.Driver
- Conexões (Connection, Statement, ResultSet) não são fechadas.
- Possível vazamento de conexão (connection leak).

### Vulnerabilidades
Principais vulnerabilidades encontradas:
- SQL Injection (alta criticidade)
- Senhas em texto plano
- Credenciais expostas no código
- Falta de tratamento adequado de exceções
- Possível vazamento de conexão

### Boas práticas
O código não segue diversas boas práticas de desenvolvimento:
- Não utiliza PreparedStatement
- Não encapsula atributos (uso de variáveis públicas)
- Não fecha recursos JDBC
- Não utiliza logs
- Não separa responsabilidades corretamente (conexão e regra de negócio na mesma classe)
- Credenciais hardcoded no código

## Grafo de Fluxo

<img width="392" height="942" alt="image" src="https://github.com/user-attachments/assets/9dac1659-0469-4afb-bc13-978f2e03b56d" />

Método analisado
- Classe: User
- Método: verificarUsuario(String login, String senha)

### Nós
N1 — Início do método
N2 — Conexão com banco (conectarBD)
N3 — Montagem da query SQL
N4 — Bloco try
N5 — Criação do Statement
N6 — Execução da query (executeQuery)
N7 — Verificação rs.next()
N8 — Atribuição de dados (nome, result = true)
N9 — Catch de exceção
N10 — Retorno do resultado

### Arestas 
N1 → N2
N2 → N3
N3 → N4
N4 → N5
N5 → N6
N6 → N7
N7 → N8 (TRUE)
N7 → N10 (FALSE)
N8 → N10
N4 → N9 (exceção)
N9 → N10

## Complexidade Ciclomática

Fórmula: V(G) = E − N + 2P

### Contagem
- Nós (N): 10
- Arestas (E): 11
- Componentes conectados (P): 1

### Cálculo
- V(G) = 11 − 10 + 2(1)
- V(G) = 1 + 2
- V(G) = 3
Resultado final: 3

- Complexidade ciclomática = 3

### Caminhos Básicos

A complexidade indica 3 caminhos independentes:

#### Caminho 1 — Usuário encontrado
- Início
- Conectar banco
- Montar SQL
- Executar query
- rs.next() = TRUE
- Atribui dados
- Retorna resultado
  
#### Caminho 2 — Usuário não encontrado
- Início
- Conectar banco
- Montar SQL
- Executar query
- rs.next() = FALSE
- Retorna falso
  
#### Caminho 3 — Exceção no banco
- Início
- Conectar banco
- Montar SQL
- Erro no TRY
- Catch executado
- Retorna resultado padrão

### Melhorias Implementadas
Foram realizadas melhorias estruturais no código com foco em:
- Correção de vulnerabilidade de SQL Injection
- Substituição de concatenação por PreparedStatement
- Melhor tratamento de exceções
- Encapsulamento de atributos
- Fechamento adequado de conexões JDBC
- Separação de responsabilidades
- Melhoria na legibilidade e manutenção

### Conclusão
A análise estrutural baseada em Teste de Caixa Branca permitiu identificar os fluxos de execução do sistema e determinar o conjunto mínimo de testes necessários para garantir cobertura lógica adequada. Foram identificadas melhorias importantes relacionadas à segurança, manutenção e organização do código, especialmente no tratamento de SQL Injection e exceções. A aplicação de boas práticas contribui diretamente para maior confiabilidade, segurança e facilidade de manutenção do sistema.

##

Maria Eduarda Souza Santos | 240212

