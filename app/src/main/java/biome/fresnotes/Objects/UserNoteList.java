package biome.fresnotes.Objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Drew McDonald on 3/29/2017.
 */

public class UserNoteList {

    private String userId;
    private ArrayList<String> allNotes;
    private HashMap<String, ArrayList<String>> labelIdCOMMANotesForThatLabel;

    public void setAllNotes(ArrayList<String> allNotes) {
        this.allNotes = allNotes;
    }

    public ArrayList<String> getAllNotes() {
        return allNotes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, ArrayList<String>> getLabelIdCOMMANotesForThatLabel() {
        return labelIdCOMMANotesForThatLabel;
    }

    public void setLabelIdCOMMANotesForThatLabel(HashMap<String, ArrayList<String>> labelIdCOMMANotesForThatLabel) {
        this.labelIdCOMMANotesForThatLabel = labelIdCOMMANotesForThatLabel;
    }
}
