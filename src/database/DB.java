package database;

import java.sql.*;
/**
 * Created by elby on 7/20/16.
 */
public class DB {

    public static Connection conn;
    private static boolean initialized = false;

    public static void init(){
        //Load database.DB
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection ("jdbc:mysql://classvm51.cs.rutgers.edu/proj2016","root","DigDagDug55");
            initialized = true;
        } catch (Exception e) { //Generic exception, don't do this.
            e.printStackTrace();
        }
    }

    public static int getUserID(String username, String password){

        if(!initialized) init();

        int id = -1;

        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + "' AND PassWord = '" + password + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                id = rs.getInt("AccountID");
            }
        }catch (SQLException e){

        }

        return id;
    }

    public static String getName(String username){

        if(!initialized) init();

        String name = "";

        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                name = rs.getString("Name");
            }
        }catch (SQLException e){

        }

        return name;
    }

    public static int checkUserExists(String username){

        if(!initialized) init();

        int id = -1;

        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                id = rs.getInt("AccountID");
            }
        }catch (SQLException e){

        }

        return id;
    }

    public static void insertUser(String username, String name, String password){

        if(!initialized) init();

        try {
            String sql = "INSERT INTO Account(UserName, Name, PassWord) VALUES('" + username + "', '" + name +
                    "', '" + password + "')";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
        }catch (SQLException e){

        }
    }
}
