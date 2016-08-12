package database;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Auction {

    public final int sellerID;
    public final String name;
    public final int itemID;
    public final float reserve;
    public final Timestamp startTime;
    public final Timestamp endTime;
    public final String condition;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();

    public Auction(int sellerID, String name, int itemID, float reserve, Timestamp startTime, Timestamp endTime, String condition) {
        this.sellerID = sellerID;
        this.name = name;
        this.itemID = itemID;
        this.reserve = reserve;
        this.startTime = startTime;
        this.endTime = endTime;
        this.condition = condition;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    public void addBid(Bid bid) {
        bidList.add(bid);
    }

}