package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Set<PrintWriter> clientOutputs = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("✅ Server started on port 1234. Waiting for clients...");

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("👥 New client connected: " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                synchronized (clientOutputs) {
                    clientOutputs.add(out);
                }

                // ✅ Receive username FIRST
                clientName = in.readLine();
                System.out.println("👤 " + clientName + " joined the chat!");
                broadcast("🟢 " + clientName + " joined the chat!");

                // Then send welcome message
                out.println("Welcome to Anonymous Chat!");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    broadcast(clientName + ": " + message);
                }

            } catch (IOException e) {
                System.out.println("⚠️ Client disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientOutputs) {
                    clientOutputs.remove(out);
                }
                broadcast("🔴 " + clientName + " left the chat.");
            }
        }

        private void broadcast(String message) {
            synchronized (clientOutputs) {
                for (PrintWriter writer : clientOutputs) {
                    writer.println(message);
                }
            }
            System.out.println(message);
        }
    }
}
