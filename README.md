# 🗳️ Election System for Minecraft

A complete and customizable plugin for Paper 1.21.8 that adds an election system to your Minecraft server. Allows players to vote for candidates in an interactive and secure way.

## 📝 Note

I developed this plugin for a dynamic with my friends and decided to publish it here on GitHub.

## ✨ Features

- ✅ **Clickable interface** for voting
- ✅ **Single vote** per player
- ✅ **Persistent storage** of data
- ✅ **Real-time results** with percentages
- ✅ **Permission system** for administration
- ✅ **Intuitive commands** with tab-completion
- ✅ **Fully configurable**
- ✅ **Multi-language support** (English, Portuguese)
- ✅ **Multi-version compatibility**

## 🚀 Installation

1. **Download** the `election-system-1.0.0.jar` file
2. **Place** it in your server's `plugins` folder
3. **Restart** the server
4. **Done!** The plugin is working

## 📋 Commands

### 👤 Commands for Players

| Command | Description |
|---------|-------------|
| `/vote` | Shows the list of candidates to vote for |
| `/results` | Shows the results of the current election |

### ⚡ Commands for Administrators (OP)

| Command | Description |
|---------|-------------|
| `/election create <player>` | Adds a candidate to the election |
| `/election remove <player>` | Removes a candidate from the election |
| `/resetelection` | Completely resets the election |
| `/election lang <language>` | Changes your language (pt_BR, en_US) |

## 🎮 How to Use

### 1. Setting Up the Election
```mcfunction
# Add candidates (as OP)
/election create Steve
/election create Alex
/election create Enderman