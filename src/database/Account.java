package database;

import java.sql.Timestamp;

/**
 * Created by Jon on 8/8/16.
 */
public abstract class Account {

    private String name;
    private String username;
    private String password;
    private final Timestamp dateCreated;

    public Account(String name, String username, String password, Timestamp dateCreated) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
    }

}
