package database;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Jon on 8/8/16.
 */
public class Answer {

    private final int posterID;
    private final int questionID;
    private String contents;
    private final Timestamp timePosted;

    public Answer(int posterID, int questionID, String contents, Timestamp timePosted) {
        this.posterID = posterID;
        this.questionID = questionID;
        this.contents = contents;
        this.timePosted = timePosted;
    }

}
