package biome.fresnotes.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import biome.fresnotes.MainActivity;
import biome.fresnotes.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUser extends Fragment {


   TextView register;
    EditText emailForRegister;
    EditText passwordForRegister;

    private OnFragmentInteractionListener mListener;

    public NewUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewUser.
     */
    // TODO: Rename and change types and number of parameters
    public static NewUser newInstance() {
        NewUser fragment = new NewUser();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_new_user, container, false);

        emailForRegister = (EditText) view.findViewById(R.id.email);
        passwordForRegister = (EditText)view.findViewById(R.id.password);


        register =(TextView) view.findViewById(R.id.sign_up_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = emailForRegister.getText().toString();
                String newPass = emailForRegister.getText().toString();

                createAccount(newEmail,newPass);

            }
        });


        return view;
    }

    public void createAccount(String email, String password){


        ((MainActivity)getActivity()).mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(),"WELCOME TO FRESCO, BITCH", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
