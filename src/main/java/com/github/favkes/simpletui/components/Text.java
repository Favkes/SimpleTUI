package com.github.favkes.simpletui.components;

import java.util.ArrayList;

public class Text extends Widget {
    public String content;
    public int charLimit;
    public FormatTuple format;
    public ArrayList<Pixel> pixelArray;

    public Text(Widget parent,
                int posY,
                int posX,
                String content,
                FormatTuple format) {
        super(parent, posY, posX, 1, content.length());
        this.content = content;
        this.format = format;
        parent.children.add(this);
        charLimit = 10_000;

        updateContentLength();
        renderToPixels();
    }
    public Text(Widget parent,
                int posY,
                int posX,
                String content) {
        this(parent, posY, posX, content, new FormatTuple("", ""));
    }
    public Text(Widget parent,
                String content) {
        this(parent, 0, 0, content);
    }
    public Text(Widget parent,
                int posY,
                int posX) {
        this(parent, posY, posX, "");
    }
    public Text(Widget parent) {
        this(parent, "");
    }

    public void updateContentLength() {
        width = content.length();
    }

    public boolean updateContent(String newContent) {
        if (charLimit < newContent.length()) {
            return false;
        }
        content = newContent;
        updateContentLength();
        renderToPixels();
        return true;
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
