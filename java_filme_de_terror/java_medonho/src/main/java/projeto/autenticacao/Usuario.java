package projeto.autenticacao;

/**
 * Dados do usuário, nível de acesso (RF08) e controle de tentativas (RF07).
 */
public class Usuario {

    public enum Nivel { ADMIN, GERENTE, CLIENTE }

    private final String login;
    private final String senha;
    private final Nivel nivel;
    private int tentativasInvalidas;
    private boolean bloqueado;

    public Usuario(String login, String senha, Nivel nivel) {
        this.login = login;
        this.senha = senha;
        this.nivel = nivel;
    }

    public void registrarTentativaInvalida() {
        tentativasInvalidas++;
        if (tentativasInvalidas >= 3) {
            bloqueado = true;
        }
    }

    public void zerarTentativas() {
        tentativasInvalidas = 0;
    }

    public void bloquear() {
        bloqueado = true;
    }

    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public Nivel getNivel() { return nivel; }
    public boolean isBloqueado() { return bloqueado; }
    public int getTentativasInvalidas() { return tentativasInvalidas; }
}
