package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    // 🔹 Store all global chat clients
    private static Set<PrintWriter> globalClients = new HashSet<>();

    // 🔹 Store all rooms (room name -> Room object)
    private static Map<String, Room> rooms = new HashMap<>();

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

    // 🔸 Helper methods for room creation and joining
    public static synchronized boolean createRoom(String name, String password) {
        if (rooms.containsKey(name)) return false;
        rooms.put(name, new Room(name, password));
        return true;
    }

    public static synchronized Room joinRoom(String name, String password) {
        Room room = rooms.get(name);
        if (room == null || !room.checkPassword(password)) return null;
        return room;
    }

    // ===========================================================
    // 🔹 Inner class: ClientHandler
    // ===========================================================
    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;
        private Room currentRoom = null;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                synchronized (globalClients) {
                    globalClients.add(out);
                }

                // 🔸 Read username first
                clientName = in.readLine();
                System.out.println("👤 " + clientName + " joined!");
                broadcast("🟢 " + clientName + " joined the chat!");

                out.println("Welcome " + clientName + "! Type /help for commands.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) break;
                    if (message.trim().isEmpty()) continue;

                    // ⚙️ Command handling
                    if (message.startsWith("/")) {
                        handleCommand(message);
                        continue;
                    }

                    // 📡 Broadcast either to room or global
                    if (currentRoom != null) {
                        currentRoom.broadcast(clientName + ": " + message);
                    } else {
                        broadcast(clientName + ": " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("⚠️ Client disconnected.");
            } finally {
                cleanup();
            }
        }

        private void handleCommand(String message) {
            if (message.equalsIgnoreCase("/help")) {
                out.println("""
                        🧠 Available commands:
                        /create <room> <password> - Create a new room
                        /join <room> <password>   - Join an existing room
                        /leave                    - Leave current room
                        /rooms                    - Show available rooms
                        /exit                     - Quit chat
                        """);
                return;
            }

            if (message.startsWith("/create ")) {
                String[] parts = message.split(" ", 3);
                if (parts.length < 3) {
                    out.println("⚠️ Usage: /create <room> <password>");
                    return;
                }
                String name = parts[1];
                String pass = parts[2];

                if (Server.createRoom(name, pass)) {
                    out.println("✅ Room '" + name + "' created successfully!");
                } else {
                    out.println("❌ Room name already exists!");
                }
                return;
            }

            if (message.startsWith("/join ")) {
                String[] parts = message.split(" ", 3);
                if (parts.length < 3) {
                    out.println("⚠️ Usage: /join <room> <password>");
                    return;
                }
                String name = parts[1];
                String pass = parts[2];

                Room room = Server.joinRoom(name, pass);
                if (room == null) {
                    out.println("❌ Invalid room or password.");
                } else {
                    if (currentRoom != null) {
                        currentRoom.removeClient(out);
                    }
                    currentRoom = room;
                    room.addClient(out);
                    out.println("🎉 Joined room: " + name);
                    room.broadcast("🟢 " + clientName + " joined the room!");
                }
                return;
            }

            if (message.equalsIgnoreCase("/leave")) {
                if (currentRoom != null) {
                    currentRoom.broadcast("🔴 " + clientName + " left the room.");
                    currentRoom.removeClient(out);
                    currentRoom = null;
                    out.println("👋 You left the room.");
                } else {
                    out.println("⚠️ You are not in any room.");
                }
                return;
            }

            if (message.equalsIgnoreCase("/rooms")) {
                if (rooms.isEmpty()) {
                    out.println("🚫 No rooms available.");
                } else {
                    out.println("🏠 Available Rooms:");
                    for (String roomName : rooms.keySet()) {
                        out.println(" - " + roomName);
                    }
                }
                return;
            }

            if (message.equalsIgnoreCase("/exit")) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            out.println("⚠️ Unknown command. Type /help for options.");
        }

        private void broadcast(String message) {
            synchronized (globalClients) {
                for (PrintWriter writer : globalClients) {
                    writer.println(message);
                }
            }
            System.out.println(message);
        }

        private void cleanup() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (globalClients) {
                globalClients.remove(out);
            }
            if (currentRoom != null) {
                currentRoom.removeClient(out);
                currentRoom.broadcast("🔴 " + clientName + " left the room.");
            }
            broadcast("🔴 " + clientName + " left the chat.");
        }
    }

    // ===========================================================
    // 🔹 Inner class: Room
    // ===========================================================
    static class Room {
        private String name;
        private String password;
        private Set<PrintWriter> clients = new HashSet<>();

        Room(String name, String password) {
            this.name = name;
            this.password = password;
        }

        boolean checkPassword(String inputPassword) {
            return password.equals(inputPassword);
        }

        void addClient(PrintWriter out) {
            clients.add(out);
        }

        void removeClient(PrintWriter out) {
            clients.remove(out);
        }

        void broadcast(String message) {
            for (PrintWriter client : clients) {
                client.println(message);
            }
            System.out.println("[Room: " + name + "] " + message);
        }
    }
}
