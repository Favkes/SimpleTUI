package components;
import ui.Color;

import java.util.ArrayList;
import java.util.regex.*;


public class Texture {
    public String content;
    public String raw_symbols;  // content - ANSI
    ArrayList<FormattedChunk> formattedChunks;  // little optimized ANSI formatting sequence (LOAFS)

//    private String foreground;
//    private String background;
    private static final Pattern ANSI_ESC_PATTERN = Pattern.compile(
            "\\u001B\\[[;\\d]*m"
    );

    public Texture() {
        this(Color.Background.BLUE);
    }
    public Texture(String content) {
        this.content = content;
    }

    public void loadTexture(String content) {
        // if unformatted beginning, add reset.
        if (content.charAt(0) != '\u001b') {
            content = Color.RESET + content;
        }

        Matcher matcher = ANSI_ESC_PATTERN.matcher(content);

        String[] currentFormat = new String[] {"", ""}; // (backgr, foregr)

        raw_symbols = "";
        formattedChunks = new ArrayList<>();

        System.out.println(content + Color.RESET);

        int previous_index = 0;
        int current_index;

        int rawIndex = 0;
        boolean firstFind = true;
        while (matcher.find()) {
            // Important indices
            String targeted_ansi_code = matcher.group();
            current_index = matcher.start();

            if (firstFind) {
                rawIndex = current_index;
            }

            // Cutting out the raw substr
            String substr = content.substring(previous_index, current_index);
            raw_symbols += substr;

            // Adding new formattedBlock to the processed array
//            System.out.println(
//                    String.format(Color.RESET + "%d %d %d >%s< >%sX" + Color.RESET + "< >%sX" + Color.RESET + "<",
//                            previous_index, current_index, rawIndex, substr, currentFormat[0], currentFormat[1]));
            if (!firstFind) {
                formattedChunks.add(new FormattedChunk(rawIndex, currentFormat.clone(), substr));
            }

            // new format update
            if (Color.isBackgroundCode(targeted_ansi_code)) {
                currentFormat[0] = targeted_ansi_code;
            }
            else {
                currentFormat[1] = targeted_ansi_code;
            }

            // Set previous index to the next character index (following the currently focused)
            previous_index = matcher.end();
            rawIndex += substr.length();
            firstFind = false;
        }
//        System.out.println(
//                String.format(">>%d/%d", previous_index, content.length())
//        );
        if (previous_index < content.length()) {
            // there are some final raw chars left
            String substr = content.substring(previous_index, content.length());
//            System.out.println(
//                    String.format(Color.RESET + "%d %d %d >%s< >%sX" + Color.RESET + "< >%sX" + Color.RESET + "<",
//                            previous_index, content.length(), rawIndex, substr, currentFormat[0], currentFormat[1]));
            formattedChunks.add(new FormattedChunk(rawIndex, currentFormat.clone(), substr));
        }
    }


    public void test() {
        System.out.print("test(): ");
        for (FormattedChunk formattedChunk : formattedChunks) {
            System.out.print("|");
            System.out.print(formattedChunk.format + formattedChunk.rawContents);
        }
        System.out.print(Color.RESET);
    }

    public String fetchChunk(int from, int to) {
        // Obv safety clause
        if (from >= to) {
            System.err.print("The 'from' parameter has to be smaller than the 'to' parameter.");
            return "";
        }

        // div -> whole array repetitions
        // mod -> local edge subarray boundaries
        int fromDivArrLen = from / raw_symbols.length();
        int fromModArrLen = from % raw_symbols.length();
        int toDivArrLen = to / raw_symbols.length();
        int toModArrLen = to / raw_symbols.length();

        // Locating the first chunk
        int chunkIndex = 0;
        while (formattedChunks.get(++chunkIndex).start < from) {
            System.out.println(String.format("--start chunk%d from=%d .start=%d", chunkIndex, from, formattedChunks.get(chunkIndex).start));
        }
        chunkIndex--;

        boolean areBothBoundsInOneChunk = (formattedChunks.get(chunkIndex + 1).start > to);
        if (areBothBoundsInOneChunk) {
            // Returning the substring
            FormattedChunk currentChunk = formattedChunks.get(chunkIndex);
            System.out.println(String.format("start > chunk%d from=%d .start=%d", chunkIndex, from, currentChunk.start));
            return currentChunk.format +
                    currentChunk.rawContents.substring(
                            from - currentChunk.start,
                            to - currentChunk.start
                    );
        } // else: add first, increment&check if last: { add chunk }, add last :

        // var init
        String out = "";

        // Adding the first block substring (from)
        FormattedChunk currentChunk = formattedChunks.get(chunkIndex);
        System.out.println(String.format("start > chunk%d from=%d .start=%d", chunkIndex, from, currentChunk.start));
        out += currentChunk.format + currentChunk.rawContents.substring(from - currentChunk.start);

        // Adding the whole blocks (between)
        while (formattedChunks.get((++chunkIndex + 1) % formattedChunks.size()).start < to) {
//            chunkIndex++;
            System.out.println(String.format("--to chunk%d to=%d .start=%d", chunkIndex, to, formattedChunks.get(chunkIndex).start));
            currentChunk = formattedChunks.get(chunkIndex);
            out += currentChunk.format + currentChunk.rawContents;
        }


        // Adding the last block substring (to)
        currentChunk = formattedChunks.get(chunkIndex);
        System.out.println(String.format("to > chunk%d to=%d .start=%d", chunkIndex, to, currentChunk.start));
        out += currentChunk.format + currentChunk.rawContents.substring(0, to - currentChunk.start);

        return out;
    }
}

class FormattedChunk {
    public int start;
    public String[] formatTuple;
    public String format;
    public String rawContents;

    FormattedChunk(int start, String[] formatTuple, String rawContents) {
        this.start = start;
        this.formatTuple = formatTuple;
        this.format = formatTuple[0] + formatTuple[1];
        this.rawContents = rawContents;
    }
}
