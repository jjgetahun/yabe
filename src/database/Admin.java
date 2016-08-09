package database;

import java.sql.Timestamp;

/**
 * Created by Jon on 8/8/16.
 */
public class Admin extends Account {

    public Admin(String name, String username, String password, Timestamp dateCreated) {
        super(name, username, password, dateCreated);
    }

    public CustomerRep createCustomerRep(String name, String username, String password, Timestamp dateCreated) {
        CustomerRep customerRep = new CustomerRep(name, username, password, dateCreated);
        return customerRep;
    }

    public String generateSalesReport(String type) {
        switch(type) {
            case "Total Earnings":
                return "Total Earnings sales report";

            case "Earnings per Item":
                return "Earnings per Item sales report";

            case "Earnings per Item Type":
                return "Earnings per Item Type sales report";

            case "Best-Selling Items":
                return "Earnings per Best-Selling Items sales report";

            case "Best Buyers":
                return "Earnings per Best Buyers sales report";

            default:
                return "";
        }
    }

}
