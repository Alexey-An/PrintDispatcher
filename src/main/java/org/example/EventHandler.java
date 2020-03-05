package org.example;

import java.util.Scanner;

public class EventHandler {

    private Dispatcher dispatcher = new Dispatcher();

    public void handle() {

        System.out.println("Starting print dispatcher...");
        dispatcher.startDispatcher();
        System.out.println("Enter command input");
        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();



        boolean runState = true;

        while (runState) {

            String[] command = userInput.split(" ");
            switch (command[0]) {
                case "termintate" : {
                    dispatcher.terminate();
                    runState = false;
                    break;
                }
                case "cancel" : {
                    try {
                        dispatcher.cancelJob((long) Integer.parseInt(command[1]));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please provide correct job ID");
                        break;
                    } catch (ArrayIndexOutOfBoundsException y) {
                        System.out.println("You're supposed to specify exact ID the job to cancel it");
                        break;
                    }

                }
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
                    } catch (ArrayIndexOutOfBoundsException y) {
                        dispatcher.list();
                        break;
                    }

                }
            }
            userInput = in.nextLine();

        }

    }

    public static void main(String[] args) {
        EventHandler handler = new EventHandler();
        handler.handle();

    }
}
