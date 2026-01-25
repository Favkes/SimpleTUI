package com.github.favkes.simpletui.components;
import com.github.favkes.simpletui.ui.Color;

import java.util.ArrayList;
import java.util.regex.*;


public class Texture {
    public String patternSeed;

    ArrayList<Pixel> pixelArray;

    private static final Pattern ANSI_ESC_PATTERN = Pattern.compile(
            "\\u001B\\[[;\\d]*m"
    );


    public Texture(String patternSeed) {
        this(patternSeed, 1);
    }

    public Texture(String patternSeed, int repeatTimes) {
        this.patternSeed = patternSeed;
        loadTexture(patternSeed);

        ArrayList<Pixel> pixelArrayRepeat = new ArrayList<>(pixelArray.size() * repeatTimes);
        for (int i = 0; i < repeatTimes; i++) pixelArrayRepeat.addAll(pixelArray);
        pixelArray = pixelArrayRepeat;
    }


    public void loadTexture(String patternSeed) {
        // if unformatted beginning, add reset.
        if (patternSeed.charAt(0) != '\u001b') {
            patternSeed = Color.RESET + patternSeed;
        }

        Matcher matcher = ANSI_ESC_PATTERN.matcher(patternSeed);

        System.out.printf("Loading texture: %s\n", patternSeed + Color.RESET);

        FormatTuple currentFormat = new FormatTuple("", "");
        pixelArray = new ArrayList<>();
        int previous_index = 0;
        while (matcher.find()) {

            // Add all pixels from last format code to the currently targetted format code
            for (int i = previous_index; i < matcher.start(); i++) {
                pixelArray.add(
                        new Pixel(currentFormat, patternSeed.charAt(i))
                );
            }

            // Update currentFormat with the currently targetted format code for next iteration
            String ansi = matcher.group();
            if (Color.isBackgroundCode(ansi)) {
                currentFormat = currentFormat.withBackground(ansi);
            } else {
                currentFormat = currentFormat.withForeground(ansi);
            }

            previous_index = matcher.end();
        } // no format codes remaining

        // Add all pixels from the last format code to the end of the string
        for (int i = previous_index; i < patternSeed.length(); i++) {
            pixelArray.add(
                    new Pixel(currentFormat, patternSeed.charAt(i))
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

    public ArrayList<Pixel> generateRepeatingSubarray(int from, int to) {
        ArrayList<Pixel> out = new ArrayList<>();
        for (int i = from; i < to; i++) {
            out.add(
                    pixelArray.get(i % pixelArray.size())
            );
        }
        return out;
    }
}
