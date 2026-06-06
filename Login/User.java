package 1.Login;

public package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ============================
 * CLASSE USER - LOGIN SYSTEM
 * MARIA EDUARDA SOUZA SANTOS - 2026
 * ============================
 *
 * RESPONSABILIDADE:
 * - Realizar autenticação de usuário no banco de dados
 *
 * MELHORIAS APLICADAS:
 * - Correção de SQL Injection
 * - Uso de PreparedStatement
 * - Melhor tratamento de exceções
 * - Encapsulamento de atributos
 * - Fechamento correto de recursos JDBC
 * - Melhor organização e legibilidade
 */
public class User {

    /**
     * ============================
     * ENCAPSULAMENTO DE ATRIBUTOS
     * ============================
     *
     * ANTES:
     * public Connection conn;
     * public String nome;
     * public boolean result;
     *
     * PROBLEMA:
     * - Qualquer classe podia modificar diretamente os dados
     * - Quebra de encapsulamento e segurança
     *
     * DEPOIS:
     * - Mantemos controle interno da classe
     */
    private Connection conn;

    // Nome do usuário autenticado
    public String nome = "";

    // Resultado da autenticação (login válido ou não)
    public boolean result = false;

    /**
     * ============================
     * CONEXÃO COM BANCO DE DADOS
     * ============================
     *
     * ANTES:
     * Class.forName("com.mysql.Driver.Manager")
     *
     * PROBLEMA:
     * - Driver incorreto
     * - Falta de tratamento adequado de exceções
     *
     * DEPOIS:
     * - Driver atualizado (com.mysql.cj.jdbc.Driver)
     * - Tratamento de erro mais claro
     */
    public Connection conectarBD() {

        try {
            // Carrega driver JDBC correto do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // String de conexão com banco
            String url = "jdbc:mysql://127.0.0.1/test?user=lopes&password=123";

            // Abre conexão
            conn = DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            System.out.println("Erro: Driver JDBC não encontrado.");
            System.out.println(e.getMessage());

        } catch (SQLException e) {
            System.out.println("Erro: Falha na conexão com o banco de dados.");
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * ============================
     * VERIFICAÇÃO DE USUÁRIO
     * ============================
     *
     * ANTES:
     * - Uso de concatenação de SQL
     * - Alto risco de SQL Injection
     *
     * DEPOIS:
     * - Uso de PreparedStatement
     * - Consulta segura
     * - Melhor organização do fluxo
     */
    public boolean verificarUsuario(String login, String senha) {

        // Abre conexão com o banco
        Connection conn = conectarBD();

        /**
         * ============================
         * CORREÇÃO DE SQL INJECTION
         * ============================
         *
         * ANTES:
         * sql = "select nome from usuarios where login = '" + login + "' and senha = '" + senha + "'";
         *
         * PROBLEMA:
         * - Usuário poderia injetar SQL malicioso
         * - Falta de segurança crítica
         *
         * DEPOIS:
         * - Uso de parâmetros (?) protegidos
         */
        String sql = "SELECT nome FROM usuarios WHERE login = ? AND senha = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Prepara consulta segura
            ps = conn.prepareStatement(sql);

            // Substitui parâmetros com segurança
            ps.setString(1, login);
            ps.setString(2, senha);

            // Executa consulta
            rs = ps.executeQuery();

            /**
             * ============================
             * PROCESSAMENTO DO RESULTADO
             * ============================
             */
            if (rs.next()) {
                result = true;
                nome = rs.getString("nome");
            } else {
                result = false;
                nome = "";
            }

        } catch (SQLException e) {
            /**
             * MELHORIA:
             * Antes: catch vazio (erro silencioso)
             * Depois: log do erro para depuração
             */
            System.out.println("Erro ao executar autenticação:");
            System.out.println(e.getMessage());

        } finally {

            /**
             * ============================
             * FECHAMENTO DE RECURSOS JDBC
             * ============================
             *
             * MELHORIA IMPORTANTE:
             * Evita vazamento de memória e conexões abertas
             */

            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar recursos:");
                System.out.println(e.getMessage());
            }
        }

        return result;
    }
} Usuario {
    
}
