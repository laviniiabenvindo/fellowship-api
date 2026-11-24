# Relatório: Testes automatizados, aplicação executável, Docker e observabilidade em uma API Spring Boot

**Aluno(a):** Lavínia Benvindo
**Disciplina / Professor(a):** Luiz Fernando  
**Repositório GitHub:** https://github.com/laviniiabenvindo/fellowship-api.git

> Legenda: 📚 = pesquisa conceitual | 🧪 = testado por mim na minha API

## 1. Introdução
A API não deve depender das configurações e dos serviços que existem só no computador de quem desenvolveu. Para funcionar em outros ambientes, ela precisa ser empacotada, receber as configurações de cada ambiente com segurança e ter suas dependências — como o banco de dados — disponíveis. Testes, persistência dos dados e formas de acompanhar a saúde da aplicação também ajudam a deixá-la mais confiável. A Fellowship API é uma API REST feita com Spring Boot que permite cadastrar, listar, buscar, atualizar e excluir personagens de O Senhor dos Anéis, armazenando os dados no PostgreSQL.

## 2. Testes automatizados
Olhe: `PersonagemServiceTest.java`, `PersonagemControllerTest.java`

1. O que é um teste automatizado? É um código que verifica sozinho se uma parte do sistema está funcionando como deveria. Assim, dá para repetir as mesmas verificações sempre que o projeto muda.
2. Diferença entre teste manual e automatizado: No manual, uma pessoa executa os passos e confere o resultado. No automatizado, o próprio teste faz isso, o que costuma ser mais rápido e fácil de repetir.
3. O que são Arrange, Act e Assert? São as três partes comuns de um teste: Arrange prepara o cenário, Act executa o que será testado e Assert confere o resultado. No `deveCadastrarPersonagemValido`, o Arrange cria o Frodo e configura o mock do repository; o Act chama `service.cadastrar(frodo)`; e o Assert confere o nome, verifica se ele é portador do anel e confirma que `save` foi chamado.

## 3. JUnit e Mockito
4. Função do JUnit: É a ferramenta que organiza e executa os testes Java, além de oferecer recursos para conferir se os resultados estão corretos.
5. Função do Mockito: Ele ajuda a criar dependências falsas para os testes e a controlar como elas devem se comportar.
6. O que é um Mock? É uma versão simulada de uma dependência real, usada para testar uma parte do sistema sem precisar chamar, por exemplo, um banco de dados ou outro serviço.
7. Por que o teste de Service não deve depender do PostgreSQL? Porque a ideia é testar a lógica do Service de forma isolada. Sem depender do banco, o teste roda mais rápido e não precisa de uma configuração externa para funcionar.
8. Diferença entre `@Mock` e `@InjectMocks`: `@Mock` cria uma dependência simulada. Já `@InjectMocks` cria a classe que será testada e injeta nela os mocks necessários.
9. Para que servem `when().thenReturn()`, `verify()` e `assertThrows()`: `when().thenReturn()` define o resultado que um mock deve devolver; `verify()` confere se uma chamada aconteceu; e `assertThrows()` verifica se o código lançou a exceção esperada.

### Exemplo comentado de teste de Service
_(Cole um teste do projeto e explique cada parte com suas palavras: preparação, mock, execução, asserção, verify.)_

## 4. Testes de Service e de Controller
10. Diferença entre teste de Service e de Controller: O teste de Service foca nas regras de negócio. O de Controller verifica a comunicação HTTP, como as rotas, os dados enviados e os códigos de resposta.
11. Para que serve o MockMvc: Ele permite testar endpoints de uma aplicação Spring MVC sem precisar iniciar um servidor web de verdade.
12. Por que 100% de cobertura não garante um sistema correto: Cobertura só mostra quais partes do código foram executadas pelos testes. Ela não garante que os testes verificaram todos os comportamentos importantes nem que as regras estão certas.

**Resultado da execução (`mvn test`):** _(print + legenda)_

## 5. Build e JAR
1. O que é build? É o processo que prepara o projeto para uso: compila o código, executa as etapas configuradas e gera os arquivos da aplicação.
2. O que `mvn clean package` executa? `clean` remove os arquivos gerados em builds anteriores. Depois, `package` compila o projeto, executa os testes configurados e empacota a aplicação, normalmente em um JAR.
3. O que é um JAR? É um arquivo que reúne classes e recursos de uma aplicação Java para facilitar a distribuição e a execução.
4. Como executar sem o IntelliJ? Depois de gerar o JAR, dá para executá-lo pelo terminal com `java -jar target/nome-do-arquivo.jar`, usando o nome correto do arquivo gerado.

**Evidência 🧪:** _(print do `java -jar target/aplicacao.jar` + legenda)_

## 6. Configuração por ambiente
5. Por que segredos não ficam no código? Porque o código pode ser compartilhado ou enviado para um repositório. Se uma senha ou chave for junto, outras pessoas podem ter acesso a recursos que deveriam ser protegidos.
6. Como variáveis de ambiente ajudam? Elas deixam configurações, principalmente as sensíveis, fora do código e permitem trocar os valores conforme o ambiente. Neste projeto, o `application.properties` lê `DB_URL`, `DB_USER` e `DB_PASSWORD`; a URL tem um valor padrão para desenvolvimento, enquanto usuário e senha são fornecidos pelo ambiente.
7. O que são profiles do Spring? São configurações diferentes para cada ambiente, como desenvolvimento e produção. Com elas, a aplicação pode usar valores apropriados para cada situação sem misturar essas configurações.

