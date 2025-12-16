import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.function.Consumer;

public class InputAreaView extends HBox implements Themeable {
    private TextField messageInput;
    private HBox inputWrapper;
    private Consumer<String> onSendMessage;
    
    public InputAreaView() {
        setupLayout();
        buildContent();
        ThemeManager.getInstance().register(this);
    }
    
    public void setOnSendMessage(Consumer<String> handler) {
        this.onSendMessage = handler;
    }
    
    public void clear() {
        messageInput.clear();
    }
    
    public void requestFocus() {
        messageInput.requestFocus();
    }
    
    private void setupLayout() {
        setPadding(new Insets(20, 40, 30, 40));
        setAlignment(Pos.CENTER);
        setSpacing(12);
    }
    
    private void buildContent() {
        inputWrapper = createInputWrapper();
        Button sendBtn = createSendButton();
        
        getChildren().addAll(inputWrapper, sendBtn);
    }
    
    private HBox createInputWrapper() {
        HBox wrapper = new HBox();
        applyInputWrapperStyle(wrapper);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setPadding(new Insets(5, 15, 5, 20));
        HBox.setHgrow(wrapper, Priority.ALWAYS);
        
        messageInput = new TextField();
        messageInput.setPromptText("Scrivi un messaggio...");
        messageInput.setFont(Font.font("System", 15));
        applyInputStyle();
        HBox.setHgrow(messageInput, Priority.ALWAYS);
        messageInput.setOnAction(e -> sendMessage());
        
        wrapper.getChildren().addAll(messageInput);
        return wrapper;
    }
    
    private void applyInputWrapperStyle(HBox wrapper) {
        wrapper.setStyle("-fx-background-color: " + StyleConstants.getSurfaceColor() + "; " +
                "-fx-background-radius: 25; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 3);");
    }
    
    private void applyInputStyle() {
        messageInput.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: transparent; -fx-text-fill: " + StyleConstants.getTextPrimary() + ";");
    }
    
    private Button createSendButton() {
        Button btn = new Button("Invia");
        
        
                                                                                                    // MODIFICARE DA METTERE ICONA DOPO
        
        
        btn.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        String normalStyle = "-fx-background-color: " + StyleConstants.PRIMARY_COLOR + "; " +
                "-fx-text-fill: white; -fx-background-radius: 25; " +
                "-fx-padding: 15 30; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(108,92,231,0.4), 10, 0, 0, 4);";
        
        String hoverStyle = "-fx-background-color: " + StyleConstants.SECONDARY_COLOR + "; " +
                "-fx-text-fill: white; -fx-background-radius: 25; " +
                "-fx-padding: 15 30; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(108,92,231,0.6), 15, 0, 0, 5);";
        



        // Aggiungi icona SVG
        SVGPath sendIcon = SvgIcons.createIcon(SvgIcons.SEND, 16, "#FFFFFF");
        btn.setGraphic(sendIcon);
        btn.setGraphicTextGap(8);

        btn.setStyle(normalStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));
        btn.setOnAction(e -> sendMessage());
        
        return btn;
    }
    
    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (!text.isEmpty() && onSendMessage != null) {
            onSendMessage.accept(text);
            messageInput.clear();
        }
    }
    
    @Override
    public void applyTheme() {
        if (inputWrapper != null) {
            applyInputWrapperStyle(inputWrapper);
        }
        if (messageInput != null) {
            applyInputStyle();
        }
    }
}