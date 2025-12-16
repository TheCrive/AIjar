import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    
    private final String baseUrl;
    private final int timeoutMs;
    private final ObjectMapper mapper;
    
    public HttpClient(String baseUrl) {
        this(baseUrl, 5000);
    }
    
    public HttpClient(String baseUrl, int timeoutMs) {
        this.baseUrl = baseUrl;
        this.timeoutMs = timeoutMs;
        this.mapper = new ObjectMapper();
    }
    
    /**
     * GET /health - Verifica stato del server
     */
    public boolean isHealthy() {
        try {
            JsonNode health = getAsJson("/health");
            return "ok".equals(health.path("status").asText());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * GET /health
     */
    public JsonNode getHealth() throws Exception {
        return getAsJson("/health");
    }
    
    /**
     * GET /props
     */
    public JsonNode getProps() throws Exception {
        return getAsJson("/props");
    }
    
    /**
     * GET generico con parsing JSON
     */
    public JsonNode getAsJson(String endpoint) throws Exception {
        String response = get(endpoint);
        return mapper.readTree(response);
    }
    
    private String get(String endpoint) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeoutMs);
            conn.setReadTimeout(timeoutMs);
            conn.setRequestProperty("Accept", "application/json");
            
            int status = conn.getResponseCode();
            if (status != 200) {
                throw new Exception("HTTP " + status + " per " + endpoint);
            }
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } finally {
            conn.disconnect();
        }
    }
}