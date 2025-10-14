package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void start(String username, Scanner scanner) {
        try (Socket socket = new Socket("localhost", 1234)) {
            System.out.println("✅ Connected to server!");

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // ✅ Send username IMMEDIATELY after connection
            output.println(username);

            // Now receive the welcome message
            String welcome = input.readLine();
            if (welcome != null) {
                System.out.println(welcome);
            }

            // Chat loop
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
                    System.out.println("👋 Exiting chat...");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
