package ui;

import java.io.IOException;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;


public class DisplayCore implements AutoCloseable {
    String frameBody;
    int cols;
    int rows;

    private final Terminal terminal;

    public DisplayCore() throws Exception {
        terminal = TerminalBuilder
                .builder()
                .system(true)
                .build();
        refreshDisplayDimensions();
    }

    void refreshDisplayDimensions() throws IOException {
        cols = terminal.getWidth();
        rows = terminal.getHeight();
    }

    public void enterAltBuffer() { terminal.writer().print("\u001B[?1049h"); }
    public void exitAltBuffer() { terminal.writer().print("\u001B[?1049l"); }
    public void enterRawMode() { terminal.enterRawMode(); }
    public void exitRawMode() throws IOException { terminal.close(); }

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
    }
    public void exit() throws IOException {
        /*
            Cleanup sequence called at program exit to ensure no unwanted terminal behaviour.

            Exits raw mode, shows back the cursor and exits the alt buffer if it was entered.
        */
        exitRawMode();
        Cursor.show();

        if (isTerminalAltBufferCompatible()) {
            exitAltBuffer();
        }
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

    public void updateFrame() {
        terminal.writer().print(frameBody);
        terminal.flush();
    }

    @Override
    public void close() throws IOException {
        exit();
        terminal.close();
    }
}
