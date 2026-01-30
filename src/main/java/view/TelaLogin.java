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
 * Tela de Login do sistema
 */
public class TelaLogin extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnInscrever;

    public TelaLogin() {
        setTitle("Login - Sistema CRUD");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("Bem-vindo ao Sistema", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Painel de campos
        JPanel painelCampos = new JPanel(new GridLayout(2, 2, 10, 10));

        painelCampos.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        painelCampos.add(txtLogin);

        painelCampos.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelCampos.add(txtSenha);

        painelPrincipal.add(painelCampos, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnEntrar = new JButton("Entrar");
        btnInscrever = new JButton("Inscreva-se");

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnInscrever);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Eventos
        btnEntrar.addActionListener(e -> realizarLogin());
        btnInscrever.addActionListener(e -> abrirTelaAutocadastro());

        // Enter para login
        txtSenha.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha todos os campos!",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        PessoaDAO dao = new PessoaDAO();
        Pessoa usuario = dao.autenticar(login, senha);

        if (usuario != null) {
            Sessao.setUsuarioLogado(usuario);
            this.dispose();
            new TelaListagem().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Login ou senha inválidos!",
                "Erro de Autenticação",
                JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
        }
    }

    private void abrirTelaAutocadastro() {
        this.dispose();
        new TelaAutocadastro().setVisible(true);
    }
}
