package components;
import ui.Color;

import java.util.ArrayList;
import java.util.regex.*;


public class StaticTexture {
    public String patternSeed;

    ArrayList<Pixel> pixelArray;

    private static final Pattern ANSI_ESC_PATTERN = Pattern.compile(
            "\\u001B\\[[;\\d]*m"
    );


    public StaticTexture(String patternSeed) {
        this(patternSeed, 20);
    }

    public StaticTexture(String patternSeed, int repeatTimes) {
        this.patternSeed = patternSeed;
        loadTexture(patternSeed);
    }


    public void loadTexture(String patternSeed) {
        // if unformatted beginning, add reset.
        if (patternSeed.charAt(0) != '\u001b') {
            patternSeed = Color.RESET + patternSeed;
        }

        Matcher matcher = ANSI_ESC_PATTERN.matcher(patternSeed);

        String[] currentFormat = new String[] {"", ""}; // (backgr, foregr)

        pixelArray = new ArrayList<>();

        System.out.printf("Loading texture: %s\n", patternSeed + Color.RESET);
        

        int previous_index = 0;
        while (matcher.find()) {

            for (int i = previous_index; i < matcher.start(); i++) {
                pixelArray.add(
                        new Pixel(currentFormat.clone(), patternSeed.charAt(i))
                );
            }

            String ansi = matcher.group();
            if (Color.isBackgroundCode(ansi)) {
                currentFormat[0] = ansi;
            } else {
                currentFormat[1] = ansi;
            }

            previous_index = matcher.end();
        }
        for (int i = previous_index; i < patternSeed.length(); i++) {
            pixelArray.add(
                    new Pixel(currentFormat.clone(), patternSeed.charAt(i))
            );
        }
    }


    public void test() {
        System.out.print("test(): ");
        System.out.print(pixelArray.size());
        System.out.print(" ");
        for (Pixel pixel : pixelArray) {
            System.out.print(pixel.formatTuple.fmt + pixel.raw);
        }
        System.out.print(Color.RESET + "\n");
    }
}
