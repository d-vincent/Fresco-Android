package biome.fresnotes.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import biome.fresnotes.ProjectDetailActivity;
import biome.fresnotes.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    String photoUrl;
    PhotoView photoView;
    ImageView downloadPhoto;
    ImageView sharePhoto;
    String photoName;

    private OnFragmentInteractionListener mListener;

    public PhotoViewFragment() {
        // Required empty public constructor
    }


    public static PhotoViewFragment newInstance(String photoUrl, String photoName) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putString("photoUrl", photoUrl);
        args.putString("photoName", photoName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoUrl = getArguments().getString("photoUrl");
            photoName = getArguments().getString("photoName");
        }

       ProjectDetailActivity.viewingPhoto = true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_photo_view, container, false);
        downloadPhoto = (ImageView)view.findViewById(R.id.download_photo);
        downloadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Bitmap bmp = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
                    //MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, photoName , photoName);

                    String root = Environment.getExternalStorageDirectory().toString() + "/fresboard";
                    File myDir = new File(root);
                    myDir.mkdirs();
                    String fname = "Image-" + photoName+ ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists()) file.delete();
                    Log.i("LOAD", root + fname);
                    try {

                        FileOutputStream out = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            final Uri contentUri = Uri.fromFile(file);
                            scanIntent.setData(contentUri);
                            getActivity().sendBroadcast(scanIntent);
                        } else {
                            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                            getActivity().sendBroadcast(intent);
                        }
                        Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);
                }
            }
        });
        photoView = (PhotoView)view.findViewById(R.id.photo_view);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePhoto.setVisibility(View.VISIBLE);
                downloadPhoto.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation anim = new AlphaAnimation(1.0f,0.0f);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                sharePhoto.setVisibility(View.GONE);
                                downloadPhoto.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        anim.setDuration(400);
                        sharePhoto.startAnimation(anim);
                        downloadPhoto.startAnimation(anim);

                    }

                }, 3000);
            }
        });
        Picasso.with(getContext()).load(photoUrl).into(photoView);
        sharePhoto = (ImageView)view.findViewById(R.id.share_photo);
        sharePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri bmpUri = getLocalBitmapUri(photoView);
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                startActivity(Intent.createChooser(shareIntent, "Share image using"));
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
    public void onResume() {
        ProjectDetailActivity.viewingPhoto = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        ProjectDetailActivity.viewingPhoto = false;
        super.onPause();
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

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".biome.com.fresboard.fresnotes.provider",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
