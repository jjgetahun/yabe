package database;

import java.util.ArrayList;
import java.util.Date;

public class Auction {

    private final String sellerName;
    private final int itemID;
    private final int reserve;
    private final Date endTime;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();

    public Auction(String sellerName, int itemID, int reserve, Date endTime) {
        this.sellerName = sellerName;
        this.itemID = itemID;
        this.reserve = reserve;
        this.endTime = endTime;
    }

    public ArrayList<Bid> getBidList() {
        return bidList;
    }

}