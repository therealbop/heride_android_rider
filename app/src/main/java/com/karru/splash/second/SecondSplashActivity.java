package com.karru.splash.second;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.heride.rider.R;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.MainActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.WaveDrawableRequest;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>SecondSplashActivity Activity</h1>
 * This class is used to provide the SecondSplashActivity screen, where we can get all the Vehicle details, and it will be called after passenger login.
 * @author 3embed
 * @since 21 JUNE 2017.
 */
public class SecondSplashActivity extends DaggerAppCompatActivity implements SecondSplashContract.View
{
    @Inject AppTypeface appTypeface;
    @Inject WaveDrawableRequest waveDrawable;
    @Inject SecondSplashContract.Presenter presenter;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @BindView(R.id.tv_splash_wave) TextView tv_splash_wave;
    @BindView(R.id.rl_splash_not_register) RelativeLayout rl_splash_wave;
    @BindDrawable(R.drawable.splash) Drawable splash;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(splash);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tv_splash_wave.setTypeface(appTypeface.getPro_narMedium());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.checkRTLConversion();
        initGetVehiclesAPI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservables();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    /**
     * <h2>initGetVehiclesAPI</h2>
     * Calling login service and if success storing values in session manager and start main activity
     */
    private void initGetVehiclesAPI()
    {
        tv_splash_wave.setBackground(waveDrawable);
        tv_splash_wave.setVisibility(View.VISIBLE);
        /*
         * <p>Defining a LinearInterpolator animation object for doing animation.
         * and passing that object to the Wave animator class.
         * @see LinearInterpolator
         * @see WaveDrawable
         * </p>
         */
        LinearInterpolator interpolator = new LinearInterpolator();
        waveDrawable.setWaveInterpolator(interpolator);
        waveDrawable.startAnimation();
        presenter.getVehicleDetails();
    }

    @Override
    public void startMainActivity()
    {
        Intent intent = new Intent(SecondSplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void showWaveAnimation()
    {
        rl_splash_wave.setVisibility(View.VISIBLE);
        waveDrawable.startAnimation();
        Toast.makeText(SecondSplashActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideWaveAnimation()
    {
        waveDrawable.stopAnimation();
        rl_splash_wave.setVisibility(View.GONE);
    }
}
