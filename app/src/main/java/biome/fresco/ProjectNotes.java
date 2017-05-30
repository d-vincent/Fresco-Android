package biome.fresco;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static biome.fresco.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectNotes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectNotes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectNotes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RecyclerView mRecyclerView;
    List<NoteObject> mNotes;
    NoteListAdapter mAdapter;

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public ProjectNotes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectNotes.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectNotes newInstance() {
        ProjectNotes fragment = new ProjectNotes();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        mNotes = new ArrayList<>();

        mDatabase.child("projects").child(ProjectDetailActivity.mProject.getProjectId()).child("notes").child("publicNotes").child("allNotes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        View view =  inflater.inflate(R.layout.fragment_project_notes, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.public_note_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new NoteListAdapter(mNotes);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CustomTextViewLogo noteName;
        private TextView noteContent;
        private TextView responseCount;
        private ImageView authorImage;
        private TextView authorName;
        private TextView timeDateStamp;
        private View entireNoteLayout;
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");


        public NoteHolder(View itemView){
            super(itemView);
            noteName = (CustomTextViewLogo)itemView.findViewById(R.id.note_title);
            responseCount = (TextView) itemView.findViewById(R.id.response_count);
            authorImage = (ImageView) itemView.findViewById(R.id.author_profile_image);
            authorName =(TextView) itemView.findViewById(R.id.author_name);
            timeDateStamp = (TextView)itemView.findViewById(R.id.note_timestamp);
            entireNoteLayout = itemView.findViewById(R.id.entire_note_layout);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindProject(NoteObject note){

            noteName.setText(note.getTitle());
            responseCount.setText(note.getResponses().size());
            timeDateStamp.setText(timeFormat.format(new Date(note.getTimestamp())));



        }
    }

    private class NoteListAdapter extends RecyclerView.Adapter<NoteHolder>{

        public List<NoteObject> mNotes;
        public NoteListAdapter(List<NoteObject> notes){
            mNotes = notes;
        }

        @Override
        public void onBindViewHolder(final NoteHolder holder, final int position) {
            holder.bindProject(mNotes.get(position));

            holder.entireNoteLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    }
                    return true;
                }


            });
            mDatabase.child("users").child(mNotes.get(position).getAuthorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Picasso.with(getContext())
                            .load((String)dataSnapshot.child("photoUrl").getValue())
//                    .placeholder(R.mipmap.contact)
                            .into(holder.authorImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                    holder.authorName.setText((String)dataSnapshot.child("name").getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
            return new NoteHolder(view);
        }
    }

}
