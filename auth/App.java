package auth;

import java.util.*;

import client.Client; // Import your chat client

public class App {
    public static HashMap<String, Register> usersInfo = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=============================");
        System.out.println("Welcome to Anonymous Chat App");
        System.out.println("=============================");
        System.out.print("1. Register\n2. Login\n3. Exit\nChoose an Option: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1 -> register(sc);
            case 2 -> login(sc);
            case 3 -> System.exit(0);
            default -> System.out.println("Invalid option!");
        }

        sc.close();
    }

    static void register(Scanner sc) {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Random rn = new Random();
        int randomNum = 100 + rn.nextInt(900);
        String username = "Anonymous#" + randomNum;

        Register user = new Register(username, email, password);
        usersInfo.put(user.getEmail(), user);

        System.out.println("\n✅ Registration successful!");
        System.out.println("Your username: " + username);
        System.out.println("Welcome " + username);

        // Launch chat client
        startChat(username);
    }

    static void login(Scanner sc) {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Register user = usersInfo.get(email);

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("\n✅ Login successful! Welcome " + user.getUserName());
            startChat(user.getUserName());
        } else {
            System.out.println("❌ Invalid credentials!");
        }
    }

    static void startChat(String username) {
        // Start the client in a new thread so it doesn’t block
        new Thread(() -> {
            try {
                Client.start(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
