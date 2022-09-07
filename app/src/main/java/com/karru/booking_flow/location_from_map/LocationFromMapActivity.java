package com.karru.booking_flow.location_from_map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.karru.authentication.signup.SignUpActivity;
import com.heride.rider.R;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.DROP_ADDRESS_SCREEN;

public class LocationFromMapActivity extends DaggerAppCompatActivity implements OnMapReadyCallback
        ,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveStartedListener,LocationFromMapContract.LocationFromMapView
{
    private static final String TAG = "LocationFromMapActivity";
    @BindView(R.id.tvSelectedLocation) TextView tvSelectedLocation;
    @BindView(R.id.tvPlacePinLabel) TextView tvPlacePinLabel ;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title ;
    @BindView(R.id.tvConfirmAddress) TextView tvConfirmAddress;
    @BindView(R.id.tvSearchLocationLabel) TextView tvSearchLocationLabel;
    @BindString(R.string.search) String search;
    @BindString(R.string.select_business_address) String select_business_address;
    @BindString(R.string.select_drop_address) String select_drop_address;
    @Inject AppTypeface appTypeface;
    @Inject LocationFromMapContract.LocationFromMapPresenter presenter;

    private String  pickLtrTime, comingFrom, flag;
    private String dropAddress="", pickAddress="";
    private String name, phone, email, picture, password, company_name, referralCode;
    private int login_type = 1;
    private boolean isItBusinessAccount = true;
    private GoogleMap googleMap;
    boolean isIndividual,isBuisness;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_from_map);
        ButterKnife.bind(this);
        initializeMap();
        initializeViews();
        retrieveData();
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
        tvSearchLocationLabel.setTypeface(appTypeface.getPro_News());
        tvSelectedLocation.setTypeface(appTypeface.getPro_News());
        tvPlacePinLabel.setTypeface(appTypeface.getPro_News());
        tvConfirmAddress.setTypeface(appTypeface.getPro_News());
    }

    /**
     *<h2>retrieveData</h2>
     * <p> method to retrieve data from passed in intent </p>
     */
    private void retrieveData()
    {
        Bundle bundle = getIntent().getExtras();
        Utility.printLog("MyLocationFind"+"retrieveData");
        comingFrom = getIntent().getStringExtra("comingFrom");
        Utility.printLog(TAG+"comingFrom "+comingFrom);
        if (comingFrom != null && comingFrom.equals("signup"))
        {
            comingFrom = getIntent().getStringExtra("comingFrom");
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            picture = getIntent().getStringExtra("picture");
            password = getIntent().getStringExtra("password");
            company_name = getIntent().getStringExtra("company_name");
            login_type = getIntent().getIntExtra("login_type", 1);
            referralCode = getIntent().getStringExtra("referral_code");
            isIndividual=getIntent().getBooleanExtra("is_Individual_checked",false);
            isBuisness=getIntent().getBooleanExtra("is_buisness_checked",false);
            isItBusinessAccount = getIntent().getBooleanExtra("is_business_Account", isItBusinessAccount);
        }
        if(bundle!=null)
        {
            comingFrom=  bundle.getString("comingFrom");
            pickLtrTime=bundle.getString("pickltrtime");
            if (bundle.getString("ANimation") != null) {
                flag = bundle.getString("ANimation");
            }
            presenter.getStoredAddresses();
            Utility.printLog("deliver id in detail:laterTime:1: "+pickLtrTime);
            if(pickLtrTime == null || pickLtrTime.equals(""))
            {
                Calendar calendar = Calendar.getInstance(Locale.US);
                Date date = new Date();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DATE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                pickLtrTime = (year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + "00");
            }

            if(comingFrom.equals("signup"))
                tv_all_tool_bar_title.setText(select_business_address);
            else
                tv_all_tool_bar_title.setText(select_drop_address);
        }
    }

    @Override
    public void setStoredAddresses(String pickAddress, String dropAddress)
    {
        this.pickAddress = pickAddress;
        this.dropAddress = dropAddress;
        tvSelectedLocation.setText(pickAddress);
    }

    @OnClick({R.id.rl_back_button, R.id.tvConfirmAddress})
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_back_button:
                onBackPressed();
                overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                break;

            case R.id.tvConfirmAddress:
                String address;
                Utility.printLog("MyLocationFind"+"tvConfirmAddress");
                address = tvSelectedLocation.getText().toString();
                if (!address.equals(search))
                {
                    if (comingFrom.equals("pick"))
                    {
                        pickAddress = tvSelectedLocation.getText().toString();
                        presenter.storePickAddress(pickAddress);
                    }
                    else
                    {
                        dropAddress = tvSelectedLocation.getText().toString();
                        presenter.storeDropAddress(dropAddress);
                    }

                    if (!address.equals("")) {
                        if (!address.equals("")) {
                            if (comingFrom.equals("signup")) {
                                Intent intent = new Intent(this, SignUpActivity.class);
                                intent.putExtra("drop_addr", dropAddress);//Last these 6 data, we used only for SIGN UP ACTIVITY.
                                intent.putExtra("name", name);
                                intent.putExtra("phone", phone);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                intent.putExtra("picture", picture);
                                intent.putExtra("company_name", company_name);
                                intent.putExtra("comingFrom", comingFrom);
                                intent.putExtra("login_type", login_type);
                                intent.putExtra("referral_code", referralCode);
                                intent.putExtra("is_business_Account",isItBusinessAccount);
                                intent.putExtra("is_Individual_checked",isIndividual);
                                intent.putExtra("is_buisness_checked",isBuisness);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
                            }
                            else if(comingFrom.equals(DROP_ADDRESS_SCREEN))
                            {
                                Intent intentReturn = getIntent();
                                setResult(RESULT_OK, intentReturn);
                                finish();
                                overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
                            }
                        }
                    }
                }
                break;
        }
    }

    /**
     * <h1>startCurrLocation</h1>
     * This method is used to get the current location
     */
    private void startCurrLocation()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                presenter.getCurrentLocation();
            }
        }
        else
            presenter.getCurrentLocation();
    }

    /**
     * <h1>onResume</h1>
     * This method is keep on calling each time
     */
    @Override
    public void onResume()
    {
        super.onResume();
        presenter.checkRTLConversion();
        startCurrLocation();
        presenter.onResumeActivity();
    }

    /**
     * <h1>initializeMap</h1>
     * This method is used to initialize google Map
     */
    private void initializeMap()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentMap);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utility.printLog(" google Map onMapReady");
        this.googleMap = googleMap;

        if (this.googleMap == null)
            return;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnCameraMoveStartedListener(this);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
        presenter.findCurrentLocation();
    }

    @Override
    public void onCameraIdle()
    {
        initGeoDecoder();
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE)
        {
            presenter.subscribeLocationChange();
            presenter.subscribeAddressChange();
        }
    }

    @Override
    public void moveGoogleMapToLocation(double newLatitude, double newLongitude)
    {
        if(googleMap == null)
            return;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(newLatitude, newLongitude)).zoom(16.00f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void initGeoDecoder()
    {
        Handler handler = new Handler();
        handler.post(this::getLatLngFromMapMarker);
    }


    private void getLatLngFromMapMarker()
    {
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();

        Point x1 = googleMap.getProjection().toScreenLocation(visibleRegion.farRight);
        Point y = googleMap.getProjection().toScreenLocation(visibleRegion.nearLeft);
        Point centerPoint = new Point(x1.x / 2, y.y / 2);
        LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(centerPoint);
        presenter.verifyAndUpdateNewLocation(centerFromPoint);
    }

    @Override
    public void updateCameraPosition(Double currentLat, Double currentLng)
    {
        moveGoogleMapToLocation(currentLat, currentLng);
    }

    @Override
    public void updateAddress(String address)
    {
        tvSelectedLocation.setText(address);
    }
}
