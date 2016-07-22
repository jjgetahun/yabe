import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet{

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String initial = config.getInitParameter("initial");
        DB.init();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{
        //Does login, will probably move this out to JSP and handle with DB
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);



        try {
            Statement statement = DB.conn.createStatement();
            ResultSet login = statement.executeQuery("SELECT * FROM Account where UserName = \"" + username + "\" AND PassWord = \"" + password);
                System.out.println(login.getRow());
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}