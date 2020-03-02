package org.example;


public class PrintJob {

    private Status status;
    private Document document;
    private static long id = 0;
    private long nextId;
    private long start;
    private long finish;


    public PrintJob(Document document, long start) {
        this.document = document;
        this.start = start;
        this.status = Status.AWAIT;
        this.nextId = ++id;
    }

    public Status getStatus() {
        return status;
    }

    public long getId() {
        return nextId;
    }

    public Document getDocument() {
        return document;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }
}
