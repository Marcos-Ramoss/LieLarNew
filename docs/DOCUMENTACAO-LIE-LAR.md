# 📋 DOCUMENTAÇÃO COMPLETA - LIE LAR APP WEB

## **1. VISÃO GERAL DO PROJETO**

**Nome:** Lie Lar - Sistema de Gestão de Materiais Eletrônicos e Construção  
**Tecnologia Atual:** Node.js + Express.js + EJS + MongoDB  
**Arquitetura:** MVC (Model-View-Controller) com separação de camadas  
**Status:** Sistema funcional com dashboard administrativo completo  
**Versão:** 1.0.0  
**Data:** Janeiro 2025  

---

## **2. REQUISITOS FUNCIONAIS IMPLEMENTADOS**

### **2.1 Sistema de Autenticação e Autorização**
- ✅ **Login/Logout** com sessões
- ✅ **Controle de permissões** baseado em roles (admin, gerente, funcionário, vendedor)
- ✅ **Middleware de autenticação** para rotas protegidas
- ✅ **Sistema de sessões** com express-session
- ✅ **Verificação de permissões** por recurso e ação

### **2.2 Dashboard Administrativo**
- ✅ **Interface responsiva** com EJS templates
- ✅ **Menu de navegação** entre módulos
- ✅ **Exibição de informações** do usuário logado
- ✅ **Sistema de mensagens** (sucesso/erro)
- ✅ **Logout seguro** com destruição de sessão

### **2.3 Módulo de Categorias**
- ✅ **CRUD completo** (Criar, Ler, Atualizar, Deletar)
- ✅ **Formulários de criação/edição** com validação
- ✅ **Listagem com paginação** e filtros
- ✅ **Controle de ordem** das categorias
- ✅ **Ativação/desativação** de categorias
- ✅ **Campos personalizáveis** (ícone, cor, ordem)

### **2.4 Módulo de Produtos**
- ✅ **CRUD completo** com campos extensos
- ✅ **Gestão de estoque** e preços
- ✅ **Associação com categorias**
- ✅ **Controle de ativação**
- ✅ **Formulários com validação**
- ✅ **Campos técnicos** (peso, dimensões, marca, modelo)

### **2.5 Módulo de Usuários**
- ✅ **CRUD completo** de usuários
- ✅ **Sistema de roles** e permissões
- ✅ **Gestão de departamentos**
- ✅ **Controle de acesso** baseado em permissões
- ✅ **Senhas criptografadas** com bcrypt
- ✅ **Controle de status** ativo/inativo

---

## **3. REQUISITOS FUNCIONAIS NÃO IMPLEMENTADOS**

### **3.1 Módulo de Orçamentos**
- ❌ **Criação de orçamentos** para clientes
- ❌ **Gestão de itens** do orçamento
- ❌ **Cálculo automático** de valores
- ❌ **Aprovação/reprovação** de orçamentos
- ❌ **Histórico** de orçamentos
- ❌ **Template de orçamento** para impressão

### **3.2 Módulo de Relatórios**
- ❌ **Relatórios de vendas**
- ❌ **Relatórios de estoque**
- ❌ **Relatórios financeiros**
- ❌ **Exportação** para PDF/Excel
- ❌ **Dashboards** com gráficos
- ❌ **Relatórios personalizáveis**

### **3.3 Módulo de Clientes**
- ❌ **Cadastro de clientes**
- ❌ **Histórico de compras**
- ❌ **Gestão de endereços**
- ❌ **Sistema de fidelidade**
- ❌ **Cadastro de endereços** de entrega
- ❌ **Histórico de orçamentos**

### **3.4 Módulo de Fornecedores**
- ❌ **Cadastro de fornecedores**
- ❌ **Gestão de produtos** por fornecedor
- ❌ **Histórico de compras**
- ❌ **Avaliações** de fornecedores
- ❌ **Contatos** e informações comerciais
- ❌ **Produtos associados** ao fornecedor

