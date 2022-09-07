package com.karru.booking_flow.ride.live_tracking.mqttChat;

import android.app.Dialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.karru.booking_flow.ride.live_tracking.mqttChat.model.ChatData;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Constants;
import com.heride.rider.R;

import java.util.ArrayList;
import javax.inject.Inject;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class ChattingActivity extends DaggerAppCompatActivity implements ChattingContract.ViewOperations,View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<ChatData> chatDataArry=new ArrayList<>();
    private ChattingAdapter cAdapter;
    private int pageNo=1;
    ArrayList<ChatData> chatDataArrayListTemp = new ArrayList<>();

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @BindView(R.id.ivAddFiles) ImageView ivAddFiles ;
    @BindView(R.id.iv_back_button) ImageView iv_back_button ;
    @BindView(R.id.chatProgress) ProgressBar chatProgress;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title ;
    @BindView(R.id.tv_all_tool_bar_title2) TextView tv_all_tool_bar_title2 ;
    @BindView(R.id.tvSend) TextView tvSend ;
    @BindView(R.id.srl_chatting) SwipeRefreshLayout srl_chatting ;
    @BindView(R.id.etMsg) EditText etMsg;
    @BindView(R.id.rcvChatMsg) RecyclerView rcvChatMsg;

    @Inject ChattingContract.PresenterOperations presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);
        presenter.getData(getIntent());
        presenter.setActionBar();
        presenter.initViews();
    }

    @OnClick({R.id.tvSend,R.id.iv_back_button,R.id.rl_back_button})
    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.tvSend:
               presenter.message(etMsg.getText().toString());
               etMsg.setText("");
            break;

            case R.id.iv_back_button:
            case R.id.rl_back_button:
               onBackPressed();
            break;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Constants.IS_CHAT_OPEN = true;
        presenter.subscribeDriverCancelDetails();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.dispose();
        Constants.IS_CHAT_OPEN = false;
    }

    @Override
    public void showDriverCancelDialog(String message)
    {
        Dialog driverCancellationDialog = alerts.userPromptWithOneButton(message,this);
        TextView tv_alert_ok =  driverCancellationDialog.findViewById(R.id.tv_alert_ok);
        tv_alert_ok.setOnClickListener(v ->
        {
            driverCancellationDialog.dismiss();
            finish();
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        });
        driverCancellationDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void scrollToBottom() {
        rcvChatMsg.scrollToPosition(cAdapter.getItemCount() - 1);
    }

    @Override
    public void showProgress() {
        chatProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        chatProgress.setVisibility(View.GONE);
    }

    @Override
    public void setActionBar(String custName)
    {
        tv_all_tool_bar_title.setText(custName);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_News());

    }

    @Override
    public void setViews(String bid) {

        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title2.setTypeface(appTypeface.getPro_News());
        etMsg.setTypeface(appTypeface.getPro_News());
        tvSend.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title2.setVisibility(View.VISIBLE);
        tv_all_tool_bar_title2.setText(getResources().getString(R.string.bid)+" "+bid);
        presenter.getChattingData(0);
        srl_chatting.setOnRefreshListener(this);

    }

    @Override
    public void setRecyclerView()
    {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        cAdapter = new ChattingAdapter(this,chatDataArry);
        rcvChatMsg.setLayoutManager(mLayoutManager);
        int resId = R.anim.slide_in_bottom;
        /*LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        rcvChatMsg.setLayoutAnimation(animation);*/
        rcvChatMsg.setAdapter(cAdapter);
    }

    @Override
    public void updateData(ArrayList<ChatData> chatDataArryList) {
        /*chatDataArrayListTemp.clear();
        chatDataArrayListTemp.addAll(this.chatDataArry);*/
        //this.chatDataArry.clear();
        this.chatDataArry.addAll(0,chatDataArryList);
        //this.chatDataArry.addAll(chatDataArrayListTemp);
        //chatDataArry.add
        //this.chatDataArry.clear();
        //this.chatDataArry.addAll(chatDataArryList);
        cAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    @Override
    public void onRefresh() {
        presenter.getChattingData(pageNo);
        srl_chatting.setRefreshing(false);
        pageNo++;
    }
}
