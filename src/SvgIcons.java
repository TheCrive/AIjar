import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;

public final class SvgIcons {
    private SvgIcons() {}
    
    // Icona cestino (trash)
    public static final String TRASH = "M21.5,4h-3.551c-.252-2.244-2.139-4-4.449-4h-3c-2.31,0-4.197,1.756-4.449,4H2.5c-.276,0-.5,.224-.5,.5s.224,.5,.5,.5h1.5v14.5c0,2.481,2.019,4.5,4.5,4.5h7c2.481,0,4.5-2.019,4.5-4.5V5h1.5c.276,0,.5-.224,.5-.5s-.224-.5-.5-.5ZM10.5,1h3c1.758,0,3.204,1.308,3.449,3H7.051c.245-1.692,1.691-3,3.449-3Zm8.5,18.5c0,1.93-1.57,3.5-3.5,3.5h-7c-1.93,0-3.5-1.57-3.5-3.5V5h14v14.5ZM10,10.5v7c0,.276-.224,.5-.5,.5s-.5-.224-.5-.5v-7c0-.276,.224-.5,.5-.5s.5,.224,.5,.5Zm5,0v7c0,.276-.224,.5-.5,.5s-.5-.224-.5-.5v-7c0-.276,.224-.5,.5-.5s.5,.224,.5,.5Z";
    
    // Icona ingranaggio (settings)
    public static final String SETTINGS = "M10.931,2.75a3.728,3.728,0,0,0-7.195,0H0v2H3.736a3.728,3.728,0,0,0,7.195,0H24v-2ZM7.333,5.5a1.75,1.75,0,1,1,1.75-1.75A1.753,1.753,0,0,1,7.333,5.5ZM16.667,8.25A3.745,3.745,0,0,0,13.07,11H0v2H13.07a3.727,3.727,0,0,0,7.194,0H24V11H20.264A3.745,3.745,0,0,0,16.667,8.25Zm0,5.5A1.75,1.75,0,1,1,18.417,12,1.752,1.752,0,0,1,16.667,13.75ZM7.333,16.5a3.745,3.745,0,0,0-3.6,2.75H0v2H3.736a3.728,3.728,0,0,0,7.195,0H24v-2H10.931A3.745,3.745,0,0,0,7.333,16.5Zm0,5.5a1.75,1.75,0,1,1,1.75-1.75A1.753,1.753,0,0,1,7.333,22Z";
    
    // Icona chat/messaggio
    public static final String CHAT = "M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z";
    
    // Icona cartella
    public static final String FOLDER = "M22 19a2 2 0 01-2 2H4a2 2 0 01-2-2V5a2 2 0 012-2h5l2 3h9a2 2 0 012 2z";
    
    // Icona tema/palette
    public static final String THEME = "M12 2.69l5.66 5.66a8 8 0 11-11.31 0z";
    
    // Icona invia
    public static final String SEND = "m21.916,8.727L3.965.282C2.951-.211,1.756-.041.917.713.076,1.47-.216,2.646.172,3.708c.017.043,4.411,8.296,4.411,8.296,0,0-4.313,8.251-4.328,8.293-.387,1.063-.092,2.237.749,2.993.521.467,1.179.708,1.841.708.409,0,.819-.092,1.201-.279l17.872-8.438c1.285-.603,2.083-1.859,2.082-3.278,0-1.42-.801-2.675-2.084-3.275ZM2.032,2.967c-.122-.415.138-.69.223-.768.089-.079.414-.324.838-.116.005.002,17.974,8.455,17.974,8.455.239.112.438.27.591.462H6.315L2.032,2.967Zm19.034,10.504L3.178,21.917c-.425.209-.749-.035-.838-.116-.086-.076-.346-.353-.223-.769l4.202-8.032h15.345c-.153.195-.355.357-.597.471Z";
    
    // Icona X (chiudi)
    public static final String CLOSE = "M18 6L6 18M6 6l12 12";
    
    // Icona sole (tema chiaro)
    public static final String SUN = "M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42M12 6a6 6 0 100 12 6 6 0 000-12z";
    
    // Icona luna (tema scuro)
    public static final String MOON = "M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z";
    
    // Metodo per creare SVGPath
    public static SVGPath createIcon(String pathData, double size, String color) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(pathData);
        svgPath.setFill(Color.web(color));
        svgPath.setStroke(Color.web(color));
        svgPath.setStrokeWidth(1.5);
        
        // Scala l'icona
        double scale = size / 24.0; // Le icone sono basate su 24x24
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);
        
        return svgPath;
    }
    
    public static SVGPath createIcon(String pathData, double size) {
        return createIcon(pathData, size, "#000000");
    }
}