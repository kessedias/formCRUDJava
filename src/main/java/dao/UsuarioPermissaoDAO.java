/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioPermissaoDAO {

    /**
     * Lista permissões de um usuário
     */
    public List<String> listarPermissoesDoUsuario(int usuarioId) {
        List<String> permissoes = new ArrayList<>();
        String sql = """
            SELECT p.nome FROM usuario_permissao up
            INNER JOIN permissao p ON up.permissao_id = p.id
            WHERE up.usuario_id = ? AND up.status = 1 AND p.status = 1
        """;

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    permissoes.add(rs.getString("nome"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissoes;
    }

    /**
     * Verifica se usuário possui uma permissão específica
     */
    public boolean temPermissao(int usuarioId, String nomePermissao) {
        String sql = """
            SELECT COUNT(*) FROM usuario_permissao up
            INNER JOIN permissao p ON up.permissao_id = p.id
            WHERE up.usuario_id = ? AND p.nome = ? AND up.status = 1 AND p.status = 1
        """;

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setString(2, nomePermissao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