## 7. Docker e Docker Compose
8. Imagem x container: A imagem é o pacote com o que o Docker precisa para criar a aplicação. O container é uma instância dessa imagem em execução.
9. Função de `FROM`, `WORKDIR`, `COPY`, `ENTRYPOINT`: _(explique o Dockerfile do projeto linha a linha)_
10. Para que serve o Docker Compose? Ele facilita subir serviços que precisam funcionar juntos usando um único arquivo de configuração. Neste `compose.yaml`, ele inicia o PostgreSQL (`db`) e a API (`api`), passa as configurações do banco por variáveis de ambiente, espera o banco ficar saudável antes de iniciar a API e mantém os dados do PostgreSQL no volume `fellowship-pgdata`.
11. Por que não usar `localhost` para outro container? Dentro de um container, `localhost` aponta para ele mesmo, não para os outros. No Compose, a aplicação deve usar o nome do serviço do outro container para se conectar a ele.
12. Para que servem volumes? Eles guardam dados fora do ciclo de vida do container. Assim, dados importantes, como os do banco, podem continuar existindo mesmo que o container seja removido.

**Evidência 🧪:** _(print do `docker compose ps` + legenda)_

## 8. Logs e Actuator
13. Função dos logs: Eles registram o que acontece na aplicação e ajudam a entender erros e acompanhar seu funcionamento.
14. O que não deve aparecer nos logs: Senhas, tokens, chaves secretas e dados pessoais ou sensíveis que não sejam necessários.
15. Para que serve o Actuator: É um recurso do Spring Boot que disponibiliza informações e endpoints para acompanhar a aplicação e verificar seu estado.
16. O que representa `/actuator/health`: É um endpoint que informa a condição de saúde da aplicação e, conforme a configuração, de dependências como o banco de dados. Um `UP` indica que as verificações configuradas passaram naquele momento.

**Evidência 🧪:** _(print do health com status UP + legenda)_

## 9. Solução do estudo de caso
| Item | Problema que resolve | Como foi aplicado | Cuidados |
|---|---|---|---|
| Testes de Service e Controller | | | |
| Geração do JAR | | | |
| Variáveis de ambiente | | | |
| Dockerfile | | | |
| Docker Compose com PostgreSQL | | | |
| Volume persistente | | | |
| Logs | | | |
| Actuator / health | | | |

## 10. Questões de verificação
- Qual dependência deve ser simulada ao testar um Service? Normalmente, as dependências do Service, como o Repository. Assim, dá para testar a lógica sem acessar o banco de verdade.
- O que acontece quando o Mock não tem comportamento configurado? Ele retorna o valor padrão do tipo; por exemplo, `null` para muitos objetos. Por isso, quando o teste precisa de um resultado específico, é bom configurar o mock com `when(...).thenReturn(...)`.
- Diferença entre `assertEquals` e `verify`: `assertEquals` compara valores esperados e obtidos. `verify` confere se uma chamada foi feita em uma dependência simulada.
- Um teste unitário deve iniciar toda a aplicação? Em geral, não. A ideia é testar uma unidade isolada, sem subir o contexto inteiro do Spring nem depender de serviços externos.
- O que o Maven coloca em `target`? Os arquivos gerados durante o build, como classes compiladas, relatórios de teste e, quando o empacotamento termina, o JAR da aplicação.
- Por que o JAR roda sem a IDE? Porque ele reúne a aplicação em um pacote executável; basta ter uma versão compatível do Java instalada e iniciar o arquivo pelo terminal.
- Por que não colocar segredos no Dockerfile? Porque eles podem ficar gravados na imagem ou aparecer no histórico do repositório, e acabar expostos. É mais seguro fornecê-los por configuração do ambiente.
- O que `localhost` representa dentro do container da API? O próprio container da API. Para acessar o PostgreSQL no Compose, a aplicação usa o nome do serviço do banco, `db`.
- Diferença entre parar e remover um container: Parar interrompe sua execução, mas mantém o container para poder iniciá-lo de novo. Remover apaga o container; volumes nomeados, como o do projeto, continuam separados dele.
- O que acontece com o banco se o container for removido sem volume? Os dados gravados apenas dentro do container são perdidos quando ele é removido. Um volume persistente evita essa perda.
- `UP` garante que as regras de negócio funcionam? Não. Quer dizer que as verificações de saúde configuradas passaram naquele momento; não prova que todos os fluxos e regras de negócio estão corretos.
- Por que configurar com cuidado os endpoints do Actuator? Porque eles podem revelar informações sobre a aplicação e seu ambiente. É importante expor só o necessário e proteger os endpoints adequadamente.

> Experimentos que ajudam (faça e registre): trocar `db` por `localhost` no `DB_URL`; `docker compose down` vs `down -v`; remover um `when(...)` de um teste; parar o banco e consultar o health.

## 11. Conclusão
Com este trabalho, entendi melhor como os testes automatizados ajudam a encontrar problemas e como separar os testes de Service dos testes de Controller. Também aprendi como gerar e executar o JAR, configurar a aplicação por variáveis de ambiente e usar o Docker Compose para iniciar a API junto com o PostgreSQL. Um ponto que exige atenção é a configuração da conexão entre os containers: dentro do Compose, a API precisa usar o nome do serviço do banco, e não `localhost`. Como melhoria, eu incluiria mais testes para os casos de erro e automatizaria a execução dos testes e do build a cada alteração no projeto.


## 12. Repositório e evidências
https://github.com/laviniiabenvindo/fellowship-api.git
