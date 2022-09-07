package com.karru.landing.history.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.heride.rider.R;
import com.karru.booking_flow.ride.live_tracking.view.LiveTrackingActivity;
import com.karru.landing.history.history_details.view.HistoryDetailsActivity;
import com.karru.landing.history.model.HistoryDataModel;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;
import com.karru.util.TimezoneMapper;
import com.karru.utility.Utility;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.karru.utility.Constants.BOOKING_DATE;
import static com.karru.utility.Constants.BOOKING_ID;
import static com.karru.utility.Constants.BOOKING_TIME;
import static com.karru.utility.Constants.DROP_ADDRESS;
import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.LOGIN_TYPE;
import static com.karru.utility.Constants.PICK_ADDRESS;
import static com.karru.utility.Constants.PICK_LAT;
import static com.karru.utility.Constants.PICK_LONG;
import static com.karru.utility.Constants.VEHICLE_NAME;

public class BookingHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = "BookingHistoryAdapter";
    private AppTypeface appTypeface;
    private ArrayList<HistoryDataModel> historyDataList = new ArrayList<>();
    private int historyType;
    private Activity activity;
    private com.karru.util.Utility utility;
    private DateFormatter dateFormatter;
    private BookingHistoryChildFragment bookingHistoryChildFragment;
    private int loginType;

    /**
     * This is the constructor of our adapter.
     * //@param reasons contains an array list.
     * //@param rv_OnItemViewsClickNotifier reference of OnItemViewClickNotifier Interface.
     */
    BookingHistoryAdapter(Activity activity, AppTypeface appTypeface, int historyType,
                          BookingHistoryChildFragment bookingHistoryChildFragment, com.karru.util.Utility utility,
                          DateFormatter dateFormatter, int loginType)
    {
        this.utility = utility;
        this.dateFormatter = dateFormatter;
        this.appTypeface = appTypeface;
        this.activity = activity;
        this.bookingHistoryChildFragment = bookingHistoryChildFragment;
        this.historyType = historyType;   //1 - un assigned 2: assigned 3 : past
        this.loginType = loginType;
    }

    /**
     * <h2>provideFeedbackList</h2>
     * This method is used to notify the adapter with the list of feedback
     * @param historyDataList list of feedback
     */
    void provideHistoryList(ArrayList<HistoryDataModel> historyDataList)
    {
        this.historyDataList.clear();
        this.historyDataList = historyDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return historyDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent, false);
        return new BookingHistoryAdapter.ListViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position)
    {
        if(viewHolder instanceof BookingHistoryAdapter.ListViewHolder)
            initListViewHolder((BookingHistoryAdapter.ListViewHolder) viewHolder, position);
    }

    /**
     * <h2>initListViewHolder</h2>
     * <p>
     *     method to init ListViewHolder cell and also init its functionalities
     * </p>
     * @param listViewHolder: instance of ListViewHolder
     * @param position: item index
     */
    @SuppressLint("SetTextI18n")
    private void initListViewHolder(final ListViewHolder listViewHolder, final int position)
    {
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(Double.parseDouble(GMT_CURRENT_LAT),
                Double.parseDouble(GMT_CURRENT_LNG));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);

        Utility.printLog(TAG +" history status date pre "+ historyDataList.get(position).getBookingDate());
        Utility.printLog(TAG +" history status date post "+ dateFormatter.getDateInSpecificFormatWithTime
                (historyDataList.get(position).getBookingDate(), timeZone));
        listViewHolder.tv_history_date.setText(dateFormatter.getDateInSpecificFormatWithTime
                (historyDataList.get(position).getBookingDate(), timeZone));
        listViewHolder.tv_history_type.setText(historyDataList.get(position).getBusinessName());
        listViewHolder.tv_history_pick_address.setText(historyDataList.get(position).getPickupAddress());

        if(historyDataList.get(position).getDropAddress().equals(""))
            listViewHolder.ll_history_drop.setVisibility(View.GONE);
        else
            listViewHolder.tv_history_drop_address.setText(historyDataList.get(position).getDropAddress());

        switch (historyDataList.get(position).getStatus())
        {
            case 3:
            case 4:
            case 5:
                listViewHolder.iv_history_cancel.setVisibility(View.VISIBLE);
                break;

            default:
                listViewHolder.iv_history_cancel.setVisibility(View.GONE);
                break;
        }

        switch (historyType)
        {
            case 1:
                listViewHolder.tv_history_date.setText(historyDataList.get(position).getBookingDate()+" "+
                        DateFormatter.getDateWithTimeZone(historyDataList.get(position).getBookingTime(),
                                timeZone));
                listViewHolder.tv_history_amount.setText(historyDataList.get(position).getStatusText());
                listViewHolder.tv_history_amount.setTextColor(listViewHolder.colorPrimary);
                break;

            case 2:
                listViewHolder.tv_history_amount.setText(historyDataList.get(position).getStatusText());
                listViewHolder.tv_history_amount.setTextColor(listViewHolder.order_status);
                break;

            case 3:
                String currency = historyDataList.get(position).getCurrencySymbol();
                int currAbbr = historyDataList.get(position).getCurrencyAbbr();
                listViewHolder.tv_history_amount.setText(utility.currencyAdjustment(currAbbr,currency,
                        historyDataList.get(position).getAmount()));
                listViewHolder.tv_history_amount.setTextColor(listViewHolder.vehicle_unselect_color);
                break;
        }

        listViewHolder.tv_history_pick_address.setSelected(true);
        listViewHolder.tv_history_drop_address.setSelected(true);

        listViewHolder.cv_history_layout.setOnClickListener(v ->
        {
            switch (historyType)
            {
                case 1:
                    Bundle bundle = new Bundle();
                    bundle.putString(BOOKING_ID,historyDataList.get(position).getBookingId());
                    bundle.putString(PICK_ADDRESS,historyDataList.get(position).getPickupAddress());
                    bundle.putString(DROP_ADDRESS,historyDataList.get(position).getDropAddress());
                    bundle.putString(VEHICLE_NAME,historyDataList.get(position).getVehicleTypeName());
                    bundle.putString(BOOKING_DATE,historyDataList.get(position).getBookingDate());
                    bundle.putString(BOOKING_TIME,historyDataList.get(position).getBookingTime());
                    bundle.putDouble(PICK_LAT,historyDataList.get(position).getPickupLatitude());
                    bundle.putDouble(PICK_LONG,historyDataList.get(position).getPickupLongitude());
                    bookingHistoryChildFragment.openScheduleBooking(bundle);
                    break;

                case 2:
                    if(loginType != 0){
                       return;
                    }else{
                        Intent liveTrackIntent = new Intent(activity, LiveTrackingActivity.class);
                        liveTrackIntent.putExtra(BOOKING_ID,historyDataList.get(position).getBookingId());
                        activity.startActivity(liveTrackIntent);
                    }

                    break;

                case 3:
                    Intent intent = new Intent(activity, HistoryDetailsActivity.class);
                    intent.putExtra(BOOKING_ID,historyDataList.get(position).getBookingId());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    break;
            }
        });
    }

    /**
     * <h1>ListViewHolder</h1>
     * <p>
     *   This method is used to initialize the views
     * </p>
     */
    class ListViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_history_date) TextView tv_history_date;
        @BindView(R.id.tv_history_type) TextView tv_history_type;
        @BindView(R.id.tv_history_amount) TextView tv_history_amount;
        @BindView(R.id.tv_history_pick_address) TextView tv_history_pick_address;
        @BindView(R.id.tv_history_drop_address) TextView tv_history_drop_address;
        @BindView(R.id.ll_history_drop) LinearLayout ll_history_drop;
        @BindView(R.id.iv_history_cancel) ImageView iv_history_cancel;
        @BindView(R.id.cv_history_layout)
        CardView cv_history_layout;
        @BindColor(R.color.vehicle_unselect_color) int vehicle_unselect_color;
        @BindColor(R.color.order_status) int order_status;
        @BindColor(R.color.colorPrimary) int colorPrimary;
        @BindString(R.string.scheduled) String scheduled;

        ListViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tv_history_date.setTypeface(appTypeface.getPro_narMedium());
            tv_history_amount.setTypeface(appTypeface.getPro_narMedium());
            tv_history_type.setTypeface(appTypeface.getPro_News());
            tv_history_pick_address.setTypeface(appTypeface.getPro_News());
            tv_history_drop_address.setTypeface(appTypeface.getPro_News());
        }
    }
}