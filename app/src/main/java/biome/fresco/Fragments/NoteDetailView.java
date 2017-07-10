package biome.fresco.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biome.fresco.R;
import jp.wasabeef.richeditor.RichEditor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteDetailView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteDetailView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDetailView extends Fragment {

    RichEditor noteEditor;
    String content;

    public NoteDetailView() {
        // Required empty public constructor
    }


    public static NoteDetailView newInstance(String noteContent) {
        NoteDetailView fragment = new NoteDetailView();
        Bundle args = new Bundle();
        args.putString("noteContent", noteContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            content = getArguments().getString("noteContent", "Write your note here");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_detail_view, container, false);

        noteEditor = (RichEditor)view.findViewById(R.id.note_detail_editor);
        noteEditor.setHtml(content);
        return view;
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