### **3.5 Funcionalidades Avançadas**
- ❌ **Sistema de notificações**
- ❌ **Upload de imagens** para produtos
- ❌ **Backup automático** do banco
- ❌ **Logs de auditoria**
- ❌ **API para aplicações externas**
- ❌ **Sistema de busca avançada**

---

## **4. ARQUITETURA TÉCNICA ATUAL**

### **4.1 Estrutura de Pastas**
```
src/
├── controllers/          # Controladores das rotas
│   ├── categoriaController.js
│   ├── produtoController.js
│   └── usuarioController.js
├── models/              # Modelos MongoDB/Mongoose
│   ├── Categoria.js
│   ├── Produto.js
│   └── Usuario.js
├── services/            # Lógica de negócio
│   ├── CategoriaService.js
│   ├── ProdutoService.js
│   └── UsuarioService.js
├── repositories/        # Acesso a dados
│   ├── BaseRepository.js
│   ├── CategoriaRepository.js
│   ├── ProdutoRepository.js
│   └── UsuarioRepository.js
├── middlewares/         # Middlewares de autenticação
│   ├── auth.js          # JWT para APIs
│   └── sessionAuth.js   # Sessão para web
├── routes/              # Definição de rotas API
│   ├── categorias.js
│   ├── produtos.js
│   └── usuarios.js
├── views/               # Templates EJS
│   ├── admin/
│   │   ├── dashboard.ejs
│   │   ├── categorias.ejs
│   │   ├── produtos.ejs
│   │   ├── usuarios.ejs
│   │   └── forms/
│   └── shared/
└── utils/               # Utilitários

public/
├── css/                 # Estilos CSS
│   ├── admin.css
│   └── main.css
├── js/                  # JavaScript do cliente
│   ├── admin/
│   │   ├── dashboard.js
│   │   ├── categorias.js
│   │   ├── produtos.js
│   │   └── usuarios.js
│   └── shared/
└── images/              # Imagens e assets
```

### **4.2 Tecnologias Utilizadas**
- **Backend:** Node.js + Express.js
- **Template Engine:** EJS (Embedded JavaScript)
- **Banco de Dados:** MongoDB + Mongoose
- **Autenticação:** express-session + JWT
- **Frontend:** HTML + CSS + JavaScript vanilla
- **Segurança:** Helmet, CORS, Rate Limiting
- **Validação:** Validação customizada no frontend
- **Criptografia:** bcrypt para senhas

### **4.3 Padrões de Design**
- **MVC:** Separação clara entre Model, View e Controller
- **Repository Pattern:** Abstração do acesso a dados
- **Service Layer:** Lógica de negócio isolada
- **Middleware Chain:** Autenticação e autorização em camadas
- **Factory Pattern:** Para criação de entidades
- **Observer Pattern:** Para notificações de mudanças

---

## **5. MODELOS DE DADOS (ENTIDADES)**

### **5.1 Usuário (User)**
```javascript
{
  _id: ObjectId,
  nome: String (required, max: 100),
  email: String (required, unique, lowercase),
  senha: String (required, min: 6, hashed),
  tipo: String (enum: ['admin', 'gerente', 'funcionario', 'vendedor']),
  cargo: String (required, max: 50),
  departamento: String (enum: ['administrativo', 'vendas', 'estoque', 'financeiro', 'ti']),
  telefone: String (regex validation),
  avatar: String (optional),
  ativo: Boolean (default: true),
  ultimoLogin: Date,
  permissoes: {
    produtos: { criar: Boolean, editar: Boolean, excluir: Boolean, visualizar: Boolean },
    categorias: { criar: Boolean, editar: Boolean, excluir: Boolean, visualizar: Boolean },
    usuarios: { criar: Boolean, editar: Boolean, excluir: Boolean, visualizar: Boolean },
    orcamentos: { criar: Boolean, editar: Boolean, excluir: Boolean, visualizar: Boolean },
    relatorios: { visualizar: Boolean }
  },
  resetPasswordToken: String,
  resetPasswordExpires: Date,
  createdAt: Date,
  updatedAt: Date
}
```

### **5.2 Categoria (Category)**
```javascript
{
  _id: ObjectId,
  nome: String (required, max: 100, unique),
  descricao: String (max: 500),
  icone: String (default: '🏷️'),
  cor: String (default: '#00d4ff'),
  ordem: Number (default: 0),
  ativo: Boolean (default: true),
  slug: String (unique, auto-generated),
  createdAt: Date,
  updatedAt: Date
}
```

### **5.3 Produto (Product)**
```javascript
{
  _id: ObjectId,
  nome: String (required, max: 200),
  descricao: String (max: 1000),
  preco: Number (required, min: 0),
  categoria: ObjectId (ref: Category, required),
  codigo: String (unique, required),
  marca: String (max: 100),
  modelo: String (max: 100),
  estoque: Number (default: 0, min: 0),
  unidade: String (default: 'un'),
  peso: Number (min: 0),
  dimensoes: String,
  ativo: Boolean (default: true),
  imagens: [String],
  destaque: Boolean (default: false),
  createdAt: Date,
  updatedAt: Date
}
```

---

## **6. ROTAS E ENDPOINTS IMPLEMENTADOS**

### **6.1 Rotas Web (Dashboard)**
```
GET  /admin/login                    # Página de login
POST /admin/login                    # Processar login
GET  /admin/logout                   # Logout
GET  /admin/dashboard                # Dashboard principal

# Categorias
GET  /admin/categorias               # Lista de categorias
GET  /admin/categorias/nova          # Form nova categoria
POST /admin/categorias               # Criar categoria
GET  /admin/categorias/:id/editar    # Form editar categoria
POST /admin/categorias/:id/editar    # Atualizar categoria
POST /admin/categorias/:id/excluir   # Excluir categoria

# Produtos
GET  /admin/produtos                 # Lista de produtos
GET  /admin/produtos/novo            # Form novo produto
POST /admin/produtos                 # Criar produto
GET  /admin/produtos/:id/editar      # Form editar produto
POST /admin/produtos/:id/editar      # Atualizar produto
POST /admin/produtos/:id/excluir     # Excluir produto

# Usuários
GET  /admin/usuarios                 # Lista de usuários
GET  /admin/usuarios/novo            # Form novo usuário
POST /admin/usuarios                 # Criar usuário
GET  /admin/usuarios/:id/editar      # Form editar usuário
POST /admin/usuarios/:id/editar      # Atualizar usuário
POST /admin/usuarios/:id/excluir     # Excluir usuário
```

### **6.2 Rotas API (JWT)**
```
# Categorias
GET    /api/categorias               # Listar categorias
POST   /api/categorias               # Criar categoria
PUT    /api/categorias/:id           # Atualizar categoria
DELETE /api/categorias/:id           # Excluir categoria
GET    /api/categorias/ativas        # Listar categorias ativas
GET    /api/categorias/slug/:slug    # Buscar por slug
GET    /api/categorias/:id           # Buscar por ID

# Produtos
GET    /api/produtos                 # Listar produtos (admin)
GET    /api/produtos/publico         # Listar produtos públicos
GET    /api/produtos/destaque        # Produtos em destaque
GET    /api/produtos/relacionados    # Produtos relacionados
GET    /api/produtos/codigo/:codigo  # Buscar por código
POST   /api/produtos                 # Criar produto
PUT    /api/produtos/:id             # Atualizar produto
DELETE /api/produtos/:id             # Excluir produto
GET    /api/produtos/:id             # Buscar por ID

# Usuários
POST   /api/usuarios/autenticar      # Login
POST   /api/usuarios/login           # Alias para login
GET    /api/usuarios                 # Listar usuários
POST   /api/usuarios                 # Criar usuário
GET    /api/usuarios/:id             # Buscar usuário
PUT    /api/usuarios/:id             # Atualizar usuário
DELETE /api/usuarios/:id             # Excluir usuário
```

---

## **7. FUNCIONALIDADES DE FRONTEND IMPLEMENTADAS**

### **7.1 Interface do Usuário**
- ✅ **Design responsivo** com CSS customizado
- ✅ **Navegação intuitiva** entre módulos
- ✅ **Formulários validados** com JavaScript
- ✅ **Mensagens de feedback** (sucesso/erro)
- ✅ **Tabelas com ordenação** e filtros
- ✅ **Layout adaptativo** para diferentes tamanhos de tela

### **7.2 JavaScript do Cliente**
- ✅ **Validação de formulários** em tempo real
- ✅ **Contadores de caracteres** para campos
- ✅ **Busca em tempo real** nas listagens
- ✅ **Confirmações** para ações destrutivas
- ✅ **Carregamento dinâmico** de dados
- ✅ **Manipulação do DOM** para melhor UX

### **7.3 Estilos e Componentes**
- ✅ **Sistema de cores** consistente
- ✅ **Componentes reutilizáveis** (botões, inputs, tabelas)
- ✅ **Animações CSS** para transições
- ✅ **Ícones e elementos visuais** consistentes
- ✅ **Tipografia** hierárquica e legível

---

## **8. RECOMENDAÇÕES PARA MIGRAÇÃO ANGULAR + JAVA**

### **8.1 Estrutura Angular Recomendada**
```
src/
├── app/
│   ├── components/                  # Componentes reutilizáveis
│   │   ├── shared/
│   │   │   ├── header/
│   │   │   ├── sidebar/
│   │   │   ├── table/
│   │   │   └── forms/
│   │   └── admin/
│   │       ├── dashboard/
│   │       ├── categorias/
│   │       ├── produtos/
│   │       └── usuarios/
│   ├── pages/                       # Páginas principais
│   ├── services/                    # Serviços de API
│   ├── models/                      # Interfaces TypeScript
│   ├── guards/                      # Guards de rota
│   ├── interceptors/                # Interceptadores HTTP
│   ├── shared/                      # Componentes compartilhados
│   └── core/                        # Serviços core
```

### **8.2 Estrutura Java Recomendada**
```
src/
├── main/
│   ├── java/
│   │   └── com/lielar/
│   │       ├── controllers/          # Controllers REST
│   │       ├── services/             # Serviços de negócio
│   │       ├── repositories/         # Repositórios JPA
│   │       ├── entities/             # Entidades JPA
│   │       ├── dto/                  # Data Transfer Objects
│   │       ├── config/               # Configurações
│   │       ├── security/             # Configurações de segurança
│   │       ├── exceptions/           # Tratamento de exceções
│   │       └── utils/                # Utilitários
│   └── resources/
│       ├── application.yml           # Configurações
│       ├── data.sql                  # Dados iniciais
│       └── schema.sql                # Schema do banco
```

### **8.3 Tecnologias Java Recomendadas**
- **Framework:** Spring Boot 3.x
- **Banco:** PostgreSQL ou MySQL
- **ORM:** Spring Data JPA + Hibernate
- **Segurança:** Spring Security + JWT
- **API:** REST com OpenAPI/Swagger
- **Build:** Maven ou Gradle
- **Testes:** JUnit 5 + Mockito
- **Documentação:** SpringDoc OpenAPI

### **8.4 Benefícios da Migração**
- ✅ **Frontend moderno** com Angular
- ✅ **Backend robusto** com Spring Boot
- ✅ **Tipagem forte** com TypeScript
- ✅ **Melhor performance** e escalabilidade
- ✅ **Ferramentas de desenvolvimento** mais avançadas
- ✅ **Comunidade ativa** e documentação rica
- ✅ **Testes automatizados** mais robustos
- ✅ **Deploy e CI/CD** mais profissionais

---

## **9. PLANO DE MIGRAÇÃO SUGERIDO**

