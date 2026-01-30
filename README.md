# 📋 CRUD Java Swing com MySQL

Projeto de estudo que implementa um **CRUD completo** utilizando **Java (Swing)**, **Maven** e **MySQL**, com sistema de **autenticação**, **controle de permissões por roles** e conexão configurada via arquivo `.env`.

---

## 🚀 Tecnologias Utilizadas

- Java 21+
- Swing (Interface Gráfica)
- Maven
- MySQL Server
- JDBC (mysql-connector-j 9.2.0)
- Git

---

## ✨ Funcionalidades

### 🔐 Sistema de Autenticação
- Tela de login com validação de credenciais
- Gerenciamento de sessão do usuário logado
- Logout com redirecionamento para tela de login

### 👥 Gerenciamento de Usuários
- **CRUD completo** de usuários (Criar, Ler, Atualizar, Deletar)
- Campos: Nome, Sobrenome, Email, Telefone, Login, Senha, Role e Status
- Validação de duplicidade de login e email
- Autocadastro para novos usuários

### 🛡️ Sistema de Permissões (RBAC)
- **Roles**: `admin` e `user`
- **Permissões disponíveis**:
    - `LISTAR_DADOS` - Visualizar usuários
    - `CRIAR_DADOS` - Cadastrar novos usuários
    - `ATUALIZAR_DADOS` - Editar informações de usuários
    - `EXCLUIR_DADOS` - Remover usuários

| Role    | Permissões                                              |
|---------|---------------------------------------------------------|
| `admin` | LISTAR, CRIAR, ATUALIZAR, EXCLUIR                       |
| `user`  | LISTAR, ATUALIZAR (apenas próprio perfil e outros users)|

### 🖥️ Telas do Sistema
- **TelaLogin** - Autenticação de usuários
- **TelaAutocadastro** - Cadastro de novos usuários
- **TelaListagem** - Listagem de usuários com ações (editar/excluir)
- **TelaEdicao** - Formulário de criação/edição de usuários

---

## 📁 Estrutura do Projeto

```
formCRUDJava/
├── pom.xml
├── .env                    # Configurações do banco de dados
├── src/main/java/
│   ├── app/
│   │   └── Main.java       # Classe principal
│   ├── config/
│   │   ├── Conexao.java    # Conexão com o banco MySQL
│   │   ├── EnvLoader.java  # Carrega variáveis do .env
│   │   └── Sessao.java     # Gerenciamento de sessão
│   ├── dao/
│   │   ├── PessoaDAO.java           # CRUD de usuários
│   │   ├── PermissaoDAO.java        # CRUD de permissões
│   │   └── UsuarioPermissaoDAO.java # Associação usuário-permissão
│   ├── model/
│   │   ├── Pessoa.java              # Entidade Usuário
│   │   ├── Permissao.java           # Entidade Permissão
│   │   └── UsuarioPermissao.java    # Entidade de relacionamento
│   ├── seed/
│   │   └── DatabaseSeed.java        # Criação automática do banco/tabelas
│   └── view/
│       ├── FormCRUD.java            # Formulário base
│       ├── TelaLogin.java           # Tela de login
│       ├── TelaAutocadastro.java    # Tela de cadastro
│       ├── TelaListagem.java        # Listagem de usuários
│       └── TelaEdicao.java          # Edição de usuários
```

---

## 🗄️ Estrutura do Banco de Dados

O sistema cria automaticamente o banco e as tabelas ao iniciar:

### Tabela `pessoa`
| Campo         | Tipo                        | Descrição               |
|---------------|-----------------------------|-------------------------|
| id            | INT (PK, AUTO_INCREMENT)    | Identificador único     |
| nome          | VARCHAR(100)                | Nome do usuário         |
| sobrenome     | VARCHAR(100)                | Sobrenome               |
| email         | VARCHAR(150) UNIQUE         | Email (único)           |
| telefone      | VARCHAR(20)                 | Telefone                |
| login         | VARCHAR(100) UNIQUE         | Login (único)           |
| senha         | VARCHAR(255)                | Senha                   |
| role          | ENUM('admin', 'user')       | Papel do usuário        |
| status        | TINYINT                     | 0 = Inativo, 1 = Ativo  |
| timecreated   | TIMESTAMP                   | Data de criação         |
| timemodified  | TIMESTAMP                   | Data de modificação     |

### Tabela `permissao`
| Campo        | Tipo                     | Descrição           |
|--------------|--------------------------|---------------------|
| id           | INT (PK, AUTO_INCREMENT) | Identificador único |
| nome         | VARCHAR(100) UNIQUE      | Nome da permissão   |
| status       | TINYINT                  | Status              |

### Tabela `usuario_permissao`
| Campo        | Tipo                     | Descrição           |
|--------------|--------------------------|---------------------|
| id           | INT (PK, AUTO_INCREMENT) | Identificador único |
| usuario_id   | INT (FK → pessoa.id)     | ID do usuário       |
| permissao_id | INT (FK → permissao.id)  | ID da permissão     |

---

## 📦 Como executar o projeto

### 1️⃣ Pré-requisitos
- Java 21 ou superior instalado
- Maven instalado
- MySQL Server instalado e em execução

### 2️⃣ Clonar o repositório
```bash
git clone https://github.com/kessedias/formCRUDJava.git formCRUD
cd formCRUD
```

### 3️⃣ Configurar o arquivo `.env`
Crie um arquivo `.env` na raiz do projeto com as configurações do banco:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=formcrud
DB_USER=root
DB_PASS=sua_senha
```

### 4️⃣ Compilar e executar
```bash
# Compilar o projeto
mvn compile

# Executar a aplicação
mvn exec:java -Dexec.mainClass="app.Main"
```

Ou simplesmente:
```bash
mvn compile exec:java
```

---

## 🔑 Primeiro Acesso

Ao iniciar o sistema pela primeira vez:

1. O banco de dados e as tabelas serão criados automaticamente
2. As permissões padrão serão inseridas
3. Use a opção **"Inscreva-se"** para criar o primeiro usuário
4. O primeiro usuário cadastrado será do tipo `user`
5. Para criar um `admin`, altere manualmente a role no banco de dados:

```sql
UPDATE pessoa SET role = 'admin' WHERE login = 'seu_login';
```

---

## 📝 Licença

Este projeto é de uso livre para fins educacionais.

---

## 👤 Autor

**Kesse Dias** - [GitHub](https://github.com/kessedias)
