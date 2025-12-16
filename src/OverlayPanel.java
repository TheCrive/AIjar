import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OverlayPanel extends VBox implements Themeable {
    private Label titleLabel;
    private Label messageLabel;
    private Runnable onClose;
    
    public OverlayPanel() {
        setupLayout();
        buildContent();
        ThemeManager.getInstance().register(this);
    }
    
    public void setContent(String title, String message) {
        titleLabel.setText(title);
        messageLabel.setText(message);
    }
    
    public void setOnClose(Runnable handler) {
        this.onClose = handler;
    }
    
    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPrefWidth(400);
        setPrefHeight(250);
        applyTheme();
    }
    
    private void buildContent() {
        titleLabel = new Label("Titolo");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        
        messageLabel = new Label("Messaggio");
        messageLabel.setFont(Font.font("System", 16));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        
        Button closeButton = new Button("Chiudi");
        closeButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; " +
                "-fx-background-radius: 10; -fx-padding: 10 30; -fx-cursor: hand;");
        closeButton.setOnAction(e -> {
            if (onClose != null) onClose.run();
        });
        
        getChildren().addAll(titleLabel, messageLabel, closeButton);
    }
    
    @Override
    public void applyTheme() {
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.85); " +
                "-fx-padding: 30; -fx-background-radius: 15;");
    }
}