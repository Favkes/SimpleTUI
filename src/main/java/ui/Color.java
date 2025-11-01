package ui;

@SuppressWarnings("unused")
public class Color {
    public static final String RESET    = "\u001B[0m";

    public static class Foreground {
        public static final String BLACK    = "\u001B[30m";
        public static final String RED      = "\u001B[31m";
        public static final String GREEN    = "\u001B[32m";
        public static final String YELLOW   = "\u001B[33m";
        public static final String BLUE     = "\u001B[34m";
        public static final String MAGENTA  = "\u001B[35m";
        public static final String CYAN     = "\u001B[36m";
        public static final String WHITE    = "\u001B[37m";
    }

    public static class Background {
        public static final String BLACK    = "\u001B[40m";
        public static final String RED      = "\u001B[41m";
        public static final String GREEN    = "\u001B[42m";
        public static final String YELLOW   = "\u001B[43m";
        public static final String BLUE     = "\u001B[44m";
        public static final String MAGENTA  = "\u001B[45m";
        public static final String CYAN     = "\u001B[46m";
        public static final String WHITE    = "\u001B[47m";
    }

    public static class Template {
        public static final String BASE     = "\u001B[%sm";
        public static final String BASERGB  = "\u001B[%d;2;%d;%d;%dm";
    }

    public static boolean isBackgroundCode(String ansiCode) {
        return ansiCode.matches("\u001B\\[(?:4[0-7]|10[0-7]|48;[0-9;]*|0)m");
    }

    public static String generateRGB(int R, int G, int B) {
        return generateRGB(false, R, G, B);
    }
    public static String generateRGB(boolean isBackground, int R, int G, int B) {
        return String.format(Template.BASERGB, (isBackground) ? 48 : 38, R, G, B);
    }
}
