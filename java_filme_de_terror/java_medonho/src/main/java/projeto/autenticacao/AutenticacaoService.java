package projeto.autenticacao;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Cadastro, autenticação e bloqueio de contas (RF01, RF04 a RF07).
 * Os usuários ficam em memória (Map), sem banco de dados.
 *
 * Exceções usadas:
 *  - IllegalArgumentException: campo nulo/vazio ou senha fora das regras
 *  - NoSuchElementException:   usuário inexistente
 *  - SecurityException:        senha incorreta
 *  - IllegalStateException:    conta bloqueada
 */
public class AutenticacaoService {

    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final ValidacaoSenhaService validacaoSenha = new ValidacaoSenhaService();

    public Usuario cadastrar(String login, String senha, Usuario.Nivel nivel) {
        exigir(login, "login");
        exigir(senha, "senha");
        if (nivel == null) {
            throw new IllegalArgumentException("Campo obrigatório: nivel");
        }
        if (!validacaoSenha.validarSenha(senha)) {
            throw new IllegalArgumentException("Senha fora das regras de tamanho e composição");
        }
        Usuario usuario = new Usuario(login, senha, nivel);
        usuarios.put(login, usuario);
        return usuario;
    }

    public Usuario autenticar(String login, String senha) {
        exigir(login, "login");
        exigir(senha, "senha");

        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new NoSuchElementException("Usuário inexistente");
        }

        if (usuario.isBloqueado()) {
            throw new IllegalStateException("Conta bloqueada");
        }

        if (!usuario.getSenha().equals(senha)) {
            usuario.registrarTentativaInvalida();
            throw new SecurityException("Senha incorreta");
        }

        usuario.zerarTentativas();
        return usuario;
    }

    public Usuario buscar(String login) {
        return usuarios.get(login);
    }

    private void exigir(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório: " + campo);
        }
    }
}
