# 🗳️ Sistema de Eleições para Minecraft

Um plugin completo e personalizável para Paper 1.21.8 que adiciona um sistema de eleições ao seu servidor Minecraft. Permite que jogadores votem em candidatos de forma interativa e segura.

# NOTA

Desenvolvi esse plugin para uma dinâmica com meus amigos e decidi publicar aqui no GitHub.
 
## ✨ Características

- ✅ **Interface clicável** para votação
- ✅ **Voto único** por jogador
- ✅ **Armazenamento persistente** de dados
- ✅ **Resultados em tempo real** com porcentagens
- ✅ **Sistema de permissões** para administração
- ✅ **Comandos intuitivos** e tab-completion
- ✅ **Totalmente configurável**

## 🚀 Instalação

1. **Baixe** o arquivo `sistema-eleicoes-1.0.0.jar`
2. **Cole** na pasta `plugins` do seu servidor
3. **Reinicie** o servidor
4. **Pronto!** O plugin está funcionando

## 📋 Comandos

### 👤 Comandos para Jogadores

| Comando | Descrição |
|---------|-----------|
| `/votar` | Mostra a lista de candidatos para votar |
| `/resultado` | Mostra os resultados da eleição atual |

### ⚡ Comandos para Administradores (OP)

| Comando | Descrição |
|---------|-----------|
| `/eleicao criar <player>` | Adiciona um candidato à eleição |
| `/eleicao remover <player>` | Remove um candidato da eleição |
| `/resetareleicao` | Reseta completamente a eleição |

## 🎮 Como Usar

### 1. Configurando a Eleição
```mcfunction
# Adicionar candidatos (como OP)
/eleicao criar Steve
/eleicao criar Alex
/eleicao criar Enderman
```
### 2. Votação dos Jogadores
```mcfunction
/votar <candidato>
``` 
### 3. Verificando os resultados
```mcfunction
/resultado
```
### 4. Quando quiser começar uma nova eleição
```mcfunction
/resetareleicao
```

# Configuração

O plugin cria automaticamente um arquivo de configuração em:
plugins/SistemaEleicoes/eleicao.yml

## Estrutura do arquivo de dados
```yaml
candidatos:
  - steve
  - alex
votos:
  jogador1: steve
  jogador2: alex
jogadoresQueVotaram:
  - uuid-do-jogador1
  - uuid-do-jogador2
```

# 🛡️ Permissões

| Permissão | Descrição | Padrão |
|---------|-----------|----------|
| `electionplugin.admin` | Acesso aos comandos administrativos | op | 
