package biome.fresnotes.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import biome.fresnotes.CustomTextViewLogo;
import biome.fresnotes.NoteListAdapter;
import biome.fresnotes.Objects.LabelObject;
import biome.fresnotes.Objects.NoteObject;
import biome.fresnotes.ProjectDetailActivity;
import biome.fresnotes.R;

import static biome.fresnotes.MainActivity.mAuth;
import static biome.fresnotes.MainActivity.mDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserNotes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserNotes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserNotes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    UltimateRecyclerView mRecyclerView;
    List<NoteObject> mNotes;
    NoteListAdapter mAdapter;

    UltimateRecyclerView mLabelRecyclerView;
    LabelAdapter mLabelAdapter;

    List<LabelObject> activeLabels;
    List<LabelObject> mLabels;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserNotes() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserNotes newInstance() {
        UserNotes fragment = new UserNotes();
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
        activeLabels = new ArrayList<>();
        //todo it's trying to get this too fast and crashing yo
        mLabels = ProjectDetailActivity.mProject.getLabels();


        mDatabase.child("projects").child(ProjectDetailActivity.mProject.getProjectId()).child("notes").child(mAuth.getCurrentUser().getUid()).child("allNotes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String noteId = dataSnapshot.getKey();
                mDatabase.child("notes").child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final NoteObject note = new NoteObject();

                        final String authorid = (String)dataSnapshot.child("author").getValue();
                        final String noteContent = (String)dataSnapshot.child("content").getValue();

                        try {
                            note.setPublic((boolean) dataSnapshot.child("public").getValue());
                        }catch (Exception e){
                            note.setPublic(true);
                        }

                        try {
                            note.setLastEdited ((long) dataSnapshot.child("timestamp").getValue());
                        }catch (Exception e){
                            note.setLastEdited(0);
                        }
                        final String title = (String)dataSnapshot.child("title").getValue();
                        final List<String> sharedWith = new ArrayList<String>();
                        final List<String> responses = new ArrayList<String>();


                        try {
                            for (DataSnapshot snap : dataSnapshot.child("sharedWith").getChildren()) {
                                sharedWith.add(snap.getKey());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        try {
                            for (DataSnapshot responseSnap : dataSnapshot.child("responses").getChildren()) {
                                responses.add(responseSnap.getKey());
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        mDatabase.child("users").child(authorid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String authorPhotoUrl = (String) dataSnapshot.child("photoUrl").getValue();
                                String authorName = (String)dataSnapshot.child("name").getValue();

                                note.setAuthorId(authorid);
                                note.setContent(noteContent);
                                note.setTitle(title);
                                note.setAuthorName(authorName);
                                note.setAuthorPhotoUrl(authorPhotoUrl);
                                note.setSharedWith(sharedWith);
                                note.setResponses(responses);

                                mNotes.add(note);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        View view =  inflater.inflate(R.layout.fragment_project_notes, container, false);

        mRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.public_note_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new NoteListAdapter(mNotes, getContext());
        mRecyclerView.setAdapter(mAdapter);

        mLabelRecyclerView = (UltimateRecyclerView)view.findViewById(R.id.label_recycler);
        mLabelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        mLabelAdapter = new LabelAdapter(mLabels, getContext());
        mLabelRecyclerView.setAdapter(mLabelAdapter);
        mLabelAdapter.notifyDataSetChanged();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
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

    public class LabelAdapter extends UltimateViewAdapter<LabelAdapter.LabelHolder> {

        private SparseBooleanArray selectedItems;



        public List<LabelObject> mLabels;
        Context mContext;
        public LabelAdapter(List<LabelObject> labels, Context context){
            mLabels = labels;
            selectedItems = new SparseBooleanArray();
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public LabelHolder newHeaderHolder(View view) {
            return null;
        }

        @Override
        public void onBindViewHolder(LabelHolder holder, int position, List<Object> payloads) {
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
        public LabelHolder newFooterHolder(View view) {
            return null;
        }

        @Override
        public int getAdapterItemCount() {
            return 0;
        }

        @Override
        public void onBindViewHolder(final LabelHolder holder, final int position) {
            holder.bindLabel(mLabels.get(position));

            holder.entireViewWithBorder.setSelected(selectedItems.get(position, false));

            holder.entireViewWithBorder.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                        if (selectedItems.get(position, false)) {
                            selectedItems.delete(position);
                            holder.entireViewWithBorder.setSelected(false);

                            activeLabels.remove(mLabels.get(position));
                            if (activeLabels.size() > 0){

                                List<NoteObject> newNoteList = new ArrayList<NoteObject>();
                                for (NoteObject noteObject: mNotes){
                                    for (LabelObject labelObject: activeLabels){
                                        if (labelObject.getIdsForNotesWithThisLabel().contains(noteObject.getId()) && !newNoteList.contains(noteObject)){
                                            newNoteList.add(noteObject);

                                        }
                                    }
                                }
                                mAdapter.mNotes = newNoteList;

                            }
                            else {
                                mAdapter.mNotes = mNotes;
                            }
                            mAdapter.notifyDataSetChanged();

                        } else {
                            selectedItems.put(position, true);
                            holder.entireViewWithBorder.setSelected(true);

                            activeLabels.add(mLabels.get(position));
                            List<NoteObject> newNoteList = new ArrayList<NoteObject>();
                            for (NoteObject noteObject: mNotes){
                                for (LabelObject labelObject: activeLabels){
                                    if (labelObject.getIdsForNotesWithThisLabel().contains(noteObject.getId()) && !newNoteList.contains(noteObject)){
                                        newNoteList.add(noteObject);

                                    }
                                }
                            }
                            mAdapter.mNotes = newNoteList;
                            mAdapter.notifyDataSetChanged();
                        }
                    }


                    return true;
                }
            });


            holder.labelName.setText(mLabels.get(position).getLabelName());




        }

        @Override
        public int getItemCount() {
            return mLabels.size();
        }

        @Override
        public LabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.list_item_label, parent, false);

            return new LabelHolder(view);
        }

        @Override
        public LabelHolder onCreateViewHolder(ViewGroup parent) {
            return null;
        }

        public class LabelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public CustomTextViewLogo labelName;
            public View entireViewWithBorder;


            public LabelHolder(View itemView){
                super(itemView);
                labelName = (CustomTextViewLogo)itemView.findViewById(R.id.label_name);
                entireViewWithBorder = itemView.findViewById(R.id.entire_label);

            }

            @Override
            public void onClick(View view) {


            }

            public void bindLabel(LabelObject label){

                labelName.setText(label.getLabelName());

            }
        }


    }

}
