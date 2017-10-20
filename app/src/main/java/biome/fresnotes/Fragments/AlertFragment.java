package biome.fresnotes.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import biome.fresnotes.MainActivity;
import biome.fresnotes.Objects.AlertObject;
import biome.fresnotes.R;
import biome.fresnotes.RoundedCornerTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    AlertAdapter mAdapter;
    RecyclerView mRecyclerView;
    private OnFragmentInteractionListener mListener;

    public AlertFragment() {
        // Required empty public constructor
    }


    public static AlertFragment newInstance() {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_alert, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.alertRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AlertAdapter(((MainActivity)getActivity()).alertObjects);
        mRecyclerView.setAdapter(mAdapter);


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

    private class AlertHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView contactName;
        private ImageView contactImage;
        private TextView lastMessage;
        private TextView timestamp;
        private View entireView;

        SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd h:mm a");


        public AlertHolder(View itemView){
            super(itemView);
            timestamp = (TextView)itemView.findViewById(R.id.last_message_timestamp);
            lastMessage = (TextView)itemView.findViewById(R.id.last_message);
            contactName = (TextView)itemView.findViewById(R.id.chat_name);
            contactImage = (ImageView)itemView.findViewById(R.id.chat_icon);
            entireView = itemView.findViewById(R.id.entire_layout);

        }

        @Override
        public void onClick(View view) {

        }

        public void bindAlert(AlertObject alertObject){
            String name;

            lastMessage.setText(alertObject.getText());
            contactName.setText(alertObject.getAuthorName());
            try {
                timestamp.setText(timeFormat.format(new Date(alertObject.getTimestamp())));
            }catch (Exception e){

            }

        }
    }

    private class AlertAdapter extends RecyclerView.Adapter<AlertHolder>{

        public List<AlertObject> mAlerts;
        public AlertAdapter(List<AlertObject> alerts){
            mAlerts = alerts;
        }

        @Override
        public void onBindViewHolder(AlertHolder holder, final int position) {
            holder.bindAlert(mAlerts.get(position));
            Picasso.with(getContext())
                    .load(mAlerts.get(position).getAuthorPhotoUrl())
                    .transform(new RoundedCornerTransformation())
                    .fit()
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

            holder.entireView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });
        }

        @Override
        public int getItemCount() {
            return mAlerts.size();
        }

        @Override
        public AlertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_chat_head, parent, false);
            return new AlertHolder(view);
        }
    }

}
