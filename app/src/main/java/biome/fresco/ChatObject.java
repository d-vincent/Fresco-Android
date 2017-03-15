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
    private boolean isGroupChat;

    public ChatObject(String roomId, String toUserId, boolean notified, boolean unread){

        this.roomId = roomId;
        this.toUserName = toUserName;
        this.toUserId = toUserId;
        this.notified = notified;
        this.unread = unread;
        this.isGroupChat = false;
    }
    public ChatObject(String roomId){
        this.roomId = roomId;
        this.isGroupChat = true;
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

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }
}
