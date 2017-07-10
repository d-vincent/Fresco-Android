package biome.fresco.Fragments;

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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biome.fresco.MainActivity;
import biome.fresco.Objects.MessageObject;
import biome.fresco.Objects.SimpleUser;
import biome.fresco.R;
import biome.fresco.RoundedCornerTransformation;
import biome.fresco.SourceSansRegularTextView;

import static biome.fresco.MainActivity.mAuth;
import static biome.fresco.MainActivity.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DirectMessage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DirectMessage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectMessage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mChatId;
    private String mImageUrl;
    private String userId;
    private String toUserName;
    private List<MessageObject> messages;

    private EditText messageInput;
    private ImageView sendButton;


    private LinearLayoutManager mLayoutManager;
    private RecyclerView messageRecycler;
    private nameAdapter mAdapter;

    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

    private HashMap<String,String> contactNameMap;

    public DirectMessage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DirectMessage.
     */
    // TODO: Rename and change types and number of parameters
    public static DirectMessage newInstance(String chatId, String imageUrl, String userId, String toUserName) {
        DirectMessage fragment = new DirectMessage();
        //Bundle args = new Bundle();
        fragment.mChatId = chatId;
        fragment.mImageUrl = imageUrl;
        fragment.toUserName = toUserName;
        fragment.userId = userId;
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


        contactNameMap = new HashMap<>();
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
                    messages.add(new MessageObject(message, author, 0, timeStamp, false));
                }
                else {
                    messages.add(new MessageObject(message, author, 0, timeStamp, true));

                }
                try {
                    mLayoutManager.scrollToPositionWithOffset(messages.size() - 1, 0);
                }catch (Exception e){

                }
                mAdapter.notifyDataSetChanged();


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





    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).mFab.setVisibility(View.GONE);
        ((MainActivity)getActivity()).bottomBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).mFab.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).bottomBar.setVisibility(View.VISIBLE);
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

        public TextView contactName;
        public ImageView contactImage;
        private TextView messageContent;
        private View entireChatView;
        public TextView timeStamp;
        public View whoWhenLayout;
        public View dayHeader;
        public SourceSansRegularTextView dayHeaderText;



        public MessageHolder(View itemView){
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.dm_name);
            contactImage = (ImageView)itemView.findViewById(R.id.contact_image);
            messageContent = (TextView)itemView.findViewById(R.id.message_content);
            entireChatView = itemView.findViewById(R.id.entire_chat_layout);
            timeStamp = (TextView)itemView.findViewById(R.id.message_timestamp);
            whoWhenLayout = itemView.findViewById(R.id.who_when_layout);
            dayHeader = itemView.findViewById(R.id.day_header);
            dayHeaderText = (SourceSansRegularTextView)itemView.findViewById(R.id.day_header_text);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindMessage(MessageObject messageObject){
            messageContent.setText(messageObject.getMessage());
            if (!messageObject.getAuthor().equals(mAuth.getCurrentUser().getUid())){
                contactName.setText(toUserName);

            }else{
                contactName.setText("You");
            }

            Date hella = new Date(messageObject.getTimeStamp());
            timeStamp.setText(timeFormat.format(hella));
        }
    }

    private class nameAdapter extends RecyclerView.Adapter<MessageHolder>{

        public List<MessageObject> mMessages;
        SimpleDateFormat headerFormat = new SimpleDateFormat("MMM d");
        public nameAdapter(List<MessageObject> messages){

            mMessages = messages;
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {


            holder.dayHeader.setVisibility(View.GONE);
            holder.contactImage.setVisibility(View.VISIBLE);
            holder.whoWhenLayout.setVisibility(View.VISIBLE);

            CalendarDay firstMessage = CalendarDay.from(new Date(mMessages.get(position).getTimeStamp()));

            try {
                CalendarDay lastDay = CalendarDay.from(new Date(mMessages.get(position - 1).getTimeStamp()));
                CalendarDay thisDay = CalendarDay.from(new Date(mMessages.get(position).getTimeStamp()));
                CalendarDay today = CalendarDay.today();


            if (lastDay.isBefore(thisDay)){
                holder.dayHeader.setVisibility(View.VISIBLE);
                if (thisDay.equals(today)){
                    holder.dayHeaderText.setText("Today");
                }else {
                    holder.dayHeaderText.setText(headerFormat.format(thisDay.getDate()));
                }
            }
            }catch (Exception e){

                holder.dayHeader.setVisibility(View.VISIBLE);
                holder.dayHeaderText.setText(headerFormat.format(firstMessage.getDate()));
            }

            if (!messages.get(position).isMe()) {

                try {
                    if (!messages.get(position-1).isMe() ){
                        MessageObject previousMessage = messages.get(position-1);
                        MessageObject thisMessage = messages.get(position);


                        if (thisMessage.getTimeStamp() - previousMessage.getTimeStamp() < 1800000){
                            holder.contactImage.setVisibility(View.GONE);
                            holder.whoWhenLayout.setVisibility(View.GONE);
                        }

                    }
                }catch (Exception e){

                }
                Picasso.with(getContext())
                        .load(mImageUrl)
                        .transform(new RoundedCornerTransformation())
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

                try {
                    if (messages.get(position-1).isMe() ){
                        MessageObject previousMessage = messages.get(position-1);
                        MessageObject thisMessage = messages.get(position);



                        if (thisMessage.getTimeStamp() - previousMessage.getTimeStamp() < 1800000){
                            holder.contactImage.setVisibility(View.GONE);
                            holder.whoWhenLayout.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){

                }
                Picasso.with(getContext())
                        .load(mAuth.getCurrentUser().getPhotoUrl())
                        .transform(new RoundedCornerTransformation())
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

            holder.bindMessage(messages.get(position));
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view;
            switch (viewType){

                case 0:
                     view = layoutInflater.inflate(R.layout.list_item_direct_message, parent, false);
                    return new MessageHolder(view);

                case 1:
                    view = layoutInflater.inflate(R.layout.list_item_outgoing_message, parent, false);
                    return new MessageHolder(view);

                default:
                    view = layoutInflater.inflate(R.layout.list_item_direct_message, parent, false);
                    return new MessageHolder(view);
            }

        }

        @Override
        public int getItemViewType(int position) {

            if (mMessages.get(position).getAuthor().equals(userId)){
                return 0;
            }else{
                return 1;
            }
        }
    }

}
