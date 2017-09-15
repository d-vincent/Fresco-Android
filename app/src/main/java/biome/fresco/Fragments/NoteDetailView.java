package biome.fresco.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    EditText titleEditor;
    String content;
    String title;

    public NoteDetailView() {
        // Required empty public constructor
    }


    public static NoteDetailView newInstance(String noteContent, String noteTitle) {
        NoteDetailView fragment = new NoteDetailView();
        Bundle args = new Bundle();
        args.putString("noteContent", noteContent);
        args.putString("noteTitle", noteTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            content = getArguments().getString("noteContent", "Write your note here");
            title = getArguments().getString("noteTitle", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_detail_view, container, false);

        noteEditor = (RichEditor)view.findViewById(R.id.note_detail_editor);
        noteEditor.setHtml(content);
        titleEditor = (EditText) view.findViewById(R.id.note_title_editor);
        titleEditor.setText(title);

        view.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.undo();
            }
        });

        view.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.redo();
            }
        });

        view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setBold();
            }
        });

        view.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setItalic();
            }
        });

//        view.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                noteEditor.setSubscript();
//            }
//        });
//
//        view.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                noteEditor.setSuperscript();
//            }
//        });

        view.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setStrikeThrough();
            }
        });

        view.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setUnderline();
            }
        });

        view.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setHeading(1);
            }
        });

        view.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setHeading(2);
            }
        });

        view.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setHeading(3);
            }
        });

        view.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setHeading(4);
            }
        });

        view.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setHeading(5);
            }
        });

        view.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setHeading(6);
            }
        });

        view.findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                noteEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });


        view. findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setIndent();
            }
        });

        view.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setOutdent();
            }
        });

        view.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setAlignLeft();
            }
        });

        view.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setAlignCenter();
            }
        });

        view.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setAlignRight();
            }
        });

        view.findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setBlockquote();
            }
        });

        view.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setBullets();
            }
        });

        view.findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                noteEditor.setNumbers();
            }
        });



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
