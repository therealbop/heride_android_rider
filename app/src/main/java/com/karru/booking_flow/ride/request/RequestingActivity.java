package com.karru.booking_flow.ride.request;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.booking_flow.scheduled_booking.ScheduledBookingActivity;
import com.karru.util.ActivityUtils;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.WaveDrawableRequest;
import com.karru.utility.Utility;
import com.heride.rider.R;

import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.IGNORED;
import static com.karru.utility.Constants.REQUESTING_BUSY_DRIVERS;
import static com.karru.utility.Constants.REQUESTING_DATA;
import static com.karru.utility.Constants.REQUEST_CHECK_SETTINGS;
import static com.karru.utility.Constants.SHOW_HOTEL;

public class RequestingActivity extends DaggerAppCompatActivity implements RequestingContract.View,
        OnMapReadyCallback,GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener
{
    private static final String TAG = "RequestingActivity";
    @Inject Alerts alerts;
    @Inject Context mContext;
    @Inject AppTypeface appTypeface;
    @Inject RequestingContract.Presenter requestingPresenter;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_pick_address) TextView tv_live_tracking_pick;
    @BindView(R.id.tv_drop_address) TextView tv_live_tracking_drop;
    @BindView(R.id.rl_requesting_cancel) TextView rl_requesting_cancel;
    @BindView(R.id.sb_requesting_timer) SeekBar sb_requesting_timer;
    @BindView(R.id.rl_requesting_wave_animation) RelativeLayout rl_requesting_wave_animation;
    @BindView(R.id.rl_requesting_retry) RelativeLayout rl_requesting_retry;
    @BindView(R.id.ll_requesting_details) LinearLayout ll_requesting_details;
    @BindView(R.id.ll_address_drop_change) LinearLayout ll_live_tracking_change;
    @BindView(R.id.ll_requesting_bottom) LinearLayout ll_requesting_bottom;
    @BindView(R.id.tv_requesting_retry_title) TextView tv_requesting_retry_title;
    @BindView(R.id.tv_requesting_retry_btn) TextView tv_requesting_retry_btn;

    @BindView(R.id.tv_confirm_pick_button) TextView tv_confirm_pick_button;
    @BindView(R.id.tv_confirm_pick_address) TextView tv_confirm_pick_address;
    @BindView(R.id.tv_requesting_bid) TextView tv_requesting_bid;
    @BindView(R.id.tv_requesting_type_name) TextView tv_requesting_type_name;
    @BindView(R.id.iv_confirm_pick_curr_location) ImageView iv_confirm_pick_curr_location;
    @BindView(R.id.iv_confirm_pick_back) AppCompatImageView iv_confirm_pick_back;
    @BindView(R.id.cl_confirm_pick_layout) ConstraintLayout cl_confirm_pick_layout;
    @BindView(R.id.rl_requesting_layout) RelativeLayout rl_requesting_layout;
    @BindView(R.id.ll_requesting_action_bar) LinearLayout ll_requesting_action_bar;
    @BindView(R.id.rl_back_button) RelativeLayout rl_back_button;
    @BindView(R.id.rl_drop_address) RelativeLayout rl_live_tracking_drop;
    @BindView(R.id.rl_requesting_tick) RelativeLayout rl_requesting_tick;
    @BindView(R.id.iv_requesting_vehicle_pic) ImageView iv_requesting_vehicle_pic;
    @BindView(R.id.iv_requesting_tick_icon) AppCompatImageView iv_requesting_tick_icon;
    @BindView(R.id.pBar_requesting_vehicle) ProgressBar pBar_requesting_vehicle;
    @BindView(R.id.tv_requesting_tick_status) TextView tv_requesting_tick_status;
    @BindString(R.string.confirm_pick_at) String confirm_pick_at;
    @BindString(R.string.id) String id;
    @BindString(R.string.search_drivers) String search_drivers;
    @BindString(R.string.sorry) String sorry;
    @BindString(R.string.not_available) String not_available;
    @BindString(R.string.no_rides) String no_rides;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;


    private ProgressDialog progressDialog;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesting);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initialize();
        initializeMap();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        requestingPresenter.checkRTLConversion();
        requestingPresenter.startCurrLocation();
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize the views
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        RequestBookingDetails requestBookingDetails = (RequestBookingDetails) getIntent().getSerializableExtra(REQUESTING_DATA);
        requestingPresenter.extractData(bundle,requestBookingDetails);
        startWaveAnimation();
        progressDialog= alerts.getProcessDialog(this);
        showProgress();
        ll_live_tracking_change.setVisibility(View.GONE);
        tv_live_tracking_pick.setSelected(true);
        tv_live_tracking_drop.setSelected(true);

        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        rl_requesting_cancel.setTypeface(appTypeface.getPro_narMedium());
        tv_live_tracking_pick.setTypeface(appTypeface.getPro_News());
        tv_live_tracking_drop.setTypeface(appTypeface.getPro_News());
        tv_confirm_pick_button.setTypeface(appTypeface.getPro_narMedium());
        tv_requesting_retry_title.setTypeface(appTypeface.getPro_narMedium());
        tv_requesting_retry_btn.setTypeface(appTypeface.getPro_narMedium());
        tv_requesting_tick_status.setTypeface(appTypeface.getPro_narMedium());
        tv_confirm_pick_address.setTypeface(appTypeface.getPro_News());
        tv_requesting_bid.setTypeface(appTypeface.getPro_News());
        tv_requesting_type_name.setTypeface(appTypeface.getPro_News());

        tv_all_tool_bar_title.setText(search_drivers);
        rl_back_button.setVisibility(View.GONE);
    }

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_confirm_pick_map);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }

    /**
     * <h2>startWaveAnimation</h2>
     * This method is used to start the wave animation
     */
    private void startWaveAnimation()
    {
        WaveDrawableRequest waveDrawable = new WaveDrawableRequest(ContextCompat.getColor(this,
                R.color.colorPrimary), 450);
        rl_requesting_wave_animation.setBackground(waveDrawable);
        Interpolator interpolator = new LinearInterpolator();
        waveDrawable.setWaveInterpolator(interpolator);
        waveDrawable.startAnimation();
    }

    @Override
    public void showTick(String text)
    {
        runOnUiThread(() ->
        {
            tv_requesting_tick_status.setText(text);
            rl_requesting_tick.setVisibility(View.VISIBLE);
            if(Build.VERSION.SDK_INT >= 21) {
                //only api 21 above
                ((Animatable) iv_requesting_tick_icon.getDrawable()).start();
            }
        });
    }

    @Override
    public void openLiveTrackingScreen(String bookingId, int loginType)
    {
        if(loginType != 0 && !isFinishing())
        {
                finish();
        }else {
            if(!isFinishing())
            {
                Intent intent = new Intent(this, LiveTrackingActivity.class);
                intent.putExtra(BOOKING_ID,bookingId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void openHotelScreen() {
        if(!isFinishing())
        {
            Intent intentReturn = getIntent();
            intentReturn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(SHOW_HOTEL,intentReturn);
            finish();
        }
    }

    @Override
    public void openScheduledBookingScreen(Bundle bundle) {
        Intent intent = new Intent(this, ScheduledBookingActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.rl_requesting_cancel,R.id.iv_confirm_pick_curr_location,R.id.tv_confirm_pick_button,
            R.id.iv_confirm_pick_back,R.id.tv_requesting_retry_btn,R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_requesting_cancel:
                Dialog dialog = alerts.userPromptWithTwoButtons(this);
                TextView tv_alert_yes =  dialog.findViewById(R.id.tv_alert_yes);
                tv_alert_yes.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    requestingPresenter.cancelBooking();
                });
                dialog.show();
                break;

            case R.id.iv_confirm_pick_curr_location:
                requestingPresenter.getCurrentLocation();
                break;

            case R.id.tv_confirm_pick_button:
                enableRequestingScreen();
                requestingPresenter.bookingAPI();
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                finishActivity();
                break;

            case R.id.iv_confirm_pick_back:
                finishWithConfirm();
                break;

            case R.id.tv_requesting_retry_btn:
                dismissRetryUI();
                requestingPresenter.bookingAPI();
                break;
        }
    }

    @Override
    public void enableRequestingScreen()
    {
        cl_confirm_pick_layout.setVisibility(View.GONE);
        iv_confirm_pick_curr_location.setVisibility(View.GONE);
        rl_requesting_layout.setVisibility(View.VISIBLE);
        ll_requesting_action_bar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setBookingDetails(String bookingId, String vehicleType)
    {
        tv_requesting_bid.setText(id+" "+bookingId);
        tv_requesting_type_name.setText(vehicleType.toUpperCase());
    }

    @Override
    protected void onPause() {
        super.onPause();
        requestingPresenter.handleBackgroundState();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setUIWithAddress(String pickAddress, String dropAddress,String vehicleImage,
                                 String vehicleName)
    {
        Utility.printLog(TAG+" drop address "+dropAddress);
        tv_live_tracking_pick.setText(pickAddress);
        tv_live_tracking_drop.setText(dropAddress);
        tv_requesting_retry_title.setText(sorry+" "+vehicleName+" "+not_available);
        setUIWithAddress(pickAddress);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(signup_profile_default_image);
        requestOptions = requestOptions.optionalCircleCrop();
        if(vehicleImage!=null)
        {
            if(!vehicleImage.equals(""))
            {
                Glide.with(mContext)
                        .load(vehicleImage)
                        .listener(new RequestListener<Drawable>()
                        {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pBar_requesting_vehicle.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pBar_requesting_vehicle.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .apply(requestOptions)
                        .into(iv_requesting_vehicle_pic);
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMaxBookingTime(int timeInSec)
    {
        sb_requesting_timer.setMax(timeInSec);
    }

    @Override
    public void setProgressTimer(int progress)
    {
        sb_requesting_timer.setProgress(progress);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finishActivity()
    {
        Intent returnIntent = new Intent();
        setResult(REQUESTING_BUSY_DRIVERS,returnIntent);
        finish();
        overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
    }

    /**
     * <h2>finishWithConfirm</h2>
     * to finish the activity without finishing the previous confirm
     */
    private void finishWithConfirm()
    {
        Intent returnIntent = new Intent();
        setResult(IGNORED,returnIntent);
        finish();
        overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
    }

    @Override
    public void showRetryUI()
    {
        rl_requesting_retry.setVisibility(View.VISIBLE);
        ll_requesting_bottom.setVisibility(View.GONE);
        rl_requesting_wave_animation.setVisibility(View.INVISIBLE);
        tv_all_tool_bar_title.setText(no_rides);
        rl_back_button.setVisibility(View.VISIBLE);
    }

    /**
     * <h2>dismissRetryUI</h2>
     * used to dismiss the retry UI
     */
    public void dismissRetryUI() {
        rl_requesting_wave_animation.setVisibility(View.VISIBLE);
        rl_requesting_retry.setVisibility(View.GONE);
        ll_requesting_bottom.setVisibility(View.VISIBLE);
        tv_all_tool_bar_title.setText(search_drivers);
        rl_back_button.setVisibility(View.GONE);
    }

    @Override
    public void showProgress()
    {
        if(progressDialog!=null && !progressDialog.isShowing() && !isFinishing())
            progressDialog.show();
    }

    @Override
    public void dismissProgress()
    {
        if(progressDialog!=null && progressDialog.isShowing() && !isFinishing())
            progressDialog.dismiss();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.setMyLocationEnabled(true);
        requestingPresenter.fetchPickLocation();
    }

    @Override
    public void onCameraIdle()
    {
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
        Point x1 = googleMap.getProjection().toScreenLocation(visibleRegion.farRight);
        Point y = googleMap.getProjection().toScreenLocation(visibleRegion.nearLeft);
        Point centerPoint = new Point(x1.x / 2, y.y / 2);
        LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(centerPoint);
        requestingPresenter.getAddressFromLocation(centerFromPoint);
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

    @Override
    public void moveMapToCurrentLocation(LatLng latLng)
    {
        dismissProgress();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16.00f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void setUIWithAddress(String address) {
        ActivityUtils.changeTextColor(this,confirm_pick_at,address,tv_confirm_pick_address);
        tv_live_tracking_pick.setText(address);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE)
        {
            requestingPresenter.subscribeLocationChange();
            requestingPresenter.subscribeAddressChange();
        }
    }
}
