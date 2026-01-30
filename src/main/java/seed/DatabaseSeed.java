/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seed;

import config.Conexao;
import config.EnvLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseSeed {

    public static void run() {
        try (Connection c = Conexao.semBanco();
             Statement st = c.createStatement()) {

            st.execute("CREATE DATABASE IF NOT EXISTS " + EnvLoader.get("DB_NAME"));
            st.execute("USE " + EnvLoader.get("DB_NAME"));

            // Tabela pessoa (usuários)
            st.execute("""
                CREATE TABLE IF NOT EXISTS pessoa (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    sobrenome VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    telefone VARCHAR(20),
                    login VARCHAR(100) NOT NULL UNIQUE,
                    senha VARCHAR(255) NOT NULL,
                    role ENUM('admin', 'user') NOT NULL DEFAULT 'user',
                    status TINYINT NOT NULL DEFAULT 1,
                    timecreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);

            // Tabela permissao
            st.execute("""
                CREATE TABLE IF NOT EXISTS permissao (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL UNIQUE,
                    status TINYINT NOT NULL DEFAULT 1,
                    timecreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);

            // Tabela usuario_permissao
            st.execute("""
                CREATE TABLE IF NOT EXISTS usuario_permissao (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    usuario_id INT NOT NULL,
                    permissao_id INT NOT NULL,
                    status TINYINT NOT NULL DEFAULT 1,
                    timecreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    timemodified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (usuario_id) REFERENCES pessoa(id) ON DELETE CASCADE,
                    FOREIGN KEY (permissao_id) REFERENCES permissao(id) ON DELETE CASCADE,
                    UNIQUE KEY unique_usuario_permissao (usuario_id, permissao_id)
                )
            """);

            // Inserir permissões iniciais
            String[] permissoes = {"LISTAR_DADOS", "CRIAR_DADOS", "ATUALIZAR_DADOS", "EXCLUIR_DADOS"};
            for (String permissao : permissoes) {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT IGNORE INTO permissao (nome, status) VALUES (?, 1)")) {
                    ps.setString(1, permissao);
                    ps.execute();
                }
            }

            System.out.println("Banco e tabelas prontos 🚀");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Associa permissões ao usuário baseado na role
     */
    public static void atribuirPermissoesParaUsuario(int usuarioId, String role) {
        try (Connection c = Conexao.comBanco()) {
            String[] permissoesAdmin = {"LISTAR_DADOS", "CRIAR_DADOS", "ATUALIZAR_DADOS", "EXCLUIR_DADOS"};
            String[] permissoesUser = {"LISTAR_DADOS", "ATUALIZAR_DADOS"};

            String[] permissoesParaAtribuir = "admin".equals(role) ? permissoesAdmin : permissoesUser;

            for (String nomePermissao : permissoesParaAtribuir) {
                // Buscar id da permissão
                int permissaoId = -1;
                try (PreparedStatement ps = c.prepareStatement("SELECT id FROM permissao WHERE nome = ?")) {
                    ps.setString(1, nomePermissao);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            permissaoId = rs.getInt("id");
                        }
                    }
                }

                if (permissaoId > 0) {
                    try (PreparedStatement ps = c.prepareStatement(
                            "INSERT IGNORE INTO usuario_permissao (usuario_id, permissao_id, status) VALUES (?, ?, 1)")) {
                        ps.setInt(1, usuarioId);
                        ps.setInt(2, permissaoId);
                        ps.execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
