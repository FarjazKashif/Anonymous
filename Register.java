import java.util.Random;

public class Register {
    private String username;
    private String email;
    private String password;

    int Random;

    String getEmail() {
        return email;
    }
    public String getUserName() {
        return username;
    }
    Register(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
} 