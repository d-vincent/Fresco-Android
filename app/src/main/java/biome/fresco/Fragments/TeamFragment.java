package biome.fresco.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biome.fresco.CustomTextViewLogo;
import biome.fresco.Objects.NoteObject;
import biome.fresco.R;
import biome.fresco.SourceSansRegularTextView;
import biome.fresco.TeamsListAdapter;

import static biome.fresco.MainActivity.mAuth;
import static biome.fresco.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RecyclerView mRecyclerView;
    String mId;
    List<TeamListObject> teams;

    TeamsListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public TeamFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        teams = new ArrayList<>();

        mId = mAuth.getCurrentUser().getUid();

        mDatabase.child("users").child(mId).child("teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String teamId = dataSnapshot.getKey();

                mDatabase.child("teams").child(teamId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long chatCount = dataSnapshot.child("chats").getChildrenCount();
                        long memberCount = dataSnapshot.child("members").getChildrenCount();
                        long projectCount = dataSnapshot.child("projects").getChildrenCount();
                        String name = (String)dataSnapshot.child("metadata").child("name").getValue();
                        String description = (String)dataSnapshot.child("metadata").child("desc").getValue();

                        TeamListObject team = new TeamListObject();
                        team.setChats(chatCount);
                        team.setMembers(memberCount);
                        team.setProjects(projectCount);
                        team.setName(name);
                        team.setDescription(description);
                        teams.add(team);

                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_teams, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.team_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new TeamsListAdapter(teams, getContext());
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class TeamHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CustomTextViewLogo teamName;
        public SourceSansRegularTextView teamDescription;
        public TextView memberCount;
        public TextView chatCount;
        public TextView projectCount;
        public View entireNoteLayout;
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd h:mm a");


        public TeamHolder(View itemView){
            super(itemView);
            teamName = (CustomTextViewLogo)itemView.findViewById(R.id.team_name);
            teamDescription = (SourceSansRegularTextView) itemView.findViewById(R.id.team_description);

            memberCount = (TextView)itemView.findViewById(R.id.member_count);
            entireNoteLayout = itemView.findViewById(R.id.team_layout);
            chatCount = (TextView)itemView.findViewById(R.id.chat_count);
            projectCount = (TextView)itemView.findViewById(R.id.project_count);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindProject(TeamListObject team){

            teamName.setText(team.getName());
            memberCount.setText(Long.toString(team.getMembers()));
            teamDescription.setText(team.getDescription());
            chatCount.setText(Long.toString(team.getChats()));
            projectCount.setText(Long.toString(team.getProjects()));

        }
    }

}
