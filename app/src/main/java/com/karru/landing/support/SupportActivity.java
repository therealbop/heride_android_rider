package com.karru.landing.support;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;
import com.karru.authentication.privacy.WebViewActivity;
import com.karru.landing.support.model.SupportDataModel;
import com.karru.landing.support.model.SupportModel;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import java.util.ArrayList;
import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.WEB_LINK;

/**
 * <h1>SupportActivity</h1>
 * used to provide customer support for app
 */
public class SupportActivity extends DaggerAppCompatActivity
        implements AdapterView.OnItemClickListener,SupportActivityContract.View
{
    @BindView(R.id.rv_support_types) RecyclerView rv_support_types;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindString(R.string.support) String support;
    @BindString(R.string.bad_gateway) String somethingWentWrongMsg;
    @BindString(R.string.network_problem) String networkError;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.title_support) String title_support;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject SupportActivityContract.Presenter presenter;
    @Inject SupportListAdapter supportAdapter;

    private ArrayList<SupportDataModel> supportDataAl;
    private ProgressDialog pDialog;

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_support);
        initialize();
    }

    @OnClick({R.id.iv_back_button,R.id.rl_back_button})
    public void ClickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.checkRTLConversion();
        presenter.callSupportAPI();
    }

    /**
     * <h2>initialize</h2>
     * <p>Initializing view elements</p>
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        pDialog = alerts.getProcessDialog(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_support_types.setLayoutManager(layoutManager);
        tv_all_tool_bar_title.setText(support);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        supportDataAl = new ArrayList<>();
        supportAdapter.setFragmentRef(this);
        supportAdapter.setAdapterList(supportDataAl);
        rv_support_types.setAdapter(supportAdapter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.disposeObservables();
    }

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.dismiss();
    }

    @Override
    public void startWebView(int position)
    {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WEB_LINK, supportDataAl.get(position).getLink());
        intent.putExtra(SCREEN_TITLE, title_support);
        intent.putExtra(COMING_FROM, "web");
        startActivity(intent);
    }

    /**
     * <h2>onItemClick</h2>
     * <p> This is an Overrided method got call when a item_country_picker is clicked </p>
     * @param parent parent instance
     * @param view view instance
     * @param position position of item_country_picker
     * @param id id.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id)
    {
        presenter.isLinkNull(supportDataAl.get(position).getLink(),position);
    }


    @Override
    public void onError(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSupportSuccess(SupportModel supportModel)
    {
        pDialog.dismiss();
        supportDataAl.clear();
        supportDataAl.addAll(supportModel.getData());
        supportAdapter.notifyDataSetChanged();
    }
}
