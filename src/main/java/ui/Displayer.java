package ui;


import components.*;

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

    public void renderComponentOfIndex(int componentIndex) {
        Widget component = windowManager.contents.get(componentIndex);

        int globalComponentY = component.y;
        int globalComponentX = component.x;
        Widget componentParent = component.parent;
        while (componentParent != null) {
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
