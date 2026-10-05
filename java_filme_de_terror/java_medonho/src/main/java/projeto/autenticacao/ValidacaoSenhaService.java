package projeto.autenticacao;

/**
 * Regras de tamanho e composição da senha (RF02 e RF03).
 */
public class ValidacaoSenhaService {

    public boolean validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            return false;
        }

        if (senha.length() < 10 || senha.length() > 12) {
            return false;
        }

        boolean possuiNumero =
                senha.matches(".*\\d.*");

        boolean possuiLetra =
                senha.matches(".*[a-zA-Z].*");

        boolean possuiEspecial =
                senha.matches(".*[!@#$%&*()].*");

        return possuiNumero &&
               possuiLetra &&
               possuiEspecial;
    }
}
