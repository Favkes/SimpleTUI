package ui;


public class Displayer extends DisplayCore {

    public Displayer() throws Exception {
        super();

        frameBody = Color.Background.BLUE + Color.Foreground.BLUE
                + ".".repeat(cols * rows) + Color.RESET;
    }
}
