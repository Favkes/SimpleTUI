package components;
import ui.Color;

import java.util.ArrayList;
import java.util.regex.*;


public class Texture2 {
    public String patternSeed;
    public int rawLength;

    public ArrayList<Integer> rawIndexMap;      // translates raw index into real index
    public ArrayList<Integer> formatPointers;         // links every raw char to a format code from formatsList
    public ArrayList<FormatTuple2> formatsList;    // contains all format codes used
    public String patternRepeating;

    ArrayList<FormattedChunk2> formattedChunks;  // little optimized ANSI formatting sequence (LOAFS)

    private static final Pattern ANSI_ESC_PATTERN = Pattern.compile(
            "\\u001B\\[[;\\d]*m"
    );


    public Texture2(String patternSeed) {
        this(patternSeed, 20);
    }

    public Texture2(String patternSeed, int repeatTimes) {
        this.patternSeed = patternSeed;
        loadTexture(patternSeed);
        preGenerate(repeatTimes);
    }


    public void loadTexture(String patternSeed) {
        // if unformatted beginning, add reset.
        if (patternSeed.charAt(0) != '\u001b') {
            patternSeed = Color.RESET + patternSeed;
        }

        Matcher matcher = ANSI_ESC_PATTERN.matcher(patternSeed);

        String[] currentFormat = new String[] {"", ""}; // (backgr, foregr)

        formattedChunks = new ArrayList<>();

        System.out.println(patternSeed + Color.RESET);

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

            // Previous block contained only ansi code? -> update previous block instead of making new one
            if (substr != null && substr.isEmpty() && matchNumber > 1) {
                doUpdateLast = true;
            }

            // Cutting out the raw substr
            substr = patternSeed.substring(previous_index, current_index);

            // Adding new formattedBlock to the processed array
            if (!(matchNumber == 0) && !doUpdateLast) {
                System.out.printf("%s%n", substr);
                formattedChunks.add(new FormattedChunk2(rawIndex, currentFormat.clone(), substr));
            }
            else if (doUpdateLast) {
                // ..or updating the last added block if it only contained an ansi escape code.
                FormattedChunk2 previousChunk = formattedChunks.getLast();
                formattedChunks.set(
                        formattedChunks.size()-1,
                        new FormattedChunk2(previousChunk.start, currentFormat.clone(), substr)
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

        // remainder of the string
        if (previous_index < patternSeed.length()) {
            // there are some final raw chars left
            substr = patternSeed.substring(previous_index);

            formattedChunks.add(new FormattedChunk2(rawIndex, currentFormat.clone(), substr));
        }
        rawLength = rawIndex + substr.length();
    }


    public void test() {
        System.out.print("test(): ");
        for (FormattedChunk2 formattedChunk : formattedChunks) {
            System.out.print("|");
            System.out.print(formattedChunk.format + formattedChunk.rawContents);
        }
        System.out.print(Color.RESET + "\n");
    }

    public void preGenerate(int minLength) {
        if (minLength < 1) {
            System.err.println("Texture cannot be repeated less than once!");
        }
        int repeatTimes = minLength / rawLength + 1;

        StringBuilder textureBody = new StringBuilder();
        rawIndexMap = new ArrayList<>();
        formatPointers = new ArrayList<>();
        formatsList = new ArrayList<>();

//        int absolute_index = -1;
        for (FormattedChunk2 chunk : formattedChunks) {

            textureBody.append(chunk.format);
            formatsList.add(chunk.formatTuple);

            int current_real_i = textureBody.length();

            textureBody.append(chunk.rawContents);

            while (current_real_i < textureBody.length()) {
//                absolute_index++;

                formatPointers.add(formatsList.size() - 1);  // add index of most recent format

//                System.out.printf("%d i%d: \"%s\"", absolute_index, current_real_i, textureBody.charAt(current_real_i));
//                FormatTuple2 format = formatsList.get(formatPointers.get(absolute_index));
//                System.out.printf(" %sformat%d%s\n", format.fg() + format.bg(), formatPointers.get(absolute_index), Color.RESET);

                rawIndexMap.add(current_real_i++);
            }
        }

        StringBuilder patternRepeating_builder = new StringBuilder();
        while (repeatTimes-- != 0) {
            patternRepeating_builder.append(textureBody);
        }

        patternRepeating = patternRepeating_builder.toString();
    }

    public int fetchRawIndex(int realIndex) {
        return rawIndexMap.get(
                realIndex % rawIndexMap.size())
                + (rawIndexMap.getLast() + 1)
                * (realIndex / rawIndexMap.size()
        );
    }

    public String fetchChunk(int from, int to) {
        int chunkSize = to - from;
        from %= rawIndexMap.size();
        to = from + chunkSize;

        return formatsList.get(formatPointers.get(from)).fmt()
                + patternRepeating.substring(
                    fetchRawIndex(from),
                    fetchRawIndex(to)
        );
    }
}

record FormatTuple2(String fg, String bg, String fmt) {
    public FormatTuple2(String[] formatTuple) {
        this(formatTuple[0], formatTuple[1], formatTuple[0] + formatTuple[1]);
    }
}

class FormattedChunk2 {
    public int start;
    public FormatTuple2 formatTuple;
    public String format;
    public String rawContents;

    FormattedChunk2(int start, String[] formatTuple, String rawContents) {
        this.start = start;
        this.formatTuple = new FormatTuple2(formatTuple);
        this.format = formatTuple[0] + formatTuple[1];
        this.rawContents = rawContents;
    }
}
