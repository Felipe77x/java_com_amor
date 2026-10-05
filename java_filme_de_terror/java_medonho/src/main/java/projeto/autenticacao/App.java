package projeto.autenticacao;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Classe principal: menu no terminal para cadastrar usuário e fazer login.
 * Os usuários ficam salvos em memória enquanto o programa está rodando.
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        AutenticacaoService service = new AutenticacaoService();
        ValidacaoSenhaService validacao = new ValidacaoSenhaService();

        int opcao = -1;
        while (opcao != 0) {
            System.out.println();
            System.out.println("===== MENU =====");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Fazer login");
            System.out.println("3 - Testar uma senha");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            String texto = entrada.nextLine().trim();
            opcao = texto.matches("\\d") ? Integer.parseInt(texto) : -1;

            switch (opcao) {
                case 1 -> cadastrar(entrada, service, validacao);
                case 2 -> login(entrada, service);
                case 3 -> testarSenha(entrada, validacao);
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrar(Scanner entrada, AutenticacaoService service,
                                  ValidacaoSenhaService validacao) {
        System.out.print("Novo usuário (login): ");
        String login = entrada.nextLine();
        System.out.print("Senha (10 a 12 caracteres, letra, número e especial !@#$%&*()): ");
        String senha = entrada.nextLine();
        System.out.print("Nível (1 = ADMIN, 2 = GERENTE, 3 = CLIENTE): ");
        String nivelTexto = entrada.nextLine().trim();

        Usuario.Nivel nivel = switch (nivelTexto) {
            case "1" -> Usuario.Nivel.ADMIN;
            case "2" -> Usuario.Nivel.GERENTE;
            case "3" -> Usuario.Nivel.CLIENTE;
            default -> null;
        };

        if (validacao.validarSenha(senha)) {
            System.out.println("Senha válida");
        } else {
            System.out.println("Senha inválida");
        }

        if (login != null && !login.isBlank() && service.buscar(login) != null) {
            System.out.println("Usuário inválido: esse login já existe.");
            return;
        }

        try {
            service.cadastrar(login, senha, nivel);
            System.out.println("Usuário válido. Cadastro realizado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro não realizado: " + e.getMessage());
        }
    }

    private static void login(Scanner entrada, AutenticacaoService service) {
        System.out.print("Usuário: ");
        String login = entrada.nextLine();
        System.out.print("Senha: ");
        String senha = entrada.nextLine();

        try {
            Usuario usuario = service.autenticar(login, senha);
            System.out.println("Usuário válido");
            System.out.println("Senha válida");
            System.out.println("Acesso liberado. Nível: " + usuario.getNivel());
        } catch (NoSuchElementException e) {
            System.out.println("Usuário inválido (não cadastrado).");
        } catch (SecurityException e) {
            Usuario usuario = service.buscar(login);
            System.out.println("Usuário válido");
            System.out.println("Senha inválida (tentativa " + usuario.getTentativasInvalidas() + " de 3)");
            if (usuario.isBloqueado()) {
                System.out.println("Conta bloqueada após 3 tentativas inválidas.");
            }
        } catch (IllegalStateException e) {
            System.out.println("Usuário válido, mas a conta está bloqueada. Acesso negado.");
        } catch (IllegalArgumentException e) {
            System.out.println("Usuário ou senha vazio: " + e.getMessage());
        }
    }

    private static void testarSenha(Scanner entrada, ValidacaoSenhaService validacao) {
        System.out.print("Digite a senha: ");
        String senha = entrada.nextLine();
        if (validacao.validarSenha(senha)) {
            System.out.println("Senha válida");
        } else {
            System.out.println("Senha inválida");
        }
    }
}
