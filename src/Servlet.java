import database.DB;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Servlet extends HttpServlet{

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String initial = config.getInitParameter("initial");
        System.out.println("Initialized db connection");
        DB.init();
    }

//    public void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws IOException{
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//    }
}