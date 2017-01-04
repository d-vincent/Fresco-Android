package biome.fresco;

/**
 * Created by Drew on 1/3/2017.
 */

public class MessageObject {
    private String message;
    private String author;
    private long type;
    private String timeStamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MessageObject(String message, String author, long type, String timeStamp){
        this.author = author;
        this.timeStamp = timeStamp;
        this.type = type;
        this.message = message;
    }
}
