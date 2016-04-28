package boulhexanome.application_smartooz;

/**
 * Created by Aiebobo on 26/04/2016.
 */
public class User {

    private static User mInstance = null;

    private String username;
    private String email;

    public static User getInstance(){
        if(mInstance == null)
        {
            mInstance = new User();
        }
        return mInstance;
    }


    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User() {
        username = "";
        email = "";
    }
}
