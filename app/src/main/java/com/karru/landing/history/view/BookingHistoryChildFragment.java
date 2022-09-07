package com.karru.landing.history.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.heride.rider.R;
import com.karru.landing.history.model.HistoryDataModel;
import com.karru.booking_flow.scheduled_booking.ScheduledBookingActivity;
import com.karru.util.DateFormatter;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_CANCELED;
import static com.karru.utility.Constants.BOOKING_HISTORY_TYPE;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.CANCELLED_BOOKING;
import static com.karru.utility.Constants.LOGIN_TYPE;
import static com.karru.utility.Constants.SCHEDULE_CODE;

public class BookingHistoryChildFragment extends Fragment
{
    private static final String TAG = "BookingHistoryChildFragment" ;
    @BindView(R.id.rv_history_list) public RecyclerView rv_history_list;
    @BindView(R.id.ll_rate_card_child_empty) public LinearLayout ll_rate_card_child_empty;
    @BindView(R.id.tv_rate_card_child_empty) public TextView tv_rate_card_child_empty;
    @BindString(R.string.empty_message_booking) String empty_message_booking;
    private ArrayList<HistoryDataModel> historyDataModels;
    private int historyType;  //1 ==unassigned 2 == assigned 3 ==past
    private Unbinder unbinder;
    private BookingHistoryAdapter adapter;
    private  com.karru.util.AppTypeface appTypeface;
    private String bookingId;
    private int loginType;

    public BookingHistoryChildFragment()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyDataModels =new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_rate_card_child,container,false);
        unbinder=ButterKnife.bind(this,view);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            historyDataModels = (ArrayList<HistoryDataModel>) bundle.getSerializable(Constants.BOOKING_HISTORY);
            historyType =  bundle.getInt(BOOKING_HISTORY_TYPE);
            loginType = bundle.getInt(LOGIN_TYPE);
            Utility.printLog(TAG+" list of history in child "+historyDataModels.size());
        }
        initializeViews();
        return view;
    }

    /**
     *<h2>initialize Fragment members</h2>
     * <p> method to initialize tool bar for this fragment </p>
     */
    private void initializeViews()
    {
        appTypeface = new com.karru.util.AppTypeface(getActivity());
        rv_history_list.setLayoutManager(new LinearLayoutManager(getContext()));
        showEmptyScreen();

        adapter = new BookingHistoryAdapter(getActivity(),appTypeface,historyType,this,
                new com.karru.util.Utility(),new DateFormatter(),loginType);
        adapter.provideHistoryList(historyDataModels);
        rv_history_list.setAdapter(adapter);
    }

    /**
     * <h2>openScheduleBooking</h2>
     * used to open schedule booking screen
     * @param bundle bundle of data
     */
    public void openScheduleBooking(Bundle bundle)
    {
        if(loginType != 0)
        {
            return;
        }else {
            bookingId = bundle.getString(BOOKING_ID);
            Intent schedule = new Intent(getActivity(), ScheduledBookingActivity.class);
            schedule.putExtras(bundle);
            startActivityForResult(schedule,SCHEDULE_CODE);
            getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        com.karru.utility.Utility.printLog(TAG+" onActivityResult "+resultCode+" "+requestCode);
        com.karru.utility.Utility.printLog(TAG+" onActivityResult size"+historyDataModels.size());
        switch (requestCode)
        {
            case SCHEDULE_CODE:
                switch (resultCode)
                {
                    case CANCELLED_BOOKING:
                        String bookingId = data.getStringExtra(BOOKING_ID);
                        for(int i=0;i<historyDataModels.size();i++)
                        {
                            if(historyDataModels.get(i).getBookingId().equals(bookingId))
                            {
                                historyDataModels.remove(i);
                                adapter.provideHistoryList(historyDataModels);
                                adapter.notifyDataSetChanged();
                                showEmptyScreen();
                                break;
                            }
                        }
                        break;

                    case RESULT_CANCELED:
                       /* BookingHistoryActivity bookingHistoryFragment = ((BookingHistoryActivity)BookingHistoryChildFragment.this.getParentFragment());
                        assert bookingHistoryFragment != null;
                        bookingHistoryFragment.callAPI(false);*/
                        break;
                }
                break;
        }
    }

    /**
     * <h2>showEmptyScreen</h2>
     * used to show empty screen
     */
    private void showEmptyScreen()
    {
        if(historyDataModels.isEmpty()) {
            tv_rate_card_child_empty.setText(empty_message_booking);
            ll_rate_card_child_empty.setVisibility(View.VISIBLE);
            tv_rate_card_child_empty.setTypeface(appTypeface.getPro_News());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
