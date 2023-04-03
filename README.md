
# <div align="center">  CONSUMINDO API EXTERNA DO BANCO CENTRAL COM FEIGN</div>

<div align="center"> 

### Projeto consiste em consumir dados de uma API pública com intuito de salvar e manipular suas informações.
</div>

<br>

## 📑 Arquitetura do Projeto

```
├──📁 feignClient
    ├─📁  src   
    | ├─📁 main
    | | ├─📁 java
    | | | ├─📁 com.eliza.feignClient
    │ | | | ├─📁client 
    │ | │ │ │ └─📄BacenClient 
    | | | | ├─📁 controllers
    | │ | | | ├─📁exceptionHandler            
    │ │ | │ │ │ └─📄ControllerExceptionHandler 
    │ │ | │ │ │ └─📄FieldMessage 
    │ | | | | │ └─📄StandardError 
    │ | | | | | └─📄ValidationError 
    | | | | | └─📄TaxaJurosController          
    │ | | | ├─📁mapper 
    │ | │ │ │ └─📄TaxaJurosMapper
    │ | | | ├─📁model        
    │ | | | | ├─📁dto    
    | │ | | | | └─📄TaxaJurosDTO 
    | │ | | | | └─📄TaxaJurosResponse
    | | | | | └─📄TaxaJurosRequest
    │ | | | ├─📁repositories              
    │ | │ │ │ └─📄TaxaJurosRepository 
    │ | | | ├─📁services
    | │ | | | ├─📁exceptions            
    │ │ | │ │ │ └─📄TaxaJurosNotFoundException             
    │ | │ │ │ └─📄TaxaJurosService 
    | | | | │ └─📄TaxaJurosServiceImpl 
    │ | | | ├─📁utils
    | │ | | | ├─📁constants            
    │ │ | │ │ │ └─📄Messages             
    │ | │ │ │ └─📄CNPJValidator 
    | │ | └─📄FeignClientApplication  
    | | ├─📁 resources  
    | | | ├─📁 static          
    | | | ├─📁 template          
    | | | └─📄 application.properties  
    | | ├─📁 test  
    | | | ├─📁 java   
    | | | ├─📁 com.eliza.feignClient
    | | | | ├─📁 controllers
    | │ | | | ├─📁exceptionHandler            
    │ │ | │ │ │ └─📄ControllerExceptionHandler    
    | | | | | └─📄TaxaJurosControllerTest
    │ | | | ├─📁feignClient              
    │ | │ │ │ └─📄BacenClientTest
    │ | | | ├─📁integration              
    │ | │ │ │ └─📄TaxaJurosControllerTest
    │ | | | ├─📁services            
    │ | │ │ │ └─📄TaxaJurosServiceImplTest
    │ | │ └─📄FeignClientApplicationTests
    ├─📁 target
    ├─📄.gitignore
    ├─📄 HELP.md         
    ├─📄 mvnw                 
    ├─📄 mvnw.cmd                 
    ├─📄 pom.xml                 
    └─📄 README.md
```


## ⚙️ Dependências do Projeto
    - OpenFeign cloud
    - Lombok
    - Validation
    - jUnit5 - Teste
    - Jacoco
    - Caellum Stella (CNPJ Validation)
    - Mapstruct

## 💻 Dependências de ambiente
    - Java 11 
    - MySQL
    - Maven

## 📚 Collection

<div> 

A `collection` possui uma coleção onde armazenamos as informações de retorno da API externa.

</div>

<p>

Exemplo de `Dado` cadastrado no BACEN:

</p>

```json
[
    {
      "id": 1,
      "Mes": "Jan-2023",
      "Modalidade": "FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PRÉ-FIXADO",
      "Posicao": 1,
      "InstituicaoFinanceira": "BCO DO BRASIL S.A.",
      "TaxaJurosAoMes": 0.0,
      "TaxaJurosAoAno": 0.0,
      "cnpj8": "00000000",
      "anoMes": "2023-01"
    }
]
```

##  🛣️ ROTAS

<br>

O projeto foi estruturado seguindo modelo da estrura de Arquitetura de Software Rest/Restful, utilizando os protocolos HTTP - POST, GET, PUT, DELETE - CRUD.

<br>

###  Método GET

<div align = "center">

|  Método  |                          Rota                          |                        Descrição                        |
| :------: |:------------------------------------------------------:|:-------------------------------------------------------:|
|  `GET`   |      http://localhost:8090/taxasMes?retornar=qtd       |           Lista que retorna os itens do BACEN           |
|  `GET`   |            http://localhost:8090/taxasMes/             | Lista todos os itens que foram salvos no Banco de Dados |
|  `GET`   |           http://localhost:8090/taxasMes/id            |                      Busca por ID                       |
|  `GET`   |  http://localhost:8090/taxasMes/anoMes?anoMes=anoMes   |                    Busca por ANO-MES                    |
|  `GET`   | http://localhost:8090/taxasMes/filtrar?pag=pag&tam=tam |        Busca por PAGINAÇÃO e QUATIDADE de itens         |

<br>
</div>

### Método POST

<div align = "center">

|  Método  |                Rota                 |                         Descrição                          |
| :------: |:-----------------------------------:|:----------------------------------------------------------:|
|  `POST`  | http://localhost:8090/taxasMes/novo |               Cria novo registro               |

<br>
</div>

###  Método PUT

<div align = "center">

|  Método  |               Rota                |                                Descrição                     |
| :------: |:---------------------------------:| :-------------------------------------------------------:    |
|   `PUT`  | http://localhost:8090/taxasMes/id |       Atualizar os dados dos clientes por ID                  |

<br>
</div>

###  Método DELETE

<div align = "center">

|  Método  |               Rota                |                                Descrição                     |
| :------: |:---------------------------------:| :-------------------------------------------------------:    |
| `DELETE` | http://localhost:8090/taxasMes/id |                      Deletar registro de cliente por ID          |

<br>
</div>


<div align = "center">
<a href="https://www.linkedin.com/in/eliza-pimentel/">
<img alt="linkedin" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/>
</a> 
</div > 
