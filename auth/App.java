package auth;

import client.Client;
import java.util.*;

public class App {
    static Scanner sc = new Scanner(System.in); // <-- single scanner for whole app

    public static void main(String[] args) {
        System.out.println("=============================");
        System.out.println("Welcome to Anonymous Chat App");
        System.out.println("=============================");
        System.out.print("1. Register\n2. Login\n3. Exit\nChoose an Option: ");

        HashMap<String, Register> usersInfo = new HashMap<>();
        int option = sc.nextInt();
        sc.nextLine(); // consume newline
        
        switch (option) {
            case 1:
                System.out.print("Enter Email: ");
                String email = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                Random rn = new Random();
                int randomNum = 100 + rn.nextInt(900);
                String username = "Anonymous#" + randomNum;

                System.out.println("✅ Registration successful!");
                System.out.println("Your username: " + username);

                // set username in server
                System.out.println("Initial Commit!");

                usersInfo.put(email, new Register(username, email, password));
                System.out.println("Welcome " + username);

                System.out.println("Detailed of HashMap: " + usersInfo.get(email));


                // pass global scanner
                Client.start(username, sc);
                break;
        }
    }
}