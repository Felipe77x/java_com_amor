package projeto.autenticacao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class AutenticacaoServiceTest {

    private static final String LOGIN = "ana";
    private static final String SENHA = "Java@123456";
    private static final String ERRADA = "Errada@1234";

    private AutenticacaoService service;

    @BeforeEach
    void setUp() {
        // serviço novo a cada teste: nenhum teste herda o bloqueio do outro
        service = new AutenticacaoService();
        service.cadastrar(LOGIN, SENHA, Usuario.Nivel.CLIENTE);
    }

    private void loginInvalido() {
        Assertions.assertThrows(SecurityException.class,
                () -> service.autenticar(LOGIN, ERRADA));
    }

    // ---------- Autenticação (RF04, RF05, RF06) ----------

    @Test
    void aut01UsuarioESenhaValidos() {
        Usuario usuario = service.autenticar(LOGIN, SENHA);
        Assertions.assertEquals(LOGIN, usuario.getLogin());
    }

    @Test
    void aut02UsuarioInexistente() {
        NoSuchElementException erro = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> service.autenticar("fantasma", SENHA)
        );
        Assertions.assertEquals("Usuário inexistente", erro.getMessage());
    }

    @Test
    void aut03SenhaIncorreta() {
        SecurityException erro = Assertions.assertThrows(
                SecurityException.class,
                () -> service.autenticar(LOGIN, ERRADA)
        );
        Assertions.assertEquals("Senha incorreta", erro.getMessage());
    }

    @Test
    void aut04UsuarioNulo() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.autenticar(null, SENHA));
    }

    @Test
    void aut05SenhaNula() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.autenticar(LOGIN, null));
    }

    @Test
    void aut06UsuarioVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.autenticar("", SENHA));
    }

    @Test
    void aut07SenhaVazia() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.autenticar(LOGIN, ""));
    }

    // ---------- Bloqueio (RF07) ----------

    @Test
    void aut08UsuarioBloqueadoNaoAutentica() {
        service.buscar(LOGIN).bloquear();
        IllegalStateException erro = Assertions.assertThrows(
                IllegalStateException.class,
                () -> service.autenticar(LOGIN, SENHA)
        );
        Assertions.assertEquals("Conta bloqueada", erro.getMessage());
    }

    @Test
    void aut09PrimeiraTentativaInvalidaNaoBloqueia() {
        loginInvalido();
        Assertions.assertFalse(service.buscar(LOGIN).isBloqueado());
    }

    @Test
    void aut10SegundaTentativaInvalidaNaoBloqueia() {
        loginInvalido();
        loginInvalido();
        Assertions.assertFalse(service.buscar(LOGIN).isBloqueado());
    }

    @Test
    void aut11TerceiraTentativaInvalidaBloqueia() {
        loginInvalido();
        loginInvalido();
        loginInvalido();
        Assertions.assertTrue(service.buscar(LOGIN).isBloqueado());
    }

    @Test
    void aut12TentativaAposBloqueioRejeitadaMesmoComSenhaCorreta() {
        loginInvalido();
        loginInvalido();
        loginInvalido();
        Assertions.assertThrows(IllegalStateException.class,
                () -> service.autenticar(LOGIN, SENHA));
    }

    // ---------- Cadastro (RF01) e níveis (RF08) ----------

    @Test
    void cadastroComLoginVazio() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("", SENHA, Usuario.Nivel.CLIENTE));
    }

    @Test
    void cadastroComSenhaFraca() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("bia", "Java123456", Usuario.Nivel.CLIENTE));
    }

    @Test
    void niveisDeUsuario() {
        service.cadastrar("adm", SENHA, Usuario.Nivel.ADMIN);
        service.cadastrar("ger", SENHA, Usuario.Nivel.GERENTE);

        Assertions.assertEquals(Usuario.Nivel.ADMIN, service.autenticar("adm", SENHA).getNivel());
        Assertions.assertEquals(Usuario.Nivel.GERENTE, service.autenticar("ger", SENHA).getNivel());
        Assertions.assertEquals(Usuario.Nivel.CLIENTE, service.autenticar(LOGIN, SENHA).getNivel());
    }
}
