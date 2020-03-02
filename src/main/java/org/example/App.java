package org.example;

import java.sql.Time;
import java.time.Instant;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;


public class App 
{
    public static void main( String[] args )
    {

//        Scanner in = new Scanner(System.in);
//        String userInput = in.nextLine();
//        long k = System.currentTimeMillis();
//        System.out.println(k);
//        while (!userInput.equals("CANCEL")) {
//            System.out.println(userInput + "@@@");
//            userInput = in.nextLine();
//        }
//
//        System.out.println("Tasks canceled");
//
//        long l = System.currentTimeMillis();
//        System.out.println(l);
//        System.out.println((l - k) * 0.001 + " seconds" );


        Queue<String> buffer = new ConcurrentLinkedQueue<>();
        buffer.add("String1");
        buffer.add("String2");
        if (!buffer.isEmpty()) {
            System.out.println(buffer.poll());
            System.out.println(buffer.poll());
            System.out.println(buffer.poll());
        }

    }
}
