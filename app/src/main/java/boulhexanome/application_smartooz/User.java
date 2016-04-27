package boulhexanome.application_smartooz;

/**
 * Created by Aiebobo on 26/04/2016.
 */
public class User {

    private static User mInstance = null;

    private String username;
    private String password;

    public static User getInstance(){
        if(mInstance == null)
        {
            mInstance = new User();
        }
        return mInstance;
    }


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public User() {
        username = "";
        password = "";
    }
}
