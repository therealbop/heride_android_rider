package com.karru.landing.invite;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.heride.rider.R;
import com.karru.util.AppTypeface;
import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>Invite Screen</h1>
 * This class is used to provide the Invite screen, where we can share_logo other persons, by sending shareBodyMessage or mail.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class InviteActivity extends DaggerAppCompatActivity implements InviteActivityContract.View
{
    @Inject InviteActivityContract.Presenter presenter;
    @Inject AppTypeface appTypeface;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_invite_info) TextView tv_invite_info;
    @BindView(R.id.tv_invite_code) TextView tv_invite_code;
    @BindView(R.id.tv_invite_shareText_label) TextView tvShareTextLabel;
    @BindView(R.id.tv_invite_facebook) TextView tvFacebook;
    @BindView(R.id.tv_invite_twitter) TextView tvTwitter;
    @BindView(R.id.tv_invite_message) TextView tvMessage;
    @BindView(R.id.tv_invite_mail) TextView tvMail;
    @BindString(R.string.invite) String invite;
    @BindString(R.string.subject_email) String subject_email;
    @BindString(R.string.choose_client) String chooseMsg;
    @BindString(R.string.FACEBOOK_SHARE) String FACEBOOK_SHARE;
    @BindString(R.string.TWITTER_SHARE) String TWITTER_SHARE;
    @BindString(R.string.network_problem) String network_problem;

    private String shareBodyMessage = "";
    private Intent fbIntent;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        initializeViews();
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

    /**
     * <h2>initViews</h2>
     * <p>initialize view elements</p>
     */
    private void initializeViews()
    {
        ButterKnife.bind(this);

        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_invite_info.setTypeface(appTypeface.getPro_News());
        tv_invite_code.setTypeface(appTypeface.getPro_narMedium());
        tvShareTextLabel.setTypeface(appTypeface.getPro_News());
        tvFacebook.setTypeface(appTypeface.getPro_News());
        tvTwitter.setTypeface(appTypeface.getPro_News());
        tvMessage.setTypeface(appTypeface.getPro_News());
        tvMail.setTypeface(appTypeface.getPro_News());

        tv_all_tool_bar_title.setText(invite);
        presenter.fetchReferralCode();

        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            } });

    }

    @Override
    public void showReferralCode(String referral, String title, String shareTextMessage)
    {
        tv_invite_info.setText(title);
        shareBodyMessage = shareTextMessage;
        tv_invite_code.setText(referral);
    }

    @Override
    public void showToast(String string) {
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.tv_invite_facebook,R.id.tv_invite_twitter,R.id.tv_invite_message,
            R.id.tv_invite_mail,R.id.rl_back_button,R.id.iv_back_button})
    public void OnclickEvent(android.view.View view)
    {
        switch (view.getId())
        {
            case R.id.tv_invite_facebook:
                facebookShare();
                break;

            case R.id.tv_invite_twitter:
                callTwitterWebView();
                break;

            case R.id.tv_invite_message:
                messageShare();
                break;

            case R.id.tv_invite_mail:
                emailShare();
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    /**
     * <h2>facebookShare</h2>
     * <p>
     *     method to share on facebook
     * </p>
     */
    private void facebookShare()
    {
      /*  fbIntent = new Intent(Intent.ACTION_SEND);
        fbIntent.setType("text/plain");
        fbIntent.putExtra(Intent.EXTRA_TEXT, FACEBOOK_SHARE);
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(fbIntent, 0);
        presenter.callFacebookShare(matches);*/

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(getString(R.string.FACEBOOK_SHARE)))
                    .setQuote("Connect on a global scale.")
                    .build();
            shareDialog.show(content);
        }
    }

    @Override
    public void setFbIntent(ResolveInfo info)
    {
        fbIntent.setPackage(info.activityInfo.packageName);
    }

    @Override
    public void showNetworkError()
    {
        Toast.makeText(this,network_problem,Toast.LENGTH_LONG).show();
    }

    @Override
    public void launchFacebook()
    {
        startActivity(fbIntent);
    }

    @Override
    public void callFacebookWebView()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(FACEBOOK_SHARE));
        startActivity(i);
    }

    /**
     * <h2>callTwitterWebView</h2>
     * used to share the twitter link
     */
    public void callTwitterWebView()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(TWITTER_SHARE));
        startActivity(i);
    }
    /**
     * <h2>messageShare</h2>
     * <p>
     *     method to share through sms
     * </p>
     */
    private void messageShare()
    {
        Intent sms=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"));
        sms.putExtra("sms_body", shareBodyMessage);
        startActivity(sms);
    }

    /**
     * <h2>emailShare</h2>
     * <p>
     *     method to share via email
     * </p>
     */
    private void emailShare()
    {
        Intent email=new Intent(Intent.ACTION_SENDTO);
        email.putExtra(Intent.EXTRA_SUBJECT,subject_email);
        email.putExtra(Intent.EXTRA_TEXT, shareBodyMessage);
        email.setType("text/plain");
        email.setType("shareBodyMessage/rfc822");
        email.setData(Uri.parse("mailto:" + " "));
        startActivity(Intent.createChooser(email, chooseMsg));
    }
}
