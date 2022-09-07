package com.karru.network;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.util.AppTypeface;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.NETWORK_SCREEN_OPEN;

/**
 * <h1>NetworkReachableActivity</h1>
 * Used to show the network not available acreen
 * @author 3Embed
 * @since on 2/19/2018.
 */
public class NetworkReachableActivity extends DaggerAppCompatActivity implements NetworkReachableContract.View
{
    @Inject AppTypeface appTypeface;

    @BindView(R.id.tv_no_internet_title) TextView tv_no_internet_title;

    @Inject NetworkReachableContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_network_avalability);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
        initialize();
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize
     */
    private void initialize()
    {
        NETWORK_SCREEN_OPEN = true;
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        tv_no_internet_title.setTypeface(appTypeface.getPro_narMedium());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
        presenter.subscribeNetworkObserver();
    }

    @Override
    public void networkAvailable()
    {
        if(!isFinishing())
            finish();
    }

    @Override
    public void networkNotAvailable()
    {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NETWORK_SCREEN_OPEN = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }
}

