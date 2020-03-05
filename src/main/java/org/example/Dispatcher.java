package org.example;

import java.util.concurrent.*;

public class Dispatcher {

    ExecutorService executorService = Executors.newCachedThreadPool();
    Future producerFuture;
    Future consumerFuture;

    ConcurrentLinkedQueue<Document> buffer = new ConcurrentLinkedQueue<>();
    LinkedBlockingQueue<PrintJob> printingQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<PrintJob> printedDocs = new LinkedBlockingQueue<>();



    Runnable producer = () -> {

        while (true) {
            if (!buffer.isEmpty()) {
                System.out.println("Adding document to printing queue");
                PrintJob job = new PrintJob(buffer.poll(), System.currentTimeMillis());
                job.setStatus(Status.AWAIT);
                try {
                    Thread.sleep(job.getDocument().getType().getPrintTime() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                job.setStatus(Status.ENQUEUED);
                printingQueue.add(job);
                System.out.println("Document " + job.getDocument().getName() +
                        " added to print queue. Job ID: " + job.getId());
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    Runnable consumer = () -> {

        PrintJob job = null;
        while (true) {

            try {
                job = printingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            job.setStatus(Status.PRINTING);
            try {
                System.out.println("Initailizing printing process");
                Thread.sleep(job.getDocument().getType().getPrintTime() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            job.setStatus(Status.COMPLETE);
            job.setFinish(System.currentTimeMillis());
            System.out.println("Document " + job.getDocument().getName() +
                    " has been printed. Job ID: " + job.getId() + " Print time: " +
                    (job.getFinish() - job.getStart())*0.001 + " seconds");
            printedDocs.add(job);
        }
    };


    public void list() {

        printedDocs.forEach(job -> System.out.println("Job ID: " + job.getId() + " Document: " +
                job.getDocument().getName() + " Type: " + job.getDocument().getType() +
                " Print time: " + (job.getFinish() - job.getStart())*0.001));
    };

    public void list(String sortType) {

        PrintJob[] copy = printedDocs.stream().toArray(PrintJob[]::new);
        System.out.println("We're so sorry! Sorting has not been implemented yet.");

        printedDocs.forEach(job -> System.out.println("Job ID: " + job.getId() + " Document: " +
                job.getDocument().getName() + " Type: " + job.getDocument().getType() +
                " Print time: " + (job.getFinish() - job.getStart())*0.001));


    };

    public void terminate() {
        this.list();
        executorService.shutdown();
    }

    public void cancelJob(long jobId) {

        for (PrintJob job : printingQueue) {
            if (job.getId() == jobId) {
                printingQueue.remove(job);
            }
        }

    }

    public void putDocumentToBuffer(String docName, String type) {
        buffer.add(Document.getInstance(docName, DocType.valueOf(type)));
    }

    public void restart() {

    }

    public void startDispatcher() {
        this.producerFuture = executorService.submit(producer);
        this.consumerFuture = executorService.submit(consumer);
    }


    public static void main(String[] args) throws InterruptedException {


        ConcurrentLinkedQueue<Document> buffer = new ConcurrentLinkedQueue<>();
        LinkedBlockingQueue<PrintJob> printingQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<PrintJob> printedDocs = new LinkedBlockingQueue<>();

        Runnable producer = () -> {

            while (!buffer.isEmpty()) {
                System.out.println("Adding document to printing queue");
                PrintJob job = new PrintJob(buffer.poll(), System.currentTimeMillis());
                job.setStatus(Status.AWAIT);
                try {
                    Thread.sleep(job.getDocument().getType().getPrintTime() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                job.setStatus(Status.ENQUEUED);
                printingQueue.add(job);
                System.out.println("Document " + job.getDocument().getName() +
                        " added to print queue. Job ID: " + job.getId());
            }
        };
//
        Runnable consumer = () -> {
            while (true) {
                //System.out.println("");
                PrintJob job = null;
                try {
                    job = printingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                job.setStatus(Status.PRINTING);
                try {
                    System.out.println("Initailizing printing process");
                    Thread.sleep(job.getDocument().getType().getPrintTime() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                job.setStatus(Status.COMPLETE);
                job.setFinish(System.currentTimeMillis());
                System.out.println("Document " + job.getDocument().getName() +
                        " has been printed. Job ID: " + job.getId() + " Print time: " +
                        (job.getFinish() - job.getStart())*0.001 + " seconds");
                printedDocs.add(job);
            }
        };
//
        Document doc1 = Document.getInstance("doc1", DocType.A);
        Document doc2 = Document.getInstance("doc2", DocType.B);
        Document doc3 = Document.getInstance("doc3", DocType.C);
        Document doc4 = Document.getInstance("doc4", DocType.B);
        Document doc5 = Document.getInstance("doc5", DocType.A);


        buffer.add(doc1);
        buffer.add(doc2);
        buffer.add(doc3);
        buffer.add(doc4);
        buffer.add(doc5);

        Thread enqueueingThread = new Thread(producer);
        Thread printingThread = new Thread(consumer);
        ExecutorService executorService = Executors.newFixedThreadPool(4);


        enqueueingThread.start();
        Thread.sleep(2000);
        executorService.submit(printingThread);




    }

}
