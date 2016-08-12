package database;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DB {

    private static class checkAuctionWinners extends TimerTask {
        public void run() {
            if(!initialized) init();
            try {
                //check for ended auctions
                Date date = new Date();
                Timestamp currentTime = new Timestamp(date.getTime());
                String sql = "SELECT A.AuctionID as aid, A.SellerID as sid, B.BidderID as bid, A.reserve as reserve, B.amount as amount" +
                        " FROM Auction A, Bid B where A.AuctionID = B.AuctionID and A.EndTime <= '" + currentTime +
                        "' and A.HasEnded = " + "0 GROUP BY B.AuctionId HAVING MAX(B.Amount);";
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                int aID;
                int sID;
                int bID;
                float reserve;
                float amount;
                String sellerMessage;
                String buyerMessage;
                while (rs.next()) {
                    //notify seller and winner
                    aID = rs.getInt("aid");
                    sID = rs.getInt("sid");
                    bID = rs.getInt("bid");
                    reserve = rs.getFloat("reserve");
                    amount = rs.getFloat("amount");

                    if (amount >= reserve) {
                        sellerMessage = "Auction #" + Integer.toString(aID) + " has sold for $" + Float.toString(amount) + ".";
                        buyerMessage = "Congratulations! You have won auction #" + Integer.toString(aID) + ".";
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + sID + ", '" + sellerMessage + "');";
                        statement.executeUpdate(sql);
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + bID + ", '" + buyerMessage + "');";
                        statement.executeUpdate(sql);
                    }
                    else{
                        sellerMessage = "Auction #" + Integer.toString(aID) + " has not met its reserve of " + Float.toString(amount) + ".";
                        buyerMessage = "Your bid on auction #" + Integer.toString(aID) + " has not met the reserve.";
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + sID + ", '" + sellerMessage + "');";
                        statement.executeUpdate(sql);
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + bID + ", '" + buyerMessage + "');";
                        statement.executeUpdate(sql);
                    }


                    //mark auction as ended
                    sql = "UPDATE Auction SET HasEnded = 1 WHERE AuctionID = " + aID + ";";
                    statement.executeUpdate(sql);
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private static Connection conn;
    private static boolean initialized = false;

    private static void init(){

        //Load database.DB
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection ("jdbc:mysql://classvm51.cs.rutgers.edu/proj2016","root","DigDagDug55");
//            conn = DriverManager.getConnection ("jdbc:mysql://localhost/proj2016","root","themysql");
            initialized = true;
            //start auction win checker
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new checkAuctionWinners(), 5, 10000);
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

    public static int getUserID(String username){

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
        }catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }

    public static String getNameFromID(int id){

        if(!initialized) init();

        String name = "";

        try {
            String sql = "SELECT UserName FROM Account where AccountID = " + id + ";";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            //Assuming only one or zero users comes back
            while (rs.next()) {
                name = rs.getString("UserName");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return name;
    }

    public static boolean isAdmin(int accountID) {

        if (!initialized)
            init();

        try {
            String sql = "SELECT * FROM Account WHERE AccountID = "+accountID+";";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int isAdmin = -1;

            while (rs.next()) {
                isAdmin = rs.getInt("isAdmin");
            }

            return isAdmin == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isCustomerRep(int accountID) {

        if (!initialized)
            init();

        try {
            String sql = "SELECT * FROM Account WHERE AccountID = "+accountID+";";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int isCustomerRep = -1;

            while (rs.next()) {
                isCustomerRep = rs.getInt("isCustomerRep");
            }

            return(isCustomerRep == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean insertUser(String username, String name, String password, int isAdmin, int isCustomerRep){

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

            sql = "INSERT INTO Account(UserName, Name, PassWord, isAdmin, isCustomerRep) VALUES('" + username + "', '" + name +
                    "', '" + password + "', "+isAdmin+", "+isCustomerRep+");";
            statement.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean placeBid(int bidderID, int auctionID, float amount, int isAuto){

        if(!initialized) init();
        float highestBid = 0.01f;
        int oldBidderID = -1;
        try{
            //find current highest bidder

            String sql = "SELECT Amount, BidderID FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID and A.AuctionID" +
                    " = " + auctionID +" GROUP BY Amount DESC;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            highestBid = rs.getFloat("Amount");
            oldBidderID = rs.getInt("BidderID");

            System.out.println(highestBid);
            System.out.println(oldBidderID);
            if (amount <= highestBid || oldBidderID == bidderID)
                return false;

            //notify outbid
            sql = "INSERT INTO Message(Contents, ReceiverID) VALUES('You have been outbid on auction #" + auctionID + ".', " +
                    oldBidderID + ");";
            statement.executeUpdate(sql);

            //insert new bid
            sql = "INSERT INTO Bid(Amount, BidderID, AuctionID, IsAuto) VALUES("+ amount + ", " + bidderID + ", " +
                    auctionID + ", " + isAuto + ");";
            statement.executeUpdate(sql);

//            if(isAuto == 1) {
//                sql = "INSERT INTO Bid(Amount, BidderID, AuctionID, IsAuto) VALUES("+ Math.ceil(highestBid+0.01) + ", " + bidderID + ", " +
//                        auctionID + ", 0);";
//                statement.executeUpdate(sql);
//            }

//            //recursively insert competing auto bids
//            sql = "SELECT MAX(Amount) as amt, BidderID FROM Bid WHERE IsAuto = 1 and BidderID <> " + bidderID + "GROUP BY BidderID;";
//            rs = statement.executeQuery(sql);
//            float newAmount = 0;
//            int newBidderID = -1;
//            while(rs.next()){
//                newAmount = rs.getFloat("amt");
//                newBidderID = rs.getInt("BidderID");
//            }
//            placeBid(newBidderID, auctionID, newAmount, 0);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeBid(int auctionID, float amount) {

        if(!initialized) init();

        try {
            String sql = "DELETE FROM Bid WHERE AuctionID = " + auctionID + " AND Amount = + " + amount + ";";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Item getItem(int modelNumber) {
        if (!initialized) init();
        System.out.println(modelNumber);
        try {
            String sql = "SELECT * FROM Item WHERE ModelNumber = " + modelNumber;
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            String type, a, b, c;

            type = a = b = c = "oops";

            while (rs.next()) {
                type = rs.getString("Type");
                switch (type) {
                    case "backpacks":
                        a = rs.getString("Pockets");
                        b = rs.getString("Material");
                        c = rs.getString("Waterproof");
                        break;
                    case "tents":
                        a = rs.getString("Color");
                        b = rs.getString("Capacity");
                        c = rs.getString("SpareParts");
                        break;
                    default:
                        a = rs.getString("Battery");
                        b = rs.getString("Rechargeable");
                        c = rs.getString("LED");
                        break;
                }
            }
            return new Item(modelNumber, type, a, b, c);
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    private static boolean createItem(int modelNumber, String type, String[] attr) {
        if (!initialized) init();

        try {

            String sql = "";
            switch(type) {
                case "backpacks":
                    sql = "INSERT INTO Item(ModelNumber, Type, Pockets, Material, Waterproof) VALUES(" + modelNumber + ", 'backpacks', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    break;

                case "tents":
                    sql = "INSERT INTO Item(ModelNumber, Type, Color, Capacity, SpareParts) VALUES(" + modelNumber + ", 'tents', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    break;

                case "flashlights":
                    sql = "INSERT INTO Item(ModelNumber, Type, Battery, Rechargeable, LED) VALUES(" + modelNumber + ", 'flashlights', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    break;

                default:
                    return false;
            }

            Statement statement = conn.createStatement();

            statement.executeUpdate(sql);
            return true;

        }
        catch (SQLException e ) {
            e.printStackTrace();
            return false;
        }

    }

    public static ResultSet getMessages(int accountID) {
        if(!initialized) init();

        try {
            String sql = "SELECT * FROM Message where ReceiverID = " + accountID + ";";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private static void resolveAlerts(int modelNumber) {
        try {
            String sql = "SELECT * FROM Alert where ModelNumber = " + modelNumber + ";";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int id = -1;
            while (rs.next()) {
                id = rs.getInt("AccountID");
                sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + id + ", 'An auction has been posted for model #"+ modelNumber + "' );";
                statement.executeUpdate(sql);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int createAuction(int sellerID, String name, int modelNumber, String type, String[] attr, float reserve, Date endTime, String condition) {
        if(!initialized) init();
        int id = -1;

        try {

            String sql = "SELECT * FROM Item where ModelNumber = " + modelNumber + ";";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("ModelNumber");
            }
            if (id != modelNumber)
                createItem(modelNumber, type, attr);

            sql = "INSERT INTO Auction(SellerID, Name, ItemID, Reserve, EndTime, Cond) VALUES(" + sellerID + ", '" + name + "', " + modelNumber + ", " + reserve + ", '" + endTime + "', '" + condition + "');";
            statement.executeUpdate(sql);

            int auctionID = -1;
            sql = "SELECT MAX(AuctionID) as aid FROM Auction;";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                auctionID = rs.getInt("aid");
            }
            resolveAlerts(modelNumber);
            return auctionID;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Auction getAuction(int auctionID) {
        if(!initialized) init();
        try {

            String sql = "SELECT * FROM Auction WHERE AuctionID = " + auctionID + ";";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int sellerID = -1;
            String name = "";
            int itemID = -1;
            float reserve = -1;
            Timestamp startTime = null;
            Timestamp endTime = null;
            String condition = "";

            while (rs.next()) {
                sellerID = rs.getInt("SellerID");
                name = rs.getString("Name");
                itemID = rs.getInt("ItemID");
                reserve = rs.getFloat("Reserve");
                startTime = rs.getTimestamp("StartTime");
                endTime = rs.getTimestamp("EndTime");
                condition = rs.getString("Cond");
            }
            Auction auction = new Auction(sellerID, name, itemID, reserve, startTime, endTime, condition);

            sql = "SELECT Amount, BidderID, Time FROM Bid WHERE IsAuto = 0 and AuctionID = " + auctionID + " GROUP BY Amount;";
            rs = statement.executeQuery(sql);
            float amount = -1;
            int bidderID = -1;
            Timestamp time = null;
            while (rs.next()) {
                amount = rs.getFloat("Amount");
                bidderID = rs.getInt("BidderID");
                time = rs.getTimestamp("Time");
                auction.addBid(new Bid(bidderID, amount, time));
            }

            return auction;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static AbstractMap.SimpleEntry searchAuction(String modelNumber, String type, String[] attr, String endTime, boolean browseMode, String condition) {

        if (!initialized)
            init();

        String query = "";
        try {

            if (browseMode) {

                if (modelNumber == null || modelNumber.equals("")) {
                    query = "SELECT DISTINCT A.AuctionID FROM Auction A, Bid B WHERE A.AuctionID = A.AuctionID;"; //Redundency for inclusion of a where clause
                } else {
                    query = "SELECT DISTINCT A.AuctionID FROM Auction A, Bid B WHERE ItemID = " + Integer.parseInt(modelNumber) + ";";
                }

                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);

                return new AbstractMap.SimpleEntry(query, rs);

            } else {

                query = "SELECT DISTINCT A.AuctionID FROM Auction A, Item I, Bid B WHERE A.ItemID = I.ModelNumber AND I.Type = '" + type + "' AND A.Cond = '" + condition + "' AND ";

                if (endTime != null && !endTime.equals("")) {

                    DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
                    Date endDate = df.parse(endTime);

                    query += "A.EndTime <= " + endDate.getTime() + " AND ";
                }

                switch (type) {
                    case "backpacks":
                        query += "(I.Pockets = '" + attr[0] + "' AND I.Material = '" + attr[1] + "' AND I.Waterproof = '" + attr[2] + "');";
                        break;
                    case "tents":
                        query += "(I.Color = '" + attr[0] + "' AND I.Capacity = '" + attr[1] + "' AND I.SpareParts = '" + attr[2] + "');";
                        break;
                    case "flashlights":
                        query += "(I.Battery = '" + attr[0] + "' AND I.Rechargeable = '" + attr[1] + "' AND I.LED = '" + attr[2] + "');";
                        break;
                }

                System.out.println(query);
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);
                return new AbstractMap.SimpleEntry(query, rs);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet sortAuctionSearchByTime (String baseQuery, boolean asc) {

        if(!initialized) init();

        try {
            String query = baseQuery.substring(0, baseQuery.indexOf(';')); //remove ;

            if(!asc) query += " ORDER BY A.EndTime DESC;";
            else  query += " ORDER BY A.EndTime;";

            System.out.println(query);
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet sortAuctionSearchByPrice (String baseQuery, boolean asc) {

        if(!initialized) init();

        try {
            String query = baseQuery.substring(0, baseQuery.indexOf(';')); //remove ;

            if(!asc) query += " AND A.AuctionID = B.AuctionID ORDER BY B.Amount DESC;";
            else  query += " AND A.AuctionID = B.AuctionID ORDER BY B.Amount;";

            System.out.println(query);
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean setAlert(int accountID, int modelNumber) {
        if(!initialized) init();

        try {
            String sql = "INSERT INTO Alert(AccountID, ModelNumber) VALUES("+ accountID+ ", " + modelNumber +");";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getAuctionsParticipatedIn(int userID) {
        if (!initialized)
            init();
        try {
            String sql = "(SELECT * FROM Auction A WHERE EXISTS(SELECT * FROM Bid B WHERE A.AuctionID = B.AuctionID and B.BidderID = " + userID + ")) UNION (SELECT * FROM Auction WHERE SellerID = " + userID + ");";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet getSimilarAuctions(int modelNumber){
        if (!initialized)
            init();
        try {
            Item item = getItem(modelNumber);
            String sql = "SELECT * FROM Auction A WHERE EXISTS (SELECT * FROM Item I WHERE A.ItemID = I.ModelNumber AND I.Type = '" + item.category + "');";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static boolean postQuestion(int posterID, int auctionID, String header, String contents) {

        if (!initialized)
            init();

        try {
            String sql = "INSERT INTO Question(Header, Contents, PosterID, AuctionID) VALUES('"+header+"', '"+contents+"', "+posterID+", "+auctionID+");";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            sql = "SELECT AccountID FROM Account WHERE isCustomerRep = 1";
            sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(, " +"'A new question has been posted');";
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean answerQuestion(int questionID, int posterID, String contents){

        if (!initialized)
            init();

        int qPosterID = -1;

        try{
            //Find the given question
            String sql = "SELECT Q.QuestionID FROM Question Q WHERE Q.QuestionID = "+questionID+";";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                qPosterID = rs.getInt("QuestionID");
            }

            if (qPosterID != 0) {
                //Post answer
                sql = "INSERT INTO Answer(Contents, QuestionID, PosterID) VALUES('" + contents + "', " + questionID + ", " + posterID + ");";
                statement.executeUpdate(sql);
                return true;
            }
            else
                return false;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getAuctionQuestions(int auctionID){

        if (!initialized)
            init();

        try{
            String sql = "SELECT * FROM Question WHERE AuctionID = "+auctionID+" ORDER BY QuestionID;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public static ResultSet getAllQuestions(){

        if (!initialized)
            init();

        try{

            //Find the given question
            String sql = "SELECT * FROM Question Q WHERE NOT EXISTS (SELECT * FROM Answer A WHERE Q.QuestionID = A.QuestionID);";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAnswered(int questionID) {
        if (!initialized)
            init();

        try{

            String sql = "SELECT * FROM Question Q, Answer A WHERE Q.QuestionID = " + questionID + " AND Q.QuestionID = A.QuestionID;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            return rs != null;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    public static ResultSet getAnswer(int questionID){

        if (!initialized)
            init();

        try{

            //Find the given question
            String sql = "SELECT * FROM Answer WHERE QuestionID = "+questionID+";";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getAuctionBids(int auctionID){

        if (!initialized)
            init();

        try{
            String sql = "SELECT * FROM Bid WHERE AuctionID = "+auctionID+" AND isAuto = 0 ORDER BY Amount DESC;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getTotalEarningsReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT SUM(maxBids) AS Amount FROM (SELECT MAX(B.Amount) maxBids FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID AND A.HasEnded = 1 GROUP BY A.AuctionID) a;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet getEarningsPerItemReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT A.ItemID, B.Amount FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID AND A.HasEnded = 1 AND B.Amount = (SELECT MAX(B2.AMOUNT) FROM Bid B2 WHERE B2.AuctionID = B.AuctionID) GROUP BY A.ItemID;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet getEarningsPerItemTypeReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT I.Type, SUM(B.Amount) AS Amount FROM Auction A, Bid B, Item I WHERE A.AuctionID = B.AuctionID AND A.HasEnded = 1 AND A.ItemID = I.ModelNumber AND B.Amount = (SELECT MAX(B2.AMOUNT) FROM Bid B2 WHERE B2.AuctionID = B.AuctionID) GROUP BY I.Type;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet getEarningsPerEndUserReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT A.SellerID, SUM(B.Amount) AS Amount FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID AND A.HasEnded = 1 AND B.Amount = (SELECT MAX(B2.AMOUNT) FROM Bid B2 WHERE B2.AuctionID = B.AuctionID) GROUP BY A.SellerID ASC;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet getBestSellingItemsReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT A.ItemID, B.Amount AS Amount FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID AND A.HasEnded = 1 AND B.Amount = (SELECT MAX(B2.AMOUNT) FROM Bid B2 WHERE B2.AuctionID = B.AuctionID GROUP BY A.ItemID) ORDER BY B.Amount DESC LIMIT 5;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet getBestBuyersReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT B.BidderID, SUM(B.Amount) AS Amount FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID AND A.HasEnded = 1 AND B.Amount = (SELECT MAX(B2.AMOUNT) FROM Bid B2 WHERE B2.AuctionID = B.AuctionID) GROUP BY B.Amount DESC LIMIT 5;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

}