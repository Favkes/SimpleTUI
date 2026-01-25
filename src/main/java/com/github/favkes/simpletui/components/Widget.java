package com.github.favkes.simpletui.components;

import java.util.ArrayList;

public abstract class Widget {
    public int x;
    public int y;
    public int width;
    public int height;
    public Texture texture;
    public Widget parent;
    public ArrayList<Widget> children;
    public boolean isFocused;
    public boolean shouldRender;

    public Widget(Widget parent,
                  int posY,
                  int posX,
                  int height,
                  int width,
                  Texture texture) {
        y = posY;
        x = posX;
        this.height = height;
        this.width = width;
        this.texture = texture;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.isFocused = false;
        this.shouldRender = true;
    }

    public Widget(Widget parent,
                  int posY,
                  int posX,
                  int height,
                  int width,
                  String textureSeed) {
        this(parent, posY, posX, height, width, new Texture(textureSeed));
    }

    public Widget(Widget parent,
                  int posY,
                  int posX,
                  int height,
                  int width) {
        this(parent, posY, posX, height, width, "DefaultTexture_");
    }

    public Widget(Widget parent) {
        this(parent, 0, 0, 0, 0);
    }

    public Widget() {
        this(null);
    }

    public boolean isRoot() { return parent == null; }

    public void show() { this.shouldRender = true; }
    public void hide() { this.shouldRender = false; }

    public abstract void printInfo();
}
