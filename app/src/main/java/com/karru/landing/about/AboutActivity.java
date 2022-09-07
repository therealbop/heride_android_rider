package com.karru.landing.about;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.network.NetworkReachableActivity;
import com.heride.rider.R;
import com.karru.authentication.privacy.TermsActivity;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.ABOUT;
import static com.karru.utility.Constants.PRIVACY_LINK;
import static com.karru.utility.Constants.TERMS_LINK;

/**
 * <h1>About Fragment</h1>
 * <p>
 * This Fragment is used to provide the About Fragment, where we can know about our product and services.
 * </p>
 */
public class AboutActivity extends DaggerAppCompatActivity implements AboutActivityContract.View
{
    @BindView(R.id.tv_about_rateInGooglePlay) TextView tv_about_rateInGooglePlay;
    @BindView(R.id.tv_about_likeInFacebook) TextView tvLikeInFacebook;
    @BindView(R.id.tv_about_legal) TextView tv_about_legal;
    @BindView(R.id.tv_about_app_version) TextView tv_about_app_version;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindString(R.string.about) String about;
    @BindString(R.string.version) String version;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.FACEBOOK_SHARE) String FACEBOOK_SHARE;

    @Inject AboutActivityContract.AboutPresenter presenter;
    @Inject AppTypeface appTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    /**
     * <h2>initViews</h2>
     * <p>Initializing view elements</p>
     */
    @SuppressLint("SetTextI18n")
    void initViews()
    {
        ButterKnife.bind(this);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_about_rateInGooglePlay.setTypeface(appTypeface.getPro_News());
        tvLikeInFacebook.setTypeface(appTypeface.getPro_News());
        tv_about_legal.setTypeface(appTypeface.getPro_News());
        tv_about_app_version.setTypeface(appTypeface.getPro_News());
        tv_about_app_version.setText(version +" "+ Utility.getAppVersion(this));
        tv_all_tool_bar_title.setText(about);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button,R.id.rl_about_rateInGooglePlay,
            R.id.rl_about_likeInFacebook,R.id.rl_about_legal})
    public void OnClick(android.view.View view)
    {
        switch (view.getId())
        {
            case R.id.rl_back_button:
            case R.id.iv_back_button:
                finish();
                break;

            case R.id.rl_about_rateInGooglePlay:
                presenter.checkNetworkForGoogleConnect();
                break;


            case R.id.rl_about_likeInFacebook:
                presenter.checkNetworkForFacebookConnect();
                break;

            case R.id.rl_about_legal:
                presenter.checkTermsPrivacyLinks();
                break;
        }
    }

    @Override
    public void connectToWeb()
    {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try
        {
            startActivity(goToMarket);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @Override
    public void connectToFacebook()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(FACEBOOK_SHARE));
        startActivity(intent);
    }

    @Override
    public void showNetworkErrorMsg()
    {
        Toast.makeText(this,network_problem,Toast.LENGTH_LONG).show();
    }

    @Override
    public void openTermsScreen(String[] links)
    {
        Intent intent=new Intent(this, TermsActivity.class);
        intent.putExtra(TERMS_LINK,links[0]);
        intent.putExtra(PRIVACY_LINK,links[1]);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }
}
