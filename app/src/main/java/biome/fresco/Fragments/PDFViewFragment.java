package biome.fresco.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import biome.fresco.ProjectDetailActivity;
import biome.fresco.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PDFViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PDFViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PDFViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String pdfUrl;

    WebView pdfView;
    private OnFragmentInteractionListener mListener;

    public PDFViewFragment() {
        // Required empty public constructor
    }


    public static PDFViewFragment newInstance(String pdfUrl) {
        PDFViewFragment fragment = new PDFViewFragment();
        Bundle args = new Bundle();
        args.putString("pdfUrl", pdfUrl);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String location = pdfUrl = getArguments().getString("pdfUrl");
            pdfUrl ="https://docs.google.com/viewer?url=" + location;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pdfview, container, false);
        pdfView = (WebView)view.findViewById(R.id.pdf_view);
        pdfView.setWebChromeClient(new WebChromeClient());
        pdfView.getSettings().setDomStorageEnabled(true);
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.loadUrl(pdfUrl);


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

    @Override
    public void onResume() {
        ProjectDetailActivity.viewingPhoto = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        ProjectDetailActivity.viewingPhoto = false;
        super.onPause();
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
