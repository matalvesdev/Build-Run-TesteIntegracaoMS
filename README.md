# Build-Run-TesteIntegracaoMS

## 📋 Descrição do Projeto

Este projeto é uma implementação de microserviços Spring Boot demonstrando boas práticas de testes de integração utilizando Testcontainers. O projeto contém dois microserviços:

- **PromoWiseMS**: Microserviço de gerenciamento de cupons promocionais
- **OrderWorkerMS**: Microserviço worker de processamento de pedidos

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura de microserviços independentes, cada um com sua própria base de dados e responsabilidades específicas.

### PromoWiseMS
- **Grupo**: tech.buildrun
- **Versão**: 0.0.1-SNAPSHOT
- **Descrição**: Serviço responsável por gerenciar cupons promocionais e suas validações
- **Banco de Dados**: MySQL
- **Integrações**: APIs externas de parceiros e telemetria via OpenFeign

### OrderWorkerMS
- **Grupo**: tech.buildrun
- **Versão**: 0.0.1-SNAPSHOT
- **Descrição**: Serviço worker para processamento assíncrono de pedidos
- **Banco de Dados**: PostgreSQL
- **Mensageria**: AWS SQS (Amazon Simple Queue Service)

## 🛠️ Tecnologias Utilizadas

### Stack Principal
- **Java**: 21
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0 (PromoWiseMS)
- **Maven**: Gerenciamento de dependências e build

### Frameworks e Bibliotecas

#### PromoWiseMS
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Cloud OpenFeign
- MySQL Connector
- Testcontainers (MySQL, JUnit Jupiter)
- WireMock (para testes de integração)

#### OrderWorkerMS
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- PostgreSQL Driver
- Spring Cloud AWS SQS
- Testcontainers (PostgreSQL, LocalStack, JUnit Jupiter)

### Testes de Integração
- **Testcontainers**: Para testes com containers Docker
- **WireMock**: Mock de APIs externas (PromoWiseMS)
- **LocalStack**: Simulação de serviços AWS localmente (OrderWorkerMS)
- **JUnit Jupiter**: Framework de testes

## 📦 Pré-requisitos

- Java 21 ou superior
- Maven 3.6 ou superior
- Docker (para execução dos Testcontainers)

## 🚀 Como Executar

### PromoWiseMS

```bash
cd promowisems/promowisems
mvn spring-boot:run
```

### OrderWorkerMS

```bash
cd orderworkerms/orderworkerms
mvn spring-boot:run
```

## 🧪 Executando os Testes

### Executar todos os testes

#### PromoWiseMS
```bash
cd promowisems/promowisems
mvn test
```

#### OrderWorkerMS
```bash
cd orderworkerms/orderworkerms
mvn test
```

### Testes de Integração

Os testes de integração utilizam Testcontainers para criar ambientes isolados com:
- Bancos de dados reais (MySQL e PostgreSQL)
- Serviços AWS simulados (LocalStack)
- Mocks de APIs externas (WireMock)

## 🏗️ Build do Projeto

### PromoWiseMS
```bash
cd promowisems/promowisems
mvn clean package
```

### OrderWorkerMS
```bash
cd orderworkerms/orderworkerms
mvn clean package
```

Os artefatos gerados estarão disponíveis no diretório `target/` de cada microserviço.

## 📝 Configurações

### PromoWiseMS
As configurações estão em `promowisems/promowisems/src/main/resources/application.properties`:
- Nome da aplicação: `promowisems`
- Dialeto JPA: MySQL
- DDL Auto: create
- API Key para cupons
- URLs de APIs externas (Partner e Telemetry)

### OrderWorkerMS
As configurações estão em `orderworkerms/orderworkerms/src/main/resources/application.properties`:
- Nome da aplicação: `orderworkerms`
- DDL Auto: create

## 🔍 Estrutura de Dependências

### PromoWiseMS (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Cloud -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-testcontainers</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mysql</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.wiremock.integrations.testcontainers</groupId>
        <artifactId>wiremock-testcontainers-module</artifactId>
        <version>1.0-alpha-13</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.wiremock</groupId>
        <artifactId>wiremock</artifactId>
        <version>3.12.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### OrderWorkerMS (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- AWS -->
    <dependency>
        <groupId>io.awspring.cloud</groupId>
        <artifactId>spring-cloud-aws-starter-sqs</artifactId>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-testcontainers</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>postgresql</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>localstack</artifactId>
        <version>1.20.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 📚 Recursos e Aprendizado

Este projeto demonstra:
- ✅ Arquitetura de microserviços
- ✅ Testes de integração com Testcontainers
- ✅ Integração com bancos de dados relacionais (MySQL e PostgreSQL)
- ✅ Comunicação entre serviços via OpenFeign
- ✅ Integração com AWS SQS
- ✅ Mock de serviços externos com WireMock
- ✅ Simulação de ambiente AWS com LocalStack
- ✅ Boas práticas de desenvolvimento Spring Boot

## 📄 Licença

Este é um projeto de demonstração para fins educacionais.

## 👥 Contribuição

Para contribuir com este projeto, siga as práticas padrão de contribuição em projetos Spring Boot e mantenha a cobertura de testes de integração.