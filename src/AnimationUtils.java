import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public final class AnimationUtils {
    private AnimationUtils() {}
    
    public static void fadeSlideIn(Node node, boolean fromRight) {
        node.setOpacity(0);
        node.setTranslateX(fromRight ? 30 : -30);
        
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), node);
        slide.setFromX(fromRight ? 30 : -30);
        slide.setToX(0);
        
        new ParallelTransition(fade, slide).play();
    }
    
    public static void fadeOut(Node node, Runnable onFinished) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> onFinished.run());
        fade.play();
    }
    
    public static void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(500), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
} 