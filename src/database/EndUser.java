package database;

import java.sql.Timestamp;

/**
 * Created by Jon on 8/8/16.
 */
public class EndUser extends Account {

    public EndUser(String name, String username, String password, Timestamp dateCreated) {
        super(name, username, password, dateCreated);
    }

}
