package org.example;

import java.util.Queue;
import java.util.concurrent.*;

public class Dispatcher {


    public static void main(String[] args) throws InterruptedException {


        ConcurrentLinkedQueue<Document> buffer = new ConcurrentLinkedQueue<>();
        LinkedBlockingQueue<PrintJob> printingQueue = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<PrintJob> printedDocs = new LinkedBlockingQueue<>();

        Runnable producer = () -> {

            while (!buffer.isEmpty()) {
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

        Runnable consumer = () -> {
            while (true) {
                System.out.println("INSIDE PRINTER");
                PrintJob job = null;
                try {
                    job = printingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                job.setStatus(Status.PRINTING);
                try {
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
