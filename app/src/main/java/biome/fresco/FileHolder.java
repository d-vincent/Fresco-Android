package biome.fresco;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import biome.fresco.Fragments.FolderForList;
import biome.fresco.Objects.NoteObject;

/**
 * Created by Drew McDonald on 6/23/2017.
 */

public class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public CustomTextViewLogo fileName;
    //public TextView timeDateStamp;
    public View entireNoteLayout;
    View headerView;
    SourceSansRegularTextView headerText;
    ImageView filePreview;
    SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd h:mm a");


    public FileHolder(View itemView){
        super(itemView);
        fileName = (CustomTextViewLogo)itemView.findViewById(R.id.file_name);
        //timeDateStamp = (TextView)itemView.findViewById(R.id.file_timestamp);
        entireNoteLayout = itemView.findViewById(R.id.entire_file_layout);
        filePreview = (ImageView)itemView.findViewById(R.id.file_preview);
        headerView = itemView.findViewById(R.id.header_layout);
        headerText = (SourceSansRegularTextView) itemView.findViewById(R.id.header_text);
    }

    @Override
    public void onClick(View view) {


    }

    public void bindProject(FolderForList thing){

        fileName.setText(thing.getName());
        //timeDateStamp.setText(timeFormat.format(new Date(thing.getTimeStamp())));

    }
}
