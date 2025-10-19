import org.fusesource.jansi.AnsiConsole;
import ui.Displayer;


public class Main {
    public static void main(String[] args) {
        // ANSI support install
        AnsiConsole.systemInstall();

        try (Displayer display = new Displayer()) {
            // App init
            display.init();

            // Main app loop
            display.refreshDisplay();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}