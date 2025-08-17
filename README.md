# 🚀 Lie Lar Backend

Sistema de Gestão de Materiais Eletrônicos e Construção - Backend Java com Spring Boot

## 📋 Descrição

Backend desenvolvido em Java com Spring Boot para o sistema Lie Lar, seguindo os princípios SOLID e arquitetura limpa. O sistema permite gerenciar categorias, produtos e usuários de forma eficiente e escalável.

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data MongoDB**
- **MongoDB**
- **Swagger/OpenAPI 3**
- **Lombok**
- **Maven**

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/lielar/
│   │   ├── domain/                    # Camada de domínio
│   │   │   ├── entities/              # Entidades JPA
│   │   │   └── dto/                   # Data Transfer Objects
│   │   ├── application/               # Camada de aplicação
│   │   │   └── services/              # Serviços de negócio
│   │   └── infrastructure/            # Camada de infraestrutura
│   │       ├── config/                # Configurações
│   │       ├── controllers/           # Controllers REST
│   │       ├── repositories/          # Repositórios MongoDB
│   │       └── mappers/               # Mapeadores DTO ↔ Entity
│   └── resources/
│       └── application.yml            # Configurações da aplicação
```

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- MongoDB 5.0+ (ou Docker)

### 1. Clonar o Projeto

```bash
git clone <repository-url>
cd lie-lar-backend
```

### 2. Configurar MongoDB

#### Opção A: MongoDB Local
1. Instalar MongoDB
2. Criar banco `lie_lar_db`
3. Verificar se está rodando na porta 27017

#### Opção B: Docker (Recomendado)
```bash
docker run -d --name mongodb -p 27017:27017 mongo:latest
```

### 3. Executar a Aplicação

```bash
# Compilar e executar
mvn spring-boot:run

# Ou compilar primeiro e depois executar
mvn clean compile
mvn spring-boot:run
```

### 4. Acessar a Aplicação

- **API Base:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/api-docs

## 📚 Endpoints Disponíveis

### 🏷️ Categorias (`/api/categorias`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/` | Criar categoria |
| GET | `/` | Listar categorias ativas |
| GET | `/{id}` | Buscar categoria por ID |
| GET | `/slug/{slug}` | Buscar categoria por slug |
| GET | `/busca?nome={nome}` | Buscar categorias por nome |
| PUT | `/{id}` | Atualizar categoria |
| DELETE | `/{id}` | Desativar categoria |
| POST | `/{id}/ativar` | Ativar categoria |
| GET | `/count` | Contar categorias ativas |

### 📦 Produtos (`/api/produtos`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/` | Criar produto |
| GET | `/` | Listar produtos com paginação |
| GET | `/ativos` | Listar produtos ativos |
| GET | `/{id}` | Buscar produto por ID |
| GET | `/codigo/{codigo}` | Buscar produto por código |
| GET | `/categoria/{categoryId}` | Produtos por categoria |
| GET | `/destaque` | Produtos em destaque |
| GET | `/busca/nome?nome={nome}` | Buscar por nome |
| GET | `/busca/codigo?codigo={codigo}` | Buscar por código |
| GET | `/estoque-baixo?quantidade={qtd}` | Produtos com estoque baixo |
| PUT | `/{id}` | Atualizar produto |
| PUT | `/{id}/estoque?estoque={qtd}` | Atualizar estoque |
| DELETE | `/{id}` | Desativar produto |
| POST | `/{id}/ativar` | Ativar produto |
| GET | `/count` | Contar produtos ativos |

### 🧪 Teste (`/api/test`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/health` | Status da aplicação |
| GET | `/ping` | Teste de conectividade |

## 🔧 Configurações

### application.yml

```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: lie_lar_db

server:
  port: 8080

springdoc:
  swagger-ui:
    path: /swagger-ui.html
```

## 📊 Banco de Dados

### Coleções MongoDB

- **users** - Usuários do sistema
- **categories** - Categorias de produtos
- **products** - Produtos

### Índices

- Nome da categoria (único)
- Slug da categoria (único)
- Código do produto (único)
- Email do usuário (único)

## 🧪 Testando a API

### 1. Teste de Conectividade

```bash
curl http://localhost:8080/api/test/ping
# Resposta esperada: "pong"
```

### 2. Criar Categoria

```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Eletrônicos",
    "descricao": "Produtos eletrônicos diversos",
    "icone": "📱",
    "cor": "#007bff"
  }'
```

### 3. Criar Produto

```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Smartphone Samsung",
    "descricao": "Smartphone Samsung Galaxy",
    "preco": 1299.99,
    "codigo": "SM001",
    "categoriaId": "ID_DA_CATEGORIA",
    "marca": "Samsung",
    "modelo": "Galaxy A54",
    "estoque": 10
  }'
```

## 🏗️ Arquitetura

### Princípios SOLID

- **S** - Single Responsibility: Cada classe tem uma única responsabilidade
- **O** - Open/Closed: Aberto para extensão, fechado para modificação
- **L** - Liskov Substitution: Classes derivadas substituem suas bases
- **I** - Interface Segregation: Interfaces pequenas e específicas
- **D** - Dependency Inversion: Depender de abstrações, não implementações

### Camadas

1. **Domain** - Entidades e regras de negócio
2. **Application** - Serviços de aplicação
3. **Infrastructure** - Controllers, repositórios e mapeadores

## 🚧 Próximos Passos

- [ ] Implementar sistema de usuários
- [ ] Adicionar autenticação JWT
- [ ] Implementar validações avançadas
- [ ] Adicionar testes unitários
- [ ] Implementar logs estruturados
- [ ] Adicionar métricas e monitoramento

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Contato

- **Email:** contato@lie-lar.com
- **Website:** https://lie-lar.com

---

**Desenvolvido com ❤️ pela equipe Lie Lar**
