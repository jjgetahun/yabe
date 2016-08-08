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

    public String generateSalesReport() {
        return "";
    }

}
