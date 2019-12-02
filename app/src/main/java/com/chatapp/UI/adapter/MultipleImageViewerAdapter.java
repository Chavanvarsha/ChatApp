package com.chatapp.UI.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chatapp.R;
import com.chatapp.UI.Activity.ChatScreenActivity;
import com.chatapp.UI.Activity.MultipleImageViewerActivity;
import com.chatapp.UI.dialog.CustomImageViewer;
import com.chatapp.Utils.ImageUtils;

import java.util.List;
import java.util.zip.Inflater;

public class MultipleImageViewerAdapter extends RecyclerView.Adapter<MultipleImageViewerAdapter.MultipleImageViewHolder> {

    List<String> urlsList;
    ViewGroup parent;

    public MultipleImageViewerAdapter() {
        if (customImageViewer == null)
            customImageViewer = new CustomImageViewer();
    }

    @NonNull
    @Override
    public MultipleImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_view, parent, false);
        return new MultipleImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MultipleImageViewHolder holder, final int position) {
        ImageUtils.displayImageFromUrl(parent.getContext(), urlsList.get(position), holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage(urlsList.get(position), holder.imageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (urlsList != null)
            return urlsList.size();
        return 0;
    }

    class MultipleImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MultipleImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_view_image);
        }
    }

    public void setUrlsList(List<String> urlsList) {
        if (urlsList != null) {
            this.urlsList = urlsList;
        }
    }

    FragmentManager fragmentManager;
    CustomImageViewer customImageViewer;

    public void showImage(String urlpath, ImageView imageView) {
        customImageViewer.setData(parent.getContext(), urlpath,false);
        if (fragmentManager == null)
            fragmentManager = ((MultipleImageViewerActivity) parent.getContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .commit();

        customImageViewer.show(fragmentManager, "");

    }
}
