package boulhexanome.application_smartooz.Model;

import java.net.CookieManager;

/**
 * Created by Aiebobo on 26/04/2016.
 */
public class User {

    private static User mInstance = null;

    private String username;
    private String password;
    private String email;
    private Circuit circuit_en_creation;
    private CookieManager cookieManager;

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

    public Circuit getCircuit_en_creation() {
        return circuit_en_creation;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCircuit_en_creation(Circuit circuit_en_creation) {
        this.circuit_en_creation = circuit_en_creation;
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
        circuit_en_creation = new Circuit();
        cookieManager = new CookieManager();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
