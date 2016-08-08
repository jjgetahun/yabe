package database;

import java.sql.Timestamp;

/**
 * Created by Jon on 8/8/16.
 */
public class CustomerRep extends Account {

    public CustomerRep(String name, String username, String password, Timestamp dateCreated) {
        super(name, username, password, dateCreated);
    }

    public void removeBid(Auction a, Bid b) {
        a.getBidList().remove(b);
    }

    public void removeAllBids(Auction a) {
        a.getBidList().removeAll(a.getBidList());
    }

    public void removeAuction(Auction a) {
        a = null;
    }

}
