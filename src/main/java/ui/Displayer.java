package ui;


import components.AdvancedTexture;
import components.Pixel;
import components.Texture;
import components.Widget;

import java.util.ArrayList;

public class Displayer extends DisplayCore {
    public String frameBodyPrevious;
    public WindowManager windowManager;

    public Displayer(WindowManager windowManager, Texture texture) throws Exception {
        super(texture);

        frameBody = new StringBuilder();
        frameBody.append(Color.Background.RED)
                .append(Color.Foreground.YELLOW)
                .append(".".repeat(frameBodySize))
                .append(Color.RESET);
        this.windowManager = windowManager;
    }

    private void goToPixel(int y, int x) {
        // ANSI codes use 1-based indexing, thus the off-by-1-shift
        terminal.writer().print(String.format("\u001B[%d;%dH", y + 1, x + 1));
    }

//    private void renderFrame() {
//        String frameBodyLatest = frameBody.toString();
//        boolean continuousEscapeCodeSequence = false;
//        String precedingCharFormatting;
//
//        for (int i=0; i<frameBodySize; i++) {
//            if (frameBodyPrevious.charAt(i) != frameBody.charAt(i)) {
//                if (continuousEscapeCodeSequence) {
//                    continuousEscapeCodeSequence = true;
//                }
//            }
//
//        }
//
//        frameBodyPrevious = frameBodyLatest;
//    }

//    private void renderFrame2() {
////        String frame
//    }

    public void renderComponentOfIndex(int componentIndex) {
        Widget component = windowManager.contents.get(componentIndex);
        String format = Color.Background.RED + Color.Foreground.YELLOW;


        for (int rowIndex = component.y + component.height; rowIndex > component.y; rowIndex--) {
//            int from = rowIndex * cols + component.x;
//            int to = from + component.width;

            // Texture coordinates global (local to display)
            int from = component.x;
            int to = from + component.width;

            // Texture coordinates local to component
            int _from = 0;
            int _to = to - from;

            ArrayList<Pixel> row = pixelMatrix.get(rowIndex);

//            frameBody.replace(
//                    to + 1,
//                    to + 1 + format.length(),
//                    format
//            );
            row.subList(from, to).clear();
            if (component.texture instanceof AdvancedTexture advTex) {
                row.addAll(from, advTex.generateRepeatingSubarray(_from, _to, rowIndex));
            } else {
                row.addAll(from, component.texture.generateRepeatingSubarray(_from, _to));
            }

//            frameBody.replace(from, to, component.texture.fetchChunk(0, to - from));
        }
    }

//    @Override
//    public void refreshDisplay() {
//        terminal.writer().print(frameBody);
//        terminal.flush();
//    }
}
