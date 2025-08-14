package ConexionSQL;

public class SessionManager {
    private static SessionManager instance;
    private int sedeIndex;
    private String password;

    private SessionManager() {} // Constructor privado

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Getters y Setters
    public int getSedeIndex() { return sedeIndex; }
    public String getPassword() { return password; }
    public void setSessionData(int sedeIndex, String password) {
        this.sedeIndex = sedeIndex;
        this.password = password;
    }
}
