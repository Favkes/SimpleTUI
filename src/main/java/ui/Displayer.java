package ui;


public class Displayer extends DisplayCore {
    public String frameBodyPrevious;

    public Displayer() throws Exception {
        super();

        frameBody = new StringBuilder();
        frameBody.append(Color.Background.RED)
                .append(Color.Foreground.YELLOW)
                .append(".".repeat(frameBodySize))
                .append(Color.RESET);
    }

    private void goToPixel(int y, int x) {
        // ANSI codes use 1-based indexing, thus the off-by-1-shift
        terminal.writer().print(String.format("\u001B[%d;%dH", y + 1, x + 1));
    }

    private void renderFrame() {
        String frameBodyLatest = frameBody.toString();
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

    private void renderFrame2() {
//        String frame
    }

    @Override
    public void refreshDisplay() {
        terminal.writer().print(frameBody);
        terminal.flush();
    }
}
