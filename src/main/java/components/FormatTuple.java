package components;

import ui.Color;

import java.text.Format;

public final class FormatTuple {
    public final String fg;
    public final String bg;
    public final String fmt;

    public FormatTuple(String _fg, String _bg) {
        if (_fg.isEmpty() && _bg.isEmpty()) {
            fg = Color.RESET;
            bg = "";
        }
        else if (_fg.isEmpty()) {
            fg = "";
            bg = _bg;
        }
        else if (_bg.isEmpty()) {
            fg = _fg;
            bg = Color.Background.DEFAULT;
        }
        else {
            fg = _fg;
            bg = _bg;
        }

        fmt = fg + bg;
    }
    public FormatTuple(String[] formatTuple) {
        this(formatTuple[0], formatTuple[1]);
    }

    public FormatTuple withBackground(String _bg) {
        return new FormatTuple(this.fg, _bg);
    }
    public FormatTuple withForeground(String _fg) {
        return new FormatTuple(_fg, this.bg);
    }
}
