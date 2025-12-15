package components;

public class FormattedChunk {
    public int start;
    public FormatTuple formatTuple;
    public String format;
    public String rawContents;

    FormattedChunk(int start, String[] formatTuple, String rawContents) {
        this.start = start;
        this.formatTuple = new FormatTuple(formatTuple);
        this.format = formatTuple[0] + formatTuple[1];
        this.rawContents = rawContents;
    }
}
