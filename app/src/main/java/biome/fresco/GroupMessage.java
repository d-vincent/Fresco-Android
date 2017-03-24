package biome.fresco;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static biome.fresco.MainActivity.mAuth;
import static biome.fresco.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupMessage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupMessage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupMessage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ROOMID = "roomid";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String roomId;

    private ArrayList<SimpleUser> mUsers;
    private HashMap<String,SimpleUser> userMap;
    private ArrayList<MessageObject> messages;

    private OnFragmentInteractionListener mListener;
    private nameAdapter mAdapter;
    private EditText messageInput;
    private ImageView sendButton;
    private RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    public GroupMessage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GroupMessage newInstance(String roomId) {
        GroupMessage fragment = new GroupMessage();
        Bundle args = new Bundle();
        args.putString(ROOMID, roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        messages = new ArrayList<>();
        userMap = new HashMap<>();
        roomId = getArguments().getString(ROOMID);

        DatabaseReference getTheseFuckinUsers = mDatabase.child("chats").child(roomId).child("members");
        getTheseFuckinUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                mDatabase.child("users").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       String  userName = (String)dataSnapshot.child("name").getValue();
                        String photoUrl = (String)dataSnapshot.child("photoUrl").getValue();
                        String id = (String)dataSnapshot.getKey();

                        SimpleUser user = new SimpleUser(id,userName,photoUrl);
                        //mUsers.add(user);
                        userMap.put(id,user);
                        //mAdapter.notifyDataSetChanged();
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


//        DatabaseReference getThoseMessages = mDatabase.child("chats").child(roomId).child("messages");
//        getThoseMessages.orderByKey().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String messageContent = (String)dataSnapshot.child("content").getValue();
//                long timeStamp = (long)dataSnapshot.child("timestamp").getValue();
//                String authorId = (String)dataSnapshot.child("author").getValue();
//                long messageType = (long)dataSnapshot.child("type").getValue();
//
//                boolean isMe;
//                if(authorId == mAuth.getCurrentUser().getUid()){
//                    isMe = true;
//                }else{
//                    isMe = false;
//                }
//                MessageObject newMessage = new MessageObject(messageContent,authorId,messageType,timeStamp, isMe);
//                messages.add(newMessage);
//                mAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
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
        View view = inflater.inflate(R.layout.fragment_group_message, container, false);


        mRecyclerView = (RecyclerView)view.findViewById(R.id.dm_recycler);
        mAdapter = new nameAdapter(messages);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mLayoutManager.scrollToPositionWithOffset(messages.size()-1,0);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        messageInput = (EditText)view.findViewById(R.id.message_input);
        sendButton = (ImageView) view.findViewById(R.id.send_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString();
                if (!message.equals("")){
                    messageInput.setText("");
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("author", mAuth.getCurrentUser().getUid());
                    updates.put("content", message);
                    updates.put("timestamp",System.currentTimeMillis());
                    updates.put("type", 0);
                    mDatabase.child("chats").child(roomId).child("messages").push().setValue(updates);

                }
            }
        });

        DatabaseReference getThoseMessages = mDatabase.child("chats").child(roomId).child("messages");
        getThoseMessages.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String messageContent = (String)dataSnapshot.child("content").getValue();
                long timeStamp = (long)dataSnapshot.child("timestamp").getValue();
                String authorId = (String)dataSnapshot.child("author").getValue();
                long messageType = (long)dataSnapshot.child("type").getValue();

                boolean isMe;
                if(authorId == mAuth.getCurrentUser().getUid()){
                    isMe = true;
                }else{
                    isMe = false;
                }
                MessageObject newMessage = new MessageObject(messageContent,authorId,messageType,timeStamp, isMe);
                messages.add(newMessage);
                if(userMap.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                }

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

    private class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactName;
        private ImageView contactImage;
        private TextView messageContent;
        private View entireChatView;
        private TextView timeStamp;
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");


        public MessageHolder(View itemView){
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.dm_name);
            contactImage = (ImageView)itemView.findViewById(R.id.contact_image);
            messageContent = (TextView)itemView.findViewById(R.id.message_content);
            entireChatView = itemView.findViewById(R.id.entire_chat_layout);
            timeStamp = (TextView)itemView.findViewById(R.id.message_timestamp);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindMessage(MessageObject messageObject, int position){
            messageContent.setText(messageObject.getMessage());
            if (!messageObject.getAuthor().equals(mAuth.getCurrentUser().getUid()) && userMap.size() != 0){
                contactName.setText(userMap.get(messageObject.getAuthor()).getUsername());

            }else{
                contactName.setText("You");
            }

            Date hella = new Date(messageObject.getTimeStamp());

            timeStamp.setText(timeFormat.format(hella));
        }
    }

    private class nameAdapter extends RecyclerView.Adapter<MessageHolder>{

        public List<MessageObject> mMessages;
        public nameAdapter(List<MessageObject> messages){

            mMessages = messages;
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
            if (!messages.get(position).getAuthor().equals(mAuth.getCurrentUser().getUid())){
                holder.entireChatView.setBackgroundColor(getResources().getColor(R.color.light_background));
            }
            else{
                holder.entireChatView.setBackgroundColor(getResources().getColor(R.color.white));
            }


            holder.bindMessage(messages.get(position),position);

            if (!messages.get(position).isMe()) {
                Picasso.with(getContext())
                        .load(userMap.get(messages.get(position).getAuthor()).getPhotoUrl())
//                    .placeholder(R.mipmap.contact)
                        .into(holder.contactImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
            }else {
                Picasso.with(getContext())
                        .load(mAuth.getCurrentUser().getPhotoUrl())
//                    .placeholder(R.mipmap.contact)
                        .into(holder.contactImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_direct_message, parent, false);
            return new MessageHolder(view);
        }
    }


}
