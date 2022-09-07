package com.karru.landing.home.view;

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
import com.heride.rider.R;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.util.AppTypeface;
import java.util.ArrayList;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1>FareBreakdownDialog</h1>
 * Used to show the fare estimate breakdown
 * @author 3Embed
 * @since on 2/19/2018.
 */
public class FareBreakdownDialog extends Dialog
{
    private AppTypeface appTypeface;
    private  ArrayList<ReceiptDetails> listOfBreakDown;

    @BindView(R.id.tv_fare_header) TextView tv_fare_header;
    @BindView(R.id.tv_fare_note) TextView tv_fare_note;
    @BindView(R.id.btn_fare_got_it) TextView btn_fare_got_it;
    @BindView(R.id.rv_fare_breakdown_list) RecyclerView rv_fare_breakdown_list;
    @BindString(R.string.distance_fare) String distance_fare;
    @BindString(R.string.time_fare) String time_fare;

    private FareBreakDownAdapter fareBreakDownAdapter;


    public FareBreakdownDialog(@NonNull Context context, AppTypeface appTypeface,
                               FareBreakDownAdapter fareBreakDownAdapter)
    {
        super(context);
        this.fareBreakDownAdapter = fareBreakDownAdapter;
        this.appTypeface = appTypeface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_fare_estimate);
        initialize();
    }

    @OnClick(R.id.btn_fare_got_it)
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_fare_got_it:
                dismiss();
                break;
        }
    }

    /**
     * <h2>initialize</h2>
     * This method is used to initialize
     */
    private void initialize()
    {
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

        tv_fare_header.setTypeface(appTypeface.getPro_narMedium());
        btn_fare_got_it.setTypeface(appTypeface.getPro_narMedium());
        tv_fare_note.setTypeface(appTypeface.getPro_News());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_fare_breakdown_list.setLayoutManager(layoutManager);
        rv_fare_breakdown_list.setAdapter(fareBreakDownAdapter);
    }

    @Override
    public void show()
    {
        super.show();
        fareBreakDownAdapter.populateReceiptList(listOfBreakDown);
    }

    /**
     * <h2>populateData</h2>
     * used to populate the data
     */
    public void populateData( ArrayList<ReceiptDetails> listOfBreakDown)
    {
        this.listOfBreakDown = listOfBreakDown;
    }
}
