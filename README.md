#  Desafio: Pix
## KeyManager-REST(expõe KeyManager-gRPC)

**KeyManager-REST**: micro serviço responsável por expor serviço [KeyManager-gRPC](https://github.com/fmchagas/orange-talents-04-template-pix-keymanager-grpc) através de uma API REST de tal forma que ela possa ser consumida pelo time de frontend de forma eficiente e segura

vai funcionar como um proxy convertendo chamadas HTPP para chamadas gRPC


## Começando
Para executar o projeto, será necessário instalar os seguintes programas:

- [Java 11+](https://openjdk.java.net/projects/jdk/11/)
- Docker
- Gradle 7+ (vem integrado com IDE IntelliJ Community)
- Ferramenta Postman ou Insomnia
- IDE IntelliJ Community

## Observação
* Framework Micronaut e suas dependência para gRPC client, HTTP Rest