package com.karru.splash.first;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;
import com.karru.authentication.login.LoginActivity;
import com.karru.authentication.signup.SignUpActivity;
import com.karru.landing.MainActivity;
import com.karru.splash.fragments.LandingFirstFragment;
import com.karru.splash.fragments.LandingFiveFragment;
import com.karru.splash.fragments.LandingFourthFragment;
import com.karru.splash.fragments.LandingSecondFragment;
import com.karru.splash.fragments.LandingSixFragment;
import com.karru.splash.fragments.LandingThirdFragment;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.util.ParallaxViewPager;
import com.karru.util.WaveDrawableRequest;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.fabric.sdk.android.Fabric;

import static com.karru.utility.Constants.ADVERTISE_DETAILS;
import static com.karru.utility.Constants.LANGUAGE;
import static com.karru.utility.Constants.LOCATION_PERMISSION_CODE;
import static com.karru.utility.Constants.LOGIN_TYPE;
import static com.karru.utility.Constants.NORMAL_LOGIN;
import static com.karru.utility.Constants.PARTNER_LOGIN;
import static com.karru.utility.Constants.PERMISSION_DENIED;
import static com.karru.utility.Constants.PERMISSION_GRANTED;
import static com.karru.utility.Constants.REQUEST_CHECK_SETTINGS;

