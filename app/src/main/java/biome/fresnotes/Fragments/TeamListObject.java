package biome.fresnotes.Fragments;

/**
 * Created by Drew McDonald on 6/28/2017.
 */

public class TeamListObject {
    String name;
    String description;
    long chats;
    long projects;
    long members;
    String id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getChats() {
        return chats;
    }

    public void setChats(long chats) {
        this.chats = chats;
    }

    public long getProjects() {
        return projects;
    }

    public void setProjects(long projects) {
        this.projects = projects;
    }

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }
}
