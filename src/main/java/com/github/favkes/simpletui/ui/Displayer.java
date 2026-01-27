package com.github.favkes.simpletui.ui;


import com.github.favkes.simpletui.components.*;

import java.util.ArrayList;

public class Displayer extends DisplayCore {
    public String frameBodyPrevious;
    public PageManager pageManager;

    private static volatile Displayer INSTANCE;

    public static synchronized Displayer init(Texture texture) throws Exception {
        if (INSTANCE != null) {
            throw new IllegalStateException("Displayer already initialized");
        }
        INSTANCE = new Displayer(texture);
        return INSTANCE;
    }

    private Displayer(Texture texture) throws Exception {
        super(texture);

        frameBody = new StringBuilder();
        frameBody.append(Color.Background.RED)
                .append(Color.Foreground.YELLOW)
                .append(".".repeat(frameBodySize))
                .append(Color.RESET);
        this.pageManager = new PageManager();
    }

    private void goToPixel(int y, int x) {
        // ANSI codes use 1-based indexing, thus the off-by-1-shift
        terminal.writer().print(String.format("\u001B[%d;%dH", y + 1, x + 1));
    }

    public void renderAll() {
        for (Page page : pageManager.pages) {
            for (Widget widget : page.components) {
                renderWidget(widget);
            }
        }
    }

    public void renderPage(Page page) {
        for (Widget widget : page.components) {
            renderWidget(widget);
        }
    }

    public void renderComponentOfIndex(int componentIndex) {
        /*
            Not useful anymore, going to disappear in a few versions!
        */

//        Widget component = pageManager.pages.get(componentIndex);
//        renderWidget(component);
    }

    public void renderWidget(Widget component) {
        if (!component.shouldRender) return;

        int globalComponentY = component.y;
        int globalComponentX = component.x;
        Widget componentParent = component.parent;
        while (!componentParent.isRoot()) {
            globalComponentY += componentParent.y;
            globalComponentX += componentParent.x;
            componentParent = componentParent.parent;
        }

        // Texture coordinates global (local to display)
        int yFrom = globalComponentY;
        int yTo = globalComponentY + component.height;
        int xFrom = globalComponentX;
        int xTo = globalComponentX + component.width;

        // Horizontal boundary handling
        if (xTo < 0) return;
        if (xFrom < 0) xFrom = 0;
        if (cols < xFrom) return;
        if (cols < xTo) xTo = cols - 1;
        if (xTo <= xFrom) return;

        //Vertical boundary handling
        if (yTo < 0) return;
        if (yFrom < 0) yFrom = 0;
        if (rows < yFrom) return;
        if (rows < yTo) yTo = rows - 0; // for whatever reason it doesnt need that extra margin
        if (yTo <= yFrom) return;

        // Texture coordinates local to component
        int _from = 0;
        int _to = xTo - xFrom;

        for (int rowIndex = yFrom; rowIndex < yTo; rowIndex++) {
            int _rowIndex = rowIndex - yFrom;

            ArrayList<Pixel> row = pixelMatrix.get(rowIndex);

            row.subList(xFrom, xTo).clear();
            // if TEXT:
            if (component instanceof Text text) {
                row.addAll(xFrom, text.generateRenderableBody(_from, _to));
                continue;
            }
            // else if TEXTURE
            if (component.texture instanceof AdvancedTexture advTex) {
                row.addAll(xFrom, advTex.generateRepeatingSubarray(_from, _to, _rowIndex));
            } else {
                row.addAll(xFrom, component.texture.generateRepeatingSubarray(_from, _to));
            }
        }
    }

//    @Override
//    public void refreshDisplay() {
//        terminal.writer().print(frameBody);
//        terminal.flush();
//    }
}
