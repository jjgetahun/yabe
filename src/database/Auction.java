package database;

public class Auction {

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