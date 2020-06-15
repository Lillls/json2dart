package com.xiaou.plugin.json2dart.dart;

import com.intellij.ui.IconManager;

import javax.swing.*;

public class DartIcons {
    public static final Icon Dart_file = load("/icons/dart_file.svg");

    private static Icon load(String path) {
        return IconManager.getInstance().getIcon(path, DartIcons.class);
    }
}