/**
 * <h1>Splash Activity</h1>
 * This class is used to provide the Splash screen, where we can select our login or register option
 * <p>
 *  and if user is already login, then it directly opens Main Activity.
 * </p>
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class SplashActivity extends DaggerAppCompatActivity implements SplashContract.View
{
    private static final String TAG = "SplashActivity";
    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject WaveDrawableRequest waveDrawable;
    @Inject SplashContract.Presenter splashPresenter;
    @Inject AppPermissionsRunTime permissionsRunTime;
    @Inject LandingFirstFragment landingFirstFragment;
    @Inject LandingSecondFragment landingSecondFragment;
    @Inject LandingThirdFragment landingThirdFragment;
    @Inject LandingFourthFragment landingFourthFragment;
    @Inject LandingFiveFragment landingFiveFragment;
    @Inject LandingSixFragment landingSixFragment;
    @Inject @Named(LANGUAGE) ArrayList<LanguagesList> languagesLists = new ArrayList<>();

    @BindView(R.id.rl_splash_not_register) RelativeLayout rl_splash_not_register;
    @BindView(R.id.ll_landing_sliderPanelDot) LinearLayout ll_landing_sliderPanelDot;
    @BindView(R.id.vp_landing_pager) ParallaxViewPager vp_landing_pager;
    @BindView(R.id.btn_landing_login) AppCompatButton btn_landing_login;
    @BindView(R.id.btn_landing_register) AppCompatButton btn_landing_register;
    @BindView(R.id.tv_splash_wave) TextView tv_splash_wave;
    @BindView(R.id.tv_login_partner) TextView tv_login_partner;
    @BindView(R.id.tv_landing_languages) AppCompatTextView tv_landing_languages;
    @BindDrawable(R.drawable.splash) Drawable splash;
    @BindString(R.string.network_problem) String network_problem;
    @BindString(R.string.something_went_wrong) String something_went_wrong;
    @BindColor(R.color.vehicle_unselect_color) int vehicle_unselect_color;

    private AlertDialog versionUpdateDialog;
    private FragmentAdapter adapter;
    private int mDotCount;
    private ImageView[] ivDots;
    private String advertiseData;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getWindow().setBackgroundDrawable(splash);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initialize();
    }


    /**
     * <h2>initialize</h2>
     * <p>
     * This method initialize the all UI elements of our splash layout.
     * </p>
     */
    private void initialize()
    {
        //to initialize the butter knife
        ButterKnife.bind(this);

        advertiseData = getIntent().getStringExtra("data");
        Utility.printLog(TAG+" notification data "+advertiseData);
        splashPresenter.generateFCMPushToken();
        btn_landing_login.setTypeface(appTypeface.getPro_narMedium());
        btn_landing_register.setTypeface(appTypeface.getPro_narMedium());
        tv_login_partner.setTypeface(appTypeface.getPro_narMedium());
        tv_splash_wave.setTypeface(appTypeface.getPro_narMedium());
        tv_landing_languages.setTypeface(appTypeface.getPro_narMedium());

        adapter=new FragmentAdapter(getSupportFragmentManager());
        vp_landing_pager.setAdapter(adapter);
        createCircularIndicator();
    }

    /**
     * <h>Circular Image Creater</h>
     * <p>this method is using to create circle indicator</p>
     */
    public void createCircularIndicator()
    {
        mDotCount = adapter.getCount();
        ivDots = new ImageView[mDotCount];
        for (int i = 0; i < mDotCount; i++) {
            ivDots[i] = new ImageView(this);
            ivDots[i].setImageResource(R.drawable.non_active_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            ll_landing_sliderPanelDot.addView(ivDots[i], params);
        }

        ivDots[0].setImageResource(R.drawable.active_dot);
        vp_landing_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mDotCount; i++) {
                    ivDots[i].setImageResource(R.drawable.non_active_dot);
                }
                ivDots[position].setImageResource(R.drawable.active_dot);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void setLanguage(String language,boolean restart)
    {
        tv_landing_languages.setText(language);
        if(restart)
        {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }

    /**
     * <h1>FragmentAdapter</h1>
     *
     */
    class FragmentAdapter extends FragmentPagerAdapter
    {
        Fragment[] fragView=new Fragment[]{landingFirstFragment, landingSecondFragment,landingThirdFragment,
                landingFourthFragment,landingFiveFragment,landingSixFragment};

        FragmentAdapter(FragmentManager fm)
        {
            super(fm);
            landingFirstFragment.getPresenter(splashPresenter);
        }

        @Override
        public Fragment getItem(int position) {

            return fragView[position];
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public void showAppUpdateAlert(boolean mandateUpdate)
    {
        versionUpdateDialog = alerts.updateAppVersionAlert(this,mandateUpdate,this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Utility.printLog(TAG+ " onStart: ");
        splashPresenter.checkForUserLoggedIn();
    }

    @Override
    public void startLocationService()
    {
        if (permissionsRunTime.checkIfPermissionNeeded())
        {
            if(permissionsRunTime.checkIfPermissionGrant(Manifest.permission.ACCESS_FINE_LOCATION,this))
                workResume();
            else
            {
                String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
                permissionsRunTime.requestForPermission(strings,this,LOCATION_PERMISSION_CODE);
            }
        }
        else
            workResume();
    }

    /**
     * <h2>workResume</h2>
     * <p>
     * This method is used to perform all the task, which we wants to do on our onResume() method.
     * </p>
     */
    private void workResume()
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
        splashPresenter.startLocationService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        splashPresenter.checkRTLConversion();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case LOCATION_PERMISSION_CODE:
                int status =  permissionsRunTime.getPermissionStatus(this,
                        Manifest.permission.ACCESS_FINE_LOCATION,true);
                switch (status)
                {
                    case PERMISSION_GRANTED:
                        workResume();
                        break;

                    case PERMISSION_DENIED:
                        String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION};
                        permissionsRunTime.requestForPermission(strings,this,LOCATION_PERMISSION_CODE);
                        break;

                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        break;
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onGettingOfVehicleDetails()
    {
        splashPresenter.stopLocationUpdate();
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(ADVERTISE_DETAILS,advertiseData);
        startActivity(intent);
        finish();
    }

    @Override
    public void ifUserNotRegistered()
    {
        Utility.printLog(TAG+ " ifUserNotRegistered: ");
        rl_splash_not_register.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btn_landing_login,R.id.btn_landing_register,R.id.tv_landing_languages,R.id.tv_login_partner})
    public void clickEvent(View view)
    {
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        switch (view.getId())
        {
            case R.id.btn_landing_login:
                loginIntent.putExtra(LOGIN_TYPE, NORMAL_LOGIN);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.tv_login_partner:
                loginIntent.putExtra(LOGIN_TYPE, PARTNER_LOGIN);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.btn_landing_register:
                Intent registerIntent=new Intent(this, SignUpActivity.class);
                registerIntent.putExtra("ent_socialMedia_id", "");
                startActivity(registerIntent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.tv_landing_languages:
                splashPresenter.getLanguages();
                break;
        }
    }

    @Override
    public void showLanguagesDialog(int indexSelected)
    {
        alerts.showLanguagesAlert(this,languagesLists,splashPresenter,null,indexSelected);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(versionUpdateDialog != null && versionUpdateDialog.isShowing())
        {
            Constants.isToUpdateAlertVisible = false;
            versionUpdateDialog.dismiss();
        }
        splashPresenter.disposeObservables();
    }

    @Override
    public void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void promptUserWithLocationAlert(Status status) {
        try
        {
            status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        }
        catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
}