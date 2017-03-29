package biome.fresco;

import java.util.ArrayList;

/**
 * Created by Drew McDonald on 3/29/2017.
 */

public class ProjectObject {

    private String projectId;
    private String authorId;
    private ArrayList<String> chatIds;
    private String projectDescription;
    private ArrayList<String> fileIds;
    private ArrayList<LabelObject> labels;
    private ArrayList<String> memberIds;
    private String name;
    private ArrayList<UserNoteList> userNotes;
    private UserNoteList allNotes;
    //TODO create project note user object thing
    //TODO for public notes as well
    private String rootChatId;
    private String rootFolderId;
    private String searchName;

}
