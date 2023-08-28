# RestAssuredTest

https://restful-booker.herokuapp.com/
                
    Relatório da execução dos testes disponível em https://wailler-prata.github.io/RestAssuredTest/

## Informações mínimas do projeto

* Java version: 20.0.1
* Maven version: 3.9.4

O Maven é fundamental para o gerênciamento das demais dependências de plugins essenciais para o projeto como ```Rest Assured``` e ```JUnit``` entre outros recursos que foram utilizados.


## Como executar

A execução do projeto resultará na criação do diretório ```target``` com outros sub-diretórios.

* Sem geração de relatório:
 
Abra o terminal na raiz do projeto, de preferencia em uma IDE ```(Recomendado: InteliJ)``` e execute ```mvn test```
* Com geração de relatório:

Abra o terminal na raiz do projeto, de preferencia em uma IDE ```(Recomendado: InteliJ)``` e execute ```mvn surefire-report:report ```. O relatório de report será disponibilizado em ```target/site/surefire-report.html```. Para facilitar a visualização, deixei no diretório ```report-sample``` o ultimo relatório extraído antes de enviar o projeto para o repositório. 

IMPORTANTE: O relatório de report precisa de todo o conteúdo da pasta que se encontra em ```target/site``` para que o html seja renderizado de forma apresentável no browser.

## Plano de teste e estratégia de testes

Foi criado um pacote ```com/herokuapp/restful/booker```, nele esta alocado pacotes de testes dos EndPoint's. Cada EndPoint possui um pacote próprio.

* A classe ```BaseRequest``` é estendida pelas classes de cada teste. Essa classe foi criada apanas para comportar os atributos e configurações padrões 
* O arquivo ```credentials.json```, esse arquivo contem as credenciais de autenticação para acesso às rotas autenticadas.

* Os pacotes ```authentication``` e ```booking```. Como dito anteriormente, cada um desses pacotes possui uma classe para os testes (incluindo casos de exceção). Cada classe possui testes de um único EndPoint.

Para redução de código duplicado, criei os pacotes ```requests.utils```, ```assertions.utils``` e ```json.utils```.

No pacote ```requests.utils``` temos a classe ```Requests``` foi construída para padronização das requisições que seriam executadas.

No pacote ```json.utils``` classe ```JsonUtils``` foi construída para armazenar algumas funções de conversão/ manipulação de formato json em um tipo de objeto que o Java consegue lidar.

No pacote ```assertions.utils``` classe ```AssertionsUtils``` foi construída para padronização das validações dos testes que são executados.
