package com.xiaou.plugin.json2dart.dart;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DartFileType extends LanguageFileType {
    public static final LanguageFileType INSTANCE = new DartFileType();

    private DartFileType() {
        super(DartLanguage.INSTANCE);
    }

    @NotNull
    public String getName() {
        return "Dart";
    }

    @NotNull
    public String getDescription() {
        return "Dart";
    }

    @NotNull
    public String getDefaultExtension() {
        return "dart";
    }

    public Icon getIcon() {
        return DartIcons.Dart_file;
    }

    public String getCharset(@NotNull VirtualFile virtualFile, @NotNull byte[] bytes) {
        return null;
    }
}
