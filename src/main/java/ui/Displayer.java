package ui;


public class Displayer extends DisplayCore {
    public String frameBodyPrevious;

    public Displayer() throws Exception {
        super();

        frameBody = Color.Background.BLUE + Color.Foreground.BLUE
                + ".".repeat(frameBodySize) + Color.RESET;
    }

    private void goToPixel(int y, int x) {
        // ANSI codes use 1-based indexing, thus the off-by-1-shift
        terminal.writer().print(String.format("\u001B[%d;%dH", y + 1, x + 1));
    }

    private void renderFrame() {
        String frameBodyLatest = frameBody;
        boolean continuousEscapeCodeSequence = false;
        String precedingCharFormatting;

        for (int i=0; i<frameBodySize; i++) {
            if (frameBodyPrevious.charAt(i) != frameBody.charAt(i)) {
                if (continuousEscapeCodeSequence) {
                    continuousEscapeCodeSequence = true;
                }
            }

        }

        frameBodyPrevious = frameBodyLatest;
    }

    @Override
    public void refreshDisplay() {
        terminal.writer().print(frameBody);
        terminal.flush();
    }
}
