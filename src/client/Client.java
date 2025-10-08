package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        System.out.print("Enter username: ");
        Scanner sc = new Scanner(System.in);
        String username = sc.nextLine();
        start(username);
    }
    
    public static void start(String username) {
        try (Socket socket = new Socket("localhost", 1234)) {
            System.out.println("✅ Connected to server!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println(input.readLine()); // Welcome message from server

            // Send messages to server
            String msg;
            while (true) {
                System.out.print(username + ": ");

                if (!scanner.hasNextLine()) {
                    System.out.println("⚠️ No input detected. Closing chat...");
                    break;
                }

                msg = scanner.nextLine();
                output.println(msg);

                if (msg.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting chat...");
                    break;
                }

                String serverResponse = input.readLine();
                if (serverResponse == null) {
                    System.out.println("⚠️ Server disconnected.");
                    break;
                }

                System.out.println("Server: " + serverResponse);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
