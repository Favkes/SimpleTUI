package components;

import ui.Color;

public class Pixel {
    public FormatTuple formatTuple;
    public Character raw;

    public Pixel(FormatTuple formatTuple, Character raw) {
        this.formatTuple = formatTuple;
        this.raw = raw;
    }
    public Pixel(String[] formatTuple, Character raw) {
        this.formatTuple = new FormatTuple(formatTuple);
        this.raw = raw;
    }

    @Override
    public String toString() {
        return formatTuple.fmt + raw + Color.RESET;
    }
}
