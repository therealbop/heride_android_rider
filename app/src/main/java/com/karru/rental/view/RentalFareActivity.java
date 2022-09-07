package com.karru.rental.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karru.util.AppTypeface;
import com.karru.util.Utility;
import com.heride.rider.R;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class RentalFareActivity extends DaggerAppCompatActivity {

    @BindView(R.id.iv_back_button) ImageView ivBackButton;
    @BindView(R.id.rl_back_button) RelativeLayout rlBackButton;
    @BindView(R.id.tv_all_tool_bar_title) TextView tvAllToolBarTitle;
    @BindView(R.id.tv_all_tool_bar_title2) TextView tvAllToolBarTitle2;
    @BindView(R.id.tvToolBarEnd) TextView tvToolBarEnd;
    @BindView(R.id.rlToolBarEnd) RelativeLayout rlToolBarEnd;
    @BindView(R.id.tv_rental_baseFare) TextView tvRentalBaseFare;
    @BindView(R.id.tv_rental_baseFareValue) TextView tvRentalBaseFareValue;
    @BindView(R.id.rl_rental_base_fare) RelativeLayout rlRentalBaseFare;
    @BindView(R.id.tv_rental_distanceFare) TextView tvRentalDistanceFare;
    @BindView(R.id.tv_rental_distanceFareValue) TextView tvRentalDistanceFareValue;
    @BindView(R.id.rl_rental_distance_fare) RelativeLayout rlRentalDistanceFare;
    @BindView(R.id.tv_rental_durationFare) TextView tvRentalDurationFare;
    @BindView(R.id.tv_rental_durationFareValue) TextView tvRentalDurationFareValue;
    @BindView(R.id.rl_rental_duration_fare) RelativeLayout rlRentalDurationFare;
    @BindView(R.id.tv_rental_advanceFare) TextView tvRentalAdvanceFare;
    @BindView(R.id.tv_rental_advanceFareValue) TextView tvRentalAdvanceFareValue;
    @BindView(R.id.rl_rental_advance_fare) RelativeLayout rlRentalAdvanceFare;
    @BindView(R.id.tv_rental_note) TextView tvRentalNote;
    @BindView(R.id.tv_rental_baseFareDescription) TextView tvRentalBaseFareDescription;
    @BindView(R.id.tv_rental_distanceFareDescription) TextView tvRentalDistanceFareDescription;
    @BindView(R.id.tv_rental_durationFareDescription) TextView tvRentalDurationFareDescription;

    @BindString(R.string.baseFare) String baseFare;
    @BindString(R.string.distance_fare) String distanceFare;
    @BindString(R.string.duration_fare) String durationFare;
    @BindString(R.string.advance_Fare) String advanceFare;
    @BindString(R.string.distance_fare_description) String distanceFareDescription;
    @BindString(R.string.duration_fare_description) String durationFareDescription;
    @BindString(R.string.additional_km_fare) String additionalKmFare;
    @BindString(R.string.additional_time_fare) String additionalTimeFare;
    @BindString(R.string.additional_booking_fee) String additionalBookingFee;

    @Inject AppTypeface appTypeface;
    @Inject Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_fare);
        ButterKnife.bind(this);
        initViews();
        initializeFonts();
    }

    private void initializeFonts() {
        tvAllToolBarTitle.setTypeface(appTypeface.getPro_narMedium());
        tvAllToolBarTitle2.setTypeface(appTypeface.getPro_narMedium());
        tvRentalBaseFare.setTypeface(appTypeface.getPro_News());
        tvRentalBaseFareValue.setTypeface(appTypeface.getPro_News());
        tvRentalDistanceFare.setTypeface(appTypeface.getPro_News());
        tvRentalDistanceFareValue.setTypeface(appTypeface.getPro_News());
        tvRentalDurationFare.setTypeface(appTypeface.getPro_News());
        tvRentalDurationFareValue.setTypeface(appTypeface.getPro_News());
        tvRentalAdvanceFare.setTypeface(appTypeface.getPro_News());
        tvRentalAdvanceFareValue.setTypeface(appTypeface.getPro_News());
    }

    private void initViews() {

//        Utility utility = new Utility();
        Intent intent = getIntent();

        int abbr = intent.getIntExtra("abbr", 1);
        String symbol = intent.getStringExtra("symbol");

        String baseCurrencyCost = utility.currencyAdjustment(abbr, symbol, String.valueOf(Double.valueOf(intent.getStringExtra("baseFare"))));
        String distanceCurrencyCost = utility.currencyAdjustment(abbr, symbol, String.valueOf(Double.valueOf(intent.getStringExtra("distanceFare"))));
        String durationCurrencyCost = utility.currencyAdjustment(abbr, symbol, String.valueOf(Double.valueOf(intent.getStringExtra("durationFare"))));
        String advanceCurrencyCost = utility.currencyAdjustment(abbr, symbol, String.valueOf(Double.valueOf(intent.getStringExtra("advanceFare"))));

        tvAllToolBarTitle.setText(intent.getStringExtra("Name"));
        tvAllToolBarTitle.setTextColor(getResources().getColor(R.color.black));
        tvAllToolBarTitle.setTextSize(12);

        tvAllToolBarTitle2.setText(intent.getStringExtra("package"));
        tvAllToolBarTitle2.setTextSize(14);
        tvAllToolBarTitle2.setVisibility(View.VISIBLE);


        tvRentalBaseFare.setText(baseFare);
        tvRentalBaseFareValue.setText(baseCurrencyCost);
//        tvRentalBaseFareDescription.setText("Includes "+ intent.getStringExtra("package"));
        tvRentalBaseFareDescription.setText("Includes " + intent.getStringExtra("package"));

        tvRentalDistanceFare.setText(additionalKmFare);
        tvRentalDistanceFareValue.setText(distanceCurrencyCost);
        tvRentalDistanceFareDescription.setText(distanceFareDescription);

        tvRentalDurationFare.setText(additionalTimeFare);
        tvRentalDurationFareValue.setText(durationCurrencyCost);
        tvRentalDurationFareDescription.setText(durationFareDescription);

        tvRentalAdvanceFare.setText(additionalBookingFee);
        tvRentalAdvanceFareValue.setText(advanceCurrencyCost);
    }

    @OnClick({R.id.iv_back_button, R.id.rl_back_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
                break;
        }
    }
}
