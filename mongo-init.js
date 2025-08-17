// Script de inicialização do MongoDB para Lie Lar
// Este script é executado automaticamente quando o container MongoDB é iniciado

// Conectar ao banco admin
db = db.getSiblingDB('admin');

// Criar usuário para o banco lie_lar_db
db.createUser({
  user: 'lie_lar_user',
  pwd: 'lie_lar_pass',
  roles: [
    {
      role: 'readWrite',
      db: 'lie_lar_db'
    }
  ]
});

// Conectar ao banco lie_lar_db
db = db.getSiblingDB('lie_lar_db');

// Criar coleções com índices
db.createCollection('users');
db.createCollection('categories');
db.createCollection('products');

// Criar índices únicos
db.users.createIndex({ "email": 1 }, { unique: true });
db.categories.createIndex({ "nome": 1 }, { unique: true });
db.categories.createIndex({ "slug": 1 }, { unique: true });
db.products.createIndex({ "codigo": 1 }, { unique: true });

// Criar índices para melhor performance
db.users.createIndex({ "ativo": 1 });
db.categories.createIndex({ "ativo": 1, "ordem": 1 });
db.products.createIndex({ "ativo": 1 });
db.products.createIndex({ "categoria": 1 });
db.products.createIndex({ "destaque": 1, "ativo": 1 });

print('✅ Banco de dados Lie Lar inicializado com sucesso!');
print('📊 Banco: lie_lar_db');
print('👤 Usuário: lie_lar_user');
print('🔑 Senha: lie_lar_pass');
print('🌐 Acesso: mongodb://lie_lar_user:lie_lar_pass@localhost:27017/lie_lar_db');
