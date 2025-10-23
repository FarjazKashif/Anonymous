package auth;

import client.Client;
import java.util.*;
import server.Server;

public class App {
    static Scanner sc = new Scanner(System.in); // <-- single scanner for whole app

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        System.out.println("=============================");
        System.out.println("Welcome to Anonymous Chat App");
        System.out.println("=============================");
        System.out.println();
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println();
        System.out.print("Choose an Option: ");

        HashMap<String, Register> usersInfo = new HashMap<>();
        int option = sc.nextInt();
        int optionRoom;
        sc.nextLine(); // consume newline

        String username = "";

        switch (option) {
            case 1:
                System.out.println();
                System.out.println("----------- Registration -----------");
                System.out.print("Enter Email: ");
                String email = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                Random rn = new Random();
                int randomNum = 100 + rn.nextInt(900);
                username = "Anonymous#" + randomNum;

                System.out.println();
                System.out.println("Registration successful!");
                System.out.println("Your username: " + username);
                System.out.println();

                // set username in server
                // System.out.println("Initial Commit!");

                usersInfo.put(email, new Register(username, email, password));
                System.out.println("Welcome " + username);
                System.out.println();

                // System.out.println("Detailed of HashMap: " + usersInfo.get(email));
                break;
        }

        System.out.println("------------------------------------");
        System.out.println("Select Chat Mode");
        System.out.println("------------------------------------");
        System.out.println("1. General Chat");
        System.out.println("2. Room");
        System.out.println();
        System.out.print("Choose an Option: ");

        option = sc.nextInt();
        System.out.println();

        switch (option) {
            case 1:
                System.out.println("Welcome to General Chat!");
                System.out.println();
                // pass global scanner
                Client.start(username, sc);
                break;
            case 2:
                System.out.println("------------------------------------");
                System.out.println("Room Options:");
                System.out.println("1. Create a Room");
                System.out.println("2. Join a Room");
                System.out.println("------------------------------------");
                System.out.println();
                optionRoom = sc.nextInt();
                switch (optionRoom) {
                    case 1:
                        System.out.print("Enter Room Name: ");
                        String roomName = sc.nextLine();
                        System.out.println();

                        System.out.print("Create a Password: ");
                        String password = sc.nextLine();
                        System.out.println();

                        break;
                    case 2:
                        System.out.print("Enter Room Name: ");
                        roomName = sc.nextLine();
                        Server.joinRoom(username, roomName);
                        break;
                
                    default:
                        break;
                }
            default:
                break;
        }
    }
}
