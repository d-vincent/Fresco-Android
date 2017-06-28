package biome.fresco;

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

import biome.fresco.Fragments.ProjectNotes;
import biome.fresco.Fragments.TeamFragment;
import biome.fresco.Fragments.TeamListObject;
import biome.fresco.Objects.NoteObject;

/**
 * Created by Drew McDonald on 6/23/2017.
 */

public class TeamsListAdapter extends UltimateViewAdapter<TeamFragment.TeamHolder> {

    public List<TeamListObject> mTeams;
    Context mContext;
    public TeamsListAdapter(List<TeamListObject> teams, Context context){
        mTeams = teams;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public TeamFragment.TeamHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(TeamFragment.TeamHolder holder, int position, List<Object> payloads) {
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
    public TeamFragment.TeamHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(final TeamFragment.TeamHolder holder, final int position) {
        holder.bindProject(mTeams.get(position));
//
//        holder.entireNoteLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//
//                }
//                return true;
//            }
//        });

//        Picasso.with(mContext)
//                .load(mTeams.get(position).getAuthorPhotoUrl())
////                    .placeholder(R.mipmap.contact)
//                .into(holder.authorImage, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });

//        holder.authorName.setText(mTeams.get(position).getAuthorName());

    }

    @Override
    public int getItemCount() {
        return mTeams.size();
    }

    @Override
    public TeamFragment.TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_team, parent, false);
        return new TeamFragment.TeamHolder(view);
    }

    @Override
    public TeamFragment.TeamHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }
}
