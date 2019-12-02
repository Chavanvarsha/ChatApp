package com.chatapp.UI.adapter;

import android.arch.persistence.room.OnConflictStrategy;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.chatapp.Callbacks.GridImageViewClickListener;
import com.chatapp.R;
import com.chatapp.Scope.BaseApplication;
import com.chatapp.Utils.ImageUtils;

import java.util.ArrayList;

public class GridAdapter implements View.OnClickListener {

    ImageView topLeft, topRight, bottomLeft, bottomRight;
    View mainLayout;

    GridImageViewClickListener listener;

    public GridAdapter(Context context, ViewGroup root, String... urls) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mainLayout = inflater.inflate(R.layout.view_single_image_layout, root);
        topLeft = mainLayout.findViewById(R.id.topLeft);
        topRight = mainLayout.findViewById(R.id.topRight);
        bottomLeft = mainLayout.findViewById(R.id.bottomLeft);
        bottomRight = mainLayout.findViewById(R.id.bottomRight);

        if (urls.length >= 4) {
            //use glide to add image
            ImageUtils.displayRoundImageFromUrl(context, urls[0], topLeft);
            ImageUtils.displayRoundImageFromUrl(context, urls[1], topRight);
            ImageUtils.displayRoundImageFromUrl(context, urls[2], bottomLeft);
            ImageUtils.displayRoundImageFromUrl(context, urls[3], bottomRight);
        }

    }


    public void setListener(GridImageViewClickListener listener) {
        this.listener = listener;
        topLeft.setOnClickListener(this);
        topRight.setOnClickListener(this);
        bottomLeft.setOnClickListener(this);
        bottomRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            switch (view.getId()) {
                case R.id.topLeft:
                    listener.onClick(view, 0);
                    break;
                case R.id.topRight:
                    listener.onClick(view, 1);
                    break;
                case R.id.bottomLeft:
                    listener.onClick(view, 2);
                    break;
                case R.id.bottomRight:
                    listener.onClick(view, 3);
                    break;
            }
        }
    }
}
