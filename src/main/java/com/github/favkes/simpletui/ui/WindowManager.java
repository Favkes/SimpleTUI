package com.github.favkes.simpletui.ui;

import com.github.favkes.simpletui.components.Widget;
import java.util.ArrayList;

public class WindowManager {
    public ArrayList<Widget> contents;
    public Widget root;
    public WindowManager() {
        contents = new ArrayList<>();
        root = new Widget() {
            @Override
            public void printInfo() {
                System.out.println("<Root Widget>");
            }
        };
    }
}
