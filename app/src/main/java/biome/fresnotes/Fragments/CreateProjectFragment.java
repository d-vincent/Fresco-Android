package biome.fresnotes.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import biome.fresnotes.R;
import biome.fresnotes.RetrofitShit;

import static biome.fresnotes.MainActivity.mAuth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateProjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    EditText projectName;
    EditText projectDescription;
    RadioGroup permissionRadios;


    View createProject;
    private OnFragmentInteractionListener mListener;

    public CreateProjectFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CreateProjectFragment newInstance() {
        CreateProjectFragment fragment = new CreateProjectFragment();
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
        View view =  inflater.inflate(R.layout.fragment_create_project, container, false);

        projectName = (EditText)view.findViewById(R.id.name_input);
        projectDescription = (EditText)view.findViewById(R.id.desc_input);
        permissionRadios = (RadioGroup)view.findViewById(R.id.radio_group);



        createProject = view.findViewById(R.id.submit);
        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer permission = -1;
                switch (permissionRadios.getCheckedRadioButtonId()){
                    case R.id.public_button:
                        permission = 0;
                        break;

                    case R.id.restricted_button:
                        permission = 1;
                        break;

                    case R.id.private_button:
                        permission = 3;
                        break;

                    case -1:

                        Toast.makeText(getContext(), "Please Select a permisison level", Toast.LENGTH_SHORT).show();
                        return;
                }

                if (projectName.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please give your project a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                RetrofitShit.createProject(mAuth.getCurrentUser().getUid(), projectName.getText().toString(), null, projectDescription.getText().toString(), permission );
                getActivity().onBackPressed();
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
}
