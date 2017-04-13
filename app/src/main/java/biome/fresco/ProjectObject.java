package biome.fresco;

import java.util.ArrayList;

/**
 * Created by Drew McDonald on 3/29/2017.
 */

public class ProjectObject {

    private String projectId;
    private String authorId;
    private String rootChatId;
    private String rootFolderId;
    private String searchName;
    private String projectDescription;
    private String name;

    private ArrayList<String> chatIds;
    private ArrayList<String> fileIds;
    private ArrayList<String> memberIds;

    private ArrayList<LabelObject> labels;
    private ArrayList<UserNoteList> userNotes;

    private UserNoteList allNotes;

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getRootChatId() {
        return rootChatId;
    }

    public String getRootFolderId() {
        return rootFolderId;
    }

    public void setRootChatId(String rootChatId) {
        this.rootChatId = rootChatId;
    }

    public void setRootFolderId(String rootFolderId) {
        this.rootFolderId = rootFolderId;
    }

    public UserNoteList getAllNotes() {
        return allNotes;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public ArrayList<String> getChatIds() {
        return chatIds;
    }

    public void setChatIds(ArrayList<String> chatIds) {
        this.chatIds = chatIds;
    }

    public ArrayList<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(ArrayList<String> fileIds) {
        this.fileIds = fileIds;
    }

    public ArrayList<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(ArrayList<String> memberIds) {
        this.memberIds = memberIds;
    }

    public ArrayList<LabelObject> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<LabelObject> labels) {
        this.labels = labels;
    }

    public ArrayList<UserNoteList> getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(ArrayList<UserNoteList> userNotes) {
        this.userNotes = userNotes;
    }

    public void setAllNotes(UserNoteList allNotes) {
        this.allNotes = allNotes;
    }
}
