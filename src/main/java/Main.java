import org.fusesource.jansi.AnsiConsole;
import ui.Displayer;


public class Main {
    public static void main(String[] args) {
        Displayer display = null;

        // ANSI support install
        AnsiConsole.systemInstall();

        try {
            // App init
            display = new Displayer();
            display.init();

            // Main app loop
            display.updateFrame();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}