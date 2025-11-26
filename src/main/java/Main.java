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
                    Color.generateRGB(false, 230, 20, 20)
                    + Color.generateRGB(true, 20, 20, 20)
                    + "|"
                    + Color.generateRGB(true, 50, 50, 50)
                    + Color.generateRGB(false, 50, 50, 50)
                    + "."
                    + Color.generateRGB(true, 70, 70, 70)
                    + Color.generateRGB(false, 70, 70, 70)
                    + "."
                    + Color.generateRGB(true, 100, 250, 100)
                    + Color.generateRGB(false, 100, 100, 100)
                    + "."
                    + Color.generateRGB(true, 130, 130, 130)
                    + Color.generateRGB(false, 130, 130, 130)
                    + "."
            );
//            texture.loadTexture(
//                    Color.Background.BLUE + "0123" + Color.generateRGB(true, 250, 170, 170) + Color.Foreground.BLACK + "4567" + Color.Foreground.RED + "89ab"
//            );
            System.out.print("\n");
            texture.preGenerate(50);


            for (int i=0; i<10; i++)
                System.out.printf("%s%s\n", texture.fetchChunk(i, i+20), Color.RESET);

            // Main app loop
//            display.refreshDisplay();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}