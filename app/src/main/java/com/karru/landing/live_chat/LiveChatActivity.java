package com.karru.landing.live_chat;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.util.AppTypeface;
import com.livechatinc.inappchat.ChatWindowFragment;
import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.LIVE_CHAT_NAME;

public class LiveChatActivity extends DaggerAppCompatActivity
{
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;

    @BindString(R.string.liveChat) String liveChat;
    @BindString(R.string.app_name) String app_name;
    @BindString(R.string.LIVE_CHAT_LICENCE_NUMBER) String LIVE_CHAT_LICENCE_NUMBER;

    @Inject AppTypeface appTypeface;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);

        initializeViews();
    }

    /**
     * <h2>initializeViews</h2>
     * used to initialize the views
     */
    private void initializeViews()
    {
        unbinder = ButterKnife.bind(this);
        tv_all_tool_bar_title.setText(liveChat);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());

        ChatWindowFragment fragment = ChatWindowFragment.newInstance(LIVE_CHAT_LICENCE_NUMBER,
                app_name,getIntent().getStringExtra(LIVE_CHAT_NAME),"");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_live_chat_fragment, fragment)
                .commit();
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbinder.unbind();
    }
}
