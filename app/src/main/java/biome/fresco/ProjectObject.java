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

}
