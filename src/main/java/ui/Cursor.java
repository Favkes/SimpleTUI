package ui;

public class Cursor {
    public static final String HIDE = "\u001B[?25l";
    public static final String SHOW = "\u001B[?25h";

    public static void hide() {
        System.out.print(HIDE);
    }
    public static void show() {
        System.out.print(SHOW);
    }
}
