package algonquin.cst2335.ayu00002;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;
    @ColumnInfo(name="SendOrReceive")
    boolean isSentButton;

    ChatMessage(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }
    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }

    public ChatMessage(){

    }
}
