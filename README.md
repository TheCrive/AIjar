# 🤖 UDPChat - AI-Powered Chat System

<p align="center">
  <img src="docs/images/logo.png" alt="UDPChat Logo" width="200"/>
</p>

<p align="center">
  <strong>Un sistema di chat intelligente che integra modelli AI locali tramite llama.cpp</strong>
</p>

<p align="center">
  <a href="#caratteristiche">Caratteristiche</a> •
  <a href="#architettura">Architettura</a> •
  <a href="#installazione">Installazione</a> •
  <a href="#configurazione">Configurazione</a> •
  <a href="#utilizzo">Utilizzo</a> •
  <a href="#api-reference">API</a> •
  <a href="#troubleshooting">Troubleshooting</a>
</p>

---

## 📋 Panoramica

UDPChat è un sistema di chat completo che combina programmazione di rete UDP con intelligenza artificiale locale. Il progetto integra un server Java con llama.cpp per fornire risposte AI in tempo reale attraverso un'interfaccia JavaFX moderna e reattiva.

### Perché UDPChat?

- **Privacy First**: I modelli AI girano localmente sul tuo server, nessun dato viene inviato a servizi esterni
- **Bassa Latenza**: Il protocollo UDP garantisce comunicazioni veloci e efficienti
- **Completamente Personalizzabile**: Scegli il modello AI che preferisci tra decine di opzioni GGUF
- **Open Source**: Codice completamente accessibile e modificabile

---

## ✨ Caratteristiche

### Core Features

| Feature | Descrizione |
|---------|-------------|
| 🔌 **Comunicazione UDP** | Protocollo leggero e veloce per lo scambio messaggi client-server |
| 🧠 **Integrazione AI** | Connessione diretta con llama.cpp server per inferenza di modelli linguistici |
| 🎨 **UI JavaFX** | Interfaccia grafica moderna con temi personalizzabili e animazioni |
| 💚 **Health Monitoring** | Sistema di verifica stato server AI con indicatori visivi in tempo reale |
| 📊 **Status Dashboard** | Monitoraggio continuo ogni 10 secondi con info su modello e risorse |
| ⚙️ **Systemd Integration** | Gestione professionale del servizio con avvio automatico e logging |

### Funzionalità Avanzate

- **Multi-Model Support**: Supporto per diversi modelli GGUF (Llama, Qwen, Mistral, etc.)
- **Cambio Modello Runtime**: Possibilità di cambiare modello AI senza riavviare l'applicazione
- **Comandi Speciali**: Sistema di comandi integrato (`/models`, `/model`, `/help`, `status`)
- **JSON Processing**: Parsing robusto con libreria Jackson
- **Cross-Platform**: Client compatibile con Windows, macOS e Linux

---

## 🏗️ Architettura

### Diagramma del Sistema

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              ARCHITETTURA UDPCHAT                            │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────┐         UDP          ┌─────────────────┐
│                 │ ◄──────────────────► │                 │
│   CLIENT JAVA   │      Port 51700      │   SERVER JAVA   │
│    (JavaFX)     │                      │    (Debian)     │
│                 │                      │                 │
└────────┬────────┘                      └────────┬────────┘
         │                                        │
         │                                        │ HTTP API
         │                                        │ Port 8080
         │                                        ▼
         │                               ┌─────────────────┐
         │                               │                 │
         │                               │  LLAMA-SERVER   │
         │                               │   (llama.cpp)   │
         │                               │                 │
         │                               └────────┬────────┘
         │                                        │
         │                                        │ Memory Mapping
         │                                        ▼
         │                               ┌─────────────────┐
         │                               │                 │
         │                               │  MODELLO GGUF   │
         │                               │  (Llama/Qwen/   │
         │                               │   Mistral...)   │
         │                               │                 │
         │                               └─────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────┐
│                        COMPONENTI CLIENT                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐       │
│  │   App.java   │───►│ ChatController│───►│ChatBotService│       │
│  │  (JavaFX UI) │    │  (Business)  │    │   (Network)  │       │
│  └──────────────┘    └──────────────┘    └──────┬───────┘       │
│                                                  │                │
│  ┌──────────────┐    ┌──────────────┐           │                │
│  │ HeaderView   │    │  HttpClient  │◄──────────┘                │
│  │(Status/Model)│    │ (Health API) │                            │
│  └──────────────┘    └──────────────┘                            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Struttura del Progetto

```
UDPChat/
├── 📁 src/
│   ├── 📄 App.java                 # Entry point JavaFX, gestione UI principale
│   ├── 📄 ChatController.java      # Controller logica business e gestione messaggi
│   ├── 📄 ChatBotService.java      # Servizio integrazione AI e comunicazione UDP
│   ├── 📄 UdpClient.java           # Client UDP per invio/ricezione pacchetti
│   ├── 📄 HttpClient.java          # Client HTTP per API llama-server
│   ├── 📄 HeaderView.java          # Componente UI header con indicatori stato
│   ├── 📄 MessageBubble.java       # Componente UI per messaggi chat
│   ├── 📄 AnimationUtils.java      # Utility per animazioni UI
│   └── 📄 Themeable.java           # Interfaccia per gestione temi
│
├── 📁 server/
│   └── 📄 Server.java              # Server UDP con integrazione AI
│
├── 📁 lib/
│   ├── 📁 javafx/lib/              # Librerie JavaFX SDK
│   │   ├── javafx.base.jar
│   │   ├── javafx.controls.jar
│   │   ├── javafx.fxml.jar
│   │   └── javafx.graphics.jar
│   │
│   └── 📁 jackson/                 # Librerie Jackson per JSON
│       ├── jackson-core-2.x.jar
│       ├── jackson-databind-2.x.jar
│       └── jackson-annotations-2.x.jar
│
├── 📁 bin/                         # Classi compilate
├── 📁 resources/
│   └── 📁 icons/                   # Icone applicazione
│       └── logo.png
│
├── 📁 docs/
│   ├── 📁 images/                  # Immagini documentazione
│   └── 📄 API.md                   # Documentazione API dettagliata
│
├── 📄 build.bat                    # Script build Windows
├── 📄 build.sh                     # Script build Linux/macOS
├── 📄 run.bat                      # Script avvio Windows
├── 📄 run.sh                       # Script avvio Linux/macOS
└── 📄 README.md                    # Questa documentazione
```

### Flusso dei Dati

```
┌──────────────────────────────────────────────────────────────────────────┐
│                           FLUSSO MESSAGGIO UTENTE                         │
└──────────────────────────────────────────────────────────────────────────┘

    ┌─────────┐     ┌──────────────┐     ┌───────────────┐     ┌──────────┐
    │  UTENTE │────►│   App.java   │────►│ChatController │────►│  ChatBot │
    │ (Input) │     │   (UI Event) │     │  (Validate)   │     │ Service  │
    └─────────┘     └──────────────┘     └───────────────┘     └────┬─────┘
                                                                     │
                    ┌──────────────────────────────────────────────┘
                    │
                    ▼
    ┌───────────────────┐     ┌─────────────────┐     ┌─────────────────┐
    │    UdpClient      │────►│  SERVER UDP     │────►│  chiediALlama() │
    │ (Send UDP Packet) │     │  (Port 51700)   │     │  (HTTP POST)    │
    └───────────────────┘     └─────────────────┘     └────────┬────────┘
                                                               │
                    ┌──────────────────────────────────────────┘
                    │
                    ▼
    ┌───────────────────┐     ┌─────────────────┐     ┌─────────────────┐
    │   LLAMA-SERVER    │────►│  MODELLO GGUF   │────►│    RISPOSTA     │
    │ /v1/chat/complete │     │   (Inference)   │     │      JSON       │
    └───────────────────┘     └─────────────────┘     └────────┬────────┘
                                                               │
                    ┌──────────────────────────────────────────┘
                    │
                    ▼
    ┌───────────────────┐     ┌─────────────────┐     ┌─────────────────┐
    │   Parse Response  │────►│  UDP Response   │────►│   Update UI     │
    │   (Jackson JSON)  │     │  (To Client)    │     │  (MessageBubble)│
    └───────────────────┘     └─────────────────┘     └─────────────────┘
```

---

## 💻 Requisiti di Sistema

### Server (Debian 12/13 o Ubuntu 22.04+)

| Componente | Requisito Minimo | Consigliato |
|------------|------------------|-------------|
| **CPU** | 4 core | 8+ core |
| **RAM** | 4 GB | 16+ GB |
| **Storage** | 20 GB | 50+ GB (per modelli multipli) |
| **OS** | Debian 12 / Ubuntu 22.04 | Debian 13 / Ubuntu 24.04 |

**Software Richiesto:**
- CMake 3.14+
- GCC/G++ 11+
- OpenBLAS (per ottimizzazione CPU)
- systemd (per gestione servizi)
- Java 17+ (per Server.java)

### Client (Windows/macOS/Linux)

| Componente | Requisito |
|------------|-----------|
| **Java** | OpenJDK 17 o superiore |
| **JavaFX** | SDK 17+ (incluso nel progetto) |
| **RAM** | 512 MB minimo |
| **Display** | 1280x720 minimo |

---

## 🚀 Installazione

### Parte 1: Setup Server (Debian/Ubuntu)

#### 1.1 Installazione Dipendenze di Sistema

```bash
# Aggiorna il sistema
sudo apt update && sudo apt upgrade -y

# Installa dipendenze di compilazione
sudo apt install -y \
    build-essential \
    cmake \
    git \
    libopenblas-dev \
    pkg-config \
    wget \
    curl

# Installa Java (per Server.java)
sudo apt install -y openjdk-17-jdk

# Verifica installazioni
cmake --version
java --version
```

#### 1.2 Compilazione llama.cpp

```bash
# Crea directory di lavoro
sudo mkdir -p /srv/llama.cpp
sudo chown $USER:$USER /srv/llama.cpp
cd /srv/llama.cpp

# Clone repository
git clone https://github.com/ggerganov/llama.cpp .

# Compila con supporto OpenBLAS (CPU ottimizzato)
cmake -B build \
    -DGGML_BLAS=ON \
    -DGGML_BLAS_VENDOR=OpenBLAS \
    -DCMAKE_BUILD_TYPE=Release

cmake --build build --config Release -j$(nproc)

# Verifica compilazione
./build/bin/llama-server --version
```

#### 1.3 Download Modelli GGUF

```bash
# Crea directory modelli
mkdir -p /srv/llama.cpp/models
cd /srv/llama.cpp/models

# Opzione 1: Download diretto (esempio Llama 3.2 1B)
wget https://huggingface.co/bartowski/Llama-3.2-1B-Instruct-GGUF/resolve/main/Llama-3.2-1B-Instruct-Q4_K_M.gguf

# Opzione 2: Usa huggingface-cli (consigliato per modelli grandi)
pip install huggingface_hub
huggingface-cli download \
    TheBloke/Llama-2-7B-Chat-GGUF \
    llama-2-7b-chat.Q4_K_M.gguf \
    --local-dir .

# Verifica integrità del file (deve mostrare "GGUF")
hexdump -C Llama-3.2-1B-Instruct-Q4_K_M.gguf | head -1
# Output atteso: 00000000  47 47 55 46 ...  |GGUF...|
```

**Modelli Consigliati:**

| Modello | Dimensione | RAM Richiesta | Use Case |
|---------|------------|---------------|----------|
| Llama-3.2-1B-Q4_K_M | ~700 MB | 2 GB | Testing, chat leggere |
| Llama-3.2-3B-Q4_K_M | ~2 GB | 4 GB | Uso generale |
| Qwen2.5-7B-Q4_K_M | ~4.5 GB | 8 GB | Alta qualità |
| Mistral-7B-Q4_K_M | ~4.5 GB | 8 GB | Coding, reasoning |

#### 1.4 Configurazione Systemd Service

Crea il file di servizio per llama-server:

```bash
sudo nano /etc/systemd/system/llama-server.service
```

Contenuto del file:

```ini
[Unit]
Description=Llama.cpp AI Server
Documentation=https://github.com/ggerganov/llama.cpp
After=network.target

[Service]
Type=simple
User=root
Group=root
WorkingDirectory=/srv/llama.cpp

# Comando di avvio - modifica il percorso del modello secondo necessità
ExecStart=/srv/llama.cpp/build/bin/llama-server \
    --model /srv/llama.cpp/models/Llama-3.2-1B-Instruct-Q4_K_M.gguf \
    --host 0.0.0.0 \
    --port 8080 \
    --ctx-size 2048 \
    --threads 4 \
    --n-gpu-layers 0

# Gestione errori e riavvio automatico
Restart=on-failure
RestartSec=5
StartLimitIntervalSec=60
StartLimitBurst=3

# Logging
StandardOutput=journal
StandardError=journal
SyslogIdentifier=llama-server

# Sicurezza (opzionale ma consigliato)
NoNewPrivileges=true
ProtectSystem=strict
ProtectHome=true
ReadWritePaths=/srv/llama.cpp/logs

[Install]
WantedBy=multi-user.target
```

Attiva il servizio:

```bash
# Ricarica systemd
sudo systemctl daemon-reload

# Abilita avvio automatico
sudo systemctl enable llama-server

# Avvia il servizio
sudo systemctl start llama-server

# Verifica stato
sudo systemctl status llama-server
```

#### 1.5 Setup Server UDP Java

```bash
# Crea directory per il server Java
sudo mkdir -p /srv/udp-server/{lib,logs}
sudo chown -R $USER:$USER /srv/udp-server

# Copia Server.java nella directory
cp /path/to/Server.java /srv/udp-server/

# Scarica Jackson libraries
cd /srv/udp-server/lib
wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.15.3/jackson-core-2.15.3.jar
wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.15.3/jackson-databind-2.15.3.jar
wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.15.3/jackson-annotations-2.15.3.jar

# Compila il server
cd /srv/udp-server
javac -cp "lib/*" -d . Server.java
```

Crea servizio systemd per il Server UDP:

```bash
sudo nano /etc/systemd/system/udp-chat-server.service
```

```ini
[Unit]
Description=UDP Chat Server with AI
After=network.target llama-server.service
Requires=llama-server.service

[Service]
Type=simple
User=root
WorkingDirectory=/srv/udp-server
ExecStart=/usr/bin/java -cp ".:lib/*" Server
Restart=always
RestartSec=3

StandardOutput=append:/srv/udp-server/logs/server.log
StandardError=append:/srv/udp-server/logs/server.log

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable udp-chat-server
sudo systemctl start udp-chat-server
```

#### 1.6 Configurazione Firewall

```bash
# Apri porta per llama-server (HTTP API)
sudo ufw allow 8080/tcp

# Apri porta per UDP Chat Server
sudo ufw allow 51700/udp

# Verifica regole
sudo ufw status
```

### Parte 2: Setup Client (Windows/macOS/Linux)

#### 2.1 Requisiti

