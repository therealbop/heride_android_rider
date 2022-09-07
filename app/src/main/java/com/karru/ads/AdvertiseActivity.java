package com.karru.ads;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.karru.landing.home.model.AdvertiseData;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;

import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.ADVERTISE_DETAILS;
import static com.karru.utility.Constants.IGNORE;
import static com.karru.utility.Constants.VIEWED;

/**
 * <h1>RideRateCardDialog</h1>
 * This class is used to show the rate card for vehicle for ride
 * @author 3Embed
 * @since on 08-02-2018.
 */
public class AdvertiseActivity extends DaggerAppCompatActivity implements AdvertiseContract.View
{
    private static final String TAG = "AdvertiseActivity";
    private AdvertiseData advertiseData;

    @Inject Context mContext;
    @Inject AppTypeface appTypeface;

    @BindView(R.id.iv_home_adv)
    AppCompatImageView iv_home_adv;
    @BindView(R.id.rl_home_adv) RelativeLayout rl_home_adv;
    @BindView(R.id.tv_home_adv_title)
    AppCompatTextView tv_home_adv_title;
    @BindView(R.id.tv_home_adv_desc) AppCompatTextView tv_home_adv_desc;
    @BindView(R.id.tv_home_adv_know_more) AppCompatTextView tv_home_adv_know_more;
    @BindView(R.id.iv_home_adv_close) ImageView iv_home_adv_close;
    @BindView(R.id.pb_home_adv) ProgressBar pb_home_adv;
    @BindDrawable(R.drawable.ic_launcher) Drawable ic_launcher;
    @BindString(R.string.ok) String ok;
    @BindString(R.string.bad_gateway) String bad_gateway;
    @Inject AdvertiseContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_home_advertise);

        Bundle bundle = getIntent().getExtras();
        advertiseData = (AdvertiseData) bundle.getSerializable(ADVERTISE_DETAILS);
        initialize();
        setTypeface();
    }

    /**
     * <h2>initialize</h2>
     * THis method is used to initialize views
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        setFinishOnTouchOutside(true);

        if(!advertiseData.getImageUrl().equals("") && advertiseData.getImageUrl() != null)
        {
            Glide.with(mContext)
                    .load(advertiseData.getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pb_home_adv.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pb_home_adv.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(new RequestOptions().fitCenter())
                    .into(iv_home_adv);
        }
        else
            rl_home_adv.setVisibility(View.GONE);

        if(advertiseData.getKnowMoreUrl().equals("") || advertiseData.getKnowMoreUrl() == null)
        {
            iv_home_adv_close.setVisibility(View.GONE);
            tv_home_adv_know_more.setText(ok);
        }

        tv_home_adv_title.setText(advertiseData.getTitle());
        tv_home_adv_desc.setText(advertiseData.getDescription());
    }

    /**
     * <h2>setTypeface</h2>
     * This method is used to set the typeface
     */
    private void setTypeface()
    {
        tv_home_adv_title.setTypeface(appTypeface.getPro_narMedium());
        tv_home_adv_desc.setTypeface(appTypeface.getPro_News());
        tv_home_adv_know_more.setTypeface(appTypeface.getPro_News());
    }

    @OnClick({R.id.tv_home_adv_know_more,R.id.iv_home_adv_close})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_home_adv_know_more:
                presenter.updateNotificationStatus(VIEWED,advertiseData.getMessageId(),
                        advertiseData.getKnowMoreUrl(),false);
                break;

            case R.id.iv_home_adv_close:
                presenter.updateNotificationStatus(IGNORE,advertiseData.getMessageId(),
                        advertiseData.getKnowMoreUrl(),true);
                break;
        }
    }

    @Override
    public void hideAdScreen()
    {
        finish();
    }

    @Override
    public void openBrowser()
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertiseData.getKnowMoreUrl()));
        startActivity(browserIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action)
        {
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_DOWN:
                Utility.printLog(TAG+ "Movement occurred outside bounds ");
                presenter.updateNotificationStatus(IGNORE,advertiseData.getMessageId(),
                        advertiseData.getKnowMoreUrl(),true);
                break;
        }
        return false;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,bad_gateway,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }
}

