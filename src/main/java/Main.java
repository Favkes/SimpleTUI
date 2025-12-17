import components.*;
import org.fusesource.jansi.AnsiConsole;
import ui.Color;
import ui.Displayer;
import ui.InputManager;
import ui.WindowManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        // ANSI support install
        AnsiConsole.systemInstall();
        WindowManager manager = new WindowManager();
        AdvancedTexture displayBackgroundTexture = new AdvancedTexture(
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


            AdvancedTexture texture = new AdvancedTexture(
                    Color.generateBgFg(130, 30, 30, 30, 30, 30)
                    + "|"
                    + Color.generateBgFg(150, 50, 50, 150, 50, 50)
                    + "."
                    + Color.generateBgFg(170, 70, 70, 170, 70, 70)
                    + "."
                    + Color.generateBgFg(170, 100, 100, 170, 100, 100)
                    + "."
                    + Color.generateBgFg(230, 130, 130, 230, 130, 130)
                    + ".",
                    1,
                    r -> (100 - r) % 5
            );
            System.out.print("\n");
            texture.test();

            AdvancedTexture texture2 = new AdvancedTexture(
                    Color.generateRGB(true, 60, 60, 60)
                            + Color.generateRGB(false,  190, 160, 190)
                            + "["
                            + Color.generateRGB(true, 150, 200, 150)
                            + Color.generateRGB(false, 30, 30, 30)
                            + "+++"
                            + Color.generateRGB(true, 60, 60, 60)
                            + Color.generateRGB(false, 190, 160, 190)
                            + "]",
                    1,
                    r -> 0
            );

            for (int i=0; i<10; i++)
                System.out.printf(
                        "%s%s\n",
                        texture.generateRepeatingSubarray(i, i+20).stream()
                                .map(Pixel::toString)
                                .collect(Collectors.joining()),
                        Color.RESET);

            Frame frame1 = new Frame(
                    5, 10, 5, 7, texture
            );
            display.windowManager.contents.add(frame1);

            Frame frame2 = new Frame(
                    25, 15, 7, 5, texture2
            );
            display.windowManager.contents.add(frame2);

            display.renderComponentOfIndex(0);
            display.renderComponentOfIndex(1);

            // Main app loop
//            display.refreshDisplay();
//            Thread.sleep(2000);


            // Input thread setup
            display.inputManager.bindKey('q', () -> display.running.set(false));
            display.inputManager.bindKey('w', () -> frame1.y--);
            display.inputManager.bindKey('a', () -> frame1.x--);
            display.inputManager.bindKey('s', () -> frame1.y++);
            display.inputManager.bindKey('d', () -> frame1.x++);

            while (display.running.get()) {
                display.generateBlankPixelMatrix();
                display.rebuildEmpty();
                display.renderComponentOfIndex(0);
                display.renderComponentOfIndex(1);
                display.refreshDisplay();
//                frame1.y += 1;
                Thread.sleep(1000 / 20);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
        AnsiConsole.systemUninstall();
    }
}