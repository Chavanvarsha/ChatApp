package com.chatapp.UI.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chatapp.R;
import com.chatapp.Utils.ImageUtils;

public class ProfileDialog extends Dialog {

    TextView username, userstatus;
    ImageView userpic;
    Context context;
    String imageurl, name, status;

    public ProfileDialog(@NonNull Context context, String imageurl, String name, String status) {
        super(context);
        this.context = context;
        this.name = name;
        this.imageurl = imageurl;
        this.status = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_profile_layout);
        username = findViewById(R.id.tv_username);
        userstatus = findViewById(R.id.tv_status);
        userpic = findViewById(R.id.iv_userpic);
        Glide.with(context)
                .load(imageurl)
                .into(userpic);
        username.setText(name);
        userstatus.setText(status);

    }
}
