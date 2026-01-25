package com.github.favkes.simpletui.ui;

import com.github.favkes.simpletui.components.Widget;

public class KeyActionContainer {
    Runnable action;
    Widget requiredFocusWidget;   // null if focus not required
    boolean requiresFocus;

    public KeyActionContainer(Runnable action, Widget requiredFocus, boolean requiresFocus) {
        this.action = action;
        this.requiredFocusWidget = requiredFocus;
        this.requiresFocus = requiresFocus;
    }
    public KeyActionContainer(Runnable action, Widget requiredFocus) {
        this(action, requiredFocus, true);
    }
    public KeyActionContainer(Runnable action) {
        this(action, null, false);
    }

    public void run() {
        if (!requiresFocus || requiredFocusWidget != null && requiredFocusWidget.isFocused) {
            action.run();
        }
    }
}
