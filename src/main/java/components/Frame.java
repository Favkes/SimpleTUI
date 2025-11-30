package components;

public class Frame extends Widget {
    public int[] position;
    public Texture texture;
    public int width;
    public int height;

    public Frame() {
        position = new int[]{0, 0};
        texture = null;
        width = 0;
        height = 0;
    }

    @Override
    public void printInfo() {
        System.out.printf("< Frame widget class object instance: %d %d %d %d %s >\n", y, x, height, width, texture);
    }
}
