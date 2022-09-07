package com.karru.booking_flow.invoice.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heride.rider.R;
import com.karru.booking_flow.invoice.InvoiceContract;
import com.karru.util.AppTypeface;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceTipValueAdapter extends RecyclerView.Adapter<InvoiceTipValueAdapter.ViewHolder>
{
    private static final String TAG = "InvoiceTipValueAdapter";
    private Context mContext;
    private int tipType;
    private ArrayList<Integer> tipDriverList =new ArrayList<>();
    private int lastCheckedPosition = -1;
    private AppTypeface appTypeface;
    private String currency;
    private int currencyType;
    private InvoiceContract.Presenter presenter;

    /**
     * <h2>populateTipsList</h2>
     * used to populate the drivers tip list from backend
     * @param tipDriverList list of tips to be shown
     */
    void populateTipsList(ArrayList<Integer> tipDriverList, int tipType, Context mContext,
                          AppTypeface appTypeface, String currency, int currencyType,
                          InvoiceContract.Presenter presenter)
    {
        this.mContext = mContext;
        this.currency = currency;
        this.presenter = presenter;
        this.currencyType = currencyType;
        this.tipType = tipType;
        this.appTypeface = appTypeface;
        this.tipDriverList = tipDriverList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice_tip, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.tv_invoice_tip_item.setTypeface(appTypeface.getPro_News());

        switch (tipType)
        {
            case 1:
                switch (currencyType)
                {
                    case 1:
                        holder.tv_invoice_tip_item.setText(currency+tipDriverList.get(position));
                        break;

                    case 2:
                        holder.tv_invoice_tip_item.setText(tipDriverList.get(position)+currency);
                        break;
                }
                break;

            case 2:
                holder.tv_invoice_tip_item.setText(tipDriverList.get(position)+"%");
                break;
        }

        com.karru.utility.Utility.printLog(TAG+" current pos "+position+" last pos "+lastCheckedPosition);

        if (position == lastCheckedPosition)
        {
            holder.tv_invoice_tip_item.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_color_primary));
            holder.tv_invoice_tip_item.setTextColor(mContext.getResources().getColor(R.color.white));
        } else
        {
            holder.tv_invoice_tip_item.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_stroke_background));
            holder.tv_invoice_tip_item.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.view.setOnClickListener(view ->
        {
            if(lastCheckedPosition == holder.getAdapterPosition())
            {
                lastCheckedPosition = -1;
                presenter.updateTip(0);
            }
            else
            {
                lastCheckedPosition = holder.getAdapterPosition();
                presenter.updateTip(tipDriverList.get(lastCheckedPosition));
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return tipDriverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_invoice_tip_item) AppCompatTextView tv_invoice_tip_item;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view=itemView;
        }
    }
}
