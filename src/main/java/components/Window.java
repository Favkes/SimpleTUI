package components;

public class Window extends Frame {
    public Window(int posY, int posX, int height, int width, Texture texture) {
        super(posY, posX, height, width, texture);
    }
    public Window(int posY, int posX, int height, int width, String textureSeed) {
        super(posY, posX, height, width, textureSeed);
    }
    public Window(int posY, int posX, int height, int width) {
        super(posY, posX, height, width);
    }
    public Window() { super(); }


}
