package com.karru.landing.home.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.heride.rider.R;
import com.karru.util.AppTypeface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>RideRateCardDialog</h1>
 * This class is used to show the rate card for vehicle for ride
 * @author 3Embed
 * @since on 08-02-2018.
 */
public class RideRateCardDialog extends Dialog
{
    private AppTypeface appTypeface;
    private String baseFare, distanceRate, timeRate;

    @BindView(R.id.tv_ride_rate_title) TextView tv_ride_rate_title;
    @BindView(R.id.tv_ride_rate_base_text) TextView tv_ride_rate_base_text;
    @BindView(R.id.tv_ride_rate_base_value) TextView tv_ride_rate_base_value;
    @BindView(R.id.tv_ride_rate_distance_text) TextView tv_ride_rate_distance_text;
    @BindView(R.id.tv_ride_rate_distance_value) TextView tv_ride_rate_distance_value;
    @BindView(R.id.tv_ride_rate_time_text) TextView tv_ride_rate_time_text;
    @BindView(R.id.tv_ride_rate_time_value) TextView tv_ride_rate_time_value;
    @BindView(R.id.tv_ride_rate_submit) TextView tv_ride_rate_submit;

    public RideRateCardDialog(@NonNull Context context, AppTypeface appTypeface)
    {
        super(context);
        this.appTypeface = appTypeface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialogue_ride_rate_card);

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
    }

    /**
     * <h2>setTypeface</h2>
     * This method is used to set the typeface
     */
    private void setTypeface()
    {
        tv_ride_rate_title.setTypeface(appTypeface.getPro_narMedium());
        tv_ride_rate_base_text.setTypeface(appTypeface.getPro_News());
        tv_ride_rate_base_value.setTypeface(appTypeface.getPro_narMedium());
        tv_ride_rate_distance_text.setTypeface(appTypeface.getPro_News());
        tv_ride_rate_distance_value.setTypeface(appTypeface.getPro_narMedium());
        tv_ride_rate_time_text.setTypeface(appTypeface.getPro_News());
        tv_ride_rate_time_value.setTypeface(appTypeface.getPro_narMedium());
        tv_ride_rate_submit.setTypeface(appTypeface.getPro_narMedium());
    }

    @OnClick(R.id.tv_ride_rate_submit)
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_ride_rate_submit:
                dismiss();
                break;
        }
    }

    @Override
    public void show()
    {
        super.show();
        tv_ride_rate_base_value.setText(baseFare);
        tv_ride_rate_distance_value.setText(distanceRate);
        tv_ride_rate_time_value.setText(timeRate);
    }

    /**
     * <h2>populateRates</h2>
     * This method is used to populate the rates
     * @param baseFare base fare to be shown
     *                 @param distanceRate distance rate to be shown
     *                                     @param timeRate time rate to be shown
     */
    void populateRates(String baseFare, String distanceRate, String timeRate)
    {
        this.baseFare =baseFare;
        this.distanceRate = distanceRate;
        this.timeRate = timeRate;
    }
}
