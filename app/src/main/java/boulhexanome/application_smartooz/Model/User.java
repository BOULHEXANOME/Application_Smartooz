package boulhexanome.application_smartooz.Model;

import java.net.CookieManager;

/**
 * Created by Aiebobo on 26/04/2016.
 */
public class User {

    private static User mInstance = null;

    private String username;
    private String email;
    private Circuit circuit_courant;
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

    public Circuit getCircuit_courant() {
        return circuit_courant;
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

    public void setCircuit_courant(Circuit circuit_courant) {
        this.circuit_courant = circuit_courant;
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
        circuit_courant = null;
        cookieManager = new CookieManager();
    }
}
