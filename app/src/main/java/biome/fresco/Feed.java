package biome.fresco;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static biome.fresco.MainActivity.mAuth;
import static biome.fresco.MainActivity.mDatabase;
import static biome.fresco.MainActivity.token;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Feed.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Feed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Feed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private FloatingActionMenu mFab;
    com.github.clans.fab.FloatingActionButton fabDirectMessage;
    com.github.clans.fab.FloatingActionButton fabGroupChat;
    private List<ChatObject> chatObjects;

    View newConvo;

    private List<String> directUserImages;
    private HashMap<String,String> directMessages;
    private OnFragmentInteractionListener mListener;

    private RecyclerView dmRecycler;
    private nameAdapter mAdapter;

    public Feed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feed.
     */
    // TODO: Rename and change types and number of parameters
    public static Feed newInstance(String param1, String param2) {
        Feed fragment = new Feed();
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

        if (MainActivity.token != null){
            mDatabase.child("users").child(MainActivity.uid).child("tokens").child("android").child(token).setValue("true");
        }
      //  userChatNames = new ArrayList<>();

        directMessages = new HashMap<>();
        directUserImages = new ArrayList<>();

        chatObjects = new ArrayList<>();

        String mUID = mAuth.getCurrentUser().getUid();

//       final DatabaseReference chatNames =  mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("chats");
//        chatNames.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                userChatNames.clear();
//                for(DataSnapshot snap : dataSnapshot.getChildren()){
//                    //snap.getKey();
//                    userChatNames.add(snap.getKey());
//               //     Toast.makeText(getContext(), snap.getKey(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        final DatabaseReference directChats = mDatabase.child("users").child(mUID).child("directMessages");
        directChats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String toUserId = dataSnapshot.getKey();
                String roomId = (String)dataSnapshot.child("id").getValue();
                boolean notified = (boolean)dataSnapshot.child("notified").getValue();
                boolean unread = (boolean)dataSnapshot.child("unread").getValue();

                final ChatObject chat = new ChatObject(roomId, toUserId, notified, unread);

                mDatabase.child("users").child(chat.getToUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        chat.setToUserName((String)dataSnapshot.child("name").getValue());
                        chat.setToUserImageUrl((String)dataSnapshot.child("photoUrl").getValue());
                        chatObjects.add(chat);
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

//        mDatabase.child("users").child(mUID).child("chats").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                String roomId = dataSnapshot.getKey();
//               // String roomId = (String)dataSnapshot.child("id").getValue();
//                boolean notified;
//                try {
//                     notified = (boolean) dataSnapshot.child("notified").getValue();
//                }catch (Exception e){
//                    notified = true;
//                }
//                boolean unread;
//                try {
//                    unread = (boolean) dataSnapshot.child("unread").getValue();
//                }catch(Exception e){
//                    unread = false;
//                }
//
//                final ChatObject chat = new ChatObject(roomId);
//
//                mDatabase.child("chats").child(chat.getRoomId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        chat.setToUserName((String)dataSnapshot.child("name").getValue());
//                       // chat.setToUserImageUrl((String)dataSnapshot.child("photoUrl").getValue());
//                        chatObjects.add(chat);
//                        mAdapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        dmRecycler = (RecyclerView)view.findViewById(R.id.feed_recycler);
        dmRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new nameAdapter(chatObjects);

        dmRecycler.setAdapter(mAdapter);

        setUpFabMenu(view);
//        newConvo = view.findViewById(R.id.new_conversation);
//        newConvo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.container, SelectUser.newInstance("","")).addToBackStack("").commit();
//            }
//        });

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

    private class dmHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactName;
        private ImageView contactImage;
        public dmHolder(View itemView){
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.chat_name);
            contactImage = (ImageView)itemView.findViewById(R.id.chat_icon);


        }

        @Override
        public void onClick(View view) {

        }

        public void bindName(ChatObject chatObject){
            String name;

            contactName.setText(chatObject.getToUserName());

        }
    }

    private class nameAdapter extends RecyclerView.Adapter<dmHolder>{

        public List<ChatObject> mChats;
        public nameAdapter(List<ChatObject> chats){
            mChats = chats;
        }

        @Override
        public void onBindViewHolder(dmHolder holder, final int position) {
            holder.bindName(mChats.get(position));
            Picasso.with(getContext())
                    .load(mChats.get(position).getToUserImageUrl())
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

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    if(mChats.get(position).isGroupChat()){

                        fm.beginTransaction().replace(R.id.container, GroupMessage.newInstance(mChats.get(position).getRoomId())).addToBackStack("").commit();
                    }else{

                        fm.beginTransaction().replace(R.id.container, DirectMessage.newInstance(mChats.get(position).getRoomId(), mChats.get(position).getToUserImageUrl(), mChats.get(position).getToUserId(), mChats.get(position).getToUserName())).addToBackStack("").commit();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mChats.size();
        }

        @Override
        public dmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_chat_head, parent, false);
            return new dmHolder(view);
        }
    }


    private void setUpFabMenu(View view){

        mFab = (FloatingActionMenu) view.findViewById(R.id.chat_fab);
        fabDirectMessage = (FloatingActionButton)mFab.findViewById(R.id.fabDirectMessage);
        fabDirectMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.container, SelectUser.newInstance("","")).addToBackStack("").commit();
            }
        });
        fabGroupChat = (FloatingActionButton)mFab.findViewById(R.id.fabGroupChat);
        fabGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.container, SelectGroupDialog.newInstance()).addToBackStack("").commit();
            }
        });
    }
}