### **Fase 1: Preparação e Setup (1-2 semanas)**
1. **Definir arquitetura** Angular + Java
2. **Criar projeto base** com Spring Boot
3. **Configurar banco** PostgreSQL/MySQL
4. **Definir estrutura** de entidades JPA
5. **Configurar ambiente** de desenvolvimento

### **Fase 2: Backend Java (3-4 semanas)**
1. **Implementar entidades** (User, Category, Product)
2. **Criar repositórios** JPA
3. **Implementar serviços** de negócio
4. **Criar controllers** REST
5. **Configurar segurança** com Spring Security
6. **Implementar validações** e tratamento de erros

### **Fase 3: Frontend Angular (3-4 semanas)**
1. **Criar projeto** Angular
2. **Implementar componentes** base
3. **Criar serviços** para API
4. **Implementar páginas** principais
5. **Configurar roteamento** e guards
6. **Implementar autenticação** JWT

### **Fase 4: Integração e Testes (2-3 semanas)**
1. **Conectar frontend** com backend
2. **Testar funcionalidades** principais
3. **Implementar autenticação** JWT
4. **Testes de integração**
5. **Testes de usuário**
6. **Correções** e ajustes finais

### **Fase 5: Deploy e Documentação (1 semana)**
1. **Configurar ambiente** de produção
2. **Deploy** da aplicação
3. **Documentação** técnica
4. **Manual do usuário**
5. **Treinamento** da equipe

---

## **10. CONSIDERAÇÕES FINAIS**

### **10.1 Estado Atual**
O projeto atual está **funcionalmente completo** para um MVP, com todas as funcionalidades básicas de gestão implementadas. A arquitetura Node.js + EJS funciona bem para prototipagem e projetos pequenos.

### **10.2 Limitações da Arquitetura Atual**
- **Manutenibilidade:** Código misturado entre frontend/backend
- **Escalabilidade:** Limitações do modelo EJS
- **Desenvolvimento:** Ferramentas menos modernas
- **Performance:** Renderização server-side para todas as páginas
- **Testes:** Dificuldade para implementar testes automatizados
- **Deploy:** Processo manual e menos robusto

### **10.3 Benefícios da Migração**
A migração para **Angular + Java** trará:
- **Separação clara** entre frontend e backend
- **Arquitetura moderna** e escalável
- **Ferramentas profissionais** de desenvolvimento
- **Melhor experiência** para o usuário final
- **Base sólida** para futuras expansões
- **Facilidade de manutenção** e evolução
- **Padrões de mercado** reconhecidos

### **10.4 Recomendação**
**Recomendamos fortemente** a migração para Angular + Java, pois:
1. **Reduzirá significativamente** o tempo de desenvolvimento futuro
2. **Melhorará a qualidade** do código e manutenibilidade
3. **Facilitará a contratação** de desenvolvedores
4. **Permitirá escalabilidade** para projetos maiores
5. **Seguirá padrões** da indústria

---

## **11. APÊNDICES**

### **11.1 Comandos Úteis**
```bash
# Iniciar servidor
npm start

# Modo desenvolvimento
npm run dev

# Instalar dependências
npm install

# Verificar status do banco
mongo lie_lar_db --eval "db.stats()"
```

### **11.2 Configurações de Ambiente**
```env
# .env
PORT=3000
MONGODB_URI=mongodb://localhost:27017/lie_lar_db
SESSION_SECRET=lie_lar_session_secret_2025
NODE_ENV=development
```

### **11.3 Usuário Padrão**
```
Email: admin@lie-lar.com
Senha: admin123
Tipo: admin
Permissões: Todas
```

---

**📅 Data da Documentação:** Janeiro 2025  
**👨‍💻 Desenvolvedor:** Marcos Oliveira  
**🎯 Objetivo:** Migração para Angular + Java  
**📊 Status:** Sistema funcional, pronto para refatoração  
**📝 Versão:** 1.0.0  

---

*Esta documentação foi criada para facilitar a migração do projeto Lie Lar de Node.js + EJS para Angular + Java, mantendo todas as funcionalidades implementadas e expandindo as capacidades do sistema.*
