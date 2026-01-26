package com.github.favkes.simpletui.ui;

import com.github.favkes.simpletui.components.Widget;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputManager implements AutoCloseable {
    private final Terminal terminal;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean appRunningRef;
    private Thread inputThread;

    private BindingReader reader;
    private KeyMap<Runnable> keyMap = new KeyMap<>();
//    private ArrayList<KeyActionContainer> keyActionList = new ArrayList<>();

    public InputManager(Terminal terminalRef, AtomicBoolean appRunningRef) {
        this.terminal = terminalRef;
        this.appRunningRef = appRunningRef;
    }

    public void bindKey(InfoCmp.Capability key, Runnable action) {
        KeyActionContainer actionContainer = new KeyActionContainer(action);
        keyMap.bind(actionContainer::run, KeyMap.key(terminal, key));
    }
    public void bindKey(String key, Runnable action, Widget requiredFocusWidget) {
        KeyBind keyBind = new KeyBind(key, action, requiredFocusWidget);
        keyMap.bind(keyBind::run, key);
    }
    public void bindKey(String key, Runnable action) {
        KeyBind keyBind = new KeyBind(key, action);
        keyMap.bind(keyBind::run, key);
    }
    public void bindKey(KeyBind keyBind) {
        keyMap.bind(keyBind::run, keyBind.sequence);
    }

    public void clear() {
        keyMap = new KeyMap<>();
    }

    public void loadFromIterable(Iterable<KeyBind> iterable) {
        for (KeyBind keyBind : iterable) {
            bindKey(keyBind);
        }
    }

    public void reloadFromIterable(Iterable<KeyBind> iterable) {
        clear();
        loadFromIterable(iterable);
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
