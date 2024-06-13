# Desafio Serasa Experian

## Visão Geral

O Desafio Serasa Experian tem como objetivo desenvolver um serviço robusto para o cadastro de pessoas, integrando informações de score e endereço. O sistema é construído em Java 17, utilizando os frameworks Spring Boot 3.1.1 e Spring Security 6. A segurança é reforçada com a autenticação via token JWT, e as permissões são gerenciadas pelos perfis ADMIN e USER.

## Funcionalidades Principais

### Cadastro de Pessoas

Usuários com perfil ADMIN podem criar registros de pessoas, fornecendo o CEP para obter automaticamente os dados de endereço por meio de uma API externa (viaCEP).

### Listagem Paginada

Todos os perfis de usuário (ADMIN e USER) podem acessar a funcionalidade de listar pessoas de forma paginada. Filtros por nome, idade e CEP estão disponíveis.

### Atualização e Exclusão Lógica

A atualização e exclusão lógica de registros são restritas aos usuários com perfil ADMIN.

### Descrição do Score

O serviço implementa uma lógica para associar uma descrição ao score da pessoa com base em uma tabela predefinida, como mostrada abaixo:

| scoreDescricao  | inicial | final | 
| --------------- | ------- | ----- |
| Insuficiente    | 0       | 200   |
| Inaceitável     | 201     | 500   |
| Aceitável       | 501     | 700   |
| Recomendável    | 701     | 1000  |

## Premissas Técnicas

### Linguagem e Ferramentas

- **Linguagem:** Java 17
- **Build:** Maven
- **Frameworks:**
    - Spring Boot 3.1.1
    - Spring Security 6
- **Banco de Dados:** H2 e Redis (em memória)
- **Documentação API:** Swagger
- **Autenticação:** Token JWT
- **Autorização:** Perfis ADMIN e USER

# Estrutura do Projeto

## Pacotes Principais:

- **controllers:** Gerencia as solicitações HTTP.
- **database:** Inicializa o serviço com um usuário padrão, como login: admin e senha: admin.
- **documentacao:** Gerencia os dados e segurança no Swagger para Documentação API.
- **domain:** Contém classes e DTOs armazenados no banco.
- **exceptions:** Handler de exceções personalizadas.
- **interfaces:** Contém os contratos de funções para boas práticas de desenvolvimento.
- **repositories:** Acesso ao banco de dados e interação com as entidades.
- **security:** Configuração de segurança, incluindo autenticação JWT e controle de acesso com base em roles.
- **services:** Lógica de negócios e serviços relacionados ao cadastro e score.
- **utils:** Utilitários e métodos auxiliares.

## Recursos Adicionais:

- **resources:** Arquivos de configuração do projeto, contendo certificado .jks para ambiente de produção, e, um application.yml genérico acessando por profile para configurações específicas para ambientes de produção, teste e desenvolvimento.
- **collection:** Collection em .json que poderá ser importado no Postman com as requests prontas para teste.
- **documentacao:** Documentação da API a partir do Swagger em .json e .yml.
- **diagrama:** Duas imagens de diagrama de sequência mostrando os passos de como é a parte de arquitetura de uma autenticação JWT e como é o processo de requests dos outros serviços por meio do token.

## Configuração e Execução

### Configuração:

1. **Teste:** Defina variáveis de ambiente na sua IDE de teste na posta 8080, como `profile=test;my-secret-key=my-secret-key;password-admin=admin;`.
2. **Produção:** Defina variáveis de ambiente de produção na sua IDE na porta 8443, como `profile=prod;my-secret-key=my-secret-key;password-admin=admin;`.
3. **Docker:** Defina variáveis de ambiente de teste no seu docker (teste ou produção), como `profile=test;my-secret-key=my-secret-key;password-admin=admin;`.

### Execução:

1. Execute o comando para instalar as dependências (Observação: como já irá executar o ambiente de teste, ou seja, os testes unitários, será necessário colocar as variáveis de ambiente juntamente ao comando do maven): `mvn clean install -Dprofile=test -Dmy-secret-key=my-secret-key -Dpassword-admin=admin`.
2. Inicie o serviço na sua IDE rodando o arquivo `SystemApplication` (Observação: seguir o passo de configuração acima de configuração de variáveis de ambiente).
3. Após rodar a aplicação, você pode acessar os serviços por meio da collection disponibilizada usando o Postman ou entrando por meio do [Link para acessar o Swagger localmente](http://localhost:8080/swagger-ui/index.html).
4. Para rodar o Dockerfile, gere primeiro a imagem, por meio do comando `docker build -t imagem-docker-desafio:tag -f Dockerfile .`.
5. Após gerar a imagem, pode rodar a imagem docker pelo comando `docker run -p 9090:8080 --name container-docker-desafio -d imagem-docker-desafio:tag`.

## Contribuições

Contribuições são bem-vindas! Abra issues, proponha novas funcionalidades ou envie pull requests para aprimorar o sistema.

## Contato

Para mais informações ou dúvidas, entre em contato com [rayanabonfanti@gmail.com].
