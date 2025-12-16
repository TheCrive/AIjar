import java.net.*;
import com.fasterxml.jackson.databind.JsonNode;

public class ChatBotService {
    private UdpClient udpClient;
    private HttpClient httpClient;
    private boolean isConnected = false;
    
    public ChatBotService() {
        try {
            udpClient = new UdpClient();
            httpClient = new HttpClient("http://crive.xyz:8080");
            isConnected = true;
            System.out.println("ChatBotService: Connessione UDP stabilita");
        } catch (SocketException e) {
            System.out.println("ChatBotService: Errore creazione client UDP - " + e.getMessage());
            isConnected = false;
        }
    }
    
    // --- Metodi per llama-server ---
    
    public boolean isLlamaServerHealthy() {
        return httpClient != null && httpClient.isHealthy();
    }
    
    public String getLlamaStatus() {
        if (httpClient == null) return "Client non inizializzato";
        
        try {
            JsonNode health = httpClient.getHealth();
            return health.path("status").asText("unknown");
        } catch (Exception e) {
            return "Errore: " + e.getMessage();
        }
    }
    
    public String getLlamaModelInfo() {
        if (httpClient == null) return "Client non inizializzato";
        
        try {
            JsonNode props = httpClient.getProps();
            String model = props.path("model_alias").asText("N/A");
            model = model.split("\\.")[0];
            int ctx = props.path("default_generation_settings").path("n_ctx").asInt(0);
            int slots = props.path("total_slots").asInt(0);
            return "Modello: " + model + " | Context: " + ctx + " | Slots: " + slots;
        } catch (Exception e) {
            return "Errore: " + e.getMessage();
        }
    }
    
    // --- Metodi UDP esistenti ---
    
    public void sendMessage(String message) {
        if (isConnected && udpClient != null) {
            udpClient.sendMessage(message);
        }
    }
    
    public String getLastReceivedMessage() {
        if (udpClient != null) {
            return udpClient.getLastReceivedMessage();
        }
        return "";
    }
    
    public void startReceiving(Runnable onMessageReceived) {
        if (isConnected && udpClient != null) {
            udpClient.startReceivingMessages(onMessageReceived);
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public void setServerIP(String ip) {
        if (udpClient != null) {
            udpClient.setServerIP(ip);
        }
    }
    
    public void setServerPort(int port) {
        if (udpClient != null) {
            udpClient.setPorta(port);
        }
    }
    
    public String getServerIP() {
        return udpClient != null ? udpClient.getServerIP() : "N/A";
    }
    
    public int getServerPort() {
        return udpClient != null ? udpClient.getPorta() : 0;
    }
    
    public int getLocalPort() {
        return udpClient != null ? udpClient.getLocalPort() : 0;
    }
    
    public void close() {
        if (udpClient != null) {
            udpClient.close();
            isConnected = false;
        }
    }
}