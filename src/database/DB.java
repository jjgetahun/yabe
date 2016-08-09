package database;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
/**
 * Created by elby on 7/20/16.
 */
public class DB {
    Timer timer;

    public DB() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new checkAuctionWinners(), 5, 300000);
    }

    class checkAuctionWinners extends TimerTask {
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
                        sellerMessage = "'Auction #" + Integer.toString(aID) + " has sold for $" + Float.toString(amount) + ".'";
                        buyerMessage = "'Congratulations! You have won auction #" + Integer.toString(aID) + ".'";
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES('" + sID + "', '" + sellerMessage + "');" +
                                "INSERT INTO Message(ReceiverID, Contents) VALUES('" + bID + "', '" + buyerMessage + "');";
                    }
                    else{
                        sellerMessage = "'Auction #" + Integer.toString(aID) + " has not met its reserve of " + Float.toString(amount) + ".'";
                        buyerMessage = "'Your bid on auction #" + Integer.toString(aID) + " has not met the reserve.'";
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES('" + sID + "', '" + sellerMessage + "');" +
                                "INSERT INTO Message(ReceiverID, Contents) VALUES('" + bID + "', '" + buyerMessage + "');";
                    }
                    statement.executeUpdate(sql);

                    //mark auction as ended
                    sql = "REPLACE INTO Auction(HasEnded) SELECT 1 FROM Auction WHERE AuctionID =" + aID + ";";
                    statement.executeUpdate(sql);;
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

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

    public static String getNameFromID(int id){

        if(!initialized) init();

        String name = "";

        try {
            String sql = "SELECT Name FROM Account where AccountID = '" + id + "';";
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

    public static int getItem() {
        return 0;
    }

    private static boolean createItem(int modelNumber, String type, String[] attr) {
        if (!initialized) init();

        try {

            String sql = "";

            switch(type) {
                case "Backpack":
                    sql = "INSERT INTO ITEM(ModelNumber, Pockets, Material, Waterproof) VALUES('" + modelNumber + "', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    break;

                case "Tent":
                    sql = "INSERT INTO ITEM(ModelNumber, Color, Capacity, SpareParts) VALUES('" + modelNumber + "', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    break;

                case "Flashlight":
                    sql = "INSERT INTO ITEM(ModelNumber, Battery, Rechargable, LED) VALUES('" + modelNumber + "', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
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

    public static boolean createAuction(String sellerName, int itemID, String type, String[] attr, int reserve, Date endTime) {
        if(!initialized) init();

        int sellerID = getUserID(sellerName);

        int id = -1;

        try {

            String sql = "SELECT * FROM Item where ModelNumber = '"+itemID+"';";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("ModelNumber");

                if (id != -1)
                    createItem(itemID, type, attr);
            }

            sql = "INSERT INTO AUCTION(SellerID, ItemID, Reserve, EndTime) VALUES('" + sellerID + "', '" + itemID + "', '" + reserve + "', '" + endTime + "');";
            statement.executeUpdate(sql);
            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Auction getAuction(int auctionID) {
        if(!initialized) init();
        try {

            String sql = "SELECT * FROM Auction WHERE AuctionID ='" + auctionID + "';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int sellerID = -1;
            int itemID = -1;
            float reserve = -1;
            Timestamp startTime = null;
            Timestamp endTime = null;

            while (rs.next()) {
                sellerID = rs.getInt("SellerID");
                itemID = rs.getInt("ItemID");
                reserve = rs.getFloat("Reserve");
                startTime = rs.getTimestamp("StartTime");
                endTime = rs.getTimestamp("EndTime");
            }

            Auction auction = new Auction(sellerID, itemID, reserve, startTime, endTime);

            sql = "SELECT Amount, BidderID, Time FROM Bid WHERE IsAuto = 0 and AuctionID = '" + auctionID + "' GROUP BY Amount;";
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

    public static boolean postQuestion(int posterID, int auctionID, String header, String contents) {

        if (!initialized)
            init();

        try {
            String sql = "INSERT INTO Question(Header, Contents, PosterID, AuctionID) VALUES('"+header+"', '"+contents+"', '"+posterID+"', '"+auctionID+"');";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean answerQuestion(int questionID, String contents){

        if (!initialized)
            init();

        int qPosterID = -1;

        try{
            //Find the given question
            String sql = "SELECT Q.QuestionID FROM Question Q WHERE Q.QuestionID = '"+questionID+"';";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                qPosterID = rs.getInt("QuestionID");
            }

            if (qPosterID != -1) {
                //Post answer
                sql = "INSERT INTO Answer(Contents, QuestionID) VALUES('" + contents + "', '" + questionID + "');";
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

    public static Question searchAuctionQuestions(int auctionID){

        if (!initialized)
            init();

        try{

            //Find the given question
            String sql = "SELECT * FROM Question WHERE AuctionID = '"+auctionID+"';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int posterID = -1;
            int questionID = -1;
            String header = "";
            String contents = "";
            Timestamp timePosted = null;

            while (rs.next()) {
                posterID = rs.getInt("PosterID");
                questionID = rs.getInt("QuestionID");
                header = rs.getString("Header");
                contents = rs.getString("Contents");
                timePosted = rs.getTimestamp("TimePosted");
            }

            Question question = new Question(posterID, auctionID, header, contents, timePosted);

            sql = "SELECT PosterID, Header, Contents FROM Answer WHERE QuestionID = '" + questionID + "';";
            rs = statement.executeQuery(sql);

            int ansPosterID = -1;
            int id = -1;
            String ansContents = "";
            Timestamp ansTimePosted = null;

            while (rs.next()) {
                ansPosterID = rs.getInt("PosterID");
                id = rs.getInt("QuestionID");
                ansContents = rs.getString("Contents");
                ansTimePosted = rs.getTimestamp("TimePosted");
                question.addAnswer(new Answer(ansPosterID, id, ansContents, ansTimePosted));
            }

            return question;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Question searchAllQuestions(String questionHeader, int questionID){

        if (!initialized)
            init();

        try{

            //Find the given question
            String sql = "SELECT * FROM Question WHERE Header LIKE '%"+questionHeader+"%' AND QuestionID = '"+questionID+"';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int posterID = -1;
            int auctionID = -1;
            String header = "";
            String contents = "";
            Timestamp timePosted = null;

            while (rs.next()) {
                posterID = rs.getInt("PosterID");
                auctionID = rs.getInt("AuctionID");
                header = rs.getString("Header");
                contents = rs.getString("Contents");
                timePosted = rs.getTimestamp("TimePosted");
            }

            Question question = new Question(posterID, auctionID, header, contents, timePosted);

            sql = "SELECT PosterID, Header, Contents FROM Answer WHERE QuestionID = '" + questionID + "';";
            rs = statement.executeQuery(sql);

            int ansPosterID = -1;
            int id = -1;
            String ansContents = "";
            Timestamp ansTimePosted = null;

            while (rs.next()) {
                ansPosterID = rs.getInt("PosterID");
                id = rs.getInt("QuestionID");
                ansContents = rs.getString("Contents");
                ansTimePosted = rs.getTimestamp("TimePosted");
                question.addAnswer(new Answer(ansPosterID, id, ansContents, ansTimePosted));
            }

            return question;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}