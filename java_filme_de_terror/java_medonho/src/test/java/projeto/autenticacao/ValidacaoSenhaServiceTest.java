package projeto.autenticacao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ValidacaoSenhaServiceTest {

    private ValidacaoSenhaService service;

    @BeforeEach
    void setUp() {
        service = new ValidacaoSenhaService();
    }

    @Test
    void senhaValida() { // CT01
        String senha = "Java@123456";
        Assertions.assertEquals(11, senha.length());
        Assertions.assertTrue(service.validarSenha(senha));
    }

    @Test
    void senhaNula() { // CT07
        Assertions.assertFalse(service.validarSenha(null));
    }

    @Test
    void senhaVazia() { // CT08
        Assertions.assertFalse(service.validarSenha(""));
    }

    @Test
    void senhaCom9Caracteres() { // CT02 - fora do limite
        String senha = "Java@1234";
        Assertions.assertEquals(9, senha.length());
        Assertions.assertFalse(service.validarSenha(senha));
    }

    @Test
    void senhaCom10Caracteres() { // CT09 - fronteira inferior
        String senha = "Java@12345";
        Assertions.assertEquals(10, senha.length());
        Assertions.assertTrue(service.validarSenha(senha));
    }

    @Test
    void senhaCom11Caracteres() {
        String senha = "Java@123456";
        Assertions.assertEquals(11, senha.length());
        Assertions.assertTrue(service.validarSenha(senha));
    }

    @Test
    void senhaCom12Caracteres() { // CT10 - fronteira superior
        String senha = "Java@1234567";
        Assertions.assertEquals(12, senha.length());
        Assertions.assertTrue(service.validarSenha(senha));
    }

    @Test
    void senhaCom13Caracteres() { // CT03 - fora do limite
        String senha = "Java@12345678";
        Assertions.assertEquals(13, senha.length());
        Assertions.assertFalse(service.validarSenha(senha));
    }

    @Test
    void senhaSemNumero() { // CT04
        String senha = "Java@Testes";
        Assertions.assertEquals(11, senha.length());
        Assertions.assertFalse(service.validarSenha(senha));
    }

    @Test
    void senhaSemLetra() { // CT05
        String senha = "123456@789";
        Assertions.assertEquals(10, senha.length());
        Assertions.assertFalse(service.validarSenha(senha));
    }

    @Test
    void senhaSemEspecial() { // CT06
        String senha = "Java123456";
        Assertions.assertEquals(10, senha.length());
        Assertions.assertFalse(service.validarSenha(senha));
    }
}
