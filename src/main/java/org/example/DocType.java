package org.example;

public enum DocType {

    a(10, "A4"),
    b(10, "A3"),
    c(10, "A2");

    private final int printTime;
    private final String paperFormat;

    public int getPrintTime() {
        return printTime;
    }

    public String getPaperFormat() {
        return paperFormat;
    }

    DocType(int printTime, String paperFormat) {
        this.printTime = printTime;
        this.paperFormat = paperFormat;
    }
}
