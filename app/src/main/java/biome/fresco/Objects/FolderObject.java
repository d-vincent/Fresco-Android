package biome.fresco.Objects;

import java.util.List;

/**
 * Created by Drew McDonald on 6/23/2017.
 */

public class FolderObject {

    boolean isRootFolder;
    String id;
    String name;
    String authorId;
    String authorName;
    String authorPhotoUrl;
    String parentFolderId;
    List<String> fileIds;
    List<String> childFolderIds;
    String projectId;
    long timestamp;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public String getAuthorPhotoUrl() {
        return authorPhotoUrl;
    }

    public void setAuthorPhotoUrl(String authorPhotoUrl) {
        this.authorPhotoUrl = authorPhotoUrl;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public List<String> getChildFolderIds() {
        return childFolderIds;
    }

    public void setChildFolderIds(List<String> childFolderIds) {
        this.childFolderIds = childFolderIds;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRootFolder() {
        return isRootFolder;
    }

    public void setRootFolder(boolean rootFolder) {
        isRootFolder = rootFolder;
    }
}
