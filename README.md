# AIjar
UDPChat - AI-Powered Chat System
Un sistema di chat basato su UDP che integra modelli di linguaggio AI tramite llama.cpp server. Il progetto combina programmazione di rete, deployment di modelli AI e interfaccia JavaFX per creare un'applicazione di chat che può comunicare con modelli linguistici ospitati localmente.
Caratteristiche

Architettura Client-Server UDP: Comunicazione efficiente basata su protocollo UDP
Integrazione AI: Connessione diretta con llama.cpp server per inferenza di modelli linguistici
Interfaccia JavaFX: UI moderna e reattiva con componenti personalizzati
Monitoraggio Health Check: Sistema di verifica stato server AI via HTTP API ogni 10 secondi
Zero Dipendenze Esterne (Opzionale): Possibilità di parsing JSON manuale senza librerie aggiuntive
Gestione Systemd: Configurazione per avvio automatico e logging

Architettura


L'utente invia un messaggio tramite l'UI JavaFX
ChatController gestisce l'input e lo inoltra a ChatBotService
ChatBotService invia il prompt al llama-server via HTTP API
La risposta AI viene ricevuta e processata (JSON parsing con Jackson)
Il messaggio viene distribuito via UDP agli altri client
L'UI viene aggiornata con la risposta

Requisiti
Server (Debian 12/13)

llama.cpp compilato con CMake
OpenBLAS per ottimizzazione performance
systemd per gestione servizio
Modelli GGUF (es. Llama-3.2-1B, Qwen 2.5 7B, Guanaco-13B)

Client (Java)

OpenJDK 17 o superiore
JavaFX SDK
Jackson library per JSON parsing
Dipendenze Maven/Gradle (vedere pom.xml)

Installazione
1. Setup Server llama.cpp
bash# Installazione dipendenze
sudo apt update
sudo apt install cmake build-essential libopenblas-dev

# Clone e compilazione llama.cpp
git clone https://github.com/ggerganov/llama.cpp
cd llama.cpp
cmake -B build -DGGML_BLAS=ON -DGGML_BLAS_VENDOR=OpenBLAS
cmake --build build --config Release

# Download modello (esempio)
cd models
wget https://huggingface.co/[repo]/[model]/resolve/main/model.gguf

# Verifica integrità file
hexdump -C model.gguf | head
2. Configurazione Systemd Service
Crea /etc/systemd/system/llama-server.service:
ini[Unit]
Description=Llama.cpp Server
After=network.target

[Service]
Type=simple
User=your-user
WorkingDirectory=/srv/llama-server
ExecStart=/path/to/llama.cpp/build/bin/llama-server \
    -m /path/to/models/model.gguf \
    --host 0.0.0.0 \
    --port 8080 \
    -c 2048 \
    --log-file /srv/llama-server/logs/server.log

Restart=on-failure
RestartSec=5s

[Install]
WantedBy=multi-user.target
Attiva il servizio:
bashsudo systemctl daemon-reload
sudo systemctl enable llama-server
sudo systemctl start llama-server
sudo systemctl status llama-server
3. Build Applicazione Java
bash# Clone repository
git clone https://github.com/your-username/udpchat
cd udpchat

# Build con Maven
mvn clean package

# Oppure con Gradle
gradle build
Configurazione
Client Java
Modifica i parametri di connessione in ChatBotService.java:
javaprivate static final String LLAMA_SERVER_URL = "http://your-server-ip:8080";
private static final int UDP_PORT = 9876;
Health Check Interval
In HeaderView.java puoi modificare l'intervallo di verifica stato:
javaprivate static final long HEALTH_CHECK_INTERVAL = 10000; // millisecondi
Utilizzo
Avvio Server
bash# Manuale
./llama.cpp/build/bin/llama-server -m models/model.gguf --host 0.0.0.0 --port 8080

# Con systemd
sudo systemctl start llama-server
Avvio Client
bashjava -jar udpchat.jar
Monitoraggio
bash# Verifica stato server
curl http://localhost:8080/health

# Visualizza logs
journalctl -u llama-server -f

# Oppure
tail -f /srv/llama-server/logs/server.log
Modelli AI Supportati
Il sistema supporta modelli in formato GGUF. Esempi testati:

Llama-3.2-1B: Ideale per testing, ~1.5GB RAM
Qwen 2.5 7B: Bilanciamento qualità/performance, ~4-8GB RAM
Guanaco-13B: Alta qualità, ~8-16GB RAM

Nota sulla Memoria
Grazie alla quantizzazione e memory mapping di llama.cpp, i modelli possono tecnicamente funzionare su sistemi con meno RAM della dimensione del file, ma con trade-off sulle performance.
Token Limits
Per prevenire risposte incomplete e gestire le risorse:
bashllama-server -m model.gguf -c 2048  # context length
Valori consigliati:

Chat brevi: 512-1024 token
Conversazioni medie: 2048 token
Documenti lunghi: 4096+ token

Troubleshooting
Modello Corrotto
Verifica header del file:
bashhexdump -C model.gguf | head
# Deve iniziare con magic number GGUF
Server Non Risponde
bash# Verifica porta
sudo netstat -tlnp | grep 8080

# Verifica firewall
sudo ufw status
sudo ufw allow 8080/tcp
Performance Scadenti

Verifica compilazione con OpenBLAS
Considera quantizzazione più aggressiva (Q4 vs Q8)
Riduci context length (-c parametro)

Roadmap

 Supporto per modelli multimodali
 Implementazione parsing JSON manuale (zero dipendenze)
 Ottimizzazione gestione memoria per modelli large
 Sistema di cache risposte
 Interfaccia web alternativa
 Supporto encryption UDP

Contribuire
Pull request benvenute! Per modifiche importanti, apri prima un issue per discutere i cambiamenti proposti.
Licenza
[Inserisci la tua licenza preferita - es. MIT, GPL, Apache 2.0]
Autori

Filippo - Initial work
