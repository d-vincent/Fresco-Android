package biome.fresco;

import android.content.Context;
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
import android.widget.Toast;

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
    private String mParam1;
    private String mParam2;
    private List<String> userChatNames;
    private List<String> directChatIds;
    private List<String> directUserNames;
    private List<String> userIds;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
      //  userChatNames = new ArrayList<>();
        directChatIds = new ArrayList<>();
        directUserNames = new ArrayList<>();
        userIds = new ArrayList<>();

        directMessages = new HashMap<>();
        directUserImages = new ArrayList<>();

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
        directChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                directChatIds.clear();
                directUserNames.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    final String chatId = (String)snap.child("id").getValue();

                    directChatIds.add((String)snap.child("id").getValue());
                    userIds.add(snap.getKey());
                    //Toast.makeText(getContext(), (String)snap.child("id").getValue(), Toast.LENGTH_SHORT).show();
                    DatabaseReference name = mDatabase.child("users").child(snap.getKey()).child("name");
                    name.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            directUserNames.add((String)dataSnapshot.getValue());
                          //  mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference image = mDatabase.child("users").child(snap.getKey()).child("photoUrl");
                    image.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            directUserImages.add((String)dataSnapshot.getValue());
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                   // directUserIds.add((String)snap.getKey());
                //    Toast.makeText(getContext(), (String)snap.getKey(), Toast.LENGTH_SHORT).show();
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        dmRecycler = (RecyclerView)view.findViewById(R.id.feed_recycler);
        dmRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new nameAdapter(directUserNames);

        dmRecycler.setAdapter(mAdapter);

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

    private class nameAdapter extends RecyclerView.Adapter<dmHolder>{

        public List<String> mNames;
        public nameAdapter(List<String> names){
            mNames = names;
        }

        @Override
        public void onBindViewHolder(dmHolder holder, final int position) {
            holder.bindName(mNames.get(position));
            Picasso.with(getContext())
                    .load(directUserImages.get(position))
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
                    fm.beginTransaction().replace(R.id.container, DirectMessage.newInstance(directChatIds.get(position), directUserImages.get(position), userIds.get(position))).addToBackStack("").commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNames.size();
        }

        @Override
        public dmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_direct_message, parent, false);
            return new dmHolder(view);
        }
    }
}
