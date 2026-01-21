/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.Conexao;
import model.Pessoa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    public void salvar(Pessoa p) {
        String sql = "INSERT INTO pessoa (nome, sobrenome, email, telefone) VALUES (?,?,?,?)";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getSobrenome());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelefone());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pessoa> listar() {
        List<Pessoa> lista = new ArrayList<>();
        String sql = "SELECT * FROM pessoa";

        try (Connection c = Conexao.comBanco();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Pessoa p = new Pessoa();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setSobrenome(rs.getString("sobrenome"));
                p.setEmail(rs.getString("email"));
                p.setTelefone(rs.getString("telefone"));
                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(Pessoa p) {
        String sql = "UPDATE pessoa SET nome=?, sobrenome=?, email=?, telefone=? WHERE id=?";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getSobrenome());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelefone());
            ps.setInt(5, p.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM pessoa WHERE id=?";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

