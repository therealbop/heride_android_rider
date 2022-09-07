package com.karru.landing.rate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.heride.rider.R;
import com.karru.landing.rate.model.RateCardDetail;
import com.karru.landing.rate.model.RateCardRide;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.util.Utility;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class RateCardActivity extends DaggerAppCompatActivity implements RateCardActivityContract.RateCardView
{
    @Inject Utility utility;
    @Inject Alerts alerts;
    @Inject Context mContext;
    @Inject RateCardActivityContract.RateCardPresenter presenter;
    @Inject AppTypeface appTypeface;

    @BindView(R.id.spnr_rate_card_city) Spinner spnr_rate_card_city;
    @BindView(R.id.spnr_rate_card_vehicle) Spinner spnr_rate_card_vehicle;
    @BindView(R.id.iv_rc_vehicleImage) ImageView iv_rc_vehicleImage;
    @BindView(R.id.ll_rate_card_empty) LinearLayout ll_rate_card_empty;
    @BindView(R.id.tv_rate_item_vehicle_type) TextView tv_rate_item_vehicle_type;
    @BindView(R.id.tv_rate_card_min_text) TextView tv_rate_card_min_text;
    @BindView(R.id.tv_rate_card_base_text) TextView tv_rate_card_base_text;
    @BindView(R.id.tv_rate_card_base_value) TextView tv_rate_card_base_value;
    @BindView(R.id.tv_rate_card_minFare_val) TextView tv_rate_card_minFare_val;
    @BindView(R.id.tv_rate_card_capacity) TextView tv_rate_card_capacity;
    @BindView(R.id.tv_rate_card_60min_label) TextView tv_rate_card_60min_label;
    @BindView(R.id.tv_rate_card_60min_val) TextView tv_rate_card_60min_val;
    @BindView(R.id.tv_rate_card_2mil_label) TextView tv_rate_card_2mil_label;
    @BindView(R.id.tv_waiting_fee_text) TextView tv_waiting_fee_text;
    @BindView(R.id.tv_waiting_fee_value) TextView tv_waiting_fee_value;
    @BindView(R.id.tv_rate_card_2mil_val) TextView tv_rate_card_2mil_val;
    @BindView(R.id.tv_rate_card_lbh_val) TextView tv_rate_card_lbh_val;
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_rate_card_lbh_label) TextView tv_rate_card_lbh_label;
    @BindView(R.id.tv_rate_card_city) TextView tv_rate_card_city;
    @BindView(R.id.tv_rate_card_vehicle) TextView tv_rate_card_vehicle;
    @BindView(R.id.tv_rate_card_desc) TextView tv_rate_card_desc;
    @BindView(R.id.rl_back_button) RelativeLayout rl_back_button;
    @BindView(R.id.ll_rate_card_det) LinearLayout card_rate_card_det;
    @BindView(R.id.view_rate_card_vehicle_line) View view_rate_card_vehicle_line;
    @BindString(R.string.rate_card) String rate_card;
    @BindString(R.string.seat_single) String seat_single;
    @BindString(R.string.after) String after;
    @BindString(R.string.seat_multiple) String seat_multiple;
    @BindString(R.string.waiting_fare) String waiting_fare;
    @BindString(R.string.distance_fare) String distance_fare;
    @BindString(R.string.time_fare) String time_fare;
    @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;

    private ArrayList<String> vehicles=new ArrayList<>();
    private RateCardDetail rateCardDetail;
    private ArrayList<RateCardRide> rateCardRides;
    private RateCardRide rateCardRide;
    private String currency,distanceMetrics,cityTimeMetrics;
    private int cityCurrencyAbbr;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card);
        ButterKnife.bind(this);
        setAppTypeface();
        initViews();
        presenter.getRateCard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        presenter.disposeObservable();
    }

    /**
     * <h>View initializer</h>
     * <p>this method is using to initialize View's</p>
     */
    public void initViews()
    {
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
        tv_all_tool_bar_title.setVisibility(View.VISIBLE);
        tv_all_tool_bar_title.setText(rate_card);
        spnr_rate_card_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rateCardRides=rateCardDetail.getData().get(i).getRide();
                currency=rateCardDetail.getData().get(i).getCityCurrencySymbol();
                cityTimeMetrics=rateCardDetail.getData().get(i).getCityTimeMetrics();
                cityCurrencyAbbr=rateCardDetail.getData().get(i).getCityCurrencyAbbr();
                distanceMetrics=rateCardDetail.getData().get(i).getCityDistanceMetrics();
                vehicles.clear();
                selectVehicle(rateCardRides);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        spnr_rate_card_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                rateCardRide=rateCardRides.get(i);
                selectDetails(rateCardRide);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.rl_back_button)
    public void clickEvent(View view)
    {
        this.onBackPressed();
    }

    public void setAppTypeface()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_rate_item_vehicle_type.setTypeface(appTypeface.getPro_narMedium());
        tv_rate_card_minFare_val.setTypeface(appTypeface.getPro_News());
        tv_rate_card_capacity.setTypeface(appTypeface.getPro_News());
        tv_rate_card_60min_label.setTypeface(appTypeface.getPro_News());
        tv_rate_card_60min_val.setTypeface(appTypeface.getPro_News());
        tv_rate_card_2mil_label.setTypeface(appTypeface.getPro_News());
        tv_waiting_fee_value.setTypeface(appTypeface.getPro_News());
        tv_waiting_fee_text.setTypeface(appTypeface.getPro_News());
        tv_rate_card_2mil_val.setTypeface(appTypeface.getPro_News());
        tv_rate_card_lbh_val.setTypeface(appTypeface.getPro_News());
        tv_rate_card_lbh_label.setTypeface(appTypeface.getPro_News());
        tv_rate_card_city.setTypeface(appTypeface.getPro_News());
        tv_rate_card_vehicle.setTypeface(appTypeface.getPro_News());
        tv_rate_card_desc.setTypeface(appTypeface.getPro_News());
        tv_rate_card_min_text.setTypeface(appTypeface.getPro_News());
        tv_rate_card_base_text.setTypeface(appTypeface.getPro_News());
        tv_rate_card_base_value.setTypeface(appTypeface.getPro_News());
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    /**
     * <h>Select vehicle method</h>
     * <p>this method is using to handle selected vehicle detials</p>
     * @param rateCardRides vehicle details
     */
    public void selectVehicle(ArrayList<RateCardRide> rateCardRides)
    {
        for(int i=0;i<rateCardRides.size();i++)
        {
            vehicles.add(rateCardRides.get(i).getTypeName());
        }

        if(vehicles.size()==0)
        {
            view_rate_card_vehicle_line.setVisibility(View.GONE);
            tv_rate_card_vehicle.setVisibility(View.GONE);
            spnr_rate_card_vehicle.setVisibility(View.GONE);
            ll_rate_card_empty.setVisibility(View.VISIBLE);
            card_rate_card_det.setVisibility(View.INVISIBLE);
        }
        else
        {
            view_rate_card_vehicle_line.setVisibility(View.VISIBLE);
            tv_rate_card_vehicle.setVisibility(View.VISIBLE);
            spnr_rate_card_vehicle.setVisibility(View.VISIBLE);
            ll_rate_card_empty.setVisibility(View.GONE);
            card_rate_card_det.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicles);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_rate_card_vehicle.setAdapter(vehicleAdapter);
    }

    /**
     * <h>Selected city information Displayer</h>
     * <p>this method is using to display selected city info</p>
     * @param rateCardRide whole rate card details
     */
    @SuppressLint("SetTextI18n")
    public void selectDetails(RateCardRide rateCardRide)
    {
        tv_rate_item_vehicle_type.setText(rateCardRide.getTypeName());
        tv_rate_card_desc.setText(rateCardRide.getTypeDesc());
        tv_rate_card_minFare_val.setText(utility.currencyAdjustment(cityCurrencyAbbr,currency,rateCardRide.getMinimumFare()));
        tv_rate_card_base_value.setText(utility.currencyAdjustment(cityCurrencyAbbr,currency,rateCardRide.getBaseFee()));
        tv_rate_card_60min_label.setText(time_fare+"\n("+after+" "+rateCardRide.getTimeFareAfter()+" "+cityTimeMetrics+")");
        tv_rate_card_60min_val.setText(utility.currencyAdjustment(cityCurrencyAbbr,currency,rateCardRide.getTimeFare())+" / "+cityTimeMetrics);
        tv_rate_card_2mil_label.setText(distance_fare+"\n("+after+" "+rateCardRide.getMileagePriceAfter()+" "+distanceMetrics+")");
        tv_rate_card_2mil_val.setText(utility.currencyAdjustment(cityCurrencyAbbr,currency,
                rateCardRide.getMileagePrice())+" / "+distanceMetrics);
        tv_rate_card_lbh_val.setText(rateCardRide.getVehicleDimension());
        if(rateCardRide.getSeatingCapacity() == 1)
            tv_rate_card_capacity.setText(rateCardRide.getSeatingCapacity()+" "+seat_single);
        else
            tv_rate_card_capacity.setText(rateCardRide.getSeatingCapacity()+" "+seat_multiple);
        tv_waiting_fee_text.setText(waiting_fare+"\n("+after+" "+rateCardRide.getWaitingTimeXMinute()+" "+cityTimeMetrics+")");
        tv_waiting_fee_value.setText(utility.currencyAdjustment(cityCurrencyAbbr,currency,
                rateCardRide.getWaitingFee())+" / "+cityTimeMetrics);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.placeholder(signup_profile_default_image);
        requestOptions = requestOptions.optionalCircleCrop();
        Glide.with(mContext)
                .load(rateCardRide.getVehicleImgOn())
                .apply(requestOptions)
                .into(iv_rc_vehicleImage);
    }

    /**
     * <h>City list Initializer</h>
     * <p>this method is using to initialize the city list and Adapter</p>
     * @param cities cities name list
     * @param rateCardDetail rate card information
     */
    public void initCities(ArrayList<String> cities,RateCardDetail rateCardDetail)
    {
        this.rateCardDetail=rateCardDetail;
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_rate_card_city.setAdapter(cityAdapter);
    }
}
