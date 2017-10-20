package biome.fresnotes;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;


/**
 * Created by Drew on 11/15/2016.
 */

public class FirebaseInstanceHelper extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


      MainActivity.token = refreshedToken;
       // sendRegistrationToServer(refreshedToken);
    }



}
