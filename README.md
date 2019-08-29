# Projeto Daca 2019.2
### Lista pra mim 

O Lista pra mim©, é um app que vai revolucionar a forma como você faz compras. Quanto mais você usar o Lista pra mim, mais rápido será gerar novas listas de compras. E você ainda pode economizar anotando preços e locais de compra. Depois é só deixar o Lista pra mim© indicar o melhor lugar para você fazer suas compras.

### Instalação
Inicialmente certifique-se em ter o Git Bash, Maven e a JDK Java instaladas em sua máquina com as devidas variáveis de ambiente configuradas no seu sistema operacional.
Caso não esteja familiarizado com isso segue alguns guias abaixo.
[Guia de configuração das variáveis no windows](https://www.mkyong.com/maven/how-to-install-maven-in-windows/)

Clone nosso repositório com 
`git clone https://github.com/tsleolima/daca-project`

Inicie o servidor com API REST com
`mvn spring-boot:run`

Utilize algum serviço para consumir a API fornecida, futuramente desenvolveremos uma GUI Web para o projeto, mas por enquanto aconselhamos o uso do [Postman](https://www.getpostman.com/)

### Features

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

O Lista pra mim possui funcionalidades como:
- Cadastrar produtos 
    - O produto possui nome, categoria, tipo, e um set de locais de venda do produto com preços
- Cadastrar listas de compras
    - A lista possui um descritos, sua data de criação e um set de compras, produtos escolhidos pelo usuario
- Gerar listas de compras automaticamente
    - Geração automática por data, a ultima lista cadastrada
    - Geração automática por produto, a ultima lista que apresentava este produto
    - Geração automática baseando-se nos produtos mais apresentados nas listas cadastradas
- Sugerir locais de venda baseados em uma determinada lista de compra
    - Apresenta uma lista de sugestões contendo o nome do estabelecimento, os produtos encontrados no local e o preço total da lista caso fosse realizada.

### EndPoints 
- Produto
    - GET dos produtos cadastrados `localhost:8080/produto`
    - GET em um produto especifico com seu id `localhost:8080/produto/{id}`
    - GET com produtos ordenados por nome `localhost:8080/produto/ordered`
    - GET com produtos ordenados por uma categoria `localhost:8080/produto/ordered/{categoria}`
    - GET com produtos ordenados pelo preço `localhost:8080/produto/ordered/preco`
    - GET com produtos que possuem um determinado nome `localhost:8080/produto/search/{nome}`
    - POST do produto a ser adicionado `localhost:8080/produto/`
        - Nome
        - Categoria
        - Tipo 
        - Quantidade
    - PUT do produto inserindo seu id de busca e seu objeto atualizado `localhost:8080/produto/{id}`
    - DELETE do produto já cadastrado `localhost:8080/produto/{id}`
    
- Listas de Compra
    - GET das lidas de compras já cadastradas `localhost:8080/listacompra`
    - GET de uma lista de compra especifica com seu id `localhost:8080/listacompra/{id}`
    - POST da lista de compra a ser adicionado `localhost:8080/listacompra/`
        - Descritor da lista
    - PUT da lista de compra inserindo seu id e seu objeto atualizado `localhost:8080/listacompra/{id}`
    - DELETE de uma lista de compras já cadastrada `localhost:8080/listacompra/{id}`
    - GET buscar lista a partir do seu descritor `localhost:8080/listacompra/search/descritor/{descritor}`
    - GET busca listas a partir de uma data a data segue o formato dd-mm-yyyy `localhost:8080/listacompra/search/data/{data}`
    - GET busca listas a partir de um id de um produto `localhost:8080/listacompra/search/idproduto/{idproduto}` 

    - GET gera uma lista automaticamente baseando-se na ultima lista cadastrada `localhost:8080/listacompra/automatic/recent`
    - GET gera uma lista automaticamente baseando-se na ultima lista que possui o produto desejado `localhost:8080/listacompra/automatic/itemcompra/{idproduto}`
    - GET gera uma lista automaticamente baseando-se nas compras mais encontradas nas listas, a regra é os produtos que contemplam pelo menos metade das listas `localhost:8080/listacompra/automatic/maisfrequentes`
    - GET o lista pra mim sugere os estabelicimentos, dos quais foram utilizados para atualização produtos, que possuem pelo menos um produto da lista que deseja ser comprada, mostrando um pequeno relatório de local de venda e preço final da lista caso comprada no estabelicimento proposto `localhost:8080/listacompra/suggestion/{idlista}`
    
### Arquitetura

![](https://raw.githubusercontent.com/tsleolima/daca-project/master/assets/diagram.jpg)
