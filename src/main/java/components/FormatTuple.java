package components;

import ui.Color;

public class FormatTuple {
    public String fg;
    public String bg;
    public String fmt;

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
}
