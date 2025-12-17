package ui;

import org.jline.terminal.Terminal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputManager implements AutoCloseable {
    private final Terminal terminal;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean appRunningRef;
    private final Map<Integer, Runnable> keyBindings = new ConcurrentHashMap<>();
    private Thread inputThread;

    public InputManager(Terminal terminalRef, AtomicBoolean appRunningRef) {
        this.terminal = terminalRef;
        this.appRunningRef = appRunningRef;
    }

    public void bindKey(int keyCode, Runnable action) {
        keyBindings.put(keyCode, action);
    }

    public void start() {
        if (running.get()) return;
        running.set(true);

        inputThread = new Thread(() -> {
            try {
                while (appRunningRef.get()) {
                    int ch = terminal.reader().read();  // raw mode
                    Runnable action = keyBindings.get(ch);
                    if (action != null) {
                        action.run();
                    }
                }
            } catch (Exception e) {
                if (running.get()) e.printStackTrace();
            }
        });

        inputThread.setDaemon(true);
        inputThread.start();
    }

    @Override
    public void close() throws Exception {
        running.set(false);
        try {
            if (inputThread != null) inputThread.join(100);
        } catch (InterruptedException ignored) {}
    }
}
