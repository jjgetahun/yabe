package database;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Auction {

    private final int sellerID;
    private final int itemID;
    private final float reserve;
    private final Timestamp startTime;
    private final Timestamp endTime;
    private ArrayList<Bid> bidList = new ArrayList<Bid>();

    public Auction(int sellerID, int itemID, float reserve, Timestamp startTime, Timestamp endTime) {
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

    public boolean isTimeUp() {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(endTime.getTime());
        if (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            return true;
        }
        else {
            return false;
        }
    }

    public Bid determineWinner() {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(endTime.getTime());
        if (isTimeUp()) {
            if (bidList.size() == 0 || bidList.get(bidList.size()-1).amount < reserve)
                return null;
            else
                return bidList.get(bidList.size()-1);
        }
        else
            return determineWinner();
    }

}