# DESCRIÇÃO TÉCNICA PARA REFATORAÇÃO DO SISTEMA – formCRUDJava

## Contexto atual do sistema

O projeto atual é um sistema **Java Desktop (Swing / JFrame)** com um CRUD simples de usuários (Pessoa).

### Arquitetura atual
- Java puro (sem Spring)
- Swing / JFrame para UI
- DAO para acesso a dados
- JDBC
- Banco criado via `DatabaseSeed`
- Estrutura de pacotes:
    - `app` → classe Main
    - `config` → Conexao, EnvLoader
    - `dao` → PessoaDAO
    - `model` → Pessoa
    - `seed` → DatabaseSeed
    - `view` → FormCRUD (tela única)

### Situação atual
- Apenas uma tela
- Tela única realiza:
    - Cadastro
    - Listagem
    - Atualização
    - Exclusão
- Não existe autenticação
- Não existe controle de permissões
- Não existe separação de responsabilidades por tela

---

## Objetivo da refatoração

Refatorar o sistema para incluir:

1. Autenticação (login e senha)
2. Controle de permissões
3. Separação de telas
4. Regras de acesso baseadas em role e permissões
5. Persistência de permissões no banco
6. Seeds automáticos para permissões e usuários

### Restrições
- Manter Java + Swing + DAO + JDBC
- Não utilizar frameworks como Spring ou Hibernate
- Sistema simples, acadêmico e didático

---

## Modelo de Dados (Banco)

### Pessoa (Usuário)

**Tabela:** `pessoa`

#### Campos
- `id` (PK)
- `nome`
- `sobrenome`
- `email` (único)
- `telefone`
- `login` (único)
- `senha` (string, sem criptografia avançada)
- `role` (`admin` | `user`)
- `status` (0 = inativo, 1 = ativo)
- `timecreated`
- `timemodified`

#### Regras
- `login` não pode se repetir
- `email` não pode se repetir
- Em caso de duplicidade:
    - Exibir `JOptionPane` com a mensagem:
      > "Login ou email não permitido"

---

### Permissao

**Tabela:** `permissao`

#### Campos
- `id` (PK)
- `nome`
- `status` (0 ou 1)
- `timecreated`
- `timemodified`

#### Permissões iniciais obrigatórias
1. `LISTAR_DADOS`
2. `CRIAR_DADOS`
3. `ATUALIZAR_DADOS`
4. `EXCLUIR_DADOS`

> As permissões devem ser criadas automaticamente via seed.

---

### Usuario_Permissao

**Tabela:** `usuario_permissao`

#### Campos
- `id` (PK)
- `usuario_id` (FK → pessoa.id)
- `permissao_id` (FK → permissao.id)
- `status` (0 ou 1)
- `timecreated`
- `timemodified`

#### Relacionamentos
- Pessoa 1:N Usuario_Permissao
- Permissao 1:N Usuario_Permissao

---

## Seeds (DatabaseSeed)

O `DatabaseSeed` deve:

1. Criar as tabelas:
    - `pessoa`
    - `permissao`
    - `usuario_permissao`

2. Inserir as permissões iniciais:
    - LISTAR_DADOS
    - CRIAR_DADOS
    - ATUALIZAR_DADOS
    - EXCLUIR_DADOS

3. Ao criar um usuário `admin`:
    - Associar automaticamente as 4 permissões (`status = 1`)

4. Ao criar um usuário `user`:
    - Associar apenas:
        - LISTAR_DADOS
        - ATUALIZAR_DADOS

---

## Regras de Permissão e Acesso

### Usuário ADMIN

- Role: `admin`
- Permissões:
    - Listar dados
    - Criar dados
    - Atualizar dados
    - Excluir dados

Pode:
- Visualizar todos os usuários
- Executar CRUD completo
- Editar qualquer usuário
- Excluir qualquer usuário

---

### Usuário USER

- Role: `user`
- Permissões:
    - Listar dados
    - Atualizar dados

Pode:
- Listar apenas usuários com role `user`
- Atualizar dados de usuários `user`

Não pode:
- Criar usuários
- Excluir usuários
- Visualizar usuários `admin`

---

## Telas do Sistema (Swing / JFrame)

### 1. Tela de Login

#### Componentes
- Campo Login
- Campo Senha
- Botão **Entrar**
- Botão **Inscreva-se**

#### Fluxo
- Usuário informa login e senha
- Sistema valida no banco
- Se válido:
    - Redireciona para Tela de Listagem
- Se inválido:
    - Exibir alerta (`JOptionPane`)

---

### 2. Tela de Autocadastro

Acessada pelo botão **Inscreva-se** na tela de login.

#### Campos obrigatórios
- Nome
- Sobrenome
- Email
- Telefone
- Login
- Senha
- Role (Dropdown):
    - admin
    - user

#### Regras
- Todos os campos são obrigatórios
- Login e email devem ser únicos
- Após cadastro bem-sucedido:
    - Criar usuário
    - Criar permissões conforme role
    - Redirecionar automaticamente para a Tela de Listagem

---

### 3. Tela de Listagem de Usuários

#### Para ADMIN
Tabela contendo:
- ID
- Nome
- Sobrenome
- Email
- Telefone
- Role
- Status
- Coluna **Ações**
    - Editar
    - Excluir

Ações:
- Editar → abre tela de edição
- Excluir → remove do banco e atualiza a listagem

---

#### Para USER
Tabela contendo:
- ID
- Nome
- Sobrenome
- Email
- Telefone

Sem:
- Botão de criar
- Botão de excluir
- Visualização de usuários admin

---

### 4. Tela de Edição de Usuário

- Formulário com dados do usuário
- Botão **Atualizar**

#### Regras
- USER só pode editar usuários com role `user`
- ADMIN pode editar qualquer usuário

---

## Regras Gerais

- Controle de acesso deve existir:
    - Na UI (exibição ou ocultação de botões)
    - No DAO (validações básicas)
- Manter padrão DAO
- Código simples, legível e didático
- Priorizar clareza em vez de abstrações complexas

---

## Fluxos do Sistema

### Usuário Existente
1. Abre a aplicação
2. Tela de login
3. Login com sucesso
4. Tela de listagem
5. Ações conforme role

---

### Usuário Novo
1. Abre a aplicação
2. Tela de login
3. Clica em **Inscreva-se**
4. Realiza autocadastro
5. Usuário e permissões criados
6. Redireciona para listagem
7. Ações conforme role

---

## Fora de Escopo

- Criptografia avançada de senha
- JWT, sessões ou tokens
- Logs complexos
- Frameworks externos

---

## Objetivo Final

Transformar o CRUD simples atual em um sistema com:

- Autenticação
- Autorização
- Múltiplas telas
- Controle de permissões

Mantendo o projeto **simples, acadêmico e funcional**.
