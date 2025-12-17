import components.*;
import org.fusesource.jansi.AnsiConsole;
import ui.Color;
import ui.Displayer;
import ui.WindowManager;

import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        // ANSI support install
        AnsiConsole.systemInstall();
        WindowManager manager = new WindowManager();
        Texture displayBackgroundTexture = new Texture(
                Color.generateRGB(false, 30, 30, 30)
                + Color.generateRGB(true, 30, 30, 30)
                + "|"
                + Color.generateRGB(true, 50, 50, 70)
                + Color.generateRGB(false, 50, 50, 70)
                + "."
                + Color.generateRGB(true, 70, 70, 100)
                + Color.generateRGB(false, 70, 70, 100)
                + ".",
                1
        );

        try (Displayer display = new Displayer(manager, displayBackgroundTexture)) {

            // App init
            display.init();

            Texture texture = new Texture(
                    Color.generateRGB(false, 130, 30, 30)
                    + Color.generateRGB(true, 130, 30, 30)
                    + "|"
                    + Color.generateRGB(true, 150, 50, 50)
                    + Color.generateRGB(false, 150, 50, 50)
                    + "."
                    + Color.generateRGB(true, 170, 70, 70)
                    + Color.generateRGB(false, 170, 70, 70)
                    + "."
                    + Color.generateRGB(true, 170, 100, 100)
                    + Color.generateRGB(false, 170, 100, 100)
                    + "."
                    + Color.generateRGB(true, 230, 130, 130)
                    + Color.generateRGB(false, 230, 130, 130)
                    + ".",
                    1
            );
            System.out.print("\n");

            texture.test();

            for (int i=0; i<10; i++)
                System.out.printf(
                        "%s%s\n",
                        texture.generateRepeatingSubarray(i, i+20).stream()
                                .map(Pixel::toString)
                                .collect(Collectors.joining()),
                        Color.RESET);

            Frame frame1 = new Frame(
                    5, 5, 5, 7, texture
            );
            display.windowManager.contents.add(frame1);

            display.renderComponentOfIndex(0);

            // Main app loop
            display.refreshDisplay();
            Thread.sleep(2000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}