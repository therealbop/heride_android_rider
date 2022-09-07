package com.karru.booking_flow.scheduled_booking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.karru.booking_flow.ride.live_tracking.view.CancelBookingReasonsDialog;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;
import org.json.JSONArray;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.BACK_PRESSED;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.CANCELLED_BOOKING;

public class ScheduledBookingActivity extends DaggerAppCompatActivity implements ScheduledBookingContract.View,
        OnMapReadyCallback
{
    private static final String TAG = "ScheduledBookingActivity";
    private String reason;
    private GoogleMap googleMap;
    private Dialog userPromptWithTwoButtons;
    private ProgressDialog progressDialog;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject ScheduledBookingContract.Presenter presenter;
    @Inject CancelBookingReasonsDialog cancelBookingReasonsDialog;

    @BindView(R.id.ll_address_drop_change) LinearLayout ll_address_drop_change;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_pick_address) TextView tv_pick_address;
    @BindView(R.id.tv_drop_address) TextView tv_drop_address;
    @BindView(R.id.tv_requesting_bid) TextView tv_requesting_bid;
    @BindView(R.id.tv_scheduled_time) TextView tv_scheduled_time;
    @BindView(R.id.tv_scheduled_date) TextView tv_scheduled_date;
    @BindView(R.id.tv_requesting_type_name) TextView tv_requesting_type_name;
    @BindView(R.id.tv_scheduled_cancel) TextView tv_scheduled_cancel;
    @BindView(R.id.rl_back_button) RelativeLayout rl_back_button;
    @BindView(R.id.rl_drop_address) RelativeLayout rl_drop_address;
    @BindString(R.string.title_schedule) String title_schedule;
    @BindString(R.string.id) String id;
    @BindString(R.string.cancel_booking) String cancel_booking;
    @BindString(R.string.yes_alert) String yes_alert;
    @BindString(R.string.cancle_booking) String cancle_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_booking);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initialize();
        initializeMap();
        initializeFonts();
    }

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frag_scheduled_map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);
    }

    /**
     * <h2>initializeFonts</h2>
     * used to initialize the fonts
     */
    private void initializeFonts() {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_scheduled_time.setTypeface(appTypeface.getPro_narMedium());
        tv_pick_address.setTypeface(appTypeface.getPro_News());
        tv_drop_address.setTypeface(appTypeface.getPro_News());
        tv_requesting_bid.setTypeface(appTypeface.getPro_News());
        tv_requesting_type_name.setTypeface(appTypeface.getPro_News());
        tv_scheduled_cancel.setTypeface(appTypeface.getPro_News());
        tv_scheduled_date.setTypeface(appTypeface.getPro_News());
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button,R.id.tv_scheduled_cancel})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
                break;

            case R.id.tv_scheduled_cancel:
                showPassengerCancelPopup(cancle_booking);
                break;
        }
    }

    @Override
    public void finishActivity(String bookingID)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(BOOKING_ID,bookingID);
        setResult(CANCELLED_BOOKING,returnIntent);
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void showPassengerCancelPopup(String message)
    {
        TextView tv_alert_yes =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_title =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body =  userPromptWithTwoButtons.findViewById(R.id.tv_alert_body);
        tv_alert_title.setText(cancel_booking);
        tv_alert_yes.setText(yes_alert);
        tv_alert_body.setText(message);
        tv_alert_yes.setOnClickListener(v ->
                presenter.getCancellationReasons());
        userPromptWithTwoButtons.show();
    }

    @Override
    public void onclickOfConfirmCancel() {
        Utility.printLog(TAG+" reason selected "+reason);
        presenter.cancelBookingAPI(reason);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.checkRTLConversion();
        presenter.bookingDetailsAPI();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(BACK_PRESSED,returnIntent);
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void openLiveTrackingScreen(String bookingId, int loginType)
    {

        if(loginType != 0)
        {
            finish();
        }else {

            Intent intent = new Intent(this, LiveTrackingActivity.class);
            intent.putExtra(BOOKING_ID,bookingId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void populateCancelReasonsDialog(JSONArray reasons)
    {
        userPromptWithTwoButtons.dismiss();
        cancelBookingReasonsDialog.populateWithDetails(reasons);
    }

    @Override
    public void showProgressDialog() {
        if(!isFinishing() )
            progressDialog.show();
    }

    @Override
    public void enableCancelButton(String reason)
    {
        this.reason = reason;
        cancelBookingReasonsDialog.enableCancelButton();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    /**
     * <h2>initialize</h2>
     * this method is used to initialize
     */
    private void initialize() {
        ButterKnife.bind(this);
        ll_address_drop_change.setVisibility(View.GONE);
        tv_all_tool_bar_title.setText(title_schedule);
        userPromptWithTwoButtons = alerts.userPromptWithTwoButtons(this);
        progressDialog = alerts.getProcessDialog(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setUpUI(double pickLat,double pickLong,String... params)
    {
        String bookingId = params[0];
        String vehicleName = params[1];
        String pickAddress = params[2];
        String dropAddress = params[3];
        String bookingDate = params[4];
        String bookingTime = params[5];
        tv_requesting_bid.setText(id+" "+bookingId);
        tv_requesting_type_name.setText(vehicleName.toUpperCase());
        tv_pick_address.setText(pickAddress);
        tv_drop_address.setText(dropAddress);
        tv_scheduled_time.setText(bookingTime);
        tv_scheduled_date.setText(bookingDate);
        if(dropAddress.equals(""))
            rl_drop_address.setVisibility(View.GONE);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(pickLat, pickLong)).zoom(16.00f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.getUiSettings().setAllGesturesEnabled(false);
        this.googleMap.setMyLocationEnabled(true);
        presenter.extractData(getIntent().getExtras());
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }
}
