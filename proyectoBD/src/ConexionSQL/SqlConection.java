package ConexionSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SqlConection {
    public int index;
    public String password;
    
    public Connection getConexion(int index, String password) {
        String connectionUrl = "";
        this.index = index;
        this.password = password; 
        switch (index) {
            case 1 -> {
                connectionUrl = "jdbc:sqlserver://IV4SH:1433;"
                              + "database=Quito_Norte;"
                              + "user=sa;"
                              + "password=" + password + ";"
                              + "timeout=30;"
                              + "encrypt=true;trustServerCertificate=true";
            }
            case 2 -> {
                connectionUrl = "jdbc:sqlserver://MEREKENTEGUE:1433;"
                              + "database=Quito_Sur;"
                              + "user=sag;"
                              + "password=" + password + ";"
                              + "timeout=30;"
                              + "encrypt=true;trustServerCertificate=true";
            }
            default -> {
                throw new IllegalArgumentException("Índice de sede no válido: " + index);
            }
        }

        try {
            return DriverManager.getConnection(connectionUrl);
        } catch (SQLException ex) {
            ex.printStackTrace(); // para ver el error
            return null;
        }
    }
}