# Projeto Daca 2019.2  
  
# Lista pra mim  
  
O Lista pra mim©, é um app que vai revolucionar a forma como você faz compras. Quanto mais você usar o Lista pra mim, mais rápido será gerar novas listas de compras. E você ainda pode economizar anotando preços e locais de compra. Depois é só deixar o Lista pra mim© indicar o melhor lugar para você fazer suas compras.  
  
# Instalação  
  
**Você pode facilmente utilizar nossa URL da API https://listapramim-api.herokuapp.com** seguindo todos os endpoints desejados  
**ou** realizando todos os passos abaixo utilizando como rota o **localhost:8080**  
  
Inicialmente certifique-se em ter o Git Bash, Maven e a JDK Java instaladas em sua máquina com as devidas variáveis de ambiente configuradas no seu sistema operacional.  
Caso não esteja familiarizado com isso segue alguns guias abaixo.  
[Guia de configuração das variáveis no windows](https://www.mkyong.com/maven/how-to-install-maven-in-windows/)  
  
Clone nosso repositório com  
`git clone https://github.com/tsleolima/daca-project`  
  
Inicie o servidor com API REST com  
`mvn spring-boot:run`  
  
Utilize algum serviço para consumir a API fornecida, futuramente desenvolveremos uma GUI Web para o projeto, mas por enquanto aconselhamos o uso do [Postman](https://www.getpostman.com/)  
  
## Features  
  
Os produtos apresentam certas características:  
  
**Categoria**:  
  
- ALIMENTOSINDUSTRIALIZADOS  
- ALIMENTOSNAOINDUSTRIALIZADOS  
- LIMPEZA  
- HIGIENEPESSOAL  
  
**Tipo**:  
  
- QUANTIDADE_FIXA  
- NAO_INDUS_QUILO  
- INDUS_UNI  
  
A diferença dos tipos é que proutos com `QUANTIDADE_FIXA` possuem geralmente unidades de medida, como 350ml de Refrigerante, 1L de detergente, 1Kg de Feijão etc  
Para produtos com `NAO_INDUS_QUILO` temos produtos que na maior parte das vezes são não industrializados, ou que em sua forma de etiquetação seja baseada em quilo, como hortifruti ou produtos que seguem a mesma lógica.  
Para produtos com `INDUS_UNI` temos produtos que são industrializados e não se diferem da sua unidade de medida, apenas pela sua quantidade direta de compra, por exemplo: Sabonetes, Borrachas, Fio dental, enfim, nessa linha :)  
  
### O Lista pra mim possui funcionalidades como:  
  
- Cadastrar produtos  
- O produto possui nome, categoria, tipo, e um set de locais de venda do produto com preços  
  
- Cadastrar listas de compras  
- A lista possui um descritos, sua data de criação e um set de compras, produtos escolhidos pelo  
  
- Gerar listas de compras automaticamente  
- Geração automática por data, a ultima lista cadastrada  
- Geração automática por produto, a ultima lista que apresentava este produto  
- Geração automática baseando-se nos produtos mais apresentados nas listas cadastradas  
  
- Sugerir locais de venda baseados em uma determinada lista de compra  
- Apresenta uma lista de sugestões contendo o nome do estabelecimento, os produtos encontrados no local e o preço total da lista caso fosse realizada.  
  
- Os produtos seguem essas informações  
- Nome  
- Categoria  
- Tipo  
- Quantidade  
  
- As listas de compras seguem essas informações  
- Descritor da lista  
  
## EndPoints  
  
| Funcionalidade de Produtos | EndPoint |  
| ---------------------------- | ---------------------- |  
| GET dos produtos cadastrados | https://listapramim-api.herokuapp.com/produto |  
| GET em um produto especifico com seu id | https://listapramim-api.herokuapp.com/produto/{id} |  
| GET com produtos ordenados por nome | https://listapramim-api.herokuapp.com/produto/ordered |  
| GET com produtos ordenados por uma categoria | https://listapramim-api.herokuapp.com/produto/ordered/by?categoria={categoria} |  
| GET com produtos ordenados pelo preço | https://listapramim-api.herokuapp.com/produto/ordered/preco |  
| GET com produtos que possuem um determinado nome |https://listapramim-api.herokuapp.com/produto/search?nome={nome} |  
| POST do produto a ser adicionado | https://listapramim-api.herokuapp.com/produto/ |  
| PUT do produto inserindo seu id de busca e seu objeto atualizado | https://listapramim-api.herokuapp.com/produto/{id} |  
| DELETE do produto já cadastrado | https://listapramim-api.herokuapp.com/produto/{id} |  
  
| Funcionalidade de listas de compras | EndPoint |  
| ---------------------------- | ---------------------- |  
| GET das lidas de compras já cadastradas | https://listapramim-api.herokuapp.com/listacompra |  
| GET de uma lista de compra especifica com seu id | https://listapramim-api.herokuapp.com/listacompra/{id} |  
| POST da lista de compra a ser adicionado | https://listapramim-api.herokuapp.com/listacompra/ |  
| PUT da lista de compra inserindo seu id e seu objeto atualizado | https://listapramim-api.herokuapp.com/listacompra/{id} |  
| DELETE de uma lista de compras já cadastrada | https://listapramim-api.herokuapp.com/listacompra/{id} |  
| GET buscar lista a partir do seu descritor | https://listapramim-api.herokuapp.com/listacompra/search/descritor/{descritor} |  
| GET busca listas a partir de uma data a data segue o formato dd-mm-yyyy | https://listapramim-api.herokuapp.com/listacompra/search/data/{data} |  
| GET busca listas a partir de um id de um produto | https://listapramim-api.herokuapp.com/listacompra/search/idproduto/{idproduto} |  
| GET gera uma lista automaticamente baseando-se na ultima lista cadastrada | https://listapramim-api.herokuapp.com/listacompra/automatic/recent |  
| GET gera uma lista automaticamente baseando-se na ultima lista que possui o produto desejado | https://listapramim-api.herokuapp.com/listacompra/automatic/itemcompra/{idproduto} |  
| GET gera uma lista automaticamente baseando-se nas compras mais encontradas nas listas, a regra é os produtos que contemplam pelo menos metade das listas | https://listapramim-api.herokuapp.com/listacompra/automatic/maisfrequentes |  
| GET o lista pra mim sugere os estabelicimentos, dos quais foram utilizados para atualização produtos, que possuem pelo menos um produto da lista que deseja ser comprada, mostrando um pequeno relatório de local de venda e preço final da lista caso comprada no estabelicimento proposto | https://listapramim-api.herokuapp.com/listacompra/suggestion/{idlista} |  
  
## Usuário  
  
O sistema inclui autorização e autenticação de usuários, nele cada cliente pode realizar seu cadastro e seu login e desfrutar das funcionalidades individualmente, ou seja os produtos e listas de compras de uma pessoa não é vista e nem acessada por outra, por exemplo: Joãozinho não pode dar delete no produto de Mariazinha  
  
A segurança é feita utilizando JWT (Json Web Token), ou seja, a cada login realizado com sucesso o sistema irá retornar um token válido durante 1 hora para o usuário, podendo ser modificado acessando a classe *JwtTokenProvider*. Todas as requisiçôes realizadas exceto o POST para login e cadastro são bloqueadas respondendo um STATUS 401, o token dado em resposta ao login realizado deve ser utilizado no campo *Authorization* no Header para que as rotas anteriormente bloqueadas sejam utilizadas.  
  
A senha do usuário é criptografada com o bCryptPasswordEncoder assim que o mesmo realiza o seu registro no sistema.  
  
Um filtro é usado em cada requisição para garantir que as rotas sejam acessadas ou bloqueadas. Para serem acessadas, o token é validado e o usuário é recuperado a partir do token e adicionado no contexto de segurança da aplicação através do método *SecurityContextHolder.getContext().setAuthentication(auth)*  
  
O sistema também está implementado com roles, por default todo usuário inicialmente é ADMIN, caso o sistema evolua e seja necessária a distinção entre usuários, será realizado modificações em cima dessa implementação base.  
  
### Gráfico que mostra o fluxo de autenticação  
  
![](https://raw.githubusercontent.com/tsleolima/daca-project/master/assets/authentication.jpg)  
  
### Rotas para você realizar cadastro e login no sistema  
  
  
| Funcionalidade de usuario | EndPoint |  
| ------------------------- | -------- |  
| POST para realizar cadastro | https://listapramim-api.herokuapp.com/api/auth/register |  
| POST para realizar login | https://listapramim-api.herokuapp.com/api/auth/login | 

## Desempenho

 Os testes foram realizados utilizando a ferramenta Jmeter, no qual podemos escolher quantos usuarios realizarão requisições ao sistema em determinadas rotas de quanto em quanto tempo, para obtermos o melhor uso, e consequentemente a vazão dos dados, na máquina de desenvolvimento foram obtidos estes parâmetros:
 - **Numeros de Usúarios:** 90
 - **Numero de repitições por usuário:** 30

O resultado obtido pode ser verificado na imagem abaixo
![](https://raw.githubusercontent.com/tsleolima/daca-project/master/assets/desempenho.jpg)
 
Para poder visualizar com maior clareza o diferencia de tempo entre as requisições foi utilizado um *Thread.sleep(500)* fazendo com que o sistema demore no mínimo 0.5s para dar uma resposta a requisição pedida 
Pode se observar que as rotas com cache tiveram melhor êxito ao passar do tempo foram as que possuiam cache, entretanto vale observar que as rotas que possuem cache tiveram suas primeiras requisições em tempos grandes, devido ao fato da sua configuração. 

Mais testes realizados com o Jmeter com diversos gráficos voce pode encontrar aqui: 
https://htmlpreview.github.io/?https://github.com/tsleolima/daca-project/blob/master/relatorio-project-jmeter/index.html

## Tecnologias utilizadas para escalar a aplicação

 - **Reactor - Reactive Architecture**
 - **SQS AWS - Amazon Single Queue Service**
 

**Reactor - Reactive Architecture**
 
 	A Arquitetura reativa vem como uma solução para sistemas que até então ficavam bloqueados esperando uma resposta do servidor, e através do **Reactor** podemos construir uma aplicação reativa, que reage as solicitações feitas pelo client.
 	É importante notar que precisamos modificar nosso código para que o mesmo utilize dessas features, as modificações mais importantes feitas foram a do Spring Data, utilizando a api do **ReactiveMongoRepository**, no qual adicionamos dois novos conceitos o Flux e o Mono, que nada mais são do que fluxos de dados que nossa aplicação irá trabalhar. Outro ponto a ser destacado é o refatoramento do código para o paradigma funcional majoritariamente, pois só assim podemos trabalhar bem com os metodos disponíveis do **Reactor**.
 	Para simplificar o trabalho de desenvolvimento e testes da aplicação, os resultados produzidos pelas annotations da Rest ( GET, POST etc) estão definidos por default como Json, para que voce consuma essa API de forma reativa aconselho que utilize **(produces = MediaType.TEXT_EVENT_STREAM_VALUE)** para tal.
 	As classes refatoras com Flux/Mono e paradigma funcional se concentram apenas nos controllers e services, a utilização dessa tecnologia pelo usuário é idêntica a anteriormente mesmo com os bloqueios, o diferencial é a escalabilidade que a arquitetura nos proporciona, trazendo um fluxo de blocos de dados.
 	
 **SQS AWS - Amazon Single Queue Service**
 
 	O SQS da Amazon nada mais é do que um serviço de mensagens, ele consiste em permitir envios e recebimentos de mensagens controlados por uma fila, para utilizar esse serviço da Amazon foi utilizada outra tecnologia chamada **LocalStack**, com ele podemos utilizar os serviços da amazon localmente sem nos preocuparmos em adquirir um plano diretamente na amazon, além de podemos utilizá-lo mesmo offline.
 	Tenha paciência ao ler como eu tive para configurar isso na minha máquina, o LocalStack foi instalado usando `pip install localstack`, caso esteja no Windows como eu estive voce tem duas alternativas, utilizar o `pip install localstack[full]` ou instalar o docker ( oque funcionou no meu caso ), utilizei um arquivo .yml com as configurações necessarias para inicializar o localstack, [Arquivo de inicialização do serviço sqs usando docker](https://raw.githubusercontent.com/tsleolima/daca-project/master/assets/docker-compose.yml), esse arquivo foi utilizado usando o comando docker-compose up na pasta do projeto que contem o arquivo acima, instale esse carinha usando o npm :).
 	Estamos quase lá, o próximo passo é instalar o cli da aws para criar uma queue, enviar ou até mesmo visualizar as mensagens, caso necessite procure por configure aws cli, para configurar dados como, localização e tipo de retorno dos dados, por fim foi utilizado a rota:porta do sqs do localstack para manipulação incode nesse projeto, algumas delas se encontrar no **properties**.
 	A ideia utilizada para consumir esse serviço SQS foi a troca de senha do usuario cadastrado, um Post é realizado para **api/auth/changePassword** passando como query o email do usuario e uma nova senha.  
	
 	a| Funcionalidade de usuario | EndPoint |  
| ------------------------- | -------- |  
| POST para mudar a senha | https://listapramim-api.herokuapp.com/api/auth/changePassword?email={email}&newPass={newPass} | 
	
	
Apos realizar a requisição para a rota, veja em **AuthController**, uma mensagem é enviada ao serviço sqs com o email e senha desejados, apenas em uma linha, outro lado no **CustomUserDetailsService** temos uma configuração mais avançada, primeiro olhemos para o método consume, esse método vai recuperar os dados e chamar o método de mudança de senha, entretando para isso anotamos o método com **@SqsListener** para que o método seja executado e que realmente se fique escutando, sempre que ocorra uma alteração na fila, como o envio de uma mensagem, entretando realizar apenas isso trará alguns problemas na nossa aplicação pois precisamos realizar esse Listener em uma thread em paralelo ao Spring, por isso utilizamos os dois **@Beans** e a anotação **@Async**.
	
## Arquitetura  
  
![](https://raw.githubusercontent.com/tsleolima/daca-project/master/assets/diagram.jpg)