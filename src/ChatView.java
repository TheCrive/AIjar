import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ChatView extends VBox implements Themeable {
    private VBox chatContainer;
    private ScrollPane scrollPane;
    private Label typingIndicator;
    
    public ChatView() {
        setupLayout();
        buildContent();
        ThemeManager.getInstance().register(this);
    }
    
    public void addMessage(Message message) {
        MessageBubble bubble = new MessageBubble(message);
        chatContainer.getChildren().add(bubble);
        
        boolean isUser = message.getType() == Message.Type.USER;
        AnimationUtils.fadeSlideIn(bubble, isUser);
        
        scrollToBottom();
    }
    
    public void clearMessages() {
        AnimationUtils.fadeOut(chatContainer, () -> {
            chatContainer.getChildren().clear();
            chatContainer.setOpacity(1);
        });
    }
    
    public void showTypingIndicator(boolean show) {
        typingIndicator.setVisible(show);
        if (show) scrollToBottom();
    }
    
    private void setupLayout() {
        applyTheme();
    }
    
    private void buildContent() {
        chatContainer = new VBox(15);
        chatContainer.setPadding(new Insets(30, 40, 30, 40));
        applyChatContainerStyle();
        
        scrollPane = new ScrollPane(chatContainer);
        scrollPane.setFitToWidth(true);
        applyScrollPaneStyle();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        
        typingIndicator = new Label("Bot sta scrivendo...");
        typingIndicator.setFont(Font.font("System", FontWeight.NORMAL, 13));
        typingIndicator.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        typingIndicator.setPadding(new Insets(0, 0, 10, 50));
        typingIndicator.setVisible(false);
        
        //getChildren().addAll(scrollPane, typingIndicator);
        getChildren().addAll(scrollPane);
    }
    
    private void applyChatContainerStyle() {
        chatContainer.setStyle("-fx-background-color: " + StyleConstants.getBgColor() + ";");
    }
    
    private void applyScrollPaneStyle() {
        scrollPane.setStyle("-fx-background: " + StyleConstants.getBgColor() + "; " +
                "-fx-background-color: " + StyleConstants.getBgColor() + ";");
    }
    
    private void scrollToBottom() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
    
    @Override
    public void applyTheme() {
        setStyle("-fx-background-color: " + StyleConstants.getBgColor() + ";");
        
        if (chatContainer != null) {
            applyChatContainerStyle();
        }
        if (scrollPane != null) {
            applyScrollPaneStyle();
        }
        if (typingIndicator != null) {
            typingIndicator.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        }
    }
}