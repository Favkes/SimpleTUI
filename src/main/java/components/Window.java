package components;

public class Window extends Frame {
    public Window(Widget parent,
                  int posY,
                  int posX,
                  int height,
                  int width,
                  Texture texture) {
        super(parent, posY, posX, height, width, texture);
    }
    public Window(Widget parent,
                  int posY,
                  int posX,
                  int height,
                  int width,
                  String textureSeed) {
        super(parent, posY, posX, height, width, textureSeed);
    }
    public Window(Widget parent,
                  int posY,
                  int posX,
                  int height,
                  int width) {
        super(parent, posY, posX, height, width);
    }
    public Window(Widget parent) { super(parent); }


}
