package com.karru.booking_flow.invoice.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heride.rider.R;
import com.karru.booking_flow.invoice.InvoiceContract;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

/**
 * <h1>BookingHistoryAdapter</h1>
 * This class is used to inflate the driver feedback list
 * @author 3Embed
 * @since on 05-02-2018.
 */
public class FeedbackReasonsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = "ReceiptRatingsListAdapter";
    private AppTypeface appTypeface;
    private ArrayList<String> reasonsList =new ArrayList<>();
    private ArrayList<String> reasonsSelectedList =new ArrayList<>();
    InvoiceContract.Presenter presenter;

    /**
     * This is the constructor of our adapter.
     * //@param reasons contains an array list.
     * //@param rv_OnItemViewsClickNotifier reference of OnItemViewClickNotifier Interface.
     */
    public FeedbackReasonsListAdapter(AppTypeface appTypeface)
    {
        this.appTypeface = appTypeface;
    }

    /**
     * <h2>provideFeedbackList</h2>
     * This method is used to notify the adapter with the list of feedback
     * @param reasonsList list of feedback
     */
    void provideFeedbackList(ArrayList<String> reasonsList, InvoiceContract.Presenter presenter)
    {
        this.reasonsList = reasonsList;
        this.presenter = presenter;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return reasonsList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice_feedback, parent, false);
        return new FeedbackReasonsListAdapter.ListViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position)
    {
        if(viewHolder instanceof FeedbackReasonsListAdapter.ListViewHolder)
        {
            initListViewHolder((FeedbackReasonsListAdapter.ListViewHolder) viewHolder, position);
        }
    }

    /**
     * <h2>initListViewHolder</h2>
     * <p>
     *     method to init ListViewHolder cell and also init its functionalities
     * </p>
     * @param listViewHolder: instance of ListViewHolder
     * @param position: item index
     */
    private void initListViewHolder(final ListViewHolder listViewHolder, final int position)
    {
        Utility.printLog(TAG +" reason list name "+reasonsList.get(position));
        listViewHolder.tv_receipt_rating_title.setText(reasonsList.get(position));

        listViewHolder.tv_receipt_rating_title.setOnClickListener(v ->
        {
            if(listViewHolder.tv_receipt_rating_title.isSelected())
            {
                reasonsSelectedList.remove(listViewHolder.tv_receipt_rating_title.getText().toString());
                listViewHolder.tv_receipt_rating_title.setSelected(false);
            }
            else
            {
                reasonsSelectedList.add(listViewHolder.tv_receipt_rating_title.getText().toString());
                listViewHolder.tv_receipt_rating_title.setSelected(true);
            }

            StringBuilder reasonsList = null;
            for(int userReasonCount = 0; userReasonCount<reasonsSelectedList.size(); userReasonCount++)
            {
                if(userReasonCount==0)
                    reasonsList = new StringBuilder(reasonsSelectedList.get(0));
                else
                    reasonsList.append(" ,").append(reasonsSelectedList.get(userReasonCount));
            }
            presenter.userSelectedReasonsList(reasonsList);
        });
    }

    /**
     * <h1>ListViewHolder</h1>
     * <p>
     *   This method is used to initialize the views
     * </p>
     */
    private class ListViewHolder extends RecyclerView.ViewHolder
    {
        AppCompatTextView tv_receipt_rating_title;

        ListViewHolder(View itemView)
        {
            super(itemView);
            tv_receipt_rating_title = itemView.findViewById(R.id.tv_pick_zone_title);

            tv_receipt_rating_title.setTypeface(appTypeface.getPro_News());
        }
    }
}
