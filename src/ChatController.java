import javafx.application.Platform;

public class ChatController {
    private final ChatView chatView;
    private final ChatBotService botService;
    
    public ChatController(ChatView chatView) {
        this.chatView = chatView;
        this.botService = new ChatBotService();
        
        // Avvia il thread di ricezione messaggi
        startReceivingMessages();
    }
    
    private void startReceivingMessages() {
        botService.startReceiving(() -> {
            String receivedMessage = botService.getLastReceivedMessage();
            if (!receivedMessage.isEmpty()) {
                Platform.runLater(() -> {
                    Message botMessage = new Message(receivedMessage, Message.Type.BOT);
                    chatView.addMessage(botMessage);
                });
            }
        });
    }
    
    public void sendMessage(String text) {
        if (text == null || text.trim().isEmpty()) return;
        
        // Aggiungi messaggio utente alla UI
        Message userMessage = new Message(text.trim(), Message.Type.USER);
        chatView.addMessage(userMessage);
        
        // Invia via UDP
        botService.sendMessage(text.trim());
        
        // Mostra indicatore typing (opzionale, rimuovi se non serve)
        chatView.showTypingIndicator(true);
        
        // Nascondi dopo un po' (il messaggio arriverà dal server)

        /*/
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Timeout
                Platform.runLater(() -> chatView.showTypingIndicator(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();*/
    }
    
    public void clearChat() {
        chatView.clearMessages();/*
        Platform.runLater(() -> {
            Message welcome = new Message("Chat pulita! Iniziamo una nuova conversazione!", Message.Type.BOT);
            chatView.addMessage(welcome); 
            try{
                Thread.sleep(3000); // Timeout
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        });*/
    }
    
    public void addWelcomeMessages() {
        Platform.runLater(() -> {
            String status = botService.isConnected() 
                ? "Connesso a " + botService.getServerIP() + ":" + botService.getServerPort() + " con porta locale:" + botService.getLocalPort()
                : "Connessione fallita";
            
            //chatView.addMessage(new Message("Ciao! Sono il tuo assistente AI.", Message.Type.BOT));
            
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    Platform.runLater(() -> {
                        
                        if (botService.isConnected()) {
                            chatView.addMessage(new Message(status, Message.Type.BOT));
                            //chatView.addMessage(new Message(
                            //    "Porta locale: " + botService.getLocalPort(), Message.Type.BOT));
                        }else{
                            chatView.addMessage(new Message("connessione fallita",Message.Type.BOT));
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });
    }
    
    public ChatBotService getBotService() {
        return botService;
    }
    
    public void shutdown() {
        botService.close();
    }
}