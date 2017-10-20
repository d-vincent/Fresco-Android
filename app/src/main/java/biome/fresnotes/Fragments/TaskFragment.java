package biome.fresnotes.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import biome.fresnotes.Objects.TaskObject;
import biome.fresnotes.R;

import static biome.fresnotes.MainActivity.mAuth;
import static biome.fresnotes.MainActivity.mDatabase;
import static biome.fresnotes.ProjectDetailActivity.mProject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    public String milestoneId;
    EditText taskName;

    List<TaskObject>  tasks;

    RecyclerView mRecyclerView;
    TaskListAdapter mAdapter;

    ImageView addTask;

    private OnFragmentInteractionListener mListener;

    public TaskFragment() {
        // Required empty public constructor
    }


    public static TaskFragment newInstance(String milestoneId) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, milestoneId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            milestoneId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tasks = new ArrayList<>();
        View view =  inflater.inflate(R.layout.fragment_task, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.task_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TaskListAdapter(tasks, getContext());
        mRecyclerView.setAdapter(mAdapter);


        mDatabase.child("agenda").child(mProject.getProjectId()).child("milestones").child(milestoneId).child("tasks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mDatabase.child("agenda").child(mProject.getProjectId()).child("tasks").child((String)dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String key = dataSnapshot.getKey();
                        boolean newOne = true;
                        TaskObject taskObject = new TaskObject();
                        for (TaskObject task:tasks){
                            if (task.getId().equals(key)){
                                newOne = false;
                                taskObject = task;
                            }
                        }


                        if (newOne) {
                            TaskObject task = new TaskObject();
                            task.setAuthor((String) dataSnapshot.child("author").getValue());
                            task.setName((String) dataSnapshot.child("name").getValue());
                            task.setCompleted((boolean) dataSnapshot.child("completed").getValue());
                            task.setId(dataSnapshot.getKey());
                            task.setMilestoneId(milestoneId);

                            tasks.add(task);
                            mAdapter.notifyDataSetChanged();
                        }else {

//                            tasks.remove(taskObject);
//                            TaskObject task = new TaskObject();
//                            task.setAuthor((String) dataSnapshot.child("author").getValue());
//                            task.setName((String) dataSnapshot.child("name").getValue());
//                            task.setCompleted((boolean) dataSnapshot.child("completed").getValue());
//                            task.setId(dataSnapshot.getKey());
//                            task.setMilestoneId(milestoneId);
//
//                            tasks.add(task);
//                            mAdapter.notifyDataSetChanged();

                        }

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

        taskName = (EditText)view.findViewById(R.id.task_name);
        addTask = (ImageView) view.findViewById(R.id.add_task);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskName.getText().toString().length() > 0){
                    HashMap<String, Object> taskValue = new HashMap<String, Object>();
                    taskValue.put("author", mAuth.getCurrentUser().getUid());
                    taskValue.put("completed", false);
                    taskValue.put("completedChangedTimestamp", System.currentTimeMillis());
                    taskValue.put("created", System.currentTimeMillis());
                    taskValue.put("name", taskName.getText().toString());
                    taskValue.put("milestone", milestoneId);

                    String taskKey = mDatabase.child("agendas").child(mProject.getProjectId()).child("tasks").push().getKey();
                    mDatabase.child("agenda").child(mProject.getProjectId()).child("tasks").child(taskKey).setValue(taskValue);
                    mDatabase.child("agenda").child(mProject.getProjectId()).child("milestones").child(milestoneId).child("tasks").child(taskKey).setValue(true);

                    taskName.setText("");
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

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


    public class TaskListAdapter extends UltimateViewAdapter<TaskHolder> {

        public List<TaskObject> mTasks;
        Context mContext;
        public TaskListAdapter(List<TaskObject> tasks, Context context){
            mTasks = tasks;
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public TaskHolder newHeaderHolder(View view) {
            return null;
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position, List<Object> payloads) {
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
        public TaskHolder newFooterHolder(View view) {
            return null;
        }

        @Override
        public int getAdapterItemCount() {
            return 0;
        }

        @Override
        public void onBindViewHolder(final TaskHolder holder, final int position) {
            holder.bindTask(mTasks.get(position), getContext(), milestoneId);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view = layoutInflater.inflate(R.layout.list_item_task, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public TaskHolder onCreateViewHolder(ViewGroup parent) {
            return null;
        }
    }


    public static class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView taskName;
        CheckBox box;



        public TaskHolder(View itemView){
            super(itemView);
            taskName = (TextView)itemView.findViewById(R.id.task_title);
            box = (CheckBox)itemView.findViewById(R.id.task_checkbox);


        }

        @Override
        public void onClick(View view) {

        }

        public void bindTask(final TaskObject taskObject, final Context context, final String milestoneId){

            taskName.setText(taskObject.getName());
            box.setChecked(taskObject.isCompleted());
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    mDatabase.child("agenda").child(mProject.getProjectId()).child("milestones").child(milestoneId).child("editedTime").setValue(System.currentTimeMillis());
                    mDatabase.child("agenda").child(mProject.getProjectId()).child("tasks").child(taskObject.getId()).child("completed").setValue(isChecked);
                        mDatabase.child("agenda").child(mProject.getProjectId()).child("tasks").child(taskObject.getId()).child("completedChangedTimestamp").setValue(System.currentTimeMillis());
                }
            });

        }
    }


}