Scarica e installa:
- [OpenJDK 17+](https://adoptium.net/)
- [JavaFX SDK 17+](https://openjfx.io/)

#### 2.2 Clone e Configurazione

```bash
# Clone repository
git clone https://github.com/tuousername/UDPChat.git
cd UDPChat

# Struttura attesa
# UDPChat/
# ├── src/
# ├── lib/
# │   ├── javafx/lib/
# │   └── jackson/
# └── bin/
```

#### 2.3 Configurazione Connessione Server

Modifica `src/ChatBotService.java`:

```java
// Cambia questi valori con l'IP del tuo server
private static final String SERVER_IP = "tuo-server-ip";  // es. "192.168.1.100" o "mioserver.com"
private static final int SERVER_PORT = 51700;
private static final String LLAMA_API_URL = "http://tuo-server-ip:8080";
```

#### 2.4 Compilazione

**Windows (build.bat):**
```batch
@echo off
echo Compilazione UDPChat...
javac --module-path "lib\javafx\lib" ^
      --add-modules javafx.controls,javafx.fxml ^
      -cp "bin;lib\jackson\*" ^
      -d bin ^
      src\*.java
echo Compilazione completata!
```

**Linux/macOS (build.sh):**
```bash
#!/bin/bash
echo "Compilazione UDPChat..."
javac --module-path "lib/javafx/lib" \
      --add-modules javafx.controls,javafx.fxml \
      -cp "bin:lib/jackson/*" \
      -d bin \
      src/*.java
echo "Compilazione completata!"
```

#### 2.5 Esecuzione

**Windows (run.bat):**
```batch
@echo off
java --module-path "lib\javafx\lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     -cp "bin;lib\jackson\*" ^
     App
```

**Linux/macOS (run.sh):**
```bash
#!/bin/bash
java --module-path "lib/javafx/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "bin:lib/jackson/*" \
     App
```

---

## ⚙️ Configurazione

### Configurazione Server llama.cpp

#### Parametri Principali

| Parametro | Default | Descrizione |
|-----------|---------|-------------|
| `--model` | - | Percorso al file .gguf del modello |
| `--host` | 127.0.0.1 | IP su cui ascoltare (0.0.0.0 per tutti) |
| `--port` | 8080 | Porta HTTP API |
| `--ctx-size` | 512 | Dimensione contesto (token) |
| `--threads` | auto | Numero thread CPU |
| `--n-gpu-layers` | 0 | Layer da caricare su GPU (0 = solo CPU) |
| `--parallel` | 1 | Richieste parallele |

#### Esempi di Configurazione

**Configurazione Base (Low RAM):**
```bash
llama-server \
    --model models/llama-3.2-1b-q4.gguf \
    --host 0.0.0.0 \
    --port 8080 \
    --ctx-size 1024 \
    --threads 4
```

**Configurazione Media (8GB RAM):**
```bash
llama-server \
    --model models/qwen2.5-7b-q4.gguf \
    --host 0.0.0.0 \
    --port 8080 \
    --ctx-size 2048 \
    --threads 8 \
    --parallel 2
```

**Configurazione High-End (16GB+ RAM):**
```bash
llama-server \
    --model models/llama-3.1-70b-q4.gguf \
    --host 0.0.0.0 \
    --port 8080 \
    --ctx-size 4096 \
    --threads 16 \
    --parallel 4
```

### Configurazione Client Java

#### ChatBotService.java

```java
public class ChatBotService {
    // Configurazione Server
    private static final String SERVER_IP = "192.168.1.100";
    private static final int UDP_PORT = 51700;
    private static final String LLAMA_API_URL = "http://192.168.1.100:8080";
    
    // Timeout configurazioni
    private static final int CONNECTION_TIMEOUT = 5000;  // ms
    private static final int READ_TIMEOUT = 30000;       // ms
    
    // ... resto del codice
}
```

#### HttpClient.java

```java
public class HttpClient {
    private final String baseUrl;
    private final int timeoutMs;
    
    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.timeoutMs = 5000;  // Timeout per health check
    }
    
    // Metodi per /health e /props
    public boolean isHealthy() { ... }
    public JsonNode getProps() { ... }
}
```

### Intervallo Health Check

In `App.java`, puoi modificare l'intervallo di controllo dello stato:

```java
private void startStatusChecker() {
    Timeline statusChecker = new Timeline(
        new KeyFrame(Duration.seconds(10), e -> updateLlamaStatus())  // Ogni 10 secondi
    );
    statusChecker.setCycleCount(Timeline.INDEFINITE);
    statusChecker.play();
}
```

---

## 📖 Utilizzo

### Avvio del Sistema

#### 1. Avvia il Server AI (sul server Debian)

```bash
# Verifica che llama-server sia attivo
sudo systemctl status llama-server

# Se non attivo, avvialo
sudo systemctl start llama-server

# Verifica che risponda
curl http://localhost:8080/health
# Output atteso: {"status":"ok"}
```

#### 2. Avvia il Server UDP

```bash
# Verifica stato
sudo systemctl status udp-chat-server

# Avvia se necessario
sudo systemctl start udp-chat-server

# Visualizza log in tempo reale
sudo journalctl -u udp-chat-server -f
```

#### 3. Avvia il Client

```bash
# Windows
run.bat

# Linux/macOS
./run.sh
```

### Interfaccia Utente

```
┌─────────────────────────────────────────────────────────────────────┐
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │  🤖 UDPChat           Llama-3.2-1B      ● Online            │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                                                              │    │
│  │  ┌──────────────────────────────────────────┐               │    │
│  │  │ Ciao! Come posso aiutarti oggi?          │  🤖           │    │
│  │  └──────────────────────────────────────────┘               │    │
│  │                                                              │    │
│  │               ┌──────────────────────────────────────────┐  │    │
│  │          👤   │ Spiegami cosa sono i buchi neri          │  │    │
│  │               └──────────────────────────────────────────┘  │    │
│  │                                                              │    │
│  │  ┌──────────────────────────────────────────┐               │    │
│  │  │ I buchi neri sono regioni dello spazio   │  🤖           │    │
│  │  │ dove la gravità è così intensa che       │               │    │
│  │  │ nulla può sfuggire, nemmeno la luce...   │               │    │
│  │  └──────────────────────────────────────────┘               │    │
│  │                                                              │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │ Scrivi un messaggio...                              [Invia] │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
```

### Comandi Speciali

Il sistema supporta comandi speciali che puoi digitare nella chat:

| Comando | Descrizione | Esempio Output |
|---------|-------------|----------------|
| `/models` | Lista tutti i modelli disponibili | `=== MODELLI DISPONIBILI ===`<br>`[ATTIVO] Llama3.gguf (2048 MB)`<br>`Qwen2.5.gguf (4500 MB)` |
| `/model <nome>` | Cambia modello attivo | `Modello cambiato: Qwen2.5.gguf` |
| `/help` | Mostra comandi disponibili | Lista comandi con descrizioni |
| `status` | Stato del server | `Server AI attivo - Pacchetti: 42` |
| `ping` | Test connessione | `pong` |

### Indicatori di Stato

L'header dell'applicazione mostra:

- **Nome Modello**: Il modello AI attualmente in uso
- **Stato Connessione**:
  - 🟢 **Online** - Server raggiungibile e funzionante
  - 🔴 **Offline** - Server non raggiungibile

---

## 📡 API Reference

### Endpoint llama-server

#### GET /health
Verifica lo stato del server.

```bash
curl http://localhost:8080/health
```

**Response:**
```json
{
  "status": "ok"
}
```

#### GET /props
Restituisce le proprietà del modello caricato.

```bash
curl http://localhost:8080/props
```

**Response:**
```json
{
  "default_generation_settings": {
    "model": "/srv/llama.cpp/models/Llama-3.2-1B.gguf",
    "n_ctx": 2048,
    "n_predict": -1,
    "temperature": 0.8,
    "top_k": 40,
    "top_p": 0.95
  },
  "total_slots": 1,
  "chat_template": "..."
}
```

#### POST /v1/chat/completions
Endpoint principale per chat (compatibile OpenAI).

```bash
curl -X POST http://localhost:8080/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {"role": "system", "content": "Sei un assistente utile. Rispondi in italiano."},
      {"role": "user", "content": "Ciao, come stai?"}
    ],
    "temperature": 0.7,
    "max_tokens": 150,
    "stream": false
  }'
```

**Response:**
```json
{
  "id": "chatcmpl-xxx",
  "object": "chat.completion",
  "created": 1234567890,
  "model": "llama-3.2-1b",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "Ciao! Sto bene, grazie per avermelo chiesto. Come posso aiutarti oggi?"
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 25,
    "completion_tokens": 18,
    "total_tokens": 43
  }
}
```

### Protocollo UDP

Il server UDP ascolta sulla porta 51700 e accetta messaggi in formato testo semplice.

**Request Format:**
```
<messaggio_testo>
```

**Response Format:**
```
<risposta_ai_o_comando>
```

**Esempio con netcat:**
```bash
echo "Ciao, come stai?" | nc -u -w 5 server-ip 51700
```

---

## 🔧 Troubleshooting

### Problemi Comuni

#### llama-server non si avvia

**Sintomi:** Il servizio fallisce all'avvio.

**Diagnosi:**
```bash
sudo journalctl -u llama-server -n 50 --no-pager
```

**Soluzioni comuni:**

1. **Modello corrotto:**
```bash
# Verifica header del file
hexdump -C /srv/llama.cpp/models/model.gguf | head
# Deve iniziare con "GGUF"

# Se corrotto, riscarica il modello
rm model.gguf
wget [url-modello]
```

2. **Memoria insufficiente:**
```bash
# Controlla memoria disponibile
free -h

# Usa un modello più piccolo o riduci ctx-size
--ctx-size 1024
```

3. **Porta già in uso:**
```bash
# Trova processo sulla porta
sudo lsof -i :8080

# Termina o usa porta diversa
--port 8081
```

#### Client non si connette

**Sintomi:** Timeout o "Connection refused".

**Checklist:**

1. **Verifica che il server sia attivo:**
```bash
curl http://server-ip:8080/health
```

2. **Verifica firewall:**
```bash
# Sul server
sudo ufw status

# Test connettività
nc -zv server-ip 8080
nc -zuv server-ip 51700
```

3. **Verifica IP corretto in ChatBotService.java**

#### Risposte AI troppo lente

**Soluzioni:**

1. **Aumenta thread CPU:**
```bash
--threads 8  # Usa più core
```

2. **Riduci context size:**
```bash
--ctx-size 1024  # Meno memoria, più veloce
```

3. **Usa modello più piccolo:**
   - Passa da 7B a 3B o 1B

4. **Abilita GPU (se disponibile):**
```bash
--n-gpu-layers 35  # Carica layer su GPU
```

#### Risposte AI di bassa qualità

**Soluzioni:**

1. **Migliora il system prompt in Server.java:**
```java
String systemPrompt = "Sei un assistente AI esperto e preciso. " +
    "Rispondi sempre in italiano in modo chiaro e conciso. " +
    "Se non conosci la risposta, ammettilo onestamente.";
```

2. **Regola temperature:**
```java
requestJson.put("temperature", 0.7);  // 0.1=preciso, 1.0=creativo
```

3. **Usa modello di qualità superiore:**
   - Passa da Q4 a Q5 o Q8 quantization
   - Usa modello più grande

#### Errore "Out of Memory"

**Soluzioni:**

1. **Usa modello più piccolo** (1B invece di 7B)

2. **Riduci context window:**
```bash
--ctx-size 512
```

3. **Aggiungi swap (Linux):**
```bash
sudo fallocate -l 8G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
```

### Log e Debugging

#### Visualizzare log llama-server

```bash
# Log in tempo reale
sudo journalctl -u llama-server -f

# Ultimi 100 log
sudo journalctl -u llama-server -n 100

# Log di oggi
sudo journalctl -u llama-server --since today
```

#### Visualizzare log Server UDP

```bash
# Se usa journald
sudo journalctl -u udp-chat-server -f

# Se usa file log
tail -f /srv/udp-server/logs/server.log
```

#### Debug Client Java

```bash
# Avvia con output debug
java --module-path "lib/javafx/lib" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "bin:lib/jackson/*" \
     -Djava.util.logging.level=FINE \
     App 2>&1 | tee debug.log
```

---

## 📊 Performance Tuning

### Ottimizzazione Server

#### CPU
```bash
# Usa tutti i core disponibili
--threads $(nproc)

# Verifica utilizzo CPU
htop
```

#### Memoria
```bash
# Modelli quantizzati usano meno RAM
# Q4_K_M: ~4 bits per peso (più piccolo)
# Q8_0: ~8 bits per peso (migliore qualità)

# Esempio con memory mapping efficiente
--mlock  # Blocca modello in RAM (evita swap)
```

#### GPU (NVIDIA)
```bash
# Compila con CUDA
cmake -B build -DGGML_CUDA=ON

# Usa GPU per inferenza
--n-gpu-layers 35  # Numero di layer su GPU
```

### Ottimizzazione Client

#### Riduzione Latenza
```java
// Riduci timeout per health check
private static final int HEALTH_TIMEOUT = 2000;  // 2 secondi invece di 5

// Usa connection pooling per HTTP
private final HttpClient httpClient = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(2))
    .build();
```

#### Caching Risposte
```java
// Cache per risposte frequenti
private Map<String, String> responseCache = new HashMap<>();
private static final int CACHE_SIZE = 100;
```

---

## 🤝 Contribuire

Le contribuzioni sono benvenute! Ecco come puoi aiutare:

### Reporting Bug

1. Verifica che il bug non sia già stato segnalato
2. Crea una nuova Issue con:
   - Descrizione chiara del problema
   - Passi per riprodurlo
   - Output di log rilevante
   - Versione OS e Java

### Feature Request

1. Apri una Issue descrivendo la funzionalità
2. Spiega il caso d'uso
3. Proponi un'implementazione (opzionale)

### Pull Request

1. Fork del repository
2. Crea un branch per la feature: `git checkout -b feature/nuova-feature`
3. Commit delle modifiche: `git commit -m 'Aggiunge nuova feature'`
4. Push al branch: `git push origin feature/nuova-feature`
5. Apri una Pull Request

### Coding Standards

- Usa nomi variabili descrittivi in italiano o inglese (coerente)
- Commenta il codice complesso
- Segui le convenzioni Java standard
- Testa le modifiche prima di committare

---

## 📜 Licenza

Questo progetto è rilasciato sotto la licenza MIT. Vedi il file [LICENSE](LICENSE) per i dettagli.

```
MIT License

Copyright (c) 2024 Filippo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🙏 Riconoscimenti

- [llama.cpp](https://github.com/ggerganov/llama.cpp) - Inferenza LLM in C/C++
- [Meta AI](https://ai.meta.com/) - Modelli Llama
- [Hugging Face](https://huggingface.co/) - Repository modelli
- [OpenJFX](https://openjfx.io/) - JavaFX SDK
- [Jackson](https://github.com/FasterXML/jackson) - JSON processing

---

## 📞 Contatti

- **Autore**: Filippo
- **GitHub**: [@tuousername](https://github.com/tuousername)
- **Email**: tua@email.com

---

<p align="center">
  <strong>⭐ Se questo progetto ti è utile, lascia una stella! ⭐</strong>
</p>

<p align="center">
  Made with ❤️ in Italy
</p>
