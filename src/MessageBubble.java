import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.time.format.DateTimeFormatter;

public class MessageBubble extends HBox implements Themeable {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Message message;
    private Label msgLabel;
    private Label timeLabel;
    
    public MessageBubble(Message message) {
        this.message = message;
        
        boolean isUser = message.getType() == Message.Type.USER;
        
        setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        setPadding(new Insets(0));
        
        VBox content = new VBox(5);
        content.setMaxWidth(StyleConstants.MAX_MESSAGE_WIDTH);
        content.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        
        msgLabel = createMessageLabel(message.getContent(), isUser);
        timeLabel = createTimeLabel();
        
        content.getChildren().addAll(msgLabel, timeLabel);
        getChildren().add(content);
        
        // Registra per aggiornamenti tema
        ThemeManager.getInstance().register(this);
    }
    
    private Label createMessageLabel(String text, boolean isUser) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font("System", 15));
        applyMessageStyle(label, isUser);
        return label;
    }
    
    private void applyMessageStyle(Label label, boolean isUser) {
        if (isUser) {
            label.setTextFill(Color.web(StyleConstants.getTextPrimary()));
            label.setStyle("-fx-background-color: " + StyleConstants.getBotMsgColor() + "; " +
                    "-fx-background-radius: 20 20 5 20; -fx-padding: 15 20; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        } else {
            label.setTextFill(Color.web(StyleConstants.getTextPrimary()));
            label.setStyle("-fx-background-color: " + StyleConstants.getBotMsgColor() + "; " +
                    "-fx-background-radius: 20 20 20 5; -fx-padding: 15 20; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        }
    }
    
    private Label createTimeLabel() {
        Label label = new Label(message.getTimestamp().format(TIME_FORMAT));
        label.setFont(Font.font("System", 11));
        label.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        boolean isUser = message.getType() == Message.Type.USER;
        label.setPadding(new Insets(0, isUser ? 5 : 0, 0, isUser ? 0 : 5));
        return label;
    }
    
    @Override
    public void applyTheme() {
        boolean isUser = message.getType() == Message.Type.USER;
        applyMessageStyle(msgLabel, isUser);
        timeLabel.setTextFill(Color.web(StyleConstants.getTextSecondary()));
    }
}