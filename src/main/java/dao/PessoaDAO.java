/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.Conexao;
import model.Pessoa;
import seed.DatabaseSeed;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    /**
     * Salva um novo usuário no banco
     *
     * @return ID gerado ou -1 em caso de erro
     */
    public int salvar(Pessoa p) {
        String sql = "INSERT INTO pessoa (nome, sobrenome, email, telefone, login, senha, role, status) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getSobrenome());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getLogin());
            ps.setString(6, p.getSenha());
            ps.setString(7, p.getRole());
            ps.setInt(8, p.getStatus());
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    // Atribuir permissões com base na role
                    DatabaseSeed.atribuirPermissoesParaUsuario(id, p.getRole());
                    return id;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicidade de login ou email
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Verifica se login ou email já existem
     */
    public boolean existeLoginOuEmail(String login, String email, int ignorarId) {
        String sql = "SELECT COUNT(*) FROM pessoa WHERE (login = ? OR email = ?) AND id != ?";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, email);
            ps.setInt(3, ignorarId);

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

    /**
     * Autentica usuário por login e senha
     */
    public Pessoa autenticar(String login, String senha) {
        String sql = "SELECT * FROM pessoa WHERE login = ? AND senha = ? AND status = 1";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPessoa(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lista todos os usuários (para admin)
     */
    public List<Pessoa> listar() {
        List<Pessoa> lista = new ArrayList<>();
        String sql = "SELECT * FROM pessoa ORDER BY id";

        try (Connection c = Conexao.comBanco();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPessoa(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Lista apenas usuários com role 'user' (para usuário comum)
     */
    public List<Pessoa> listarApenasUsers() {
        List<Pessoa> lista = new ArrayList<>();
        String sql = "SELECT * FROM pessoa WHERE role = 'user' ORDER BY id";

        try (Connection c = Conexao.comBanco();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPessoa(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Busca usuário por ID
     */
    public Pessoa buscarPorId(int id) {
        String sql = "SELECT * FROM pessoa WHERE id = ?";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPessoa(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Atualiza dados do usuário
     */
    public boolean atualizar(Pessoa p) {
        String sql = "UPDATE pessoa SET nome=?, sobrenome=?, email=?, telefone=?, login=?, senha=?, role=?, status=? WHERE id=?";

        try (Connection c = Conexao.comBanco();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getSobrenome());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getLogin());
            ps.setString(6, p.getSenha());
            ps.setString(7, p.getRole());
            ps.setInt(8, p.getStatus());
            ps.setInt(9, p.getId());
            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicidade de login ou email
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Exclui usuário por ID
     */
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

    /**
     * Mapeia ResultSet para objeto Pessoa
     */
    private Pessoa mapearPessoa(ResultSet rs) throws SQLException {
        Pessoa p = new Pessoa();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setSobrenome(rs.getString("sobrenome"));
        p.setEmail(rs.getString("email"));
        p.setTelefone(rs.getString("telefone"));
        p.setLogin(rs.getString("login"));
        p.setSenha(rs.getString("senha"));
        p.setRole(rs.getString("role"));
        p.setStatus(rs.getInt("status"));
        p.setTimecreated(rs.getTimestamp("timecreated"));
        p.setTimemodified(rs.getTimestamp("timemodified"));
        return p;
    }
}
