package com.karru.landing.history.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.rider.R;
import com.karru.landing.history.BookingHistoryContract;
import com.karru.landing.history.model.HistoryDataModel;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import java.util.ArrayList;

import javax.inject.Inject;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class BookingHistoryActivity extends DaggerAppCompatActivity implements BookingHistoryContract.View
{
    private ProgressDialog progressDialog;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.vp_history_fragment) ViewPager vp_history_fragment;
    @BindView(R.id.tl_history_fragment) TabLayout tl_history_fragment;
    @BindString(R.string.my_bookings) String my_bookings;

    @Inject Alerts alerts;
    @Inject AppTypeface appTypeface;
    @Inject BookingHistoryContract.Presenter presenter;
    @Inject BookingHistoryPagerAdapter bookingHistoryPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        initializeToolBar();
        callAPI(true);
    }



    /**
     *<h2>initializeToolBar</h2>
     * <p> method to initialize tool bar for this fragment </p>
     */
    private void initializeToolBar()
    {
        ButterKnife.bind(this);
        tv_all_tool_bar_title.setText(my_bookings);
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.checkRTLConversion();
        presenter.subscribeObservables();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back_button:
            case R.id.rl_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    public void notifyPagerAdapter(ArrayList<HistoryDataModel> unassignedList,
                                   ArrayList<HistoryDataModel> assignedList, ArrayList<HistoryDataModel> pastList)
    {
        bookingHistoryPagerAdapter.notifyHistoryList(unassignedList,assignedList,pastList);
        vp_history_fragment.setAdapter(bookingHistoryPagerAdapter);
        tl_history_fragment.setupWithViewPager(vp_history_fragment);
    }

    @Override
    public void notifyAssignedList(ArrayList<HistoryDataModel> assignedList)
    {
        bookingHistoryPagerAdapter.notifyAssignedList(assignedList);
    }

    @Override
    public void notifyAllList(ArrayList<HistoryDataModel> unassignedList, ArrayList<HistoryDataModel> assignedList,
                              ArrayList<HistoryDataModel> pastList) {
        bookingHistoryPagerAdapter.notifyAllList(unassignedList,assignedList);
    }

    /**
     * <h2>callAPI</h2>
     * used to call API to get the data
     */
    public void callAPI(boolean first)
    {
        presenter.getBookingHistory(first);
    }

    @Override
    public void notifyUnassignedList(ArrayList<HistoryDataModel> unassignedList) {
        bookingHistoryPagerAdapter.notifyUnAssignedList(unassignedList);
    }

    @Override
    public void notifyPastList(ArrayList<HistoryDataModel> pastList) {
        bookingHistoryPagerAdapter.notifyPastList(pastList);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        presenter.disposeObservables();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

}
