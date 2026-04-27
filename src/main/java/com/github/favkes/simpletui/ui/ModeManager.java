package com.github.favkes.simpletui.ui;

import com.github.favkes.simpletui.Logger;

import java.util.ArrayList;


public class ModeManager<T> {
    public static final Logger log = Logger.logger();

    public int mode;
    public int numberOfModes;
    public ArrayList<String> modeNames;

    public ArrayList<T> modeItems;

    public ModeManager() {
        mode = 0;
        numberOfModes = 0;
        modeNames = new ArrayList<>();
        modeItems = new ArrayList<>();
    }

    public void add(String modeName, T modeFrame) {
        modeNames.add(modeName);
        modeItems.add(modeFrame);
        numberOfModes += 1;
    }

    public void modeSwitchUp() {
        mode = (mode + 1) % numberOfModes;
        logModeSwitch();
    }

    public void modeSwitchDown() {
        mode = (mode - 1) % numberOfModes;
        logModeSwitch();
    }

    public boolean modeSet(int newMode) {
        if (newMode < 0 || numberOfModes <= newMode) return false;
        mode = newMode;
        logModeSwitch();
        return true;
    }
    public boolean modeSet(String modeName) {
        int newMode = modeNames.indexOf(modeName);
        if (newMode == -1) return false;
        mode = newMode;
        logModeSwitch();
        return true;
    }

    public T currentModeItem() {
        return modeItems.get(mode);
    }
    public String currentModeName() { return modeNames.get(mode); }

    public void logModeSwitch() {
        log.info(String.format("Set current mode to %d: \"%s\"", mode, modeNames.get(mode)));
    }
}
