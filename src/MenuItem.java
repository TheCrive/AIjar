import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MenuItem extends HBox implements Themeable {
    private final String text;
    private final String iconPath;
    private boolean active;
    private final Label textLabel;
    private SVGPath iconSvg;
    
    public MenuItem(String iconPath, String text, boolean active) {
        this.text = text;
        this.iconPath = iconPath;
        this.active = active;
        
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(12, 15, 12, 15));
        setSpacing(12);
        
        // Crea icona SVG
        iconSvg = SvgIcons.createIcon(iconPath, 20);
        
        textLabel = new Label(text);
        textLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        
        getChildren().addAll(iconSvg, textLabel);
        
        updateStyle();
        setupHoverEffects();
        
        ThemeManager.getInstance().register(this);
    }
    
    public String getText() {
        return text;
    }
    
    public void setActive(boolean active) {
        this.active = active;
        updateStyle();
    }
    
    public boolean isActive() {
        return active;
    }
    
    private void updateStyle() {
        if (active) {
            setStyle("-fx-background-radius: 10; -fx-cursor: hand; " +
                    "-fx-background-color: " + StyleConstants.PRIMARY_COLOR + ";");
            textLabel.setTextFill(Color.WHITE);
            iconSvg.setFill(Color.WHITE);
            iconSvg.setStroke(Color.WHITE);
        } else {
            setStyle("-fx-background-radius: 10; -fx-cursor: hand;");
            textLabel.setTextFill(Color.web(StyleConstants.getTextPrimary()));
            iconSvg.setFill(Color.web(StyleConstants.getTextPrimary()));
            iconSvg.setStroke(Color.web(StyleConstants.getTextPrimary()));
        }
    }
    
    private void setupHoverEffects() {
        setOnMouseEntered(e -> {
            if (!active) {
                setStyle("-fx-background-radius: 10; -fx-cursor: hand; " +
                        "-fx-background-color: " + StyleConstants.getBgColor() + ";");
            }
        });
        setOnMouseExited(e -> updateStyle());
    }
    
    @Override
    public void applyTheme() {
        updateStyle();
    }
}