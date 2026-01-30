/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import config.Sessao;
import dao.PessoaDAO;
import model.Pessoa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tela de Listagem de Usuários
 */
public class TelaListagem extends JFrame {

    private JTable tabela;
    private JButton btnNovo;
    private JButton btnLogout;
    private JLabel lblUsuarioLogado;
    private DefaultTableModel modeloTabela;

    public TelaListagem() {
        setTitle("Listagem de Usuários - Sistema CRUD");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        listar();
    }

    private void initComponents() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel superior com informações do usuário e botões
        JPanel painelSuperior = new JPanel(new BorderLayout());

        // Info do usuário logado
        Pessoa usuario = Sessao.getUsuarioLogado();
        lblUsuarioLogado = new JLabel("Logado como: " + usuario.getNome() + " (" + usuario.getRole() + ")");
        lblUsuarioLogado.setFont(new Font("Arial", Font.BOLD, 12));
        painelSuperior.add(lblUsuarioLogado, BorderLayout.WEST);

        // Painel de botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNovo = new JButton("Novo Usuário");
        btnLogout = new JButton("Sair");

        // Mostrar botão Novo apenas para admin com permissão CRIAR_DADOS
        if (Sessao.isAdmin() && Sessao.temPermissao("CRIAR_DADOS")) {
            painelBotoes.add(btnNovo);
        }
        painelBotoes.add(btnLogout);

        painelSuperior.add(painelBotoes, BorderLayout.EAST);
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

        // Tabela
        tabela = new JTable();
        configurarTabela();
        JScrollPane scroll = new JScrollPane(tabela);
        painelPrincipal.add(scroll, BorderLayout.CENTER);

        add(painelPrincipal);

        // Eventos
        btnNovo.addActionListener(e -> abrirTelaNovoUsuario());
        btnLogout.addActionListener(e -> realizarLogout());
    }

    private void configurarTabela() {
        if (Sessao.isAdmin()) {
            // Admin vê todas as colunas + ações
            modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome", "Sobrenome", "Email", "Telefone", "Role", "Status", "Ações"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 7; // Apenas coluna de ações é editável
                }
            };
        } else {
            // User vê colunas reduzidas (sem Role, Status e sem coluna de exclusão)
            modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome", "Sobrenome", "Email", "Telefone", "Ações"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 5; // Apenas coluna de ações é editável
                }
            };
        }
        tabela.setModel(modeloTabela);
        tabela.setRowHeight(30);

        // Adicionar botões na coluna de ações
        int acaoColuna = Sessao.isAdmin() ? 7 : 5;
        tabela.getColumnModel().getColumn(acaoColuna).setCellRenderer(new ButtonRenderer());
        tabela.getColumnModel().getColumn(acaoColuna).setCellEditor(new ButtonEditor(new JCheckBox(), this));
    }

    public void listar() {
        modeloTabela.setRowCount(0);

        PessoaDAO dao = new PessoaDAO();
        List<Pessoa> pessoas;

        if (Sessao.isAdmin()) {
            pessoas = dao.listar();
        } else {
            pessoas = dao.listarApenasUsers();
        }

        for (Pessoa p : pessoas) {
            if (Sessao.isAdmin()) {
                modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getSobrenome(),
                    p.getEmail(),
                    p.getTelefone(),
                    p.getRole(),
                    p.getStatus() == 1 ? "Ativo" : "Inativo",
                    "Ações"
                });
            } else {
                modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getSobrenome(),
                    p.getEmail(),
                    p.getTelefone(),
                    "Ações"
                });
            }
        }
    }

    public void editarUsuario(int id) {
        // Verificar se USER está tentando editar um admin
        if (Sessao.isUser()) {
            Pessoa pessoa = new PessoaDAO().buscarPorId(id);
            if (pessoa != null && "admin".equals(pessoa.getRole())) {
                JOptionPane.showMessageDialog(this,
                    "Você não tem permissão para editar administradores!",
                    "Acesso Negado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        this.dispose();
        new TelaEdicao(id).setVisible(true);
    }

    public void excluirUsuario(int id) {
        // Apenas admin pode excluir
        if (!Sessao.isAdmin() || !Sessao.temPermissao("EXCLUIR_DADOS")) {
            JOptionPane.showMessageDialog(this,
                "Você não tem permissão para excluir usuários!",
                "Acesso Negado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir este usuário?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            new PessoaDAO().deletar(id);
            listar();
            JOptionPane.showMessageDialog(this,
                "Usuário excluído com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirTelaNovoUsuario() {
        this.dispose();
        new TelaEdicao(0).setVisible(true);
    }

    private void realizarLogout() {
        Sessao.logout();
        this.dispose();
        new TelaLogin().setVisible(true);
    }

    // Classe para renderizar botões na tabela
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnEditar = new JButton("Editar");
        private JButton btnExcluir = new JButton("Deletar");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
            if (Sessao.temPermissao("ATUALIZAR_DADOS")) {
                add(btnEditar);
            }
            if (Sessao.isAdmin() && Sessao.temPermissao("EXCLUIR_DADOS")) {
                add(btnExcluir);
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Classe para editar/excluir na tabela
    class ButtonEditor extends DefaultCellEditor {
        private JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        private JButton btnEditar = new JButton("Editar");
        private JButton btnExcluir = new JButton("Deletar");
        private int idSelecionado;
        private TelaListagem tela;

        public ButtonEditor(JCheckBox checkBox, TelaListagem tela) {
            super(checkBox);
            this.tela = tela;

            if (Sessao.temPermissao("ATUALIZAR_DADOS")) {
                painel.add(btnEditar);
                btnEditar.addActionListener(e -> {
                    fireEditingStopped();
                    tela.editarUsuario(idSelecionado);
                });
            }

            if (Sessao.isAdmin() && Sessao.temPermissao("EXCLUIR_DADOS")) {
                painel.add(btnExcluir);
                btnExcluir.addActionListener(e -> {
                    fireEditingStopped();
                    tela.excluirUsuario(idSelecionado);
                });
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            idSelecionado = (int) table.getValueAt(row, 0);
            return painel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ações";
        }
    }
}
