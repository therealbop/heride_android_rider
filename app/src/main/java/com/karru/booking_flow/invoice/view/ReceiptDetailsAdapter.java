package com.karru.booking_flow.invoice.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heride.rider.R;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;

/**
 * <h1>FareBreakDownAdapter</h1>
 * This class is used to inflate the receipt details list
 * @author 3Embed
 * @since on 06-02-2018.
 */
public class ReceiptDetailsAdapter extends RecyclerView.Adapter<ReceiptDetailsAdapter.ReceiptDetailsHolder>
{
    private static final String TAG = "FareBreakDownAdapter";
    private Context context;
    private AppTypeface appTypeface;
    private ArrayList<ReceiptDetails> receiptDetails =new ArrayList<>();

    /**
     * <h2>FareBreakDownAdapter</h2>
     * This is constructor which is used to get the context and list of receipt details
     * @param context Context of the class from which it is called
     */
    public ReceiptDetailsAdapter(Context context, AppTypeface appTypeface)
    {
        this.context=context;
        this.appTypeface=appTypeface;
    }

    /**
     * <h2>populateReceiptList</h2>
     * to populate the list with receipt details
     * @param receiptDetails receipt details
     */
    public void populateReceiptList(ArrayList<ReceiptDetails> receiptDetails)
    {
        this.receiptDetails = receiptDetails;
        notifyDataSetChanged();
    }

    @Override
    public ReceiptDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_receipt_details,parent,false);
        return new ReceiptDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReceiptDetailsHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        Utility.printLog(TAG+ "onBindViewHolder: O"+receiptDetails.get(position).getReceiptText());
        holder.tv_receipt_details_text.setText(receiptDetails.get(position).getReceiptText());
        holder.tv_receipt_details_value.setText(receiptDetails.get(position).getReceiptValue());

        if(receiptDetails.get(position).isSubTotal() || receiptDetails.get(position).isGrandTotal())
        {
            holder.tv_receipt_details_devider.setVisibility(View.VISIBLE);
            holder.tv_receipt_details_text.setTypeface(appTypeface.getPro_narMedium());
            holder.tv_receipt_details_value.setTypeface(appTypeface.getPro_narMedium());
            if(receiptDetails.get(position).isSubTotal())
            {
                holder.tv_receipt_details_text.setTextColor(context.getResources().getColor(R.color.vehicle_unselect_color));
                holder.tv_receipt_details_value.setTextColor(context.getResources().getColor(R.color.vehicle_unselect_color));
            }
        }
        else
        {
            holder.tv_receipt_details_text.setTypeface(appTypeface.getPro_News());
            holder.tv_receipt_details_value.setTypeface(appTypeface.getPro_News());
        }
    }
    @Override
    public int getItemCount()
    {
        return receiptDetails.size();
    }

    /**
     * <h2>ReceiptDetailsHolder</h2>
     * This method is used to hold the views
     */
    class ReceiptDetailsHolder extends RecyclerView.ViewHolder
    {
        ReceiptDetailsHolder(View itemView)
        {
            super(itemView);
            tv_receipt_details_text =  itemView.findViewById(R.id.tv_receipt_details_text);
            tv_receipt_details_value =  itemView.findViewById(R.id.tv_receipt_details_value);
            tv_receipt_details_devider =  itemView.findViewById(R.id.tv_receipt_details_divider);
        }
        TextView tv_receipt_details_text,tv_receipt_details_value;
        View tv_receipt_details_devider;
    }
}
