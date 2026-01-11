package components;

public class Frame extends Widget {
    public Frame(Widget parent,
                 int posY,
                 int posX,
                 int height,
                 int width,
                 Texture texture) {
        super(parent, posY, posX, height, width, texture);
    }
    public Frame(Widget parent,
                 int posY,
                 int posX,
                 int height,
                 int width,
                 String textureSeed) {
        super(parent, posY, posX, height, width, textureSeed);
    }
    public Frame(Widget parent,
                 int posY,
                 int posX,
                 int height,
                 int width) {
        super(parent, posY, posX, height, width);
    }
    public Frame(Widget parent) { super(parent); }

    @Override
    public void printInfo() {
        System.out.printf("< Frame widget class object instance: %d %d %d %d %s >\n", y, x, height, width, texture);
    }
}
