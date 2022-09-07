package com.karru.help_center;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.karru.api.NetworkService;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.help_center.zendesk_ticket_details.view.HelpIndexAdapter;
import com.karru.help_center.zendesk_ticket_details.view.HelpIndexTicketDetailsActivity;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.util.AppTypeface;
import com.karru.util.ExpireSession;
import com.heride.rider.R;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class ZendeskHelpActivity extends DaggerAppCompatActivity implements
        ZendeskHelpContract.ZendeskView {
    @Inject AppTypeface appTypeface;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject SQLiteDataSource addressDataSource;
    @Inject MQTTManager mqttManager;
    @Inject NetworkService networkService;
    @Inject HelpIndexAdapter helpIndexAdapter;
    @Inject ZendeskHelpContract.Presenter presenter;

    @BindView(R.id.tool_helpIndex)Toolbar toolHelpIndex;
    @BindView(R.id.rlHelpIndex)RelativeLayout rlHelpIndex;
    @BindView(R.id.recyclerHelpIndex)RecyclerView recyclerHelpIndex;
    @BindView(R.id.progressbarHelpIndex)ProgressBar progressbarHelpIndex;

    private ArrayList<com.karru.help_center.model.OpenClose> openCloses = new ArrayList<>();
    private int closeSize,openSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zendesk_help);
        ButterKnife.bind(this);
        initializeToolBar();
        initializeView();
    }

    private void initializeView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerHelpIndex.setLayoutManager(layoutManager);
        helpIndexAdapter.onCreateIndex(this,openCloses,appTypeface,preferenceHelperDataSource);
        recyclerHelpIndex.setAdapter(helpIndexAdapter);
        showLoading();
        presenter.onToGetZendeskTicket();
    }

    /*
    initialize toolBar
     */
    private void initializeToolBar()
    {
        setSupportActionBar(toolHelpIndex);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tvHelpCenter = findViewById(R.id.tv_center);
        ImageView tvAddNewTicket = findViewById(R.id.ivFilter);
        RelativeLayout backButton = findViewById(R.id.rl_back_button);

        getSupportActionBar().setTitle("");
        tvHelpCenter.setText(R.string.helpcenter);
        tvAddNewTicket.setVisibility(View.VISIBLE);
        tvAddNewTicket.setImageDrawable(getResources().getDrawable(R.drawable.add));
        tvHelpCenter.setTypeface(appTypeface.getPro_narMedium());
        backButton.setOnClickListener(v -> onBackPressed());

        tvAddNewTicket.setOnClickListener(view -> {
            Intent intent = new Intent(ZendeskHelpActivity.this, HelpIndexTicketDetailsActivity.class);
            intent.putExtra("ISTOAddTICKET",true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_up,R.anim.stay_still);
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //toolHelpIndex.setVisibility(View.INVISIBLE);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onError(String error)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(this.getString(R.string.alert));
        alertDialog.setMessage(this.getString(R.string.sessionExp));
        alertDialog.setNegativeButton(R.string.ok,
                (dialog, which) -> {
                    ExpireSession.refreshApplication(this,mqttManager, preferenceHelperDataSource, addressDataSource);
                });
        alertDialog.show();

    }


    @Override
    public void onGetTicketSuccess() {

    }

    @Override
    public void onEmptyTicket()
    {
        rlHelpIndex.setVisibility(View.VISIBLE);
        recyclerHelpIndex.setVisibility(View.GONE);
    }





    @Override
    public void onTicketStatus(com.karru.help_center.model.OpenClose openClose, int openCloseSize, boolean isOpenClose)
    {
        if(isOpenClose)
            openSize = openCloseSize;
        else
            closeSize = openCloseSize;

        helpIndexAdapter.openCloseSize(openSize,closeSize);
        openCloses.add(openClose);
    }

    @Override
    public void onNotifyData() {
        rlHelpIndex.setVisibility(View.GONE);
        recyclerHelpIndex.setVisibility(View.VISIBLE);
        helpIndexAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        progressbarHelpIndex.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressbarHelpIndex.setVisibility(View.GONE);
    }

    @Override
    public void stopAct() {

    }
}
