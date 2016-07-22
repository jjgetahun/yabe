import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by elby on 7/20/16.
 */
public class DB {

    public static Connection conn;

    public static void init(){
        //Load DB
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection ("jdbc:mysql://classvm51.cs.rutgers.edu","root","DigDagDug55");
        } catch (Exception e) { //Generic exception, don't do this.
            e.printStackTrace();
        }
    }
}
