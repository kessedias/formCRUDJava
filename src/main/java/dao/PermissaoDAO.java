/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.Conexao;
import model.Permissao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissaoDAO {

    /**
     * Lista todas as permissões
     */
    public List<Permissao> listar() {
        List<Permissao> lista = new ArrayList<>();
        String sql = "SELECT * FROM permissao ORDER BY id";

        try (Connection c = Conexao.comBanco();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Permissao p = new Permissao();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setStatus(rs.getInt("status"));
                p.setTimecreated(rs.getTimestamp("timecreated"));
                p.setTimemodified(rs.getTimestamp("timemodified"));
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Busca permissão por nome
     */
    public Permissao buscarPorNome(String nome) {
        String sql = "SELECT * FROM permissao WHERE nome = ?";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Permissao p = new Permissao();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setStatus(rs.getInt("status"));
                    p.setTimecreated(rs.getTimestamp("timecreated"));
                    p.setTimemodified(rs.getTimestamp("timemodified"));
                    return p;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
