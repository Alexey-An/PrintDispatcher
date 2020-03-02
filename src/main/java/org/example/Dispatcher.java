package org.example;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Dispatcher {


    public static void main(String[] args) {


        Queue<Document> buffer = new ConcurrentLinkedQueue<>();
        Queue<PrintJob> printingQueue = new ConcurrentLinkedQueue<>();
        HashMap<PrintJob, Document> printedDocs = new HashMap<>();

        Runnable enqueuer = () -> {

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

        Callable<PrintJob> printer = () -> {

            return null;
        };


        Document doc1 = Document.getInstance("doc1", DocType.A);
        Document doc2 = Document.getInstance("doc2", DocType.B);
        Document doc3 = Document.getInstance("doc3", DocType.C);

        PrintJob job2 = new PrintJob(doc2, System.currentTimeMillis());
        PrintJob job3 = new PrintJob(doc3, System.currentTimeMillis());

        buffer.add(doc1);
        buffer.add(doc2);
        buffer.add(doc3);

        System.out.println(DocType.valueOf("C"));

    }

    class Producer2 implements Callable<PrintJob> {

        @Override
        public PrintJob call() throws Exception {
            return null;
        }
    }

    class Consumer implements Callable<PrintJob> {

        @Override
        public PrintJob call() throws Exception {
            return null;
        }
    }

}
