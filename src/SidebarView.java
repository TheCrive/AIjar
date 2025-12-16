import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.function.Consumer;

public class SidebarView extends VBox implements Themeable {
    private VBox menuItemsContainer;
    private Consumer<String> onMenuClick;
    private Label brandLabel;
    private Label versionLabel;
    private VBox statusBox;
    private Label modelLabel;
    
    public SidebarView() {
        setupLayout();
        buildContent();
        ThemeManager.getInstance().register(this);
    }
    
    public void setOnMenuClick(Consumer<String> handler) {
        this.onMenuClick = handler;
    }
    
    private void setupLayout() {
        setPrefWidth(StyleConstants.SIDEBAR_WIDTH);
        setPadding(new Insets(30, 20, 30, 20));
        setSpacing(20);
        applyTheme();
    }
    
    private void buildContent() {
        getChildren().addAll(
            createBrandSection(),
            createSeparator(),
            createMenuSection(),
            createSpacer(),
            createStatusSection()
        );
    }
    
    private VBox createBrandSection() {
        VBox brand = new VBox(8);
        brand.setAlignment(Pos.CENTER);
        
        //Circle avatar = new Circle(40, Color.web(StyleConstants.PRIMARY_COLOR));
        //avatar.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));


        Image logo = new Image("file:resources/icons/aigif.gif");  // Sostituisci con il percorso del tuo logo
        ImageView imageLogo = new ImageView(logo);
        
        imageLogo.setFitWidth(48);
        imageLogo.setFitHeight(48);

        
        brandLabel = new Label("Crive");
        brandLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        brandLabel.setTextFill(Color.web(StyleConstants.getTextPrimary()));
        
        versionLabel = new Label("v0.1 - beta");
        versionLabel.setFont(Font.font("System", 11));
        versionLabel.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        
        brand.getChildren().addAll(imageLogo, brandLabel, versionLabel);
        return brand;
    }
    
    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));
        return sep;
    }
    
    // Nel metodo createMenuSection(), cambia:

    private VBox createMenuSection() {
        menuItemsContainer = new VBox(10);
        
        // Usa i path SVG invece degli emoji
        //MenuItem cronologia = new MenuItem(SvgIcons.FOLDER, "Cronologia", false);
        MenuItem impostazioni = new MenuItem(SvgIcons.SETTINGS, "Impostazioni", false);
        MenuItem temi = new MenuItem(SvgIcons.THEME, "Temi", false);
        
        for (MenuItem item : new MenuItem[]{impostazioni, temi}) {
            item.setOnMouseClicked(e -> handleMenuClick(item));
            menuItemsContainer.getChildren().add(item);
        }
        
        return menuItemsContainer;
    }
    
    private void handleMenuClick(MenuItem selectedItem) {
        for (var node : menuItemsContainer.getChildren()) {
            if (node instanceof MenuItem) {
                ((MenuItem) node).setActive(false);
            }
        }
        selectedItem.setActive(true);
        
        if (onMenuClick != null) {
            onMenuClick.accept(selectedItem.getText());
        }
    }
    
    private Region createSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }
    
    private VBox createStatusSection() {
        statusBox = new VBox(8);
        updateStatusBoxStyle();
        
        HBox indicator = new HBox(8);
        indicator.setAlignment(Pos.CENTER_LEFT);
        
        Circle dot = new Circle(5, Color.web(StyleConstants.ACCENT_COLOR));
        FadeTransition pulse = new FadeTransition(Duration.seconds(1), dot);
        pulse.setFromValue(1.0);
        pulse.setToValue(0.3);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();
        
        Label online = new Label("Online");
        online.setFont(Font.font("System", FontWeight.BOLD, 12));
        online.setTextFill(Color.web(StyleConstants.ACCENT_COLOR));
        
        indicator.getChildren().addAll(dot, online);
        
        modelLabel = new Label("GPT-4 Turbo");                                                  // cambio modello
        modelLabel.setFont(Font.font("System", 11));
        modelLabel.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        
        statusBox.getChildren().addAll(indicator, modelLabel);
        return statusBox;
    }
    
    private void updateStatusBoxStyle() {
        statusBox.setStyle("-fx-background-color: " + StyleConstants.getBgColor() + "; " +
                "-fx-background-radius: 12; -fx-padding: 15;");
    }
    
    @Override
    public void applyTheme() {
        setStyle("-fx-background-color: " + StyleConstants.getSurfaceColor() + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 2, 0);");
        
        if (brandLabel != null) {
            brandLabel.setTextFill(Color.web(StyleConstants.getTextPrimary()));
        }
        if (versionLabel != null) {
            versionLabel.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        }
        if (statusBox != null) {
            updateStatusBoxStyle();
        }
        if (modelLabel != null) {
            modelLabel.setTextFill(Color.web(StyleConstants.getTextSecondary()));
        }
        
        // Aggiorna menu items
        if (menuItemsContainer != null) {
            for (var node : menuItemsContainer.getChildren()) {
                if (node instanceof MenuItem) {
                    ((MenuItem) node).applyTheme();
                }
            }
        }
    }
}