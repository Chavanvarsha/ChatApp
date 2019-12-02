package com.chatapp.UI.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chatapp.Callbacks.IResponseCallback;
import com.chatapp.DesignPattern.ClientFactory;
import com.chatapp.R;
import com.chatapp.UI.adapter.ChatRowFragmentAdapter;
import com.chatapp.Utils.IntegerConstant;
import com.chatapp.Utils.SharedPref;
import com.chatapp.Utils.StringConstants;
import com.chatapp.listner.ChannelListner;
import com.chatapp.services.ChannelRefreshService;

public class MainActivity extends BaseActivity implements IResponseCallback, View.OnClickListener {
    String TAG = getClass().getSimpleName();
    ViewPager viewPager;
    Button chats, groups, more;
    TextView topbarname;
    ImageView chatcircle, groupcircle, morecircle, edit, creategroup;
    ChatRowFragmentAdapter chatRowFragmentAdapter;
    int TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, ChannelRefreshService.class));
        setContentView(R.layout.activity_main);
        initializeView();
        checkPermissions();
    }

    public void initializeView() {
        chatcircle = findViewById(R.id.iv_circle_chats);
        groupcircle = findViewById(R.id.iv_circle_groups);
        morecircle = findViewById(R.id.iv_circle_more);
        topbarname = findViewById(R.id.tv_topbar_name);
        viewPager = findViewById(R.id.viewpager);
        chats = findViewById(R.id.btn_chats);
        groups = findViewById(R.id.btn_groups);
        more = findViewById(R.id.btn_more);
        creategroup = findViewById(R.id.iv_creategroup);
        edit = findViewById(R.id.iv_edit);
        chats.setOnClickListener(this);
        groups.setOnClickListener(this);
        more.setOnClickListener(this);
        creategroup.setOnClickListener(this);
        chatRowFragmentAdapter = new ChatRowFragmentAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(chatRowFragmentAdapter);
        chats.setTextColor(getResources().getColor(R.color.blue));
    }


    @Override
    public void onSuccess(Bundle bundle) {


    }

    @Override
    public void onFailure(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        if (viewPager != null)
            switch (v.getId()) {
                case R.id.btn_chats:
                    DirectChat();
                    break;
                case R.id.btn_groups:
                    TYPE = IntegerConstant.GROUP_FRAGMENT;
                    viewPager.setCurrentItem(IntegerConstant.GROUP_FRAGMENT, false);
                    topbarname.setText(getText(R.string.groups));
                    setAlphaforcircle(IntegerConstant.GROUP_FRAGMENT);
                    edit.setVisibility(View.GONE);
                    creategroup.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_more:
                    TYPE = IntegerConstant.MORE_FRAGMENT;
                    viewPager.setCurrentItem(IntegerConstant.MORE_FRAGMENT, false);
                    topbarname.setText(getText(R.string.more));
                    setAlphaforcircle(IntegerConstant.MORE_FRAGMENT);
                    edit.setVisibility(View.VISIBLE);
                    creategroup.setVisibility(View.GONE);
                    break;
                case R.id.iv_creategroup:
                    if (TYPE == IntegerConstant.GROUP_FRAGMENT)
                        startActivity(new Intent(this, CreateUserListActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.CREATE_GROUP));
                    else if (TYPE == IntegerConstant.CHAT_FRAGMENT)
                        startActivity(new Intent(this, CreateUserListActivity.class).putExtra(StringConstants.TYPE, IntegerConstant.START_CHAT));
                    break;
            }
    }

    private void DirectChat() {
        TYPE = IntegerConstant.CHAT_FRAGMENT;
        viewPager.setCurrentItem(IntegerConstant.CHAT_FRAGMENT, false);
        topbarname.setText(getText(R.string.chats));
        setAlphaforcircle(IntegerConstant.CHAT_FRAGMENT);
        edit.setVisibility(View.GONE);
        creategroup.setVisibility(View.VISIBLE);
    }

    private void setAlphaforcircle(int fragmentid) {
        setAllIconUncolored();
        chatcircle.setAlpha(0.4f);
        groupcircle.setAlpha(0.4f);
        morecircle.setAlpha(0.4f);
        chats.setTextColor(getResources().getColor(R.color.black_shade_text));
        groups.setTextColor(getResources().getColor(R.color.black_shade_text));
        more.setTextColor(getResources().getColor(R.color.black_shade_text));
        switch (fragmentid) {
            case IntegerConstant.CHAT_FRAGMENT:
                setDrawableTop(R.drawable.chat_colored_icon, chats);
                chats.setTextColor(getResources().getColor(R.color.blue));
                chatcircle.setAlpha(1f);
                break;
            case IntegerConstant.GROUP_FRAGMENT:
                setDrawableTop(R.drawable.group_colored_icon, groups);
                groups.setTextColor(getResources().getColor(R.color.blue));
                groupcircle.setAlpha(1f);
                break;
            case IntegerConstant.MORE_FRAGMENT:
                setDrawableTop(R.drawable.more_colored_icon, more);
                more.setTextColor(getResources().getColor(R.color.blue));
                morecircle.setAlpha(1f);
                break;
        }

    }


    public void setAllIconUncolored() {
        setDrawableTop(R.drawable.chat_uncolored_icon, chats);
        setDrawableTop(R.drawable.group_uncolored_icon, groups);
        setDrawableTop(R.drawable.more_uncolored_icon, more);
    }

    public void setDrawableTop(int drawableTop, Button btn) {
        if (btn != null) {
            Drawable top = getResources().getDrawable(drawableTop);
            btn.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChannelListner.getInstance().channelListner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (chatRowFragmentAdapter != null)
            chatRowFragmentAdapter.moreFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == IntegerConstant.CHAT_FRAGMENT)
            finish();
        else
            DirectChat();
    }
}
