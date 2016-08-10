package database;

import java.util.Date;

public class Bid {

    private final int bidderID;
    float amount;
    Date time;

    public Bid(int bidderID, float amount, Date time) {
        this.bidderID = bidderID;
        this.amount = amount;
        this.time = time;
    }

    public int getBidderID() {
        return bidderID;
    }

}