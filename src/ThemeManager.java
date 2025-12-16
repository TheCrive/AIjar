import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static ThemeManager instance;
    private List<Themeable> themeables = new ArrayList<>();
    
    private ThemeManager() {}
    
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }
    
    public void register(Themeable themeable) {
        if (!themeables.contains(themeable)) {
            themeables.add(themeable);
        }
    }
    
    public void unregister(Themeable themeable) {
        themeables.remove(themeable);
    }
    
    public void toggleTheme() {
        StyleConstants.toggleTheme();
        notifyAll_();
    }
    
    public void setDarkMode(boolean dark) {
        StyleConstants.setDarkMode(dark);
        notifyAll_();
    }
    
    private void notifyAll_() {
        for (Themeable t : themeables) {
            t.applyTheme();
        }
    }
}