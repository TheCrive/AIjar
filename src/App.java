import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class App extends Application implements Themeable {
    private BorderPane root;
    private StackPane centerStack;
    private SidebarView sidebar;
    private HeaderView header;
    private ChatView chatView;
    private InputAreaView inputArea;
    private OverlayPanel overlayPanel;
    private ChatController chatController;
    private VBox chatArea;
    
    @Override
    public void start(Stage primaryStage) {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        Scene scene = new Scene(root, 1400, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AI Jar");
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image("file:resources/icons/logo.png"));
        
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Chiusura applicazione...");
            if (chatController != null) {
                chatController.shutdown();
            }
            Platform.exit();
            System.exit(0);
        });
        
        AnimationUtils.fadeIn(root);
        primaryStage.show();
        
        Platform.runLater(() -> {
            inputArea.requestFocus();
            chatController.addWelcomeMessages();
            updateLlamaStatus();  // <-- Aggiungi questo
            startStatusChecker();
        });
    }

     private void updateLlamaStatus() {
        ChatBotService service = chatController.getBotService();
        
        if (service.isLlamaServerHealthy()) {
            header.setStatus("Online", true);
            header.setModelName(extractModelName(service));
        } else {
            header.setStatus("Offline", false);
            header.setModelName("MODELLO");
        }
    }
    
    private String extractModelName(ChatBotService service) {
        try {
            String info = service.getLlamaModelInfo();
            // "Modello: xxx | Context: yyy | Slots: zzz"
            String[] parts = info.split("\\|");
            return parts[0].replace("Modello:", "").trim();
        } catch (Exception e) {
            return "MODELLO";
        }
    }
    
    private void startStatusChecker() {
        Timeline statusChecker = new Timeline(
            new KeyFrame(Duration.seconds(10), e -> updateLlamaStatus())
        );
        statusChecker.setCycleCount(Timeline.INDEFINITE);
        statusChecker.play();
    }



    
    private void initializeComponents() {
        root = new BorderPane();
        
        sidebar = new SidebarView();
        header = new HeaderView();
        chatView = new ChatView();
        inputArea = new InputAreaView();
        overlayPanel = new OverlayPanel();
        
        chatController = new ChatController(chatView);
        
        ThemeManager.getInstance().register(this);
        applyTheme();
    }
    
    private void setupLayout() {
        root.setLeft(sidebar);
        
        chatArea = new VBox();
        applyChatAreaStyle();
        
        VBox.setVgrow(chatView, javafx.scene.layout.Priority.ALWAYS);
        chatArea.getChildren().addAll(header, chatView, inputArea);
        
        centerStack = new StackPane();
        centerStack.getChildren().add(chatArea);
        
        root.setCenter(centerStack);
    }
    
    private void applyChatAreaStyle() {
        chatArea.setStyle("-fx-background-color: " + StyleConstants.getBgColor() + ";");
    }
    
    private void setupEventHandlers() {
        inputArea.setOnSendMessage(text -> chatController.sendMessage(text));
        header.setOnClearChat(() -> chatController.clearChat());
        sidebar.setOnMenuClick(this::handleMenuClick);
        overlayPanel.setOnClose(this::hideOverlay);
    }
    
    private void handleMenuClick(String menuItem) {
        switch (menuItem) {
            case "Nuova Chat":
                hideOverlay();
                chatController.clearChat();
                break;
            case "Cronologia":
                showOverlay("Cronologia", "Ecco la cronologia delle tue conversazioni passate.");
                break;
            case "Impostazioni":
                showOverlay("Impostazioni", getConnectionInfo());
                break;
            case "Temi":
                toggleTheme();
                break;
        }
    }
    
    private void toggleTheme() {
        ThemeManager.getInstance().toggleTheme();
        String currentTheme = StyleConstants.isDarkMode() ? "🌙 Tema Scuro" : "☀️ Tema Chiaro";
        showOverlay("Tema Cambiato", currentTheme + " attivato!");
        
        // Chiudi automaticamente dopo 1 secondo
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                Platform.runLater(this::hideOverlay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    private String getConnectionInfo() {
        ChatBotService service = chatController.getBotService();
        return "Server: " + service.getServerIP() + ":" + service.getServerPort() + 
               "\nPorta locale: " + service.getLocalPort() +
               "\nStato: " + (service.isConnected() ? "Connesso" : "Disconnesso");
    }
    
    private void showOverlay(String title, String message) {
        overlayPanel.setContent(title, message);
        
        if (!centerStack.getChildren().contains(overlayPanel)) {
            StackPane.setAlignment(overlayPanel, Pos.CENTER);
            centerStack.getChildren().add(overlayPanel);
        }
        
        AnimationUtils.fadeIn(overlayPanel);
    }
    
    private void hideOverlay() {
        if (centerStack.getChildren().contains(overlayPanel)) {
            AnimationUtils.fadeOut(overlayPanel, () -> 
                centerStack.getChildren().remove(overlayPanel));
        }
    }
    
    @Override
    public void applyTheme() {
        root.setStyle("-fx-background-color: " + StyleConstants.getBgColor() + ";");
        if (chatArea != null) {
            applyChatAreaStyle();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}