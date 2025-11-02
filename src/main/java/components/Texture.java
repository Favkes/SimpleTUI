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

        String substr = null;
        int previous_index = 0;
        int current_index;

        int rawIndex = 0;
        int matchNumber = -1;
        boolean doUpdateLast = false;
        while (matcher.find()) { matchNumber++;
            // Important indices
            String targeted_ansi_code = matcher.group();
            current_index = matcher.start();
            if (matchNumber == 0) {
                rawIndex = current_index;
            }

            System.out.println("now..?");
            // Previous block contained only ansi code? -> update previous block instead of making new one
            if (substr != null && substr.isEmpty() && matchNumber > 1) {
                doUpdateLast = true;
            }

            // Cutting out the raw substr
            substr = content.substring(previous_index, current_index);
            raw_symbols += substr;

//            System.out.println(
//                    String.format(Color.RESET + "%d %d %d >%s< >%sX" + Color.RESET + "< >%sX" + Color.RESET + "<",
//                            previous_index, current_index, rawIndex, substr, currentFormat[0], currentFormat[1]));

            // Adding new formattedBlock to the processed array
            if (!(matchNumber == 0) && !doUpdateLast) {
                System.out.println(String.format("%s", substr));
                formattedChunks.add(new FormattedChunk(rawIndex, currentFormat.clone(), substr));
            }
            else if (doUpdateLast) {
                // ..or updating the last added block if it only contained an ansi escape code.
                FormattedChunk previousChunk = formattedChunks.getLast();
                formattedChunks.set(
                        formattedChunks.size()-1,
                        new FormattedChunk(previousChunk.start, currentFormat.clone(), substr)
                );
                doUpdateLast = false;
            }

            // New format update
            if (Color.isBackgroundCode(targeted_ansi_code)) {
                currentFormat[0] = targeted_ansi_code;
            }
            else {
                currentFormat[1] = targeted_ansi_code;
            }

            // Set previous index to the next character index (following the currently focused)
            previous_index = matcher.end();
            rawIndex += substr.length();
        }
//        System.out.println(
//                String.format(">>%d/%d", previous_index, content.length())
//        );
        if (previous_index < content.length()) {
            // there are some final raw chars left
            substr = content.substring(previous_index, content.length());
            raw_symbols += substr;
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
        int from_ = from % raw_symbols.length();            // (from) MOD (texture length)
        int toDivArrLen = to / raw_symbols.length();
        int to_ = to % raw_symbols.length();                // (to) MOD (texture length)
        int fullArrCycles = toDivArrLen - fromDivArrLen;
        System.out.println(String.format("fullarrcycles = %d - %d = %d", toDivArrLen, fromDivArrLen, fullArrCycles));

        // Locating the first chunk
        int chunkIndex = 0;
        while (formattedChunks.get(++chunkIndex).start < from_) {
            System.out.println(String.format("--start chunk%d from=%d .start=%d", chunkIndex, from, formattedChunks.get(chunkIndex).start));
        }
        chunkIndex--;

//        // SCENARIO #1: Single block
        boolean areBothBoundsInOneChunk = (formattedChunks.get(chunkIndex + 1).start > to_);
        if (areBothBoundsInOneChunk) {
            // Returning the substring
            FormattedChunk currentChunk = formattedChunks.get(chunkIndex);
            System.out.println(String.format("start > chunk%d from=%d .start=%d", chunkIndex, from, currentChunk.start));
            return currentChunk.format +
                    currentChunk.rawContents.substring(
                            from - currentChunk.start,
                            to - currentChunk.start
                    );
        }

//        //SCENARIO #2: Single subarray (No out of bounds)

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

//        // SCENARIO #3: Entire array duplication
        while (--fullArrCycles > 0) {
            System.out.println("FULLARRAYYYY-----------------------------");
        }
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
