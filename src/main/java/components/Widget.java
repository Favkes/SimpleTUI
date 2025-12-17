package components;

public abstract class Widget {
    public int x;
    public int y;
    public int width;
    public int height;
    public Texture texture;

    public Widget(int posY,
                  int posX,
                  int height,
                  int width,
                  Texture texture) {
        y = posY;
        x = posX;
        this.height = height;
        this.width = width;
        this.texture = texture;
    }

    public Widget(int posY,
                  int posX,
                  int height,
                  int width,
                  String textureSeed) {
        this(posY, posX, height, width, new Texture(textureSeed));
    }

    public Widget(int posY,
                  int posX,
                  int height,
                  int width) {
        this(posY, posX, height, width, "DefaultTexture_");
    }

    public Widget() {
        this(0, 0, 0, 0);

    }

    public abstract void printInfo();
}
