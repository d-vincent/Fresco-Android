package biome.fresnotes.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biome.fresnotes.Objects.MessageObject;
import biome.fresnotes.Objects.SimpleUser;
import biome.fresnotes.R;

import static biome.fresnotes.MainActivity.mAuth;
import static biome.fresnotes.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DirectMessage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DirectMessage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupMessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mChatId;
    private String mImageUrl;
    private String userId;
    private List<MessageObject> messages;

    private EditText messageInput;
    private ImageView sendButton;



    private OnFragmentInteractionListener mListener;
    private RecyclerView messageRecycler;
    private nameAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private HashMap<String,SimpleUser> usersInChat;

    public GroupMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DirectMessage.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupMessageFragment newInstance(String chatId, HashMap<String,SimpleUser> usersInChat) {
        GroupMessageFragment fragment = new GroupMessageFragment();
        //Bundle args = new Bundle();
        fragment.mChatId = chatId;
        fragment.usersInChat = usersInChat;

        // args.putString(ARG_PARAM1, chatId);

        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        messages = new ArrayList<>();

        DatabaseReference directChat = mDatabase.child("chats").child(mChatId).child("messages");

        directChat.orderByKey().addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                String message = (String)dataSnapshot.child("content").getValue();
                String author = (String)dataSnapshot.child("author").getValue();
                long timeStamp = (long)dataSnapshot.child("timestamp").getValue();
//                    long type = (long)snap.child("type").getValue();

                if (author.equals(userId)) {
                    messages.add(new MessageObject(message, author, 0, timeStamp,false));
                }
                else {
                    messages.add(new MessageObject(message,author,0,timeStamp,true));
                }
                mAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPositionWithOffset(messages.size()-1,0);



            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_direct_message, container, false);

        messageRecycler = (RecyclerView)view.findViewById(R.id.dm_recycler);
        mLayoutManager = new LinearLayoutManager(getContext());
        messageRecycler.setLayoutManager(mLayoutManager);
        messageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mLayoutManager.scrollToPositionWithOffset(messages.size()-1,0);
            }
        });

        mAdapter = new nameAdapter(messages);
        messageRecycler.setAdapter(mAdapter);

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
                    mDatabase.child("chats").child(mChatId).child("messages").push().setValue(updates);

                }
            }
        });
//        messageInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "What", Toast.LENGTH_SHORT).show();
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

    private class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactName;
        private ImageView contactImage;
        public MessageHolder(View itemView){
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.dm_name);
            contactImage = (ImageView)itemView.findViewById(R.id.contact_image);


        }

        @Override
        public void onClick(View view) {

        }

        public void bindMessage(MessageObject messageObject){
            contactName.setText(messageObject.getMessage());

        }
    }

    private class nameAdapter extends RecyclerView.Adapter<MessageHolder>{

        public List<MessageObject> mMessages;
        public nameAdapter(List<MessageObject> messages){

            mMessages = messages;
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
            holder.bindMessage(messages.get(position));
            if (!messages.get(position).isMe()) {
                Picasso.with(getContext())
                        .load(usersInChat.get(messages.get(position).getAuthor()).getPhotoUrl())
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
