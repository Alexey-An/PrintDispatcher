package org.example;

public enum DocType {

    A(3, "A4"),
    B(5, "A3"),
    C(7, "A2");

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
