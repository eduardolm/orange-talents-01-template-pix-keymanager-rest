# Pix KeyManager - REST
![Version](src/main/resources/static/img/version.svg)
![License](src/main/resources/static/img/license.svg)
![Coverage](src/main/resources/static/img/coverage.svg)
## Objetivos

Desenvolver um microserviço REST que deverá comunicar-se com o serviço gRPC. Este serviço REST será responsável por expor os endpoints para o frontend, permitindo que o usuário interaja com os serviço de gerenciamento de chaves Pix.
Este serviço deverá expor endpoints de cadastro, listagem, consulta e exclusão de chave Pix.
A comunicação entre os serviços será por gRPC.

## Tecnologias utilizadas
O projeto foi desenvolvido utilizando as seguintes tecnologias:

 + Kotlin 1.4.32
 + Micronaut 2.4.1
 + Docker
 + Docker-compose
 + Github

## Endpoints & Payloads
### Endpoints
#### Chave Pix
**Ação** | **Endpoint** | **Método HTTP**
---------- | ------------ | ----------
Criar chave Pix | _/api/v1/pix/keys_ | POST
Listar por id | _/api/v1/pix/keys/{id}_ | GET
Listar todas | _/api/v1/pix/keys/all/{id}_ | GET
Listar todas | _/api/v1/pix/keys_ | DELETE

### Payloads - (request & response)
#### Cadastrar Chave Pix
##### POST - request
    {
        "id": "ae93a61c-0642-43b3-bb8e-a17072295955",
        "keyType": "RANDOM",
        "key": "",
        "accountType": "CONTA_CORRENTE"
    }

##### POST - response
    {
        "clientId": "ae93a61c-0642-43b3-bb8e-a17072295955",
        "pixId": "be4de01f-fc80-457c-ac43-0509edf59921",
        "createdAt": "2021-03-31T15:02:26.501538"
    }

##### GET - Listar por id (request)
    {
        "pixId": "df6fb543-203c-4773-9400-6d2d5d9e6bce",
        "clientId": "ae93a61c-0642-43b3-bb8e-a17072295955"
    }

##### GET - Listar por id (response)
    {
        "pixId": "df6fb543-203c-4773-9400-6d2d5d9e6bce",
        "clientId": "ae93a61c-0642-43b3-bb8e-a17072295955",
        "keyType": "CPF",
        "pixKey": "40764442058",
        "bankAccount": {
            "accountType": "CONTA_CORRENTE",
            "accountInstitution": {
                "name": "ITAÚ UNIBANCO S.A.",
                "ispb": "60701190"
            },
            "accountBranch": "0001",
            "accountNumber": "125987"
        },
        "owner": {
            "ownerName": "Leonardo Silva",
            "ownerDocument": "40764442058"
        },
        "createdAt": "2021-03-29T20:57:36.428204"
    }

##### GET - Listar todas por clientId (request)
    {
        "clientId": "ae93a61c-0642-43b3-bb8e-a17072295955"
    }

##### GET - Listar todas por clientId (response)
    {
        "clientId": "ae93a61c-0642-43b3-bb8e-a17072295955",
        "keyList": [
            {
                "pixId": "5e90944c-43a1-4dc4-a72f-3e377d21407f",
                "keyType": "EMAIL",
                "pixKey": "leo@zup.com.br",
                "accountType": "CONTA_POUPANCA",
                "createdAt": "2021-03-26T18:36:29.990722"
            },
            {
                "pixId": "be4de01f-fc80-457c-ac43-0509edf59921",
                "keyType": "RANDOM",
                "pixKey": "66ac2b68-239d-4a39-9bdd-21b229a4c4af",
                "accountType": "CONTA_CORRENTE",
                "createdAt": "2021-03-31T15:02:26.501538"
            },
            {
                "pixId": "df6fb543-203c-4773-9400-6d2d5d9e6bce",
                "keyType": "CPF",
                "pixKey": "40764442058",
                "accountType": "CONTA_CORRENTE",
                "createdAt": "2021-03-29T20:57:36.428204"
            }
        ]
    }

##### DELETE - Excluir (request)
    {
        "pixId": "be4de01f-fc80-457c-ac43-0509edf59921",
        "clientId": "ae93a61c-0642-43b3-bb8e-a17072295955"
    }

##### DELETE - Excluir (response)
    {
        "pixKey": "66ac2b68-239d-4a39-9bdd-21b229a4c4af",
        "participant": "60701190",
        "deletedAt": "2021-03-31T15:09:01.323507"
    }