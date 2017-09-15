package biome.fresco;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import biome.fresco.Fragments.CreateProjectFragment;
import biome.fresco.Fragments.FolderForList;
import biome.fresco.Fragments.ProjectFilesFragment;
import biome.fresco.Fragments.ProjectNotes;
import biome.fresco.Objects.NoteObject;

/**
 * Created by Drew McDonald on 6/23/2017.
 */

public class FileListAdapter extends UltimateViewAdapter<FileHolder> {

    public List<FolderForList> mThings;
    Context mContext;
    ProjectFilesFragment mFragment;
    public FileListAdapter(List<FolderForList> things, Context context, ProjectFilesFragment fragment){
        mThings = things;
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public FileHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public FileHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(final FileHolder holder, final int position) {
        holder.bindProject(mThings.get(position));

        holder.entireNoteLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (mThings.get(position).getType() == 0){
                        mFragment.enterFolder(mThings.get(position).getId());
                    }
                    else {
                        String type = mThings.get(position).getFileType();
                        String fileUrl = mThings.get(position).getFileUrl();
                        String name = mThings.get(position).getName();
                        if (type.contains("image")){
                           mFragment.openPhoto(fileUrl, name);
                        }
                        else if (type.contains("pdf")){
                            mFragment.openPdf(fileUrl);
                        }
                    }
                }
                return true;
            }
        });

        if (mThings.get(position).getType() == 0){
            holder.filePreview.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_folder_grey600_48dp));
        }else if (mThings.get(position).getFileType().equals("image/png")){
            Picasso.with(mContext)
                    .load(mThings.get(position).getFileUrl())
//                    .placeholder(R.mipmap.contact)
                    .resizeDimen(R.dimen.preview_height, R.dimen.preview_height)
                    .into(holder.filePreview, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        else {
            holder.filePreview.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_file_grey600_48dp));
        }

    }

    @Override
    public int getItemCount() {
        return mThings.size();
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_folder_file, parent, false);
        return new FileHolder(view);
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }
}