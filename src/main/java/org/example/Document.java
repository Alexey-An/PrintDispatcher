package org.example;

public class Document {

    private String name;
    private DocType type;

    private Document(String name, DocType type) {
        this.name = name;
        this.type = type;
    }

    public static Document getInstance(String name, DocType type) {
        return new Document(name, type);
    }

    public String getName() {
        return name;
    }

    public DocType getType() {
        return type;
    }


}
