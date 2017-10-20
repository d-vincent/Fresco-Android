package biome.fresnotes.Fragments;

import android.support.annotation.NonNull;

/**
 * Created by Drew McDonald on 6/23/2017.
 */

public class FolderForList implements Comparable<FolderForList> {
    //type 0 = folder
    //type 1 = file

    String name;
    String id;
    int type;
    String fileUrl;
    String fileType;
    long timeStamp;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int compareTo(@NonNull FolderForList folderForList) {
        if(this.getType() < folderForList.getType()){
            return -1;
        }else if (this.getType() > folderForList.getType()){
            return 1;
        }
        else return this.getName().compareTo(folderForList.getName());
    }
}
