package biome.fresco;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * Created by Drew McDonald on 1/9/2017.
 */

public class CreateGroupChatDialog extends DialogFragment{


    private TextView fromGroup;
    private TextView fromProject;
    private TextView fromScratch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.group_chat_type,container);

        fromGroup = (TextView)view.findViewById(R.id.from_group);
        fromProject = (TextView)view.findViewById(R.id.from_project);
        fromScratch = (TextView)view.findViewById(R.id.from_scratch);



        return view;
    }
}
