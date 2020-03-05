package org.example;

import javax.print.Doc;
import java.util.Scanner;

public class EventHandler {

    private Dispatcher dispatcher = new Dispatcher();


    public void handle() {

        dispatcher.printedDocs.offer(new PrintJob(Document.getInstance("doc2", DocType.B), 2000));
        dispatcher.printedDocs.offer(new PrintJob(Document.getInstance("doc1", DocType.A), 1000));


        System.out.println("Enter command input");
        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();


        boolean runState = true;

        while (runState) {

            String[] command = userInput.split(" ");
            System.out.println(command[0]);
            switch (command[0]) {
                case "termintate" : dispatcher.terminate();
                case "cancel" : dispatcher.cancelJob();
                case "restart" : dispatcher.restart();
                case "print" : {
                    try {
                        dispatcher.putDocumentToBuffer(command[1], command[2]);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please provide correct document name and proper document type: A, B, C");
                        break;
                    } catch (ArrayIndexOutOfBoundsException y) {
                        System.out.println("You're supposed to provide both document name and its type. Which can be" +
                                " A, B or C. \n" +
                                "Please provide correct document type in format: \n" +
                                "[print] [DOCUMENT NAME] [DOCUMENT TYPE]");
                        break;
                    }


                }
                case "list" : {
                    try {
                        dispatcher.list(command[1]);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please provide correct sort type parameter which can be: \n" +
                                "name -- for sorting by document name; \n" +
                                "time -- for sorting by print time; \n" +
                                "size -- for sorting by paper format");
                        break;
                    }
                    catch (ArrayIndexOutOfBoundsException y) {
                        dispatcher.list();
                        break;
                    }

                }
            }
            //dispatcher.buffer.forEach(elem -> System.out.println(elem.getName() + elem.getType()));
            userInput = in.nextLine();
            //runState = false;

        }

    }

    public static void main(String[] args) {
        EventHandler handler = new EventHandler();
        handler.handle();

    }
}
