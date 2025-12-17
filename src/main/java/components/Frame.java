package components;

public class Frame extends Widget {

    public Frame(int posY,
                 int posX,
                 int height,
                 int width,
                 Texture texture) {
        super(posY, posX, height, width, texture);
    }
    public Frame(int posY,
                 int posX,
                 int height,
                 int width,
                 String textureSeed) {
        super(posY, posX, height, width, textureSeed);
    }
    public Frame(int posY,
                 int posX,
                 int height,
                 int width) {
        super(posY, posX, height, width);
    }
    public Frame() { super(); }

    @Override
    public void printInfo() {
        System.out.printf("< Frame widget class object instance: %d %d %d %d %s >\n", y, x, height, width, texture);
    }
}
