# Projeto Java + JUnit 5 – Autenticação (Caixa Branca)

Disciplina: Testes de Sistemas / Testes Automatizados
Tecnologias: Java 17 + Maven + JUnit 5 (Jupiter)
Projeto: autenticacao-junit5

## 1. Estrutura
```
autenticacao-junit5/
├── pom.xml
├── README.md
└── src/
    ├── main/java/projeto/autenticacao/
    │   ├── App.java
    │   ├── Usuario.java
    │   ├── ValidacaoSenhaService.java
    │   └── AutenticacaoService.java
    └── test/java/projeto/autenticacao/
        ├── ValidacaoSenhaServiceTest.java
        └── AutenticacaoServiceTest.java
```

## 2. IntelliJ IDEA
1. File > Open e escolha a pasta `autenticacao-junit5`.
2. Use o JDK 17 ou superior.
3. Clique em "Load Maven Changes".
4. Abra uma classe de teste, clique no ícone verde e escolha Run.

## 3. Maven
```
mvn test
mvn clean test
mvn clean package
```

## 3.1 Rodando o programa no terminal (App.java)
No IntelliJ, abra `App.java` e clique no ícone verde > Run 'App.main()'.
Digite as opções direto no console (aba Run):
```
1 - Cadastrar usuário   (login, senha e nível)
2 - Fazer login         (mostra: usuário válido/inválido e senha válida/inválida)
3 - Testar uma senha    (só mostra: senha válida/inválida)
0 - Sair
```
Os usuários ficam salvos apenas em memória: ao encerrar o programa, eles são perdidos.
Após 3 senhas erradas seguidas, a conta é bloqueada.

## 4. Resultado esperado
- ValidacaoSenhaServiceTest: 11 testes
- AutenticacaoServiceTest: 15 testes
- Total esperado: 26

```
[INFO] Tests run: 26, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
Confira o número real na sua execução.

## 5. Caminhos de validarSenha (caixa branca)
| Caminho | Condição | Teste |
|---|---|---|
| P1 | senha == null | senhaNula |
| P2 | senha vazia | senhaVazia |
| P3 | length < 10 | senhaCom9Caracteres |
| P4 | length > 12 | senhaCom13Caracteres |
| P5 | sem número | senhaSemNumero |
| P6 | sem letra | senhaSemLetra |
| P7 | sem especial | senhaSemEspecial |
| P8 | tudo válido | senhaValida, 10, 11 e 12 caracteres |

## 6. Exceções usadas
| Situação | Exceção |
|---|---|
| Campo nulo ou vazio / senha fraca no cadastro | IllegalArgumentException |
| Usuário inexistente | NoSuchElementException |
| Senha incorreta | SecurityException |
| Conta bloqueada | IllegalStateException |

## 7. Correção do enunciado
`Java@1234` tem 9 caracteres (não 10) e `Java@Test` também tem 9.
Por isso usei `Java@123456` (11) como senha válida e `Java@Testes` (11) como "sem número".
Os testes conferem o tamanho com `assertEquals`.

## 8. Como provocar uma falha
Em `ValidacaoSenhaService`, troque `senha.length() > 12` por `senha.length() >= 12` e execute.
O teste `senhaCom12Caracteres` deve falhar. Depois desfaça a alteração.

## 9. Limitações
- Caracteres especiais aceitos: `! @ # $ % & * ( )` (do código base; o RF03 não define).
- RF06 "erro de conexão com o banco": não há banco neste projeto.
- RF07: desbloqueio não especificado, não implementado.
- RF08: só a existência dos níveis é testada.
