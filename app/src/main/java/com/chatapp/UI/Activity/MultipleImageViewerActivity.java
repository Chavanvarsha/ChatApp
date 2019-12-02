package com.chatapp.UI.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chatapp.R;
import com.chatapp.UI.adapter.MultipleImageViewerAdapter;

import java.util.ArrayList;

public class MultipleImageViewerActivity extends AppCompatActivity {

    RecyclerView imageRecyclerview;
    MultipleImageViewerAdapter multipleImageViewerAdapter;
    LinearLayoutManager linearLayoutManager;
    Intent intent;
    int focusOnId = 4;
    ArrayList<String> urlsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_image_viewer);
        intent = getIntent();
        if (intent.getStringArrayListExtra("list") != null) {
            urlsList = intent.getStringArrayListExtra("list");
            focusOnId = intent.getIntExtra("focus", 3);
        }
        imageRecyclerview = findViewById(R.id.recyclerview_multipleimageview);
        multipleImageViewerAdapter = new MultipleImageViewerAdapter();
        if (urlsList.size() > 0) {
            initializeview();
            multipleImageViewerAdapter.setUrlsList(urlsList);
            imageRecyclerview.scrollToPosition(focusOnId);
            multipleImageViewerAdapter.notifyDataSetChanged();
            Log.i("ok", String.valueOf(focusOnId));
            //imageRecyclerview.smoothScrollToPosition(focusOnId);
        }
    }

    public void initializeview() {
        if (linearLayoutManager == null)
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.scrollToPositionWithOffset(focusOnId,200);
        imageRecyclerview.setLayoutManager(linearLayoutManager);
        imageRecyclerview.setAdapter(multipleImageViewerAdapter);
    }
}
