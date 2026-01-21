package view;

import dao.PessoaDAO;
import model.Pessoa;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormCRUD extends JFrame {

    private JTextField txtId, txtNome, txtSobrenome, txtEmail, txtTelefone;
    private JTable tabela;
    private JButton btnSalvar, btnAtualizar, btnExcluir, btnLimpar;

    public FormCRUD() {
        setTitle("CRUD Pessoa");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        listar();
    }

    private void initComponents() {

        JPanel painelCampos = new JPanel();
        painelCampos.setLayout(new BoxLayout(painelCampos, BoxLayout.Y_AXIS));

        txtId = new JTextField();
        txtId.setEnabled(false);

        txtNome = new JTextField();
        txtSobrenome = new JTextField();
        txtEmail = new JTextField();
        txtTelefone = new JTextField();

        painelCampos.add(new JLabel("ID"));
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome"));
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Sobrenome"));
        painelCampos.add(txtSobrenome);

        painelCampos.add(new JLabel("Email"));
        painelCampos.add(txtEmail);

        painelCampos.add(new JLabel("Telefone"));
        painelCampos.add(txtTelefone);

        btnSalvar = new JButton("Salvar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        painelCampos.add(painelBotoes);

        tabela = new JTable();
        tabela.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nome", "Sobrenome", "Email", "Telefone"}
        ));

        JScrollPane scroll = new JScrollPane(tabela);

        add(painelCampos, "West");
        add(scroll, "Center");

        // ===== EVENTOS =====

        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                preencherCampos();
            }
        });
    }

    private void salvar() {
        Pessoa p = new Pessoa();
        p.setNome(txtNome.getText());
        p.setSobrenome(txtSobrenome.getText());
        p.setEmail(txtEmail.getText());
        p.setTelefone(txtTelefone.getText());

        new PessoaDAO().salvar(p);
        listar();
        limparCampos();
    }

    private void atualizar() {
        if (txtId.getText().isEmpty()) return;

        Pessoa p = new Pessoa();
        p.setId(Integer.parseInt(txtId.getText()));
        p.setNome(txtNome.getText());
        p.setSobrenome(txtSobrenome.getText());
        p.setEmail(txtEmail.getText());
        p.setTelefone(txtTelefone.getText());

        new PessoaDAO().atualizar(p);
        listar();
        limparCampos();
    }

    private void excluir() {
        if (txtId.getText().isEmpty()) return;

        int id = Integer.parseInt(txtId.getText());
        new PessoaDAO().deletar(id);
        listar();
        limparCampos();
    }

    private void listar() {
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        List<Pessoa> pessoas = new PessoaDAO().listar();

        for (Pessoa p : pessoas) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getSobrenome(),
                p.getEmail(),
                p.getTelefone()
            });
        }
    }

    private void preencherCampos() {
        int linha = tabela.getSelectedRow();

        txtId.setText(tabela.getValueAt(linha, 0).toString());
        txtNome.setText(tabela.getValueAt(linha, 1).toString());
        txtSobrenome.setText(tabela.getValueAt(linha, 2).toString());
        txtEmail.setText(tabela.getValueAt(linha, 3).toString());
        txtTelefone.setText(tabela.getValueAt(linha, 4).toString());
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtSobrenome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
    }
}
