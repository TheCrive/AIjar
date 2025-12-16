import java.time.LocalDateTime;

public class Message {
    public enum Type { USER, BOT }
    
    private final String content;
    private final Type type;
    private final LocalDateTime timestamp;
    
    public Message(String content, Type type) {
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getContent() { return content; }
    public Type getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}