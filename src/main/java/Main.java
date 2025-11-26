import org.fusesource.jansi.AnsiConsole;
import ui.Color;
import ui.Displayer;
import components.Texture2;


public class Main {
    public static void main(String[] args) {
        // ANSI support install
        AnsiConsole.systemInstall();

        try (Displayer display = new Displayer()) {
            // App init
            display.init();
            Texture2 texture = new Texture2(
                    Color.Background.RED + "@" + Color.Background.BLUE + "#"
            );

            // Main app loop
//            display.refreshDisplay();

            texture.loadTexture(
                    "0123" + Color.generateRGB(true, 250, 170, 170) + Color.Foreground.BLACK + "4567" + Color.Foreground.RED + "89ab"
            );
            System.out.print("\n");
            texture.preGenerate(10);

            System.out.printf("%s%s\n", texture.fetchChunk(4, 29), Color.RESET);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}