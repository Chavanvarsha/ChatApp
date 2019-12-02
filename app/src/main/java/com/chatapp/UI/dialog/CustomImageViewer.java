package com.chatapp.UI.dialog;

import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chatapp.R;
import com.chatapp.Utils.CommonUtils;
import com.chatapp.Utils.ImageUtils;

public class CustomImageViewer extends DialogFragment implements View.OnClickListener {

    View view;
    ImageView showImage, back;
    String urlpath;
    Context context;
    boolean isFilepath;

    public void showImage() {
        if (context != null && urlpath != null && showImage != null && isFilepath) {
            ImageUtils.displayImagefromUri(context, Uri.fromFile(CommonUtils.getInstance().getExternalFilesPath(urlpath)), showImage);
        } else {
            if (!isFilepath) {
                ImageUtils.displayImageFromUrl(context, urlpath, showImage);
            }
        }
    }

    public void setData(Context context, String urlpath, boolean isFilePath) {
        this.context = context;
        this.urlpath = urlpath;
        this.isFilepath = isFilePath;
    }

    public CustomImageViewer() {
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_image_viewer, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        back = view.findViewById(R.id.iv_back);
        showImage = view.findViewById(R.id.iv_showFullimage);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
        }
    }

    @Override
    public void onResume() {
        showImage();
        super.onResume();
    }
}
