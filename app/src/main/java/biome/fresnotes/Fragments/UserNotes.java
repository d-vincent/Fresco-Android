package biome.fresnotes.Fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import biome.fresnotes.BackAwareEditText;
import biome.fresnotes.CustomTextViewLogo;
import biome.fresnotes.MainActivity;
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
    public LabelAdapter mLabelAdapter;

    List<LabelObject> activeLabels;
    List<LabelObject> mLabels;

    ColorPickerDialog dialog;
    String selectedColorHex;


    // TODO: Rename and change types of parameters

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

        selectedColorHex = String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.colorPrimary)));

        dialog = ColorPickerDialog.newBuilder().setColor(getResources().getColor(R.color.colorPrimary)).create();
        dialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                selectedColorHex = String.format("#%06X", (0xFFFFFF & color));
                mLabelAdapter.notifyDataSetChanged();
                //holder.colorPicker.setBackgroundColor(color);
            }

            @Override
            public void onDialogDismissed(int dialogId) {


            }
        });

        //todo it's trying to get this too fast and crashing yo
        mLabels = ((MainActivity)getActivity()).labels;

        LabelObject newLabel = new LabelObject();
        newLabel.setLabelName("New Label");

        mLabels.add(newLabel);



        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("notes").addChildEventListener(new ChildEventListener() {
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
            if(position == 0){
                holder.labelName.setTextColor(getResources().getColor(R.color.white));
            }
            else{
                int labelColor = Color.parseColor(mLabels.get(position).getColorhex());
                holder.labelName.setTextColor(labelColor);
            }
            if (position == 0 && !holder.isOpen){
                holder.entireViewWithBorder.setBackground(getResources().getDrawable(R.drawable.new_label_background));

            }
            else {
                holder.entireViewWithBorder.setBackground(getResources().getDrawable(R.drawable.label_corners));
            }

            holder.labelName.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {


                    if (position != 0) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                            if (selectedItems.get(position, false)) {
                                selectedItems.delete(position);
                                holder.entireViewWithBorder.setSelected(false);

                                activeLabels.remove(mLabels.get(position));
                                if (activeLabels.size() > 0) {

                                    List<NoteObject> newNoteList = new ArrayList<NoteObject>();
                                    for (NoteObject noteObject : mNotes) {
                                        for (LabelObject labelObject : activeLabels) {
                                            if (labelObject.getIdsForNotesWithThisLabel().contains(noteObject.getId()) && !newNoteList.contains(noteObject)) {
                                                newNoteList.add(noteObject);

                                            }
                                        }
                                    }
                                    mAdapter.mNotes = newNoteList;

                                } else {
                                    mAdapter.mNotes = mNotes;
                                }
                                mAdapter.notifyDataSetChanged();

                            } else {
                                selectedItems.put(position, true);
                                holder.entireViewWithBorder.setSelected(true);

                                activeLabels.add(mLabels.get(position));
                                List<NoteObject> newNoteList = new ArrayList<NoteObject>();
                                for (NoteObject noteObject : mNotes) {
                                    for (LabelObject labelObject : activeLabels) {
                                        try {
                                            if (labelObject.getIdsForNotesWithThisLabel().contains(noteObject.getId()) && !newNoteList.contains(noteObject)) {
                                                newNoteList.add(noteObject);

                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                }
                                mAdapter.mNotes = newNoteList;
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }else {

                        ValueAnimator va = ValueAnimator.ofFloat(0f, 200f);
                        int mDuration = 500; //in millis
                        va.setDuration(mDuration);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                Float value = (Float) animation.getAnimatedValue();
                                holder.newLabelName.getLayoutParams().width = value.intValue();
                                holder.newLabelName.requestLayout();

                            }
                        });

                        holder.entireViewWithBorder.setBackground(getResources().getDrawable(R.drawable.label_corners));
                        holder.labelName.setVisibility(View.INVISIBLE);
                        holder.newLabelName.setVisibility(View.VISIBLE);
                        //va.start();
                        holder.createNewLabel.setVisibility(View.VISIBLE);
                        holder.cancelLabel.setVisibility(View.VISIBLE);
                        holder.isOpen = true;
                        holder.colorPicker.setVisibility(View.VISIBLE);
                        holder.newLabelName.setText("");
                        //User wants to make a new label
                    }


                    return true;
                }
            });

            int parseColor = Color.parseColor(selectedColorHex);
            holder.colorPicker.setBackgroundColor(parseColor);

            holder.colorPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.newLabelName.clearFocus();
                    dialog.show(getActivity().getFragmentManager(),"");
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
            });

            holder.createNewLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.createNewLabel.setVisibility(view.GONE);
                    holder.newLabelName.setVisibility(view.GONE);
                    holder.labelName.setVisibility(View.VISIBLE);
                    holder.entireViewWithBorder.setBackground(getResources().getDrawable(R.drawable.new_label_background));
                    holder.cancelLabel.setVisibility(View.GONE);
                    holder.colorPicker.setVisibility(View.GONE);
                    holder.isOpen = false;

                    HashMap<String, Object> labelMap = new HashMap<>();
                    labelMap.put("color", selectedColorHex);
                    labelMap.put("name", holder.newLabelName.getText().toString());

                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("labels").push().setValue(labelMap);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            });

            holder.cancelLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.createNewLabel.setVisibility(view.GONE);
                    holder.newLabelName.setVisibility(view.GONE);
                    holder.labelName.setVisibility(View.VISIBLE);
                    holder.entireViewWithBorder.setBackground(getResources().getDrawable(R.drawable.new_label_background));
                    holder.cancelLabel.setVisibility(View.GONE);
                    holder.colorPicker.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    holder.isOpen = false;
                }
            });


            holder.labelName.setText(mLabels.get(position).getLabelName());
            holder.newLabelName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if (b) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            });
            holder.newLabelName.setBackPressedListener(new BackAwareEditText.BackPressedListener() {
                @Override
                public void onImeBack(BackAwareEditText editText) {
                    holder.newLabelName.clearFocus();
                }
            });


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
            public BackAwareEditText newLabelName;
            public ImageView createNewLabel;
            public ImageView cancelLabel;
            public View colorPicker;
            public boolean isOpen = false;


            public LabelHolder(View itemView){
                super(itemView);
                labelName = (CustomTextViewLogo)itemView.findViewById(R.id.label_name);
                entireViewWithBorder = itemView.findViewById(R.id.entire_label);
                newLabelName = (BackAwareEditText)itemView.findViewById(R.id.new_label_name);
                createNewLabel = (ImageView)itemView.findViewById(R.id.create_label);
                cancelLabel = (ImageView)itemView.findViewById(R.id.cancel_label);
                colorPicker = itemView.findViewById(R.id.color_picker);


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
