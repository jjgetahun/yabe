package database;

import java.util.ArrayList;
import java.util.Date;

public class Auction {

    private final int sellerID;
    private final int itemID;
    private final float reserve;
    private final Date startTime;
    private final Date endTime;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();

    public Auction(int sellerID, int itemID, float reserve, Date startTime, Date endTime) {
        this.sellerID = sellerID;
        this.itemID = itemID;
        this.reserve = reserve;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    public void addBid(Bid bid) {
        bidList.add(bid);
    }

}