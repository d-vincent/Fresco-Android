package biome.fresnotes.Objects;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by Drew McDonald on 1/6/2017.
 */

public class SimpleUser implements IUser {
    String id;
    String username;
    String photoUrl;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getAvatar() {
        return photoUrl;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public SimpleUser(String id, String username, String photoUrl){
        this.id = id;
        this.username = username;
        this.photoUrl = photoUrl;
    }
}
