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
                    Thread.sleep(job.getDocument().getType().getPrintTime() * 500);
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

            if (!printingQueue.isEmpty()) {
                job = printingQueue.poll();
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
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
        buffer.clear();
        printingQueue.clear();
        executorService.shutdown();
        System.out.println("Shutting down dispatcher...");
        System.out.println("Print dispatcher shut down: " + executorService.isShutdown());
    }

    public void cancelJob(long jobId) throws IllegalArgumentException {

        for (PrintJob job : this.printingQueue) {
            if (job.getId() == jobId) {
                this.printingQueue.remove(job);
                System.out.println("Job " + job.getId() + " has been canceled. \n");
                break;
            }
        }
    }

    public void putDocumentToBuffer(String docName, String type) {
        buffer.add(Document.getInstance(docName, DocType.valueOf(type)));
    }

    public void restart() {
        if (executorService.isShutdown()) {
            executorService = Executors.newCachedThreadPool();
            this.producerFuture = executorService.submit(producer);
            this.consumerFuture = executorService.submit(consumer);
        }
    }

    public void startDispatcher() {
        System.out.println("Starting print dispatcher...");
        this.producerFuture = executorService.submit(producer);
        this.consumerFuture = executorService.submit(consumer);
        if (!executorService.isShutdown()) {
            System.out.println("Print dispatcher is up and running.");
        }
    }

}
