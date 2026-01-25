package com.github.favkes.simpletui.ui;

import com.github.favkes.simpletui.components.Page;

import java.util.ArrayList;

public class PageManager {
    public ArrayList<Page> pages;


    public PageManager() {
        pages = new ArrayList<>();
    }

    public Page newPage() {
        Page newpage = new Page();
        pages.add(newpage);
        return newpage;
    }
}
