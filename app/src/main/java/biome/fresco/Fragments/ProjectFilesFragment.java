package biome.fresco.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import biome.fresco.FileListAdapter;
import biome.fresco.Objects.FolderObject;
import biome.fresco.ProjectDetailActivity;
import biome.fresco.R;

import static biome.fresco.MainActivity.mDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectFilesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFilesFragment extends Fragment {

    FolderObject mFolder;
    List<FolderForList> mThings;

    UltimateRecyclerView mRecyclerview;
    FileListAdapter mAdapter;

    DatabaseReference mainRef;





    public ProjectFilesFragment() {
        // Required empty public constructor
    }


    public static ProjectFilesFragment newInstance() {
        ProjectFilesFragment fragment = new ProjectFilesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThings = new ArrayList<>();

        mainRef = mDatabase.child("folders").child(ProjectDetailActivity.mProject.getRootFolderId());
        mainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFolder = new FolderObject();
                mFolder.setAuthorId((String)dataSnapshot.child("author").getValue());
                mFolder.setName((String)dataSnapshot.child("name").getValue());
                mFolder.setProjectId((String)dataSnapshot.child("project").getValue());
                mFolder.setTimestamp((long)dataSnapshot.child("timestamp").getValue());
                mFolder.setRootFolder(true);

                List<String> fileIds = new ArrayList<String>();
                try{
                    for(DataSnapshot snap: dataSnapshot.child("files").getChildren()){
                        fileIds.add(snap.getKey());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                List<String> folderIds = new ArrayList<String>();
                try{
                    for(DataSnapshot snap: dataSnapshot.child("folders").getChildren()){
                        folderIds.add(snap.getKey());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                mFolder.setFileIds(fileIds);
                mFolder.setChildFolderIds(folderIds);

                if(folderIds.size() > 0){

                    for (String string: folderIds) {
                        mDatabase.child("folders").child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FolderForList thing = new FolderForList();
                                thing.setName((String)dataSnapshot.child("name").getValue());
                                thing.setType(0);
                                thing.setId(dataSnapshot.getKey());
                                thing.setTimeStamp((long)dataSnapshot.child("timestamp").getValue());

                                mThings.add(thing);
                                Collections.sort(mThings);

                                mAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                if(fileIds.size() > 0){

                    for (String string: fileIds) {
                        mDatabase.child("files").child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FolderForList thing = new FolderForList();
                                thing.setName((String)dataSnapshot.child("name").getValue());
                                thing.setType(1);
                                thing.setId(dataSnapshot.getKey());
                                thing.setFileType((String)dataSnapshot.child("type").getValue());
                                thing.setFileUrl((String)dataSnapshot.child("src").getValue());
                                thing.setTimeStamp((long)dataSnapshot.child("timestamp").getValue());

                                mThings.add(thing);
                                Collections.sort(mThings);

                                mAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
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
        View view =  inflater.inflate(R.layout.fragment_project_files, container, false);

        mRecyclerview = (UltimateRecyclerView)view.findViewById(R.id.files_recycler);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new FileListAdapter(mThings, getContext(), this);
        mRecyclerview.setAdapter(mAdapter);




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
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

    public void enterFolder(String folderId){

        mThings.clear();

        mainRef = mDatabase.child("folders").child(folderId);

        mainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFolder = new FolderObject();
                mFolder.setAuthorId((String)dataSnapshot.child("author").getValue());
                mFolder.setName((String)dataSnapshot.child("name").getValue());
                mFolder.setProjectId((String)dataSnapshot.child("project").getValue());
                mFolder.setTimestamp((long)dataSnapshot.child("timestamp").getValue());
                mFolder.setRootFolder(false);
                mFolder.setParentFolderId((String)dataSnapshot.child("folder").getValue());

                List<String> fileIds = new ArrayList<String>();
                try{
                    for(DataSnapshot snap: dataSnapshot.child("files").getChildren()){
                        fileIds.add(snap.getKey());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                List<String> folderIds = new ArrayList<String>();
                try{
                    for(DataSnapshot snap: dataSnapshot.child("folders").getChildren()){
                        folderIds.add(snap.getKey());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                mFolder.setFileIds(fileIds);
                mFolder.setChildFolderIds(folderIds);

                if(folderIds.size() > 0){

                    for (String string: folderIds) {
                        mDatabase.child("folders").child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FolderForList thing = new FolderForList();
                                thing.setName((String)dataSnapshot.child("name").getValue());
                                thing.setType(0);
                                thing.setId(dataSnapshot.getKey());
                                thing.setTimeStamp((long)dataSnapshot.child("timestamp").getValue());

                                mThings.add(thing);
                                Collections.sort(mThings);

                                mAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                if(fileIds.size() > 0){

                    for (String string: fileIds) {
                        mDatabase.child("files").child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FolderForList thing = new FolderForList();
                                thing.setName((String)dataSnapshot.child("name").getValue());
                                thing.setType(1);
                                thing.setId(dataSnapshot.getKey());
                                thing.setFileType((String)dataSnapshot.child("type").getValue());
                                thing.setFileUrl((String)dataSnapshot.child("src").getValue());
                                thing.setTimeStamp((long)dataSnapshot.child("timestamp").getValue());

                                mThings.add(thing);
                                Collections.sort(mThings);

                                mAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public boolean navigateUp(){
        if (mFolder.isRootFolder()){
            return false;
        }
        else {
            mThings.clear();

            mainRef = mDatabase.child("folders").child(mFolder.getParentFolderId());

            mainRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mFolder = new FolderObject();
                    mFolder.setAuthorId((String)dataSnapshot.child("author").getValue());
                    mFolder.setName((String)dataSnapshot.child("name").getValue());
                    mFolder.setProjectId((String)dataSnapshot.child("project").getValue());
                    mFolder.setTimestamp((long)dataSnapshot.child("timestamp").getValue());
                    String isRoot = (String)dataSnapshot.child("type").getValue();
                    if (isRoot != null && isRoot.equals("root")){
                        mFolder.setRootFolder(true);
                    }
                    else {
                        mFolder.setRootFolder(false);
                    }
                    if (!mFolder.isRootFolder()){
                        mFolder.setParentFolderId((String)dataSnapshot.child("folder").getValue());
                    }

                    List<String> fileIds = new ArrayList<String>();
                    try{
                        for(DataSnapshot snap: dataSnapshot.child("files").getChildren()){
                            fileIds.add(snap.getKey());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    List<String> folderIds = new ArrayList<String>();
                    try{
                        for(DataSnapshot snap: dataSnapshot.child("folders").getChildren()){
                            folderIds.add(snap.getKey());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    mFolder.setFileIds(fileIds);
                    mFolder.setChildFolderIds(folderIds);

                    if(folderIds.size() > 0){

                        for (String string: folderIds) {
                            mDatabase.child("folders").child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FolderForList thing = new FolderForList();
                                    thing.setName((String)dataSnapshot.child("name").getValue());
                                    thing.setType(0);
                                    thing.setId(dataSnapshot.getKey());
                                    thing.setTimeStamp((long)dataSnapshot.child("timestamp").getValue());

                                    mThings.add(thing);
                                    Collections.sort(mThings);

                                    mAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    if(fileIds.size() > 0){

                        for (String string: fileIds) {
                            mDatabase.child("files").child(string).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    FolderForList thing = new FolderForList();
                                    thing.setName((String)dataSnapshot.child("name").getValue());
                                    thing.setType(1);
                                    thing.setId(dataSnapshot.getKey());
                                    thing.setFileType((String)dataSnapshot.child("type").getValue());
                                    thing.setFileUrl((String)dataSnapshot.child("src").getValue());
                                    thing.setTimeStamp((long)dataSnapshot.child("timestamp").getValue());

                                    mThings.add(thing);
                                    Collections.sort(mThings);

                                    mAdapter.notifyDataSetChanged();


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            return true;
        }
    }
}
