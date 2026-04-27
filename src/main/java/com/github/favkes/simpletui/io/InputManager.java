package com.github.favkes.simpletui.io;

import com.github.favkes.simpletui.Logger;
import com.github.favkes.simpletui.components.Widget;
import com.github.favkes.simpletui.ui.ModeManager;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.IOError;
import java.util.concurrent.atomic.AtomicBoolean;

public class InputManager implements AutoCloseable {
    private static final Logger log = Logger.logger();

    private final Terminal terminal;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean appRunningRef;
    private Thread inputThread;

    private BindingReader reader;
    private KeyMap<Runnable> keyMap;
    public final ModeManager<KeyMap<Runnable>> keyMapModeManager;


    public InputManager(Terminal terminalRef, AtomicBoolean appRunningRef) {
        this.terminal = terminalRef;
        this.appRunningRef = appRunningRef;

        keyMap = new KeyMap<>();
        keyMapModeManager = new ModeManager<>();
    }

    public void bindKey(InfoCmp.Capability key, Runnable action) {
        KeyActionContainer actionContainer = new KeyActionContainer(action);
        keyMapModeManager.currentModeItem().bind(actionContainer::run, KeyMap.key(terminal, key));
    }
    public void bindKey(String key, Runnable action, Widget requiredFocusWidget) {
        KeyBind keyBind = new KeyBind(key, action, requiredFocusWidget);
        keyMapModeManager.currentModeItem().bind(keyBind::run, key);
    }
    public void bindKey(String key, Runnable action) {
        KeyBind keyBind = new KeyBind(key, action);
        keyMapModeManager.currentModeItem().bind(keyBind::run, key);
    }
    public void bindKey(KeyBind keyBind) {
        keyMapModeManager.currentModeItem().bind(keyBind::run, keyBind.sequence);
    }


    public void newMap(String mapName) {
        KeyMap<Runnable> newMap = new KeyMap<>();
        newMap.setNomatch(() -> {
            log.info("No match for given key.");
        });
        newMap.setAmbiguousTimeout(200);
        keyMapModeManager.add(mapName, newMap);

        log.info(
                String.format("Added %d. map \"%s\" to keyMapModeManager.", keyMapModeManager.numberOfModes, mapName)
        );

//        BindingReader newReader = new BindingReader(terminal.reader());
//        readerModeManager.add(mapName, newReader);
    }

    public void switchModeTo(String modeName) {
        keyMapModeManager.modeSet(modeName);
        keyMap = keyMapModeManager.currentModeItem();
        log.info(String.format("Keymap switched to: %d: \"%s\"", keyMapModeManager.mode, keyMapModeManager.currentModeName()));
    }

    public void loadFromIterable(Iterable<KeyBind> iterable) {
        for (KeyBind keyBind : iterable) {
            bindKey(keyBind);
        }
    }


    public void start() {
        if (inputThread != null) return;
        log.info(".start() called");

        reader = new BindingReader(terminal.reader());

        inputThread = new Thread(() -> {
            try {
                log.info("Starting new thread...");
                while (appRunningRef.get()) {
                    try {
                        //                    KeyMap<Runnable> km = keyMapModeManager.modeItem();
                        //                    BindingReader reader = readerModeManager.modeItem();
                        Runnable action = reader.readBinding(keyMapModeManager.currentModeItem());
                        if (action != null) {
                            action.run();
                        }
                    } catch (IOError e) {
                        if (e.getCause() instanceof java.io.InterruptedIOException) {
                            log.error(e);
                            log.info("Stopping thread...");
                            break;
                        } else {
                            log.error(e);
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Throwable t) {
                if (appRunningRef.get()) {
                    t.printStackTrace();
                }
            }
            log.info("Thread stopped.");
        }, "InputManager");

        inputThread.setDaemon(true);
        inputThread.start();
    }

    public void stop() {
        running.set(false);
        if (inputThread != null) {
            inputThread.interrupt();
            try {
                inputThread.join(100);
            } catch (InterruptedException ignored) {}
            inputThread = null;
        }
    }

    @Override
    public void close() throws Exception {
        stop();
    }
}
