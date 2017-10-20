package biome.fresnotes.Objects;

import java.util.ArrayList;

/**
 * Created by Drew McDonald on 3/29/2017.
 */

public class LabelObject {


    private String id;
    private String colorhex;
    private String labelName;
    private ArrayList<String> idsForNotesWithThisLabel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColorhex() {
        return colorhex;
    }

    public void setColorhex(String colorhex) {
        this.colorhex = colorhex;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public ArrayList<String> getIdsForNotesWithThisLabel() {
        return idsForNotesWithThisLabel;
    }

    public void setIdsForNotesWithThisLabel(ArrayList<String> idsForNotesWithThisLabel) {
        this.idsForNotesWithThisLabel = idsForNotesWithThisLabel;
    }
}
