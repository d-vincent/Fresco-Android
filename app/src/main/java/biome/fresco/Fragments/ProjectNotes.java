package biome.fresco.Fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
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
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biome.fresco.CustomTextViewLogo;
import biome.fresco.NoteListAdapter;
import biome.fresco.Objects.LabelObject;
import biome.fresco.Objects.NoteObject;
import biome.fresco.ProjectDetailActivity;
import biome.fresco.R;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
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
    UltimateRecyclerView mRecyclerView;

    UltimateRecyclerView mLabelRecyclerView;
    LabelAdapter mLabelAdapter;

    List<LabelObject> mLabels;

    List<NoteObject> mNotes;
    List<NoteObject> mFilteredNotes;

    NoteListAdapter mAdapter;
    List<LabelObject> activeLabels;

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
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        activeLabels = new ArrayList<>();
        mNotes = new ArrayList<>();
        mLabels = ProjectDetailActivity.mProject.getLabels();

        mDatabase.child("projects").child(ProjectDetailActivity.mProject.getProjectId()).child("publicNotes").child("allNotes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                final String noteId = dataSnapshot.getKey();
                mDatabase.child("notes").child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final NoteObject note = new NoteObject();

                        note.setId(noteId);

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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_project_notes, container, false);

        mLabelRecyclerView = (UltimateRecyclerView)view.findViewById(R.id.label_recycler);
        mLabelRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        mLabelAdapter = new LabelAdapter(mLabels, getContext());
        mLabelRecyclerView.setAdapter(mLabelAdapter);
        mLabelAdapter.notifyDataSetChanged();

        mRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.public_note_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mAdapter = new NoteListAdapter(mNotes, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== SCROLL_STATE_IDLE){
                    ObjectAnimator anim = ObjectAnimator.ofFloat(  mLabelRecyclerView,"elevation",0);
                    anim.setDuration(250);
                    anim.start();
                }
                else{
                    ObjectAnimator anim = ObjectAnimator.ofFloat( mLabelRecyclerView,"elevation",40f);
                    anim.setDuration(250);
                    anim.start();
                }
            }
        });

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

    public static class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CustomTextViewLogo noteName;
        public TextView noteContent;
        public TextView responseCount;
        public ImageView authorImage;
        public TextView authorName;
        public TextView timeDateStamp;
        public View entireNoteLayout;
        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd h:mm a");


        public NoteHolder(View itemView){
            super(itemView);
            noteName = (CustomTextViewLogo)itemView.findViewById(R.id.note_title);
            responseCount = (TextView) itemView.findViewById(R.id.response_count);
            authorImage = (ImageView) itemView.findViewById(R.id.author_profile_image);
            authorName =(TextView) itemView.findViewById(R.id.author_name);
            timeDateStamp = (TextView)itemView.findViewById(R.id.note_timestamp);
            entireNoteLayout = itemView.findViewById(R.id.entire_note_layout);
            noteContent = (TextView)itemView.findViewById(R.id.note_content);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindProject(NoteObject note){

            noteName.setText(note.getTitle());
            responseCount.setText(Integer.toString(note.getResponses().size()));
            noteContent.setText(Html.fromHtml(note.getContent()));


            timeDateStamp.setText(timeFormat.format(new Date(note.getLastEdited())));

        }
    }



//    private class NoteListAdapter extends RecyclerView.Adapter<NoteHolder>{
//
//        public List<NoteObject> mNotes;
//        public NoteListAdapter(List<NoteObject> notes){
//            mNotes = notes;
//        }
//
//        @Override
//        public void onBindViewHolder(final NoteHolder holder, final int position) {
//            holder.bindProject(mNotes.get(position));
//
//            holder.entireNoteLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//
//                    }
//                    return true;
//                }
//            });
//
//            Picasso.with(getContext())
//                    .load(mNotes.get(position).getAuthorPhotoUrl())
////                    .placeholder(R.mipmap.contact)
//                    .into(holder.authorImage, new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
//
//            holder.authorName.setText(mNotes.get(position).getAuthorName());
//
//
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return mNotes.size();
//        }
//
//        @Override
//        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//            View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
//            return new NoteHolder(view);
//        }
//    }

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
