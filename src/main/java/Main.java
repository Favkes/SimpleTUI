import org.fusesource.jansi.AnsiConsole;
import ui.Color;
import ui.Displayer;
import components.Texture;


public class Main {
    public static void main(String[] args) {
        // ANSI support install
        AnsiConsole.systemInstall();

        try (Displayer display = new Displayer()) {
            // App init
            display.init();
            Texture texture = new Texture(
                    Color.Background.RED + "@" + Color.Background.BLUE + "#"
            );

            // Main app loop
            display.refreshDisplay();

            texture.loadTexture(
                    "0123" + Color.Background.BLUE + "4567" + Color.Foreground.RED + "89ab"
            );
            System.out.print("\n");
            texture.test();
            System.out.print("\n");
            for (int i=0; i<10; i++)
                System.out.println(String.format("%d %s" + Color.RESET + "\n", i, texture.fetchChunk(0, i)));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}