package biome.fresco;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import biome.fresco.Fragments.ProjectNotes;
import biome.fresco.Objects.MilestoneObject;
import biome.fresco.Objects.NoteObject;
import biome.fresco.Objects.TaskObject;

import static biome.fresco.MainActivity.mAuth;
import static biome.fresco.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AgendaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    String mId;
    List<MilestoneObject> mileStones;
    private OnFragmentInteractionListener mListener;

    long totalTasks;
    long totalMileStones;

    MilestoneListADapter mAdapter;
    RecyclerView mRecyclerview;

    public AgendaFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AgendaFragment newInstance() {
        AgendaFragment fragment = new AgendaFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = mAuth.getCurrentUser().getUid();
        mileStones = new ArrayList<>();

        mAdapter = new MilestoneListADapter(mileStones, getContext());



        mDatabase.child("agenda").child(((ProjectDetailActivity)getActivity()).projectId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalTasks = 0;
                totalMileStones = 0;
                mileStones.clear();

                for (final DataSnapshot milestoneSnap : dataSnapshot.child("milestones").getChildren()) {
                    totalMileStones ++;
                    MilestoneObject milestone = new MilestoneObject();
                    milestone.setAuthor((String)milestoneSnap.child("author").getValue());
                    milestone.setDescription((String)milestoneSnap.child("desc").getValue());
                    milestone.setDueDate((String)milestoneSnap.child("dueDate").getValue());
                    milestone.setName((String)milestoneSnap.child("name").getValue());
                    milestone.setCreatedTimestamp((Long)milestoneSnap.child("created").getValue());
                    milestone.setLastEditedTimestamp((Long)milestoneSnap.child("editedTime").getValue());
                    List<TaskObject> taskObjects = new ArrayList<TaskObject>();
                    List<String> taskKeys = new ArrayList<String>();
                    for (DataSnapshot keySnap : milestoneSnap.child("tasks").getChildren()){
                        taskKeys.add(keySnap.getKey());
                    }
                    long childCount = milestoneSnap.child("tasks").getChildrenCount();
                    long counter = 0;

                    for (String string: taskKeys ){
                        totalTasks++;
                        DataSnapshot taskSnap = dataSnapshot.child("tasks").child(string);

                        TaskObject task = new TaskObject();
                        task.setMilestoneId(milestoneSnap.getKey());
                        task.setAuthor((String)taskSnap.child("author").getValue());
                        task.setId(taskSnap.getKey());
                        task.setName((String)taskSnap.child("name").getValue());
                        task.setCompleted((boolean)taskSnap.child("completed").getValue());
                        try {
                            task.setCompletedTimestamp((long) taskSnap.child("completedChangedTimestamp").getValue());
                        }catch (Exception e){
                            task.setCreatedTimestamp(new Date().getTime());
                        }
                        task.setCreatedTimestamp((long)taskSnap.child("created").getValue());
                        taskObjects.add(task);

//                        counter ++;
//                        if (counter == childCount){
//
//                        }

                    }
                    milestone.setTasks(taskObjects);
                    mileStones.add(milestone);
                    mAdapter.notifyDataSetChanged();
                }
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
        View view =  inflater.inflate(R.layout.fragment_agenda, container, false);

        mRecyclerview = (RecyclerView) view.findViewById(R.id.milestone_recycler);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setAdapter(mAdapter);

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

    public class MilestoneListADapter extends UltimateViewAdapter<MilestoneHolder> {

        public List<MilestoneObject> mMilestones;
        Context mContext;
        public MilestoneListADapter(List<MilestoneObject> stones, Context context){
            mMilestones = stones;
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public MilestoneHolder newHeaderHolder(View view) {
            return null;
        }

        @Override
        public void onBindViewHolder(MilestoneHolder holder, int position, List<Object> payloads) {
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
        public MilestoneHolder newFooterHolder(View view) {
            return null;
        }

        @Override
        public int getAdapterItemCount() {
            return 0;
        }

        @Override
        public void onBindViewHolder(final MilestoneHolder holder, final int position) {


            holder.bindProject(mMilestones.get(position), getContext());

            int completed = 0;
            int total = mMilestones.get(position).getTasks().size();


            for (TaskObject task: mMilestones.get(position).getTasks()){
                if (task.isCompleted()){
                    completed ++;
                }
            }

            if (total == completed){
                holder.entireNoteLayout.setBackgroundColor(getResources().getColor(R.color.completed));
            }

//            Picasso.with(mContext)
//                    .load(mMilestones.get(position).getAuthorPhotoUrl())
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
//            holder.authorName.setText(mMilestones.get(position).getAuthorName());

        }

        @Override
        public int getItemCount() {
            return mMilestones.size();
        }

        @Override
        public MilestoneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.list_item_milestone, parent, false);
            return new MilestoneHolder(view);
        }

        @Override
        public MilestoneHolder onCreateViewHolder(ViewGroup parent) {
            return null;
        }
    }


    public static class MilestoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SourceSansRegularTextView milestoneTitle;
        SourceSansRegularTextView milestoneDescription;
        SourceSansRegularTextView daysLeftText;
        SourceSansRegularTextView taskCount;
        LinearLayout taskLayout;
        public View entireNoteLayout;
        boolean isExpanded;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        public MilestoneHolder(View itemView){
            super(itemView);
            isExpanded = false;
            milestoneTitle = (SourceSansRegularTextView) itemView.findViewById(R.id.milestone_title);
            milestoneDescription = (SourceSansRegularTextView) itemView.findViewById(R.id.milestone_description);
            taskCount = (SourceSansRegularTextView) itemView.findViewById(R.id.task_count);
            daysLeftText =(SourceSansRegularTextView) itemView.findViewById(R.id.days_left);
            entireNoteLayout = itemView.findViewById(R.id.entire_milestone);
            taskLayout = (LinearLayout) itemView.findViewById(R.id.task_list);

        }

        @Override
        public void onClick(View view) {

        }

        public void bindProject(final MilestoneObject milestoneObject, final Context context){

            milestoneTitle.setText(milestoneObject.getName());
            milestoneDescription.setText(milestoneObject.getDescription());


            CalendarDay today = CalendarDay.from(new Date());
            Date dueDate;
            try {
                dueDate = dateFormat.parse(milestoneObject.getDueDate());
            }catch (Exception e){
                dueDate = new Date();
            }
            CalendarDay dueDay = CalendarDay.from(dueDate);

            long msDiff = today.getDate().getTime() - dueDay.getDate().getTime();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);




            int completed = 0;
            int total = milestoneObject.getTasks().size();


            for (TaskObject task: milestoneObject.getTasks()){
                if (task.isCompleted()){
                    completed ++;
                }
            }

            if (total == completed){
                daysLeftText.setText("Completed");
            }else if (today.isAfter(dueDay)) {
                daysLeftText.setText(Long.toString(daysDiff) + " days left");
            }else if((today.equals(dueDay))) {
                daysLeftText.setText("Due today");
            }
            else {
                daysLeftText.setTextColor(context.getResources().getColor(R.color.errorColor));
                daysLeftText.setText("Overdue");
            }


            taskCount.setText(completed + "/" + total);

            final long height = taskLayout.getHeight();
            taskLayout.getLayoutParams().height = 0;

            entireNoteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
//            noteName.setText(note.getTitle());
//            responseCount.setText(Integer.toString(note.getResponses().size()));
//            noteContent.setText(Html.fromHtml(note.getContent()));
//
//
//            timeDateStamp.setText(timeFormat.format(new Date(note.getLastEdited())));



        }
    }

}
