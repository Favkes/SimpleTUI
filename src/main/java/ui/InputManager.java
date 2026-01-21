package ui;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputManager implements AutoCloseable {
    private final Terminal terminal;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean appRunningRef;
    private final Map<Integer, Runnable> keyBindings = new ConcurrentHashMap<>();
    private Thread inputThread;

    private BindingReader reader;
    private final KeyMap<Runnable> keyMap = new KeyMap<>();

    public InputManager(Terminal terminalRef, AtomicBoolean appRunningRef) {
        this.terminal = terminalRef;
        this.appRunningRef = appRunningRef;
    }

    public void bindKey(InfoCmp.Capability key, Runnable action) {
        keyMap.bind(action, KeyMap.key(terminal, key));
    }
    public void bindKey(String key, Runnable action) {
        keyMap.bind(action, key);
    }


    public void start() {
        if (inputThread != null) return;

        reader = new BindingReader(terminal.reader());

        inputThread = new Thread(() -> {
            try {
                while (appRunningRef.get()) {
                    Runnable action = reader.readBinding(keyMap);
                    if (action != null) {
                        action.run();
                    }
                }
            } catch (Throwable t) {
                if (appRunningRef.get()) {
                    t.printStackTrace();
                }
            }
        }, "InputManager");

        inputThread.setDaemon(true);
        inputThread.start();
    }

    @Override
    public void close() throws Exception {
        running.set(false);
        try {
            if (inputThread != null) {
                inputThread.interrupt();
                inputThread.join(100);
            }
        } catch (InterruptedException ignored) {}
    }
}
