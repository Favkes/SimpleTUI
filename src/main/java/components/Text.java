package components;

import java.util.ArrayList;

public class Text extends Widget {
    public String content;
    public FormatTuple format;
    public ArrayList<Pixel> pixelArray;

    public Text(Widget parent,
                int y,
                int x,
                String content,
                FormatTuple format) {
        super(parent, y, x, 1, content.length());
        this.content = content;
        this.format = format;
        parent.children.add(this);

        updateContentLength();
        renderToPixels();
    }
    public Text(Widget parent,
                int y,
                int x,
                String content) {
        this(parent, y, x, content, new FormatTuple("", ""));
    }
    public Text(Widget parent,
                String content) {
        this(parent, 0, 0, content);
    }
    public Text(Widget parent,
                int y,
                int x) {
        this(parent, y, x, "");
    }
    public Text(Widget parent) {
        this(parent, "");
    }

    public void updateContentLength() {
        width = content.length();
    }

    public void updateContent(String newContent) {
        content = newContent;
        updateContentLength();
        renderToPixels();
    }

    public void removeLast() {
        if (content.isEmpty()) return;
        updateContent(content.substring(0, content.length() - 1));
    }

    public void renderToPixels() {
        pixelArray = new ArrayList<>();
        for (int i = 0; i<content.length(); i++) {
            pixelArray.add(new Pixel(format, content.charAt(i)));
        }
    }

    public ArrayList<Pixel> generateRenderableBody(int from, int to) {
        ArrayList<Pixel> out = new ArrayList<>();
        for (int i = from; i < to; i++) {
            out.add(
                    pixelArray.get(i % pixelArray.size())
            );
        }
        return out;
    }

    @Override
    public void printInfo() {
        System.out.printf("< Text widget class object instance: %s %d %d %d %d %s >\n", content, y, x, height, width, texture);    }
}
