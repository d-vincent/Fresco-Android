package biome.fresco;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import biome.fresco.Fragments.NoteDetailView;
import biome.fresco.Fragments.ProjectNotes;
import biome.fresco.Objects.NoteObject;

/**
 * Created by Drew McDonald on 6/23/2017.
 */

public class NoteListAdapter extends UltimateViewAdapter<ProjectNotes.NoteHolder> {

    public List<NoteObject> mNotes;
    Context mContext;
    public NoteListAdapter(List<NoteObject> notes, Context context){
        mNotes = notes;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public ProjectNotes.NoteHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(ProjectNotes.NoteHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public ProjectNotes.NoteHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(final ProjectNotes.NoteHolder holder, final int position) {
        holder.bindProject(mNotes.get(position));

        holder.entireNoteLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    NoteDetailView fragment = NoteDetailView.newInstance(mNotes.get(position).getContent(), mNotes.get(position).getTitle());

                    android.support.v4.app.FragmentManager fm = ((ProjectDetailActivity)mContext).getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.root_layout, fragment ).addToBackStack(null).commit();

                }
                return true;
            }
        });

        Picasso.with(mContext)
                .load(mNotes.get(position).getAuthorPhotoUrl())
//                    .placeholder(R.mipmap.contact)
                .transform(new RoundedCornerTransformation())
                .into(holder.authorImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.authorName.setText(mNotes.get(position).getAuthorName());




    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public ProjectNotes.NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);

        return new ProjectNotes.NoteHolder(view);
    }

    @Override
    public ProjectNotes.NoteHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }
}
