import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

public class Server {
    DatagramSocket server;
    DatagramPacket pkt_in, pkt_out;
    int porta = 51700;
    static int conta_pkt = 0;

    // Client per comunicare con llama.cpp
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String LLAMA_SERVER_URL = "http://localhost:8080/v1/chat/completions";

    private static final String MODELS_BASE_PATH = "/srv/llama.cpp/models/";
    private static final String SERVICE_FILE = "/etc/systemd/system/llama-server.service";
    private String currentModel = "Llama3.gguf";

    public Server() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public void attivazione() {
        try {
            server = new DatagramSocket(porta);
            System.out.println("Server attivo su porta " + porta);
        } catch (SocketException e) {
            System.out.println("Errore attivazione server: " + e);
        }
    }

    // Lista modelli
    private String listaModelli() {
        try {
            File modelsDir = new File(MODELS_BASE_PATH);
            File[] files = modelsDir.listFiles((dir, name) ->
                name.endsWith(".gguf") && !name.startsWith("ggml-vocab")
            );

            if (files == null || files.length == 0) {
                return "Nessun modello trovato";
            }

            StringBuilder lista = new StringBuilder("=== MODELLI DISPONIBILI ===\n");
            for (int i = 0; i < files.length; i++) {
                String nome = files[i].getName();
                long sizeMB = files[i].length() / (1024 * 1024);

                if (nome.equals(currentModel)) {
                    lista.append("[ATTIVO] ");
                }
                lista.append((i+1) + ". " + nome + " (" + sizeMB + " MB)\n");
            }

            return lista.toString().trim();

        } catch (Exception e) {
            return "Errore: " + e.getMessage();
        }
    }

    // Cambia modello
    private String cambiaModello(String nomeModello) {
        try {
            // Verifica che il modello esista
            File modelFile = new File(MODELS_BASE_PATH + nomeModello);
            if (!modelFile.exists()) {
                return "Errore: modello '" + nomeModello + "' non trovato";
            }

            System.out.println("\n=== CAMBIO MODELLO ===");
            System.out.println("Modello richiesto: " + nomeModello);

            // Comando per cambiare modello e riavviare
            String comando = String.format(
                "sudo sed -i 's|ExecStart=.*/llama-server.*|ExecStart=/srv/llama.cpp/build/bin/llama-server -m %s%s --host 0.0.0.0 --port 8080|' %s && " +
                "sudo systemctl daemon-reload && " +
                "sudo systemctl restart llama-server",
                MODELS_BASE_PATH, nomeModello, SERVICE_FILE
            );

            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", comando});
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                return "Errore durante il cambio modello (exit code: " + exitCode + ")";
            }

            // Aspetta che il server si riavvii
            System.out.println("Attendo riavvio server...");
            Thread.sleep(5000);

            currentModel = nomeModello;
            System.out.println("Modello cambiato con successo!\n");

            return "Modello cambiato: " + nomeModello + "\n(il server è stato riavviato)";

        } catch (Exception e) {
            e.printStackTrace();
            return "Errore: " + e.getMessage();
        }
    }



    // Metodo per inviare prompt a llama.cpp
    private String chiediALlama(String prompt) {
        try {
            // Costruisci il JSON per CHAT (non completion)
            ObjectNode requestJson = objectMapper.createObjectNode();

            // Crea l'array dei messaggi
            com.fasterxml.jackson.databind.node.ArrayNode messages = objectMapper.createArrayNode();

            // SYSTEM MESSAGE - dice al modello come comportarsi
            ObjectNode systemMsg = objectMapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content",
                "Sei un assistente amichevole e conciso. " +
                "Rispondi sempre in italiano. " +
                "Sii naturale, breve e diretto. " +
                "Non ripeterti e non fare domande inutili.");
            messages.add(systemMsg);

            // USER MESSAGE - il messaggio dell'utente
            ObjectNode userMsg = objectMapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            // Aggiungi i messaggi alla richiesta
            requestJson.set("messages", messages);
            requestJson.put("temperature", 0.7);  // Più basso = più coerente
            requestJson.put("max_tokens", 150);
            requestJson.put("stream", false);

            String jsonRequest = objectMapper.writeValueAsString(requestJson);

            // CAMBIA L'URL per usare l'endpoint CHAT
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/v1/chat/completions"))  // <-- CAMBIATO
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .timeout(java.time.Duration.ofSeconds(30))
                .build();

            // Invia la richiesta
            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            // Parsa la risposta (formato diverso per chat)
            JsonNode responseJson = objectMapper.readTree(response.body());
            String risposta = responseJson
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText()
                .trim();

            // Limita la lunghezza per UDP
            if (risposta.length() > 900) {
                risposta = risposta.substring(0, 900) + "...";
            }

            return risposta;

        } catch (Exception e) {
            System.out.println("Errore comunicazione con Llama: " + e.getMessage());
            e.printStackTrace();
            return "Errore: impossibile ottenere risposta dall'AI";
        }
    }

    public void ricezioneErisposta() {
        while (true) {
            try {
                byte[] dati_in = new byte[1024];
                pkt_in = new DatagramPacket(dati_in, dati_in.length);

                // Riceve il pacchetto
                server.receive(pkt_in);
                conta_pkt++;
                System.out.println("\n=== Pacchetto n. " + conta_pkt + " ===");
                System.out.println("Ricevuto da: " + pkt_in.getSocketAddress());

                // Estrae il messaggio
                String msg_in = new String(pkt_in.getData(), 0, pkt_in.getLength());
                System.out.println("Messaggio: " + msg_in);

                String msg_out = "";

                // Controlla se è un comando speciale
                if (msg_in.equalsIgnoreCase("ping")) {
                    msg_out = "pong";
                } else if (msg_in.equalsIgnoreCase("/status")) {
                    msg_out = "Server AI attivo - Pacchetti ricevuti: " + conta_pkt;
                } else if (msg_in.equalsIgnoreCase("/models") || msg_in.equalsIgnoreCase("/lista")) {
                    msg_out = listaModelli();
                } else if (msg_in.startsWith("/model ")) {
                    String nomeModello = msg_in.substring(7).trim();
                    System.out.println("Richiesto cambio modello: " + nomeModello);
                    msg_out = cambiaModello(nomeModello);
                } else if (msg_in.equalsIgnoreCase("/help")) {
                    msg_out = "Comandi:\n" +
                    "/models - lista modelli\n" +
                    "/model <nome> - cambia modello\n" +
                    "/help - questo messaggio\n" +
                    "/status - info server";
                } else {
                    // Altrimenti chiedi all'AI
                    System.out.println("Invio richiesta all'AI...");
                    msg_out = chiediALlama(msg_in);
                    System.out.println("Risposta AI: " + msg_out);
                }

                // Prepara e invia la risposta
                byte[] dati_out = msg_out.getBytes();
                InetAddress IPDestinatario = pkt_in.getAddress();
                int portaDestinatario = pkt_in.getPort();

                pkt_out = new DatagramPacket(dati_out, dati_out.length,
                                             IPDestinatario, portaDestinatario);
                server.send(pkt_out);

                System.out.println("Risposta inviata a " + IPDestinatario + ":" + portaDestinatario);

            } catch (IOException e) {
                System.out.println("Errore durante la ricezione/invio: " + e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Server UDP con AI ===");
        System.out.println("Assicurati che llama-server sia attivo su localhost:8080");
        System.out.println();

        Server s = new Server();
        s.attivazione();
        s.ricezioneErisposta();
    }