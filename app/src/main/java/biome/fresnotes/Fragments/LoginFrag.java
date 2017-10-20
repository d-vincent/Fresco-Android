package biome.fresnotes.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;


import biome.fresnotes.R;

import static biome.fresnotes.MainActivity.mAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private View google;
    private View facebook;
    private View twitter;
    private TextView email;

    private SignInButton googleSignIn;
    private GoogleApiClient mGoogleApiClient;



    // TODO: Rename and change types of parameters



    private OnFragmentInteractionListener mListener;

    public LoginFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFrag newInstance() {
        LoginFrag fragment = new LoginFrag();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(getContext(), "Sign In Failed: Connection Lost", Toast.LENGTH_SHORT).show();

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();


        googleSignIn = (SignInButton) view.findViewById(R.id.google_button);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
//        newUser = (TextView) view.findViewById(R.id.new_user);
//        newUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment newFragment = NewUser.newInstance();
//
//                // Add the fragment to the activity, pushing this transaction
//                // on to the back stack.
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container, newFragment);
//                ft.commit();
//            }
//        });
//
//
//        login = (TextView) view.findViewById(R.id.login);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                ((MainActivity)getActivity()).login(emailField.getText().toString(),passField.getText().toString());
//
//            }
//        });



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

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

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case 0:{

               GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
               handleSignInResult(result);
           }
       }

    }
    private void handleSignInResult(GoogleSignInResult result){

        boolean hella = result.isSuccess();
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

//                    FirebaseUser user = mAuth.getCurrentUser();
//                    Toast.makeText(getContext(), user.getEmail(), Toast.LENGTH_SHORT).show();
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, ProjectFragment.newInstance()).commit();
                }
            });

        }
    }



}
