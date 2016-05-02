package boulhexanome.application_smartooz.Utils;

/**
 * Created by Papin on 29/04/2016.
 */
public class Config {

    public static final String PROTOCOL = "http";
    public static final String IP_SERV = "142.4.215.20";
    public static final String PORT = "1723";

    /************USER************/
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REGISTER = "/register";
    public static final String DELETE_USER = "/delete-user";

    /************PLACE************/
    public static final String ADD_PLACE = "/add-place";
    public static final String GET_PLACES = "/get-places";
    public static final String GET_PLACES_KEYWORD = "/get-places-keyword";
    public static final String GET_PLACES_ID = "/get-place-id";
    public static final String GET_PLACE_COORD = "/get-place-coord";
    public static final String GET_PLACE_COORD_RADIUS = "/get-place-radius-coord";
    public static final String UPDATE_PLACE = "/update-place";
    public static final String VOTE_PLACE = "/vote-place";
    public static final String DELETE_PLACE = "/delete-place";

    /************CIRCUIT************/
    public static final String ADD_CIRCUIT = "/add-circuit";
    public static final String GET_CIRCUIT_ID = "/get-circuit-id";
    public static final String UPTDATE_CIRCUIT = "/update_circuit";
    public static final String VOTE_CIRCUIT = "/vote-circuit";
    public static final String GET_CIRCUITS_KEYWORD = "/get-circuits-keyword";
    public static final String GET_KEYWORDS_OF_CIRCUIT = "/get-all-circuits-keywords";
    public static final String GET_CIRCUITS = "/get-circuits";
    public static final String DELETE_CIRCUIT = "/delete-circuit";
    public static final String CIRCUIT_DONE = "/circuit-done";
    public static final String GET_CIRCUITS_CREATED_BY_USER = "/get-circuits-created-by-user";

    /************PICTURE************/
    public static final String UPLOAD_PICTURE = "/upload";
    public static final String DOWNLOAD_PICTURE = "/download-picture";


    public static String getRequest(String service) {
        String request = PROTOCOL + "://" + IP_SERV + ":" + PORT + service;
        System.out.println(request);
        return request;
    }
}
