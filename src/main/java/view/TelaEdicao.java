/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import config.Sessao;
import dao.PessoaDAO;
import model.Pessoa;
import javax.swing.*;
import java.awt.*;

/**
 * Tela de Edição/Criação de Usuário
 */
public class TelaEdicao extends JFrame {

    private int usuarioId;
    private JTextField txtNome;
    private JTextField txtSobrenome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cmbRole;
    private JComboBox<String> cmbStatus;
    private JButton btnSalvar;
    private JButton btnVoltar;
    private boolean isNovoUsuario;

    public TelaEdicao(int usuarioId) {
        this.usuarioId = usuarioId;
        this.isNovoUsuario = (usuarioId == 0);

        setTitle(isNovoUsuario ? "Novo Usuário" : "Editar Usuário");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();

        if (!isNovoUsuario) {
            carregarDados();
        }
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        String titulo = isNovoUsuario ? "Cadastrar Novo Usuário" : "Editar Usuário";
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridLayout(8, 2, 10, 10));

        painelCampos.add(new JLabel("Nome:*"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Sobrenome:*"));
        txtSobrenome = new JTextField();
        painelCampos.add(txtSobrenome);

        painelCampos.add(new JLabel("Email:*"));
        txtEmail = new JTextField();
        painelCampos.add(txtEmail);

        painelCampos.add(new JLabel("Telefone:*"));
        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);

        painelCampos.add(new JLabel("Login:*"));
        txtLogin = new JTextField();
        painelCampos.add(txtLogin);

        painelCampos.add(new JLabel("Senha:*"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        painelCampos.add(new JLabel("Tipo de Usuário:*"));
        cmbRole = new JComboBox<>(new String[]{"user", "admin"});
        painelCampos.add(cmbRole);

        painelCampos.add(new JLabel("Status:*"));
        cmbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        painelCampos.add(cmbStatus);

        // Usuário comum não pode alterar role e status
        if (Sessao.isUser()) {
            cmbRole.setEnabled(false);
            cmbStatus.setEnabled(false);
        }

        painelPrincipal.add(painelCampos, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnSalvar = new JButton(isNovoUsuario ? "Cadastrar" : "Atualizar");
        btnVoltar = new JButton("Voltar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnVoltar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Eventos
        btnSalvar.addActionListener(e -> salvar());
        btnVoltar.addActionListener(e -> voltar());
    }

    private void carregarDados() {
        PessoaDAO dao = new PessoaDAO();
        Pessoa pessoa = dao.buscarPorId(usuarioId);

        if (pessoa != null) {
            txtNome.setText(pessoa.getNome());
            txtSobrenome.setText(pessoa.getSobrenome());
            txtEmail.setText(pessoa.getEmail());
            txtTelefone.setText(pessoa.getTelefone());
            txtLogin.setText(pessoa.getLogin());
            txtSenha.setText(pessoa.getSenha());
            cmbRole.setSelectedItem(pessoa.getRole());
            cmbStatus.setSelectedIndex(pessoa.getStatus() == 1 ? 0 : 1);
        } else {
            JOptionPane.showMessageDialog(this,
                "Usuário não encontrado!",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            voltar();
        }
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String sobrenome = txtSobrenome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String role = (String) cmbRole.getSelectedItem();
        int status = cmbStatus.getSelectedIndex() == 0 ? 1 : 0;

        // Validar campos obrigatórios
        if (nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() ||
            telefone.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Todos os campos são obrigatórios!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        PessoaDAO dao = new PessoaDAO();

        // Verificar duplicidade de login ou email
        if (dao.existeLoginOuEmail(login, email, usuarioId)) {
            JOptionPane.showMessageDialog(this,
                "Login ou email não permitido",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setSobrenome(sobrenome);
        pessoa.setEmail(email);
        pessoa.setTelefone(telefone);
        pessoa.setLogin(login);
        pessoa.setSenha(senha);
        pessoa.setRole(role);
        pessoa.setStatus(status);

        if (isNovoUsuario) {
            // Criar novo usuário
            int id = dao.salvar(pessoa);
            if (id > 0) {
                JOptionPane.showMessageDialog(this,
                    "Usuário criado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                voltar();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Login ou email não permitido",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Atualizar usuário existente
            pessoa.setId(usuarioId);
            boolean sucesso = dao.atualizar(pessoa);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                    "Usuário atualizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                voltar();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Login ou email não permitido",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void voltar() {
        this.dispose();
        new TelaListagem().setVisible(true);
    }
}
