package database;

import java.util.Date;

public class Bid {

    private final int bidID;
    float amount;
    Date time;

    public Bid(int bidID, float amount, Date time) {
        this.bidID = bidID;
        this.amount = amount;
        this.time = time;
    }

}