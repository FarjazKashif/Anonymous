import java.util.*;
// import javax.mail.*;
// import javax.mail.internet.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class OTPservice {
    // Sender's email & App Password
    private final String fromEmail = ""; // your Gmail App Password
    private final String password = "";  // your Gmail App Password

    private String toEmail;

    // Constructor: set receiver email
    public OTPservice(String email) {
        this.toEmail = email;
    }

    // Method to generate OTP
    public int generateOTP() {
        Random rn = new Random();
        return 100000 + rn.nextInt(900000); // always 6 digits
    }

    // Method to send OTP email
    public void sendOtpEmail(int otp) {
        // SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587"); 
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true"); 

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
            );
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp);

            // Send email
            Transport.send(message);

            System.out.println("OTP sent successfully to " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
