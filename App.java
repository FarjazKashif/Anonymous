import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;


public class App {
    static public void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("=============================\nWelcome to Anonymous Chat App\n=============================\n");
        System.out.print("1. Register\n2. Login\n3. Exit\nChoose an Option: ");
        
        HashMap<String, Register> usersInfo = new HashMap<>();
        int option = sc.nextInt();
        sc.nextLine();
        switch (option) {
            case 1: {
                System.out.print("Enter Email: ");
                String email = sc.nextLine();                
                System.out.print("Enter Password: ");
                String password = sc.nextLine();
                OTPservice email_OTP = new OTPservice(email);
                int otp_code  = email_OTP.generateOTP();
                email_OTP.sendOtpEmail(otp_code);
                
                int emailCode;
                do {
                    System.out.print("Enter OTP: ");
                    emailCode = sc.nextInt();
                } while(emailCode != otp_code);
                
                // Username
                Random rn = new Random();
                String username = "Anonymous#" +  1000 + rn.nextInt(9000);
                System.out.print("Username is " + username);
                Register user = new Register(username, email, password);
                usersInfo.put(user.getEmail(), user);

                System.out.print("Welcome " + usersInfo.get(user.getEmail()).getUserName());
                break;
            }
            case 2: // Login
                System.out.print("Enter Email: ");
                String email = sc.nextLine(); 
                System.out.println("Enter Password: ");
                String password = sc.nextLine();
                
                while(!usersInfo.containsKey(email)) {
                    System.out.println("Wrong Email....");
                    
                    System.out.print("Enter Email: ");
                    email = sc.nextLine(); 
                }
            default:
                break;
        }

        System.out.print("\nWelcome " + "to Anonymous Chatting App");
    }
}

