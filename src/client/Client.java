package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void start(String username) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to chat server...");

            // Read incoming messages
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Send messages
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            System.out.println("You can start chatting now! 👇");

            while (true) {
                String text = scanner.nextLine();
                out.println(username + ": " + text);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
