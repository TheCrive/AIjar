import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HeaderView extends HBox implements Themeable {
    private Runnable onClearChat;
    private Runnable onSettings;
    private Label botName;
    private Label botStatus;
    private Button clearBtn;
    private Button settingsBtn;
    private SVGPath clearIcon;
    private SVGPath settingsIcon;
    
    public HeaderView() {
        setupLayout();
        buildContent();
        ThemeManager.getInstance().register(this);
    }
    
    public void setOnClearChat(Runnable handler) {
        this.onClearChat = handler;
    }
    
    public void setOnSettings(Runnable handler) {
        this.onSettings = handler;
    }
    
    private void setupLayout() {
        setPadding(new Insets(20, 40, 20, 40));
        setAlignment(Pos.CENTER_LEFT);
        applyTheme();
    }
    
    private void buildContent() {
        //Circle botAvatar = new Circle(22, Color.web(StyleConstants.SECONDARY_COLOR));


        Image logo = new Image("file:resources/icons/logo.png");  // Sostituisci con il percorso del tuo logo
        ImageView imageLogo = new ImageView(logo);
        
        imageLogo.setFitWidth(48);
        imageLogo.setFitHeight(48);


        
        VBox botInfo = new VBox(2);
        botInfo.setPadding(new Insets(0, 0, 0, 15));
        
        botName = new Label("modello");                                                // nome modello
        botName.setFont(Font.font("System", FontWeight.BOLD, 16));
        botName.setTextFill(Color.web(StyleConstants.getTextPrimary()));



        botStatus = new Label("stato");                                                 // cambiare stato
        botStatus.setFont(Font.font("System", 12));
        botStatus.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        
        botInfo.getChildren().addAll(botName, botStatus);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Crea bottoni con icone SVG
        clearIcon = SvgIcons.createIcon(SvgIcons.TRASH, 20, StyleConstants.getTextPrimary());
        clearBtn = createIconButton(clearIcon, "Pulisci chat");
        clearBtn.setOnAction(e -> {
            if (onClearChat != null) onClearChat.run();
        });
        
        settingsIcon = SvgIcons.createIcon(SvgIcons.SETTINGS, 20, StyleConstants.getTextPrimary());
        settingsBtn = createIconButton(settingsIcon, "Impostazioni");
        settingsBtn.setOnAction(e -> {
            if (onSettings != null) onSettings.run();
        });
        
        getChildren().addAll(imageLogo, botInfo, spacer, clearBtn);
    }
    
    private Button createIconButton(SVGPath icon, String tooltipText) {
        Button btn = new Button();
        btn.setGraphic(icon);
        btn.setTooltip(new Tooltip(tooltipText));
        applyButtonStyle(btn, false);
        
        btn.setOnMouseEntered(e -> applyButtonStyle(btn, true));
        btn.setOnMouseExited(e -> applyButtonStyle(btn, false));
        
        return btn;
    }
    
    private void applyButtonStyle(Button btn, boolean hover) {
        String bgColor = hover ? StyleConstants.getBgColor() : "transparent";
        btn.setStyle("-fx-background-color: " + bgColor + "; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10;");
    }
    
    @Override
    public void applyTheme() {
        setStyle("-fx-background-color: " + StyleConstants.getSurfaceColor() + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        
        if (botName != null) {
            botName.setTextFill(Color.web(StyleConstants.getTextPrimary()));
        }
        if (botStatus != null) {
            botStatus.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        }
        if (clearIcon != null) {
            clearIcon.setFill(Color.web(StyleConstants.getTextPrimary()));
            clearIcon.setStroke(Color.web(StyleConstants.getTextPrimary()));
        }
        if (settingsIcon != null) {
            settingsIcon.setFill(Color.web(StyleConstants.getTextPrimary()));
            settingsIcon.setStroke(Color.web(StyleConstants.getTextPrimary()));
        }
        if (clearBtn != null) {
            applyButtonStyle(clearBtn, false);
        }
        if (settingsBtn != null) {
            applyButtonStyle(settingsBtn, false);
        }
    }


    public void setModelName(String name) {
        if (botName != null) {
            botName.setText(name);
        }
    }
    
    public void setStatus(String status, boolean isOnline) {
        if (botStatus != null) {
            botStatus.setText(status);
            botStatus.setTextFill(Color.web(isOnline ? "#4CAF50" : "#F44336"));
        }
    }



}