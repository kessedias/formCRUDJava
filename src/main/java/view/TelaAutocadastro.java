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
 * Tela de Autocadastro (Inscreva-se)
 */
public class TelaAutocadastro extends JFrame {

    private JTextField txtNome;
    private JTextField txtSobrenome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cmbRole;
    private JButton btnCadastrar;
    private JButton btnVoltar;

    public TelaAutocadastro() {
        setTitle("Autocadastro - Sistema CRUD");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("Criar Nova Conta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridLayout(7, 2, 10, 10));

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

        painelPrincipal.add(painelCampos, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnCadastrar = new JButton("Cadastrar");
        btnVoltar = new JButton("Voltar ao Login");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnVoltar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Eventos
        btnCadastrar.addActionListener(e -> realizarCadastro());
        btnVoltar.addActionListener(e -> voltarAoLogin());
    }

    private void realizarCadastro() {
        String nome = txtNome.getText().trim();
        String sobrenome = txtSobrenome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String role = (String) cmbRole.getSelectedItem();

        // Validar campos obrigatórios
        if (nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() ||
            telefone.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Todos os campos são obrigatórios!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar se login ou email já existem
        PessoaDAO dao = new PessoaDAO();
        if (dao.existeLoginOuEmail(login, email, 0)) {
            JOptionPane.showMessageDialog(this,
                "Login ou email não permitido",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Criar pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setSobrenome(sobrenome);
        pessoa.setEmail(email);
        pessoa.setTelefone(telefone);
        pessoa.setLogin(login);
        pessoa.setSenha(senha);
        pessoa.setRole(role);
        pessoa.setStatus(1);

        int id = dao.salvar(pessoa);

        if (id > 0) {
            // Cadastro bem-sucedido, fazer login automático
            pessoa.setId(id);
            Sessao.setUsuarioLogado(pessoa);

            JOptionPane.showMessageDialog(this,
                "Cadastro realizado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new TelaListagem().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Login ou email não permitido",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarAoLogin() {
        this.dispose();
        new TelaLogin().setVisible(true);
    }
}
