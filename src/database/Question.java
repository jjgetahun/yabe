package database;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Jon on 8/8/16.
 */
public class Question {

    private final int posterID;
    private final int auctionID;
    private String header;
    private String contents;
    private final Timestamp timePosted;
    private ArrayList<Answer> answerList = new ArrayList<Answer>();

    public Question(int posterID, int auctionID, String header, String contents, Timestamp timePosted) {
        this.posterID = posterID;
        this.auctionID = auctionID;
        this.header = header;
        this.contents = contents;
        this.timePosted = timePosted;
    }

    public ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }

}
