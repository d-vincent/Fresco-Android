package biome.fresnotes.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import biome.fresnotes.CircleTransformation;
import biome.fresnotes.Objects.SimpleUser;
import biome.fresnotes.R;

import static biome.fresnotes.MainActivity.mAuth;
import static biome.fresnotes.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectUser extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView userRecycler;
    private LinearLayoutManager mLinearLayoutManager;
    private nameAdapter adapter;

    List<SimpleUser> userObjects;

    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener mListener;

    public SelectUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectUser.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectUser newInstance(String param1, String param2) {
        SelectUser fragment = new SelectUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        userObjects = new ArrayList<>();

        DatabaseReference users = mDatabase.child("users");
        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = (String)dataSnapshot.child("name").getValue();
                String id = dataSnapshot.getKey();
                String photoUrl = (String)dataSnapshot.child("photoUrl").getValue();
                SimpleUser user = new SimpleUser(id,name,photoUrl);
                userObjects.add(user);

                adapter.notifyDataSetChanged();


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
        View view =  inflater.inflate(R.layout.fragment_select_user, container, false);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        userRecycler = (RecyclerView)view.findViewById(R.id.users_recycler);
        userRecycler.setLayoutManager(mLinearLayoutManager);
        adapter = new nameAdapter(userObjects);
        userRecycler.setAdapter(adapter);

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

    private class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactName;
        private ImageView contactImage;
        public UserHolder(View itemView){
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.dm_name);
            contactImage = (ImageView)itemView.findViewById(R.id.contact_image);


        }

        @Override
        public void onClick(View view) {

        }

        public void bindName(String string){
            contactName.setText(string);

        }
    }

    private class nameAdapter extends RecyclerView.Adapter<UserHolder>{

        public List<SimpleUser> mUsers;
        public nameAdapter(List<SimpleUser> users){
            mUsers = users;
        }

        @Override
        public void onBindViewHolder(UserHolder holder, final int position) {
            holder.bindName(mUsers.get(position).getUsername());
            Picasso.with(getContext())
                    .load(mUsers.get(position).getPhotoUrl())
                    .transform(new CircleTransformation()).fit()
                    .centerCrop()
//                    .placeholder(R.mipmap.contact)
                    .into(holder.contactImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

            holder.contactName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("directMessages").child(mUsers.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                String id = (String)dataSnapshot.child("id").getValue();
                            if (id != null) {
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.container, DirectMessage.newInstance(id, mUsers.get(position).getPhotoUrl(), mUsers.get(position).getId(), mUsers.get(position).getUsername())).addToBackStack("").commit();
                            }
                            else {


                                HashMap<String, String> members = new HashMap<String, String>();
                                members.put(mAuth.getCurrentUser().getUid(), "true");
                                members.put(mUsers.get(position).getId(), "true");
                                HashMap<String, Object> main = new HashMap<String, Object>();
                                main.put("created", System.currentTimeMillis());
                                main.put("members", members);


                                DatabaseReference newChat = mDatabase.child("chats").push();
                                String chatId = newChat.getKey();
                                newChat.setValue(main);

                                HashMap<String, Object> thing = new HashMap<String, Object>();
                                thing.put("id", chatId);
                                thing.put("notified", true);
                                thing.put("unread", false);


                                HashMap<String, Object> otherThing = new HashMap<String, Object>();
                                otherThing.put("id", chatId);
                                otherThing.put("notified", false);
                                otherThing.put("unread", true);

                                String currentUserId = mAuth.getCurrentUser().getUid();
                                mDatabase.child("users").child(mUsers.get(position).getId()).child("directMessages").child(currentUserId).setValue(otherThing);
                                mDatabase.child("users").child(currentUserId).child("directMessages").child(mUsers.get(position).getId()).setValue(thing);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                fm.beginTransaction().replace(R.id.container, DirectMessage.newInstance(chatId, mUsers.get(position).getPhotoUrl(), mUsers.get(position).getId(), mUsers.get(position).getUsername())).addToBackStack("").commit();

                            }
                            }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    HashMap<String, String> members = new HashMap<String, String>();
//                    members.put(mAuth.getCurrentUser().getUid(),"true");
//                    members.put(mUsers.get(position).getId(), "true");
//                    HashMap<String, Object> main = new HashMap<String, Object>();
//                    main.put("created", System.currentTimeMillis());
//                    main.put("members",members);
//
//
//                    DatabaseReference newChat = mDatabase.child("directMessages").push();
//                    String chatId = newChat.getKey();
//                    newChat.setValue(main);
//
//                    HashMap<String, String> thing = new HashMap<String, String>();
//                    thing.put("id",chatId);
//                    thing.put("notified", "true");
//                    thing.put("unread","false");
//                    mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("directMessages").child(mUsers.get(position).getId()).setValue(thing);
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    fm.beginTransaction().replace(R.id.container, DirectMessage.newInstance(chatId, mUsers.get(position).getPhotoUrl(), mUsers.get(position).getId())).addToBackStack("").commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_direct_message, parent, false);
            return new UserHolder(view);
        }
    }



}
