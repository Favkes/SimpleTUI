package com.github.favkes.simpletui.components;

import com.github.favkes.simpletui.ui.Displayer;
import com.github.favkes.simpletui.ui.KeyBind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page {
    public ArrayList<Widget> components;
    private final Map<String, Widget> componentsMap;
    public Widget root;
    public boolean shouldRender;

    public List<KeyBind> keyBinds;


    public Page() {
        components = new ArrayList<>();
        componentsMap = new HashMap<>();

        root = new Widget() {
            @Override
            public void printInfo() {
                System.out.println("<Root Widget>");
            }
        };

        shouldRender = true;
        keyBinds = new ArrayList<>();
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

    public void add(String widgetName, Widget widget) {
        components.add(widget);
        componentsMap.put(widgetName, widget);
    }

    public Widget componentOfName(String widgetName) {
        return componentsMap.get(widgetName);
    }
}
