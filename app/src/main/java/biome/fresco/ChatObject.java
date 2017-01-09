package biome.fresco;

/**
 * Created by Drew on 1/7/2017.
 */

public class ChatObject {

    private String toUserImageUrl;
    private String toUserName;
    private String roomId;
    private String toUserId;
    private boolean notified;
    private boolean unread;

    public ChatObject(String roomId, String toUserId, String notified, String unread){

        this.roomId = roomId;
        this.toUserName = toUserName;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserImageUrl() {
        return toUserImageUrl;
    }

    public void setToUserImageUrl(String toUserImageUrl) {
        this.toUserImageUrl = toUserImageUrl;
    }
}
