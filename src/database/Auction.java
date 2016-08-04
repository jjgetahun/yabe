package database;

import java.util.ArrayList;
import java.util.Date;

public class Auction {

    private final String sellerID;
    private final int itemID;
    private final int reserve;
    private final Date endTime;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();

    public Auction(int sellerID, int itemID, int reserve, Date startTime, Date endTime) {
        this.sellerID = sellerID;
        this.itemID = itemID;
        this.reserve = reserve;
        this.startTime = startTime
        this.endTime = endTime;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }

    public void addBid(Bid bid) {
        bidList.add(bid);
    }

}