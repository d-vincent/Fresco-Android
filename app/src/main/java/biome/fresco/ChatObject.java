package biome.fresco;

/**
 * Created by Drew on 1/7/2017.
 */

public class ChatObject {

    private String roomId;
    private String toUserId;
    private boolean notified;
    private boolean unread;

    public ChatObject(String roomId, String toUserId, String notified, String unread){

        this.roomId = roomId;
        this.toUserId = toUserId;
        if(notified.equals("true")){
            this.notified = true;
        }else{
            this.notified = false;
        }
        if(unread.equals("true")){
            this.unread = true;
        }
        else {
            this.unread = false;
        }


    }

}
