package com.karru.booking_flow.ride.live_tracking.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.karru.booking_flow.ride.live_tracking.LiveTrackingContract;
import com.karru.booking_flow.scheduled_booking.ScheduledBookingContract;
import com.karru.util.AppTypeface;
import com.heride.rider.R;
import java.util.ArrayList;

/**
 * @author embed on 30/8/17.
 * <h1>CancelBookingAdapter</h1>
 * @version v1.0.0
 */
public class CancelBookingAdapter extends RecyclerView.Adapter {
    private Context context;
    private LiveTrackingContract.View view;
    private ScheduledBookingContract.View scheduleView;
    private AppTypeface appTypeface;
    private ArrayList<String> cancelReasonsList;
    private ArrayList<AppCompatTextView> listOfTextViews =new ArrayList<>();

    /**
     * <h2>CancelBookingAdapter</h2>
     * This is constructor of the adapter
     * @param context Context of the activity from which it is called
     */
    public CancelBookingAdapter(Context context, AppTypeface appTypeface, LiveTrackingContract.View view)
    {
        this.view = view;
        this.context = context;
        this.appTypeface = appTypeface;
    }

    /**
     * <h2>CancelBookingAdapter</h2>
     * This is constructor of the adapter
     * @param context Context of the activity from which it is called
     */
    public CancelBookingAdapter(Context context, AppTypeface appTypeface, ScheduledBookingContract.View view)
    {
        this.scheduleView = view;
        this.context = context;
        this.appTypeface = appTypeface;
    }

    /**
     * <h2>populateReasonsList</h2>
     * used to populate the list with the real data
     * @param cancelReasonsList list of reasons
     */
    void populateReasonsList(ArrayList<String> cancelReasonsList)
    {
        clearAllCheckMarks();
        this.cancelReasonsList = cancelReasonsList;
        notifyDataSetChanged();
    }

    /**
     * <h2>onCreateViewHolder</h2>
     * This method calls onCreateViewHolder(ViewGroup, int) to create a new RecyclerView.ViewHolder
     * <p>
     *     and initializes some private fields to be used by RecyclerView.
     * </p>
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cancel_reason, parent, false);
        return new MyCancelReasonHolder(view);
    }

    /**
     * <h2>onBindViewHolder</h2>
     * Called by RecyclerView to display the data at the specified position.
     * <p>
     *     This method should update the contents of the itemView to reflect the item at the given position.
     * </p>
     * @param holder  The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        if (holder instanceof MyCancelReasonHolder)
        {
            if(cancelReasonsList.size() == position+1)
                ((MyCancelReasonHolder) holder).vw_cancel_reason_divider.setVisibility(View.GONE);

            ((MyCancelReasonHolder) holder).tv_cancel_reason_title.setTypeface(appTypeface.getPro_News());
            ((MyCancelReasonHolder)holder).tv_cancel_reason_title.setText(cancelReasonsList.get(position));

            ((MyCancelReasonHolder) holder).cl_cancel_reason.setOnClickListener(v ->
            {
                clearAllCheckMarks();
                ((MyCancelReasonHolder) holder).tv_cancel_reason_title.setCompoundDrawablesWithIntrinsicBounds(0,0,
                        R.drawable.ic_invoice_one_tick_mark_on,0);
                listOfTextViews.add( ((MyCancelReasonHolder) holder).tv_cancel_reason_title);
                if(view != null)
                    view.enableCancelButton(cancelReasonsList.get(position));
                if(scheduleView !=null)
                    scheduleView.enableCancelButton(cancelReasonsList.get(position));
            });
        }
    }

    /**
     * <h2>clearAllCheckMarks</h2>
     * clear all checks
     */
    void clearAllCheckMarks()
    {
        for(int i = 0; i<listOfTextViews.size();i++)
        {
            listOfTextViews.get(i).setCompoundDrawablesWithIntrinsicBounds(0,0, 0,0);
        }
        listOfTextViews.clear();
    }

    /**
     * <h2>getItemCount</h2>
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return cancelReasonsList.size();
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     *
     * <p>{@link RecyclerView.Adapter} implementations should subclass ViewHolder and add fields for caching
     * potentially expensive {@link View#findViewById(int)} results.</p>
     *
     * <p>While {@link RecyclerView.LayoutParams} belong to the {@link RecyclerView.LayoutManager},
     * {@link RecyclerView.ViewHolder ViewHolders} belong to the adapter. Adapters should feel free to use
     * their own custom ViewHolder implementations to store data that makes binding view contents
     * easier. Implementations should assume that individual item views will hold strong references
     * to <code>ViewHolder</code> objects and that <code>RecyclerView</code> instances may hold
     * strong references to extra off-screen item views for caching purposes</p>
     */
    private static class MyCancelReasonHolder extends RecyclerView.ViewHolder
    {
        AppCompatTextView tv_cancel_reason_title;
        View vw_cancel_reason_divider;
        RelativeLayout cl_cancel_reason;
        MyCancelReasonHolder(View itemView) {
            super(itemView);
            tv_cancel_reason_title = itemView.findViewById(R.id.tv_cancel_reason_title);
            cl_cancel_reason = itemView.findViewById(R.id.cl_cancel_reason);
            vw_cancel_reason_divider = itemView.findViewById(R.id.vw_cancel_reason_divider);
        }
    }
}
