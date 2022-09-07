package com.karru.help_center.zendesk_ticket_details.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.help_center.zendesk_ticket_details.HelpIndexContract;
import com.karru.help_center.model.SpinnerRowItem;
import com.karru.help_center.model.ZendeskDataEvent;
import com.karru.util.AppTypeface;
import com.karru.util.Utility;
import com.heride.rider.R;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>HelpIndexTicketDetailsActivity</h>
 * Created by Ali on 2/26/2018.
 */

public class HelpIndexTicketDetailsActivity extends DaggerAppCompatActivity implements HelpIndexContract.HelpView
{
    public static final Integer[] priorityColor = { R.color.green_continue ,
            R.color.livemblue3498, R.color.red_login_dark,
            R.color.saffron,};
    @Inject
    AppTypeface appTypeface;

    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject HelpIndexContract.presenter  presenter;
    @Inject HelpIndexRecyclerAdapter helpIndexRecyclerAdapter;

    @BindView(R.id.tool_helpindex_ticket)
    Toolbar toolBarLayout;
    @BindView(R.id.tv_center)TextView tv_center;
    @BindArray(R.array.ticketPriority)String[] priorityTitles;
    @BindView(R.id.etHelpIndexSubjectPre)EditText etHelpIndexSubjectPre;
    @BindView(R.id.etWriteMsg)EditText etWriteMsg;
    @BindView(R.id.etHelpIndexSubject)EditText etHelpIndexSubject;
    @BindView(R.id.tvHelpIndexDateNTimePre)TextView tvHelpIndexDateNTimePre;
    @BindView(R.id.spinnerHelpIndexPre)TextView spinnerHelpIndexPre;
    @BindView(R.id.tvHelpIndexImageText)TextView tvHelpIndexImageText;
    @BindView(R.id.tvHelpIndexCustName)TextView tvHelpIndexCustName;
    @BindView(R.id.tvHelpIndexDateNTime)TextView tvHelpIndexDateNTime;
    @BindView(R.id.ivHelpIndexImage)ImageView ivHelpIndexImage;
    @BindView(R.id.cardHelpIndexTicket) CardView cardHelpIndexTicket;
    @BindView(R.id.ivHelpCenterPriority) ImageView ivHelpCenterPriority;
    @BindView(R.id.spinnerHelpIndex) Spinner spinnerHelpIndex;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.cardHelpIndexTicketPre) CardView cardHelpIndexTicketPre;
    @BindView(R.id.tvHelpIndexImageTextPre) TextView tvHelpIndexImageTextPre;
    @BindView(R.id.ivHelpIndexImagePre) ImageView ivHelpIndexImagePre;
    @BindView(R.id.ivHelpCenterPriorityPre) ImageView ivHelpCenterPriorityPre;
    @BindView(R.id.tvHelpIndexCustNamePre) TextView tvHelpIndexCustNamePre;
    @BindView(R.id.tvHelpIndexSend) TextView tvHelpIndexSend;
    @BindView(R.id.rlTextInput) RelativeLayout rlTextInput;

    private String subject,priority;
    private int zenId;
    private ArrayList<SpinnerRowItem> rowItems;
    private boolean isToAddTicket;
    private ArrayList<ZendeskDataEvent> zendeskDataEvents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_help_index_ticket);
        ButterKnife.bind(this);
        isToAddTicket = getIntent().getBooleanExtra("ISTOAddTICKET",false);
        if(getIntent().getExtras()!=null)
        {
            etHelpIndexSubject.setText(getIntent().getStringExtra("SUBJECT"));
            com.karru.utility.Utility.printLog("Subject:-"+getIntent().getStringExtra("SUBJECT"));//
            zenId = getIntent().getIntExtra("ZendeskId",0);//
        }
        initializeArrayList();
        initializeToolBar();
        initializeView();
    }

    private void initializeArrayList() {
        rowItems = new ArrayList<>();
        rowItems.add(new SpinnerRowItem(priorityColor[0],priorityTitles[0]));
        rowItems.add(new SpinnerRowItem(priorityColor[1],priorityTitles[1]));
        rowItems.add(new SpinnerRowItem(priorityColor[2],priorityTitles[2]));
        rowItems.add(new SpinnerRowItem(priorityColor[3],priorityTitles[3]));
    }

    private void initializeView()
    {
        if(isToAddTicket)
        {
            etHelpIndexSubject.setTypeface(appTypeface.getPro_News());
            tvHelpIndexDateNTime.setTypeface(appTypeface.getPro_News());
            tvHelpIndexCustName.setTypeface(appTypeface.getPro_News());
            tvHelpIndexImageText.setTypeface(appTypeface.getPro_News());
            priority = rowItems.get(0).getPriority();
            SpinnerAdapter adapter = new SpinnerAdapter(this,R.layout.spinner_adapter,R.id.tvSpinnerPriority, rowItems,appTypeface);
            spinnerHelpIndex.setAdapter(adapter);

            spinnerHelpIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("TAG", "onItemSelected: "+i);
                    priority = rowItems.get(i).getPriority();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            cardHelpIndexTicket.setVisibility(View.VISIBLE);
            String name = preferenceHelperDataSource.getUsername();//+" "+sharedPrefs.getLastName();
            tvHelpIndexCustName.setText(name);
            char c = name.charAt(0);
            tvHelpIndexImageText.setText(c+"");
            Date date = new Date(System.currentTimeMillis());
            String dateTime[] = Utility.getFormattedDate(date,preferenceHelperDataSource).split(",");
            String timeToSet =  dateTime[0]+" | "+dateTime[1];
            tvHelpIndexDateNTime.setText(timeToSet);

        }else
        {
            etHelpIndexSubjectPre.setEnabled(false);
            spinnerHelpIndexPre.setTypeface(appTypeface.getPro_narMedium());
            tvHelpIndexCustNamePre.setTypeface(appTypeface.getPro_News());
            tvHelpIndexImageTextPre.setTypeface(appTypeface.getPro_News());
            etHelpIndexSubjectPre.setTypeface(appTypeface.getPro_News());
            tvHelpIndexDateNTimePre.setTypeface(appTypeface.getPro_News());
            cardHelpIndexTicketPre.setVisibility(View.VISIBLE);
            String name = preferenceHelperDataSource.getUsername();//+" "+sharedPrefs.getLastName();
            tvHelpIndexCustNamePre.setText(name);
            char c = name.charAt(0);
            tvHelpIndexImageTextPre.setText(c+"");
            showLoading();
            com.karru.utility.Utility.printLog("Zendesk Id1:-"+zenId);
            presenter.callApiToGetTicketInfo(String.valueOf(zenId));
        }
        helpIndexRecyclerAdapter.onHelpIndexRecyclerAdapter(this,zendeskDataEvents,appTypeface,preferenceHelperDataSource);
        RecyclerView recyclerViewHelpIndex = findViewById(R.id.recyclerViewHelpIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewHelpIndex.setLayoutManager(linearLayoutManager);
        recyclerViewHelpIndex.setAdapter(helpIndexRecyclerAdapter);
        etWriteMsg = findViewById(R.id.etWriteMsg);

        etWriteMsg.setTypeface(appTypeface.getPro_News());
        tvHelpIndexSend.setTypeface(appTypeface.getPro_News());
        recyclerViewHelpIndex.setNestedScrollingEnabled(false);
    }

    @OnClick(R.id.tvHelpIndexSend)
    public void msgSendText()
    {
        String trim = etWriteMsg.getText().toString().trim();
        if(!trim.isEmpty())
        {
            if(isToAddTicket)
            {
                subject = etHelpIndexSubject.getText().toString().trim();
                if(!subject.isEmpty())
                {
                    presenter.callApiToCreateTicket(trim,subject,priority);
                    // setAndNotifyAdapter(sharedPrefs.getName(),trim);
                    etHelpIndexSubject.setEnabled(false);
                    isToAddTicket = false;
                    Utility.hideKeyboard(HelpIndexTicketDetailsActivity.this);
                }
                else
                    Toast.makeText(HelpIndexTicketDetailsActivity.this,"Please add subject",Toast.LENGTH_SHORT).show();
                etWriteMsg.setText("");

            }
            else
            {
                com.karru.utility.Utility.printLog("Zendesk Id2:-"+preferenceHelperDataSource.getRequesterId());
                presenter.callApiToCommentOnTicket(trim, String.valueOf(zenId));
                etWriteMsg.setText("");
                // setAndNotifyAdapter(sharedPrefs.getName(),trim);
                Utility.hideKeyboard(HelpIndexTicketDetailsActivity.this);
            }

        }
    }

    /*
    initialize toolBar
     */
    private void initializeToolBar()
    {
        setSupportActionBar(toolBarLayout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_center.setTypeface(appTypeface.getPro_narMedium());
        RelativeLayout backButton = findViewById(R.id.rl_back_button);
        backButton.setOnClickListener(v -> onBackPressed());
        if(isToAddTicket)
            tv_center.setText(R.string.newTicket);
        else
            tv_center.setText(R.string.ticket);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
    }

    @Override
    public void onZendeskTicketAdded(String msg)
    {
        onBackPressed();
    }


    @Override
    public void onError(String errMsg) {
        hideLoading();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(this.getString(R.string.alert));
        alertDialog.setMessage(errMsg);
        alertDialog.setNegativeButton(R.string.ok,
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public void onTicketInfoSuccess(ArrayList<ZendeskDataEvent> events, String timeToSet, String subject, String priority, String type) {
        zendeskDataEvents.addAll(events);
        helpIndexRecyclerAdapter.notifyDataSetChanged();
        etHelpIndexSubjectPre.setText(subject);
        tvHelpIndexDateNTimePre.setText(timeToSet);
        this.subject = subject;
        this.priority = priority;
        spinnerHelpIndexPre.setText(priority);
        presenter.onPriorityImage(this,priority,ivHelpCenterPriorityPre);
        if(!"open".equalsIgnoreCase(type))
        {
            rlTextInput.setVisibility(View.GONE);
        }
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    public void stopAct() {
    }
}
