package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {
    public Connection dbLink;

    public Connection getConnection(){
        String dbName ="javaapp";
        String dbUser ="root";
        String dbPassword ="";
        String url = "jdbc:mysql://localhost/" + dbName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink=DriverManager.getConnection(url,dbUser,dbPassword);

        } catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return dbLink;
    }

}
