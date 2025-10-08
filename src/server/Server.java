package server;

import java.io.*;
import java.net.*;

public class Server {
    public static void start() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("✅ Server started on port 1234. Waiting for clients...");

            Socket socket = serverSocket.accept();
            System.out.println("🎉 Client connected: " + socket.getInetAddress());

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            output.println("Welcome to Anonymous Chat!");

            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("💬 Client: " + message);
                output.println("Server Echo: " + message);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        start();
    }
}
