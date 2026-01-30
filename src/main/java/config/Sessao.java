/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import dao.UsuarioPermissaoDAO;
import model.Pessoa;
import java.util.List;

/**
 * Classe para gerenciar a sessão do usuário logado
 */
public class Sessao {

    private static Pessoa usuarioLogado;
    private static List<String> permissoes;

    public static void setUsuarioLogado(Pessoa usuario) {
        usuarioLogado = usuario;
        if (usuario != null) {
            permissoes = new UsuarioPermissaoDAO().listarPermissoesDoUsuario(usuario.getId());
        } else {
            permissoes = null;
        }
    }

    public static Pessoa getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }

    public static boolean isAdmin() {
        return usuarioLogado != null && "admin".equals(usuarioLogado.getRole());
    }

    public static boolean isUser() {
        return usuarioLogado != null && "user".equals(usuarioLogado.getRole());
    }

    public static boolean temPermissao(String nomePermissao) {
        return permissoes != null && permissoes.contains(nomePermissao);
    }

    public static List<String> getPermissoes() {
        return permissoes;
    }

    public static void logout() {
        usuarioLogado = null;
        permissoes = null;
    }
}
