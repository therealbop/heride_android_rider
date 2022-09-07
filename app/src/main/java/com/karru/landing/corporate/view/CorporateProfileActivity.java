package com.karru.landing.corporate.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;
import com.karru.landing.corporate.CorporateProfileContract;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.corporate.add_corporate.AddCorporateProfileAccountActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class CorporateProfileActivity extends DaggerAppCompatActivity implements CorporateProfileContract.View
{
    @BindView(R.id.rv_corporate_container) RecyclerView rv_corporate_container;
    @BindView(R.id.ll_corporate_list) LinearLayout ll_corporate_list;
    @BindView(R.id.pb_corporate_progress) ProgressBar pb_corporate_progress;
    @BindView(R.id.tv_corporate_saferMsg) TextView tv_corporate_saferMsg;
    @BindView(R.id.tv_corporate_alert) TextView tv_corporate_alert;
    @BindView(R.id.tv_corporate_add) TextView tv_corporate_add;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.rl_back_button) RelativeLayout rl_back_button;
    @BindView(R.id.ll_corporate_background) LinearLayout ll_corporate_background;
    @BindView(R.id.rl_corporate_profiles) RelativeLayout rl_corporate_profiles;
    @BindString(R.string.corporate_profile) String corporate_profile;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject CorporateProfileContract.Presenter presenter;
    @Inject CorporateProfileAdapter corporateProfileAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_profile);
        ButterKnife.bind(this);
        initViews();
    }

    /**
     * <h>Initialize View</h>
     * <p>This method is Using to initialize views</p>
     */
    public void initViews()
    {
        tv_all_tool_bar_title.setText(corporate_profile);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_corporate_saferMsg.setTypeface(appTypeface.getPro_narMedium());
        tv_corporate_add.setTypeface(appTypeface.getPro_narMedium());
        tv_corporate_alert.setTypeface(appTypeface.getPro_News());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_corporate_container.setLayoutManager(mLayoutManager);
        rv_corporate_container.setItemAnimator(new DefaultItemAnimator());
        rv_corporate_container.setAdapter(corporateProfileAdapter);

        presenter.getCorporateProfiles();
    }

    @Override
    public void populateListWithProfiles(ArrayList<CorporateProfileData> corporateProfileData)
    {
        corporateProfileAdapter.setArrayList(corporateProfileData);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressDialog()
    {
        pb_corporate_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressDialog()
    {
        pb_corporate_progress.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.disposeObservable();
    }

    @OnClick({R.id.tv_corporate_add,R.id.rl_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_corporate_add:
                startActivity(new Intent(this,AddCorporateProfileAccountActivity.class));
                break;
            case R.id.rl_back_button:
                this.onBackPressed();
                break;
        }
    }

    @Override
    public void hideEmptyScreen()
    {
        rl_corporate_profiles.setVisibility(View.VISIBLE);
        ll_corporate_background.setVisibility(View.GONE);
        ll_corporate_list.setVisibility(View.VISIBLE);
    }


    @Override
    public void showEmptyScreen()
    {
        rl_corporate_profiles.setVisibility(View.VISIBLE);
        ll_corporate_background.setVisibility(View.VISIBLE);
        ll_corporate_list.setVisibility(View.GONE);
    }
}
