package com.ioapps.fs_selection;

public enum FsActivity {

    MAIN("MainActivity"),
    CHOOSER("ChooserActivity"),
    TEXT_EDITOR("TextEditorActivity"),
    ZIP_VIEWER("ArchiveViewerActivity");

    public final String name;

    FsActivity(String name) {
        this.name = name;
    }
}
