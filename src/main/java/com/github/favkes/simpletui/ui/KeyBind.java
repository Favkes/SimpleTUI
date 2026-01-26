package com.github.favkes.simpletui.ui;

import com.github.favkes.simpletui.components.Widget;

public class KeyBind extends KeyActionContainer {
    public String sequence;

    public KeyBind(String sequence,
                   Runnable action,
                   Widget requiredFocus,
                   boolean requiresFocus) {
        super(action, requiredFocus, requiresFocus);
        this.sequence = sequence;
    }
    public KeyBind(String sequence,
                   Runnable action,
                   Widget requiredFocus) {
        super(action, requiredFocus);
        this.sequence = sequence;
    }
    public KeyBind(String sequence,
                   Runnable action) {
        super(action);
        this.sequence = sequence;
    }
}
