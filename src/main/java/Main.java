import org.fusesource.jansi.AnsiConsole;
import ui.Displayer;


public class Main {
    public static void main(String[] args) {
        Displayer display = null;

        try {
            // ANSI support install
            AnsiConsole.systemInstall();

            // App init
            display = new Displayer();
            display.init();

            // Main app loop
            display.updateFrame();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Display cleanup
            if (display != null) {
                try {
                    display.exit();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // ANSI support uninstall
            AnsiConsole.systemUninstall();
        }
    }
}