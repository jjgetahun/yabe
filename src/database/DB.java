package database;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.util.ArrayList;
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
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + sID + ", '" + sellerMessage + "');" +
                                "INSERT INTO Message(ReceiverID, Contents) VALUES(" + bID + ", '" + buyerMessage + "');";
                    }
                    else{
                        sellerMessage = "'Auction #" + Integer.toString(aID) + " has not met its reserve of " + Float.toString(amount) + ".'";
                        buyerMessage = "'Your bid on auction #" + Integer.toString(aID) + " has not met the reserve.'";
                        sql = "INSERT INTO Message(ReceiverID, Contents) VALUES(" + sID + ", '" + sellerMessage + "');" +
                                "INSERT INTO Message(ReceiverID, Contents) VALUES(" + bID + ", '" + buyerMessage + "');";
                    }
                    statement.executeUpdate(sql);

                    //mark auction as ended
                    sql = "REPLACE INTO Auction(HasEnded) SELECT 1 FROM Auction WHERE AuctionID = " + aID + ";";
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
//            conn = DriverManager.getConnection ("jdbc:mysql://classvm51.cs.rutgers.edu/proj2016","root","DigDagDug55");
            conn = DriverManager.getConnection ("jdbc:mysql://localhost/proj2016","root","themysql");
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
            String sql = "SELECT Name FROM Account where AccountID = " + id + ";";
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

            if (isAdmin == 1)
                return true;
            else
                return false;
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

            if (isCustomerRep == 1)
                return true;
            else
                return false;
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

    public static boolean placeBid(String username, int auctionID, float amount, int isAuto){

        if(!initialized) init();
        float highestBid = 0.01f;
        int oldBidderID = -1;
        try{
            //find current highest bidder
            String sql = "SELECT MAX(B.Amount) as bid, B.BidderID FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID " +
                    "GROUP BY B.BidderID;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                highestBid = rs.getFloat("bid");
                oldBidderID = rs.getInt("BidderID");
            }

            if (amount <= highestBid)
                return false;

            //notify outbid
            sql = "INSERT INTO Message(Contents, ReceiverID) VALUES('You have been outbid on auction #" + auctionID + ".', " +
                    oldBidderID + ");";
            statement.executeUpdate(sql);

            //
            int newBidderID = getUserID(username);
            sql = "INSERT INTO Bid(Amount, BidderID, AuctionID, IsAuto) VALUES("+ amount + ", " + newBidderID + ", " +
                    auctionID + ", " + isAuto + ");";
            statement.executeUpdate(sql);
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeBid(int bidderID, int auctionID, float amount) {

        if(!initialized) init();

        try {
            String sql = "DELETE FROM Bid WHERE BidderID = " + bidderID + " AND AuctionID = " + auctionID + " AND Amount = + " + amount + ";";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            Auction a = getAuction(auctionID);
            ArrayList<Bid> bidList = a.getBidList();

            for (int i = 0; i < bidList.size(); i++) {
                if (bidList.get(i).getBidderID() == bidderID && bidList.get(i).amount == amount) {
                    bidList.remove(bidList.get(i));
                    break;
                }
            }

            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Item getItem(int modelNumber) {
        if (!initialized) init();

        try {
            String sql = "SELECT * FROM Item WHERE ModelNumber = ModelNumber";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {

            }
            return null;
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
            System.out.println("STEP 1");
            switch(type) {
                case "backpacks":
                    sql = "INSERT INTO Item(ModelNumber, Type, Pockets, Material, Waterproof) VALUES(" + modelNumber + ", 'Backpack', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    System.out.println("STEP 2");
                    break;

                case "tents":
                    sql = "INSERT INTO Item(ModelNumber, Type, Color, Capacity, SpareParts) VALUES(" + modelNumber + ", 'Tent', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
                    break;

                case "flashlights":
                    sql = "INSERT INTO Item(ModelNumber, Type, Battery, Rechargable, LED) VALUES(" + modelNumber + ", 'Flashlight', '" + attr[0] + "', '" + attr[1] + "', '" + attr[2] + "');";
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
            String sql = "SELECT * FROM Message where accountID = " + accountID + ";";
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
            String sql = "SELECT * FROM Item where ModelNumber = " + modelNumber + ";";
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

    public static int createAuction(int sellerID, int modelNumber, String type, String[] attr, float reserve, Date endTime, String condition) {
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

            sql = "INSERT INTO Auction(SellerID, ItemID, Reserve, EndTime, Cond) VALUES(" + sellerID + ", " + modelNumber + ", " + reserve + ", '" + endTime + "', '" + condition + "');";
            statement.executeUpdate(sql);

            int auctionID = -1;
            sql = "SELECT MAX(AuctionID) as aid FROM Auction;";
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                auctionID = rs.getInt("aid");
            }
            if (id != -1)
                createItem(modelNumber, type, attr);
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

    public static boolean removeAuction(int auctionID) {

        if(!initialized) init();

        try {
            String sql = "DELETE FROM Auction WHERE AuctionID = " + auctionID + ";";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static ResultSet sortAuctionSearchByType () {

        if(!initialized) init();

        try {
            String sql = "SELECT * FROM Auction A, Item I WHERE A.AuctionID = I.AuctionID ORDER BY I.Type;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ResultSet sortAuctionSearchByPrice () {

        if(!initialized) init();

        try {
            String sql = "SELECT * FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID ORDER BY MAX(B.Amount);";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;
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

    /*public static ResultSet salesReportItem(){

        if (!initialized)
            init();
        try {
            String sql = "SELECT A.ItemID, SUM(MAX(B.Amount)) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID GROUP BY A.ItemID;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }*/

//    public static ResultSet salesReportCategory(String Category){
//
//        if (!initialized)
//            init();
//        try {
//            String sql = "SELECT A.ItemID, SUM(MAX(B.Amount)) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID GROUP BY A.ItemID;";
//            Statement statement = conn.createStatement();
//            return statement.executeQuery(sql);
//        }
//        catch(SQLException e){
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static ResultSet getAuctionsParticipatedIn(int userID) {
        if (!initialized)
            init();
        try {
            String sql = "SELECT DISTINCT A.AuctionID FROM Auction A, Bid B WHERE (A.AuctionID = B.AuctionID and B.UserID = " + userID + ") or A.SellerID = " + userID + ";";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<ResultSet> getSimilarAuctions(int modelNumber){
        if (!initialized)
            init();
        try {
            Item item = getItem(modelNumber);
            String attr1, attr2, attr3;
            if (item.category.equals("backpacks")) {
                attr1 = "Pockets";
                attr2 = "Material";
                attr3 = "Waterproof";
            }
            else if (item.category.equals("tents")){
                attr1 = "Capacity";
                attr2 = "Color";
                attr3 = "SpareParts";
            }
            else {
                attr1 = "Battery";
                attr2 = "Rechargeable";
                attr3 = "LED";
            }
            String sql = "SELECT ModelNumber FROM Item WHERE Item.ModelNumber <> " + modelNumber + " and (" +
                    attr1 + " = '" + item.attr1 + "' or " + attr2 + " = '" + item.attr2 + "' or " + attr3 + " = '" + item.attr3 +
                    "');";
            ArrayList<ResultSet> similarAuctions = new ArrayList();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            int similarModel = -1;
            while(rs.next()){
                similarModel = rs.getInt("ModelNumber");
                sql = "SELECT AuctionID FROM Auction WHERE ItemID = " + similarModel + ";";
                ResultSet newRS = statement.executeQuery(sql);
                similarAuctions.add(newRS);
            }
            return similarAuctions;
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
            String sql = "SELECT Q.QuestionID FROM Question Q WHERE Q.QuestionID = "+questionID+";";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                qPosterID = rs.getInt("QuestionID");
            }

            if (qPosterID != -1) {
                //Post answer
                sql = "INSERT INTO Answer(Contents, QuestionID) VALUES('" + contents + "', " + questionID + ");";
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
            String sql = "SELECT * FROM Question WHERE AuctionID = "+auctionID+";";
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

            sql = "SELECT PosterID, Header, Contents FROM Answer WHERE QuestionID = " + questionID + ";";
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

    public static Question searchAllQuestions(String questionHeader){

        if (!initialized)
            init();

        try{

            //Find the given question
            String sql = "SELECT * FROM Question WHERE Header LIKE '%"+questionHeader+"%';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            int posterID = -1;
            int questionID = -1;
            int auctionID = -1;
            String header = "";
            String contents = "";
            Timestamp timePosted = null;

            while (rs.next()) {
                posterID = rs.getInt("PosterID");
                questionID = rs.getInt("QuestionID");
                auctionID = rs.getInt("AuctionID");
                header = rs.getString("Header");
                contents = rs.getString("Contents");
                timePosted = rs.getTimestamp("TimePosted");
            }

            Question question = new Question(posterID, auctionID, header, contents, timePosted);

            sql = "SELECT PosterID, Header, Contents FROM Answer WHERE QuestionID = " + questionID + ";";
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

    public static ResultSet getAllQuestions(){

        if (!initialized)
            init();

        try{

            //Find the given question
            String sql = "SELECT * FROM Question Q WHERE NOT EXISTS (SELECT * FROM Answer A WHERE Q.QuestionID = A.QuestionID);";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            return rs;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet generateSalesReport(String type) {

        if (!initialized)
            init();

        switch(type) {
            case "Total Earnings":
                return getTotalEarningsReport();

            case "Earnings per Item":
                return getEarningsPerItemReport();

            case "Earnings per Item Type":
                return getEarningsPerItemTypeReport();

            case "Earnings per End-User":
                return getEarningsPerEndUserReport();

            case "Best-Selling Items":
                return getBestSellingItemsReport();

            case "Best Buyers":
                return getBestBuyersReport();

            default:
                return null;
        }
    }

    public static ResultSet getTotalEarningsReport() {
        if (!initialized)
            init();

        try {
            String sql = "SELECT SUM(MAX(B.Amount)) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID;";
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
            String sql = "SELECT A.ItemID, SUM(MAX(B.Amount)) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID GROUP BY A.ItemID;";
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
            String sql = "SELECT I.Type, SUM(MAX(B.Amount)) FROM Auction A, Bid B, Item I WHERE A.AuctionID = B.AuctionID AND A.ItemID = I.ModelNumber GROUP BY I.Type;";
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
            String sql = "SELECT A.SellerID, SUM(MAX(B.Amount)) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID GROUP BY A.SellerID;";
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
            String sql = "SELECT A.ItemID, MAX(B.Amount) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID GROUP BY A.ItemID DESC LIMIT 5;";
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
            String sql = "SELECT B.BidderID, MAX(B.Amount) FROM Auction A, Bid B WHERE A.AuctionID = B.AuctionID GROUP BY B.BidderID DESC LIMIT 5;";
            Statement statement = conn.createStatement();
            return statement.executeQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

}