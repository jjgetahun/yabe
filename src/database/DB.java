package database;

import java.sql.*;
/**
 * Created by elby on 7/20/16.
 */
public class DB {

    public static Connection conn;
    private static boolean initialized = false;

    public static void init(){
        //Load database.DB
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection ("jdbc:mysql://classvm51.cs.rutgers.edu/proj2016","root","DigDagDug55");
            initialized = true;
        } catch (Exception e) { //Generic exception, don't do this.
            e.printStackTrace();
        }
    }

    public static int validateLogin(String username, String password){

        if(!initialized) init();

        int id = -1;

        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + "' AND PassWord = '" + password + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                id = rs.getInt("AccountID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }

    private static int getUserID(String username){

        if(!initialized) init();

        int id = -1;

        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + ";";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                id = rs.getInt("AccountID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }
    public static String getName(String username){

        if(!initialized) init();

        String name = "";

        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                name = rs.getString("Name");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return name;
    }

    public static boolean insertUser(String username, String name, String password){

        if(!initialized) init();
        int id = -1;
        try {
            String sql = "SELECT * FROM Account where UserName = '" + username + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                id = rs.getInt("AccountID");
            }
            if (id != -1)
                return false;

            sql = "INSERT INTO Account(UserName, Name, PassWord) VALUES('" + username + "', '" + name +
                    "', '" + password + "');";
            statement.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean placeBid(String username, int auctionID, double amount, int isAuto){

        if(!initialized) init();
        double highestBid = 0.01;
        int oldBidderID = -1;
        try{
            //find current highest bidder
            String sql = "SELECT MAX(B.Amount) as bid, B.BidderID FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID " +
                    "GROUP BY B.BidderID;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                highestBid = rs.getDouble("bid");
                oldBidderID = rs.getInt("BidderID");
            }

            if (amount <= highestBid)
                return false;

            //notify outbid
            sql = "INSERT INTO Message(Contents, ReceiverID) VALUES('You have been outbid on auction #" + auctionID + ".', '" +
                    oldBidderID + "');";
            statement.executeUpdate(sql);

            //
            int newBidderID = getUserID(username);
            sql = "INSERT INTO Bid(Amount, BidderID, AuctionID, IsAuto) VALUES('"+ amount + "','" + newBidderID + "','" +
                    auctionID + "','" + isAuto + "');";
            statement.executeUpdate(sql);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createAuction(String sellerName, int itemID, int reserve, Date endTime) {
        if(!initialized) init();

        int sellerID = getUserID(sellerName);

        int id = -1;

        try {

            String sql = "SELECT * FROM Item where ItemID = '"+itemID+"';";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("ItemID");

                if (id != -1)
                    return false;
            }

            sql = "INSERT INTO AUCTION(SellerID, ItemID, Reserve, EndTime) VALUES('" + sellerID + "', '" + itemID + "', '" + reserve + "', '" + endTime + "');";
            statement.executeUpdate(sql);
            return true;
        }
        catch(SQLException se) {
            se.printStackTrace();
            return false;
        }

    }

}
