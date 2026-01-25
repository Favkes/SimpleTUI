package com.github.favkes.simpletui.components;

import com.github.favkes.simpletui.ui.Displayer;

import java.util.ArrayList;

public class Page {
    public ArrayList<Widget> components;
    public Widget root;
    public boolean shouldRender;


    public Page() {
        components = new ArrayList<>();
        root = new Widget() {
            @Override
            public void printInfo() {
                System.out.println("<Root Widget>");
            }
        };
        shouldRender = true;
    }

    public void hide() {
        for (Widget widget : components) {
            widget.hide();
        }
    }

    public void show() {
        for (Widget widget : components) {
            widget.show();
        }
    }

    public void flipVisibility() {
        shouldRender =! shouldRender;
        if (shouldRender) show();
        else hide();
    }
}
