# Byte Bank - JDBC

## Java e JDBC: trabalhando com um banco de dados

Neste curso irei aprender a utilizar o JDBC em projetos Java com o intuito de entender melhor o que acontece por baixo dos panos quando utilizamos o JDBC por meio de algum outro pacote ou framework.

### Aula 1

#### Conectando com o banco

Nesta aula aprendi a fazer uma conexão com o banco de dados utilizando as classes "DriverManager" e "Connection" do java.sql, pacote que vem com a dependência do driver escolhido do JDBC.

Também pude relembrar um pouco de como funciona o design pattern "Factory", pois iremos utilizar esse padrão no curso, tendo em vista que precisaremos abrir e fechar conexões para diversas ações de persistência dos dados no nosso projeto.

### Aula 2

#### Persistindo contas

Nessa aula aprendi a criar SQL Statements utilizando a classe "PreparedStatement", que nos fornece métodos para inserirmos valores em lugares específicos que marcamos com o sinal "?", assim evitando brechas para ataques de SQL Injection, coisa que não conseguimos evitar muito bem utilizando a classe "Statement".

Também tive o meu primeiro contato com uma classe DAO, que eu já sabia no conceito, mas nunca havia utilizado antes, por conta de só ter feito integrações com banco de dados utilizando o Spring Boot, que utiliza o padrão repository no lugar dos DAOs.

### Aula 3

#### Listando contas e connection pool

Nesta aula aprendi a utilizar o método "executeQuery()" do PreparedStatement para executar queries de busca e conseguir ter o retorno dos registros no banco de dados por meio de um ResultSet, que funciona de maneira diferente de um HashSet, mas até que é intuitivo.

Também aprendi o conceito de connection pool, coisa que eu ainda não havia visto e fiquei bastante impressionado com o poder que ele tem. Finalmente entendi de onde que vem o tal do HikariPool e aprendi a criar a minha própria connection pool com o HikariCP.
