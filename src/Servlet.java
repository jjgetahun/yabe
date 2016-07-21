import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException{

        System.out.println(request.getParameter("username"));
        System.out.println(request.getParameter("password"));
    }
}