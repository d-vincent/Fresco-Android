package biome.fresnotes.Objects;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Drew on 1/7/2017.
 */

public class ChatObject implements Comparable<ChatObject>{

    private String toUserImageUrl;
    private String toUserName;
    private String roomId;
    private String toUserId;
    private boolean notified;
    private boolean unread;
    private boolean isGroupChat;
    private String lastMessage;
    private ArrayList<SimpleUser> users;
    private long timestamp;

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

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

//    public String getLastMessage() {
//        return lastMessage;
//    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getLastMessage() {
        return lastMessage;
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


    @Override
    public int compareTo(@NonNull ChatObject chatObject) {
        if (timestamp < chatObject.timestamp){
            return 1;
        }
        else return -1;
    }
}
