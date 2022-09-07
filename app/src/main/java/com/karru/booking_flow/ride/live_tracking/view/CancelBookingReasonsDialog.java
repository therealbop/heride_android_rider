package com.karru.booking_flow.ride.live_tracking.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.karru.booking_flow.ride.live_tracking.LiveTrackingContract;
import com.heride.rider.R;
import com.karru.booking_flow.scheduled_booking.ScheduledBookingContract;
import com.karru.util.AppTypeface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>CancelBookingReasonsDialog</h1>
 * created to show the cancel reasons on dialog
 * @author 3emebd
 * @since on 2/13/2018.
 */

public class CancelBookingReasonsDialog extends Dialog
{
    private Context mContext;
    private AppTypeface appTypeface;
    private ArrayList<String> cancelReasons = new ArrayList<>();
    private CancelBookingAdapter cancelBookingAdapter;
    private LiveTrackingContract.View view;
    private ScheduledBookingContract.View scheduleView;

    @BindView(R.id.tv_cancel_reason_title1) TextView tv_cancel_reason_title1;
    @BindView(R.id.tv_cancel_reason_title2) TextView tv_cancel_reason_title2;
    @BindView(R.id.rv_cancel_reasons) RecyclerView rv_cancel_reasons;
    @BindView(R.id.tv_alert_no) TextView tv_alert_no;
    @BindView(R.id.tv_alert_yes) TextView tv_alert_yes;
    @BindColor(R.color.order_status) int order_status;
    @BindColor(R.color.darkGray) int darkGray;

    public CancelBookingReasonsDialog(@NonNull Context context, AppTypeface appTypeface,
                               CancelBookingAdapter cancelBookingAdapter, LiveTrackingContract.View view)
    {
        super(context);
        this.view =view;
        this.appTypeface =appTypeface;
        this.mContext =context;
        this.cancelBookingAdapter =cancelBookingAdapter;
    }

    public CancelBookingReasonsDialog(@NonNull Context context, AppTypeface appTypeface,
                                      CancelBookingAdapter cancelBookingAdapter, ScheduledBookingContract.View scheduleView)
    {
        super(context);
        this.scheduleView = scheduleView;
        this.appTypeface =appTypeface;
        this.mContext =context;
        this.cancelBookingAdapter =cancelBookingAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cancel_reasons);
        initialize();
    }

    @OnClick({R.id.tv_alert_yes,R.id.tv_alert_no})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_alert_yes:
                dismiss();
                break;

            case R.id.tv_alert_no:
                dismiss();
                if(this.view != null)
                    this.view.onclickOfConfirmCancel();
                if(scheduleView != null)
                    this.scheduleView.onclickOfConfirmCancel();
                break;
        }
    }

    /**
     * <h2>populateWithDetails</h2>
     * used to populate the dialog with details
     */
    public void populateWithDetails(JSONArray jsonArray)
    {
        cancelReasons.clear();
        if(cancelBookingAdapter != null)
            cancelBookingAdapter.clearAllCheckMarks();
        disableCancelButton();
        for (int count=0; count<jsonArray.length();count++)
        {
            try
            {
                cancelReasons.add(jsonArray.getString(count));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        show();
    }

    /**
     * <h2>initialize</h2>
     * used to initialize the views
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_cancel_reasons.setLayoutManager(layoutManager);
        rv_cancel_reasons.setAdapter(cancelBookingAdapter);
        cancelBookingAdapter.populateReasonsList(cancelReasons);

        tv_cancel_reason_title1.setTypeface(appTypeface.getPro_narMedium());
        tv_cancel_reason_title2.setTypeface(appTypeface.getPro_News());
        tv_alert_no.setTypeface(appTypeface.getPro_narMedium());
        tv_alert_yes.setTypeface(appTypeface.getPro_News());
    }

    /**
     * <h2>enableCancelButton</h2>
     * enable cancel button for cancel dialog
     */
    public void enableCancelButton()
    {
        tv_alert_no.setTextColor(order_status);
        tv_alert_no.setEnabled(true);
    }

    /**
     * <h2>enableCancelButton</h2>
     * enable cancel button for cancel dialog
     */
    private void disableCancelButton()
    {
        if(tv_alert_no!=null)
        {
            tv_alert_no.setTextColor(darkGray);
            tv_alert_no.setEnabled(false);
        }
    }
}
