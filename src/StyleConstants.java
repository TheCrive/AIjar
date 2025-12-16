public final class StyleConstants {
    private StyleConstants() {}
    
    // Tema corrente
    private static boolean darkMode = false;
    
    // Colori fissi
    public static final String PRIMARY_COLOR = "#6C5CE7";
    public static final String SECONDARY_COLOR = "#A29BFE";
    public static final String ACCENT_COLOR = "#00B894";
    public static final String USER_MSG_COLOR = "linear-gradient(135deg, #667EEA 0%, #764BA2 100%)";
    
    // Colori Light Mode
    private static final String BG_COLOR_LIGHT = "#F8F9FA";
    private static final String SURFACE_COLOR_LIGHT = "#FFFFFF";
    private static final String BOT_MSG_COLOR_LIGHT = "#FFFFFF";
    private static final String TEXT_PRIMARY_LIGHT = "#000000";
    private static final String TEXT_SECONDARY_LIGHT = "#636E72";
    
    // Colori Dark Mode
    private static final String BG_COLOR_DARK = "#0E1621";
    private static final String SURFACE_COLOR_DARK = "#1A2332";
    private static final String BOT_MSG_COLOR_DARK = "#2D3748";
    private static final String TEXT_PRIMARY_DARK = "#FFFFFF";
    private static final String TEXT_SECONDARY_DARK = "#A0AEC0";
    
    // Dimensioni
    public static final int SIDEBAR_WIDTH = 280;
    public static final int MAX_MESSAGE_WIDTH = 550;
    
    // Getter dinamici basati sul tema
    public static String getBgColor() {
        return darkMode ? BG_COLOR_DARK : BG_COLOR_LIGHT;
    }
    
    public static String getSurfaceColor() {
        return darkMode ? SURFACE_COLOR_DARK : SURFACE_COLOR_LIGHT;
    }
    
    public static String getBotMsgColor() {
        return darkMode ? BOT_MSG_COLOR_DARK : BOT_MSG_COLOR_LIGHT;
    }
    
    public static String getTextPrimary() {
        return darkMode ? TEXT_PRIMARY_DARK : TEXT_PRIMARY_LIGHT;
    }
    
    public static String getTextSecondary() {
        return darkMode ? TEXT_SECONDARY_DARK : TEXT_SECONDARY_LIGHT;
    }
    
    // Gestione tema
    public static boolean isDarkMode() {
        return darkMode;
    }
    
    public static void setDarkMode(boolean dark) {
        darkMode = dark;
    }
    
    public static void toggleTheme() {
        darkMode = !darkMode;
    }
}