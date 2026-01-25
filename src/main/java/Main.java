import components.*;
//import org.fusesource.jansi.AnsiConsole;
//import org.jline.utils.InfoCmp;
import ui.Color;
import ui.Displayer;
//import ui.InputManager;
import ui.KeyActionContainer;
import ui.WindowManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        // ANSI support install
//        AnsiConsole.systemInstall();
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

//            for (int i=0; i<10; i++)
//                System.out.printf(
//                        "%s%s\n",
//                        texture.generateRepeatingSubarray(0, 20, i).stream()
//                                .map(Pixel::toString)
//                                .collect(Collectors.joining()),
//                        Color.RESET);

            Frame frame1 = new Frame(
                    display.windowManager.root, 5, 10, 5, 7, texture
            );
            display.windowManager.contents.add(frame1);


            // WINDOW BORDERS
            AdvancedTexture windowBorderTexture = new AdvancedTexture(
                    Color.generateRGB(true, 60, 60, 60)
                            + Color.generateRGB(false,  160, 250, 160)
                            + "|",
                    1,
                    r -> 0
            );
            AdvancedTexture windowBorderTexture2 = new AdvancedTexture(
                    Color.generateRGB(true, 60, 60, 60)
                            + Color.generateRGB(false,  160, 250, 160)
                            + "-",
                    1,
                    r -> 0
            );
            Frame windowBorderLeft = new Frame(
                    display.windowManager.root, 0, 0, display.terminal.getHeight(), 1, windowBorderTexture
            );
            display.windowManager.contents.add(windowBorderLeft);
            Frame windowBorderRight = new Frame(
                    display.windowManager.root, 0, display.terminal.getWidth()-1, display.terminal.getHeight(), 1, windowBorderTexture
            );
            display.windowManager.contents.add(windowBorderRight);
            Frame windowBorderTop = new Frame(
                    display.windowManager.root, 0, 0, 1, display.terminal.getWidth(), windowBorderTexture2
            );
            display.windowManager.contents.add(windowBorderTop);
            Frame windowBorderBottom = new Frame(
                    display.windowManager.root, display.terminal.getHeight()-1, 0, 1, display.terminal.getWidth(), windowBorderTexture2
            );
            display.windowManager.contents.add(windowBorderBottom);

            // vertical line between menu and chat
            display.windowManager.contents.add(
                    new Frame(
                            display.windowManager.root,
                            1, 25, display.terminal.getHeight()-2, 1,
                            windowBorderTexture
                    )
            );

            // FRAMES --------------------------------------------------------------------------------------------------
//            display.windowManager.contents.add(
//                    new Frame(
//                            display.windowManager.root,
//                            2, 3, 4, 6,
//                            texture
//                    )
//            );

            Frame menuAFrame = new Frame(
                    display.windowManager.root,
                    2, 3, 4, 6,
                    texture
            ); display.windowManager.contents.add(menuAFrame);
            Text menuAText = new Text(
                    menuAFrame,
                    0, 0,
                    "Menu A"
            ); display.windowManager.contents.add(menuAText);

            AdvancedTexture messageWriteBoxTexture = new AdvancedTexture(
                    Color.generateBgFg(200, 200, 200, 30, 30, 80)
                    + " "
            );
            Frame messageWriteBox = new Frame(
                    display.windowManager.root,
                    display.terminal.getHeight()-2-1,
                    26,
                    2,
                    display.terminal.getWidth()-26-1,
                    messageWriteBoxTexture
            );
            display.windowManager.contents.add(messageWriteBox);

            Text messageWriteBoxText = new Text(
                    messageWriteBox,
                    0, 0,
                    "Content Preview (Click Enter to remove)"
            );
            display.windowManager.contents.add(messageWriteBoxText);
            messageWriteBoxText.updateContentLength();
            messageWriteBoxText.renderToPixels();


            // Input thread setup
            display.inputManager.bindKey("\033", () -> display.running.set(false)); // escape
            display.inputManager.bindKey("\033[A", () -> frame1.y--);
            display.inputManager.bindKey("\033[D", () -> frame1.x--);
            display.inputManager.bindKey("\033[B", () -> frame1.y++);
            display.inputManager.bindKey("\033[C", () -> frame1.x++);
            display.inputManager.bindKey("\u0012", () -> frame1.x += 0);            // Ctrl+R
            display.inputManager.bindKey("a", () -> frame1.x += 2);            // Ctrl+R



            display.inputManager.bindKey("\r", () -> messageWriteBoxText.updateContent(""));    // enter
            display.inputManager.bindKey("\177", messageWriteBoxText::removeLast);  // backspace

            // adding all the valid ASCII characters:
            for (char c = 32; c <=126 ; c++) { // 33 - 126
                char keyChar = c;
                display.inputManager.bindKey(
                        String.valueOf(keyChar),
                        () -> messageWriteBoxText.updateContent(messageWriteBoxText.content + keyChar)
                );
            }


            while (display.running.get()) {
                display.generateBlankPixelMatrix();
                display.rebuildEmpty();
                for (int i=0; i<display.windowManager.contents.size(); i++)
                    display.renderComponentOfIndex(i);
//                display.renderComponentOfIndex(0);
//                display.renderComponentOfIndex(1);
                display.refreshDisplay();
//                frame1.y += 1;
                Thread.sleep(1000 / 20);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // ANSI support uninstall
//        AnsiConsole.systemUninstall();
    }
}