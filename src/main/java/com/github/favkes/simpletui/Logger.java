package com.github.favkes.simpletui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

public class Logger {
    private static final String FILE = "logs.txt";
    private static PrintWriter writer;
    private static final Object LOCK = new Object();
    private static final StackWalker WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final AtomicBoolean ENABLED = new AtomicBoolean(true);


    static {
        try {
            writer = new PrintWriter(new FileWriter(FILE, false)); // clear on start
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String name;

    private Logger(Class<?> clazz) {
        this.name = clazz.getSimpleName();
    }

    public static Logger logger () {
        return Logger.get(WALKER.getCallerClass());
    }
    public static Logger get(Class<?> clazz) {
        return new Logger(clazz);
    }

    private void log(String type, String msg) {
        synchronized (LOCK) {
            if (ENABLED.get()) {
                writer.println(String.format(
                        "%s [%-5s] | %-15s : %s",
                        LocalDateTime.now().format(FORMATTER),
                        type,
                        name,
                        msg
                ));
                writer.flush();
            }
        }
    }

    public void info(String msg) {
        log("INFO", msg);
    }

    public void error(Throwable t) {
        log("ERROR", t.getMessage() + " Full trace:");
        t.printStackTrace(writer);
        writer.flush();
    }

    public void setEnabled(Boolean bool) {
        log("STATE", String.format("log.ENABLE set to %b", bool));
        ENABLED.set(bool);
    }
    public void disable() { setEnabled(false); }
    public void enable() { setEnabled(true); }
}