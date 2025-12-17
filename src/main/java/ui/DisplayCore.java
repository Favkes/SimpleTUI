package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import components.Pixel;
import components.Texture;
import org.jline.terminal.Attributes;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;


public class DisplayCore implements AutoCloseable {
    StringBuilder frameBody;
    ArrayList<ArrayList<Pixel>> pixelMatrix;
    public Texture texture;

    int cols;
    int rows;
    int frameBodySize;

    public final Terminal terminal;
    private Attributes originalTerminalAttributes;
    public final InputManager inputManager;
    public final AtomicBoolean running;

    public DisplayCore(Texture texture) throws Exception {
        this.texture = texture;

        terminal = TerminalBuilder
                .builder()
                .system(true)
                .build();
        refreshDisplayDimensions();
        generateBlankPixelMatrix();

        this.running = new AtomicBoolean(true);
        this.inputManager = new InputManager(terminal, running);
    }

    void refreshDisplayDimensions() {
        cols = terminal.getWidth();
        rows = terminal.getHeight();
        frameBodySize = cols * rows;
    }

    public void generateBlankPixelMatrix() {
        pixelMatrix = new ArrayList<>(rows);
        for (int r = 0; r < rows; r++) {
            pixelMatrix.add(
                    texture.generateRepeatingSubarray(r % 3, cols + r % 3)
            );
        }
    }

    public void enterAltBuffer() { terminal.writer().print("\u001B[?1049h"); }
    public void exitAltBuffer() { terminal.writer().print("\u001B[?1049l"); }
    public void enterRawMode() { originalTerminalAttributes = terminal.enterRawMode(); }
    public void exitRawMode() { if (originalTerminalAttributes != null) terminal.setAttributes(originalTerminalAttributes); }

    public void init() {
        /*
            Entry sequence called at program init to ensure no unwanted terminal behaviour.

            Enters raw mode, hides the cursor and enters the alt buffer if it is possible.
        */
        enterRawMode();
        Cursor.hide();

        if (isTerminalAltBufferCompatible()) {
            enterAltBuffer();
        }

        inputManager.start();
    }
    public void exit() throws IOException {
        /*
            Cleanup sequence called at program exit to ensure no unwanted terminal behaviour.

            Exits raw mode, shows back the cursor and exits the alt buffer if it was entered.
        */

        if (isTerminalAltBufferCompatible()) {
            exitAltBuffer();
        }

        Cursor.show();

        terminal.writer().print(Color.RESET);
        terminal.flush();

        exitRawMode();

    }
    private boolean isTerminalAltBufferCompatible() {
        /*
            Determine whether running terminal supports alternate buffers.

            Some windows terminals seem to not to, therefore this method
            checks, if there is AltBuffer support.
        */
        String os = System.getProperty("os.name").toLowerCase();
        String term = System.getenv("TERM");
        return !os.contains("win") || term != null && term.contains("xterm");
    }

    public void rebuildEmpty() {
        frameBody = new StringBuilder();
        for (ArrayList<Pixel> row : pixelMatrix) {
            for (Pixel p : row) {
                frameBody.append(p);
            }
        }
    }

    public void refreshDisplay() {
        rebuildEmpty();
        terminal.writer().print(frameBody.toString());
        terminal.flush();
    }

    @Override
    public void close() throws Exception {
        this.exit();
        terminal.close();
        inputManager.close();
    }
}
