package com.karru.landing.wallet.wallet_transaction.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heride.rider.R;
import com.karru.landing.wallet.wallet_transaction.model.CreditDebitTransactionsModel;
import com.karru.util.DateFormatter;
import com.karru.util.TimezoneMapper;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import java.util.ArrayList;
import java.util.TimeZone;

import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;

/**
 * <h1>WalletTransactionsAdapter</h1>
 * This class is used to inflate the wallet transactions list
 */
public class WalletTransactionsAdapter extends RecyclerView.Adapter<WalletTransactionsAdapter.WalletViewHolder>
{
    private AppTypeface appTypeface;
    private Context mContext;
    private com.karru.util.Utility utility;
    private ArrayList<CreditDebitTransactionsModel> transactionsAL;
    private DateFormatter dateFormatter;

    /**
     * <h2>WalletTransactionsAdapter</h2>
     * This is the constructor of our adapter.
     */
    WalletTransactionsAdapter(Context context, ArrayList<CreditDebitTransactionsModel> _transactionsAL,
                              com.karru.util.Utility utility,DateFormatter dateFormatter)
    {
        this.utility = utility;
        this.mContext = context;
        this.dateFormatter = dateFormatter;
        this.transactionsAL = _transactionsAL;
        this.appTypeface = AppTypeface.getInstance(mContext);
    }

    @Override
    public WalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_transactions, parent, false);
        return new WalletViewHolder(viewList);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(WalletViewHolder walletViewHolder, final int position)
    {
        CreditDebitTransactionsModel walletDataDetailsItem = transactionsAL.get(position);
        String timeZoneString =  TimezoneMapper.latLngToTimezoneString(Double.parseDouble(GMT_CURRENT_LAT),
                Double.parseDouble(GMT_CURRENT_LNG));
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);

        if(walletDataDetailsItem.getTxnType().equalsIgnoreCase("DEBIT"))
        {
            walletViewHolder.iv_wallet_transaction_arrow.setBackgroundColor(mContext.getResources().getColor(R.color.red_light));
            walletViewHolder.iv_wallet_transaction_arrow.setRotation(90);
        }
        else
        {
            walletViewHolder.iv_wallet_transaction_arrow.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            walletViewHolder.iv_wallet_transaction_arrow.setRotation(-90);
        }

        walletViewHolder.tv_wallet_transactionId.setText(
                mContext.getString(R.string.transactionID)+ " " +walletDataDetailsItem.getTxnId().trim());

        if(walletDataDetailsItem.getTripId().isEmpty() || walletDataDetailsItem.getTripId().equalsIgnoreCase("N/A"))
            walletViewHolder.tv_wallet_transaction_bid.setVisibility(View.GONE);
        else
            walletViewHolder.tv_wallet_transaction_bid.setText(mContext.getString(R.string.bookingID) + walletDataDetailsItem.getTripId());

        walletViewHolder.tv_wallet_transaction_amount.setText(utility.currencyAdjustment(walletDataDetailsItem.getCurrencyAbbr(),walletDataDetailsItem.getCurrency(),
                Utility.getFormattedPrice(walletDataDetailsItem.getAmount().trim()) ));
        walletViewHolder.tv_wallet_transaction_description.setText(walletDataDetailsItem.getTrigger().trim());
        walletViewHolder.tv_wallet_transaction_date.setText(dateFormatter.getDateCurrentTimeZone(walletDataDetailsItem.getTxnDate().trim()));
    }

    @Override
    public int getItemCount() {
        return transactionsAL.size();
    }

    /**
     * <h1>ListViewHolder</h1>
     * <p>
     *   This method is used to hold the views
     * </p>
     */
    class WalletViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_wallet_transactionId, tv_wallet_transaction_bid, tv_wallet_transaction_amount;
        TextView tv_wallet_transaction_description, tv_wallet_transaction_date;
        ImageView iv_wallet_transaction_arrow ;

        WalletViewHolder(View cellView)
        {
            super(cellView);
            tv_wallet_transactionId = cellView.findViewById(R.id.tv_wallet_transactionId);
            tv_wallet_transactionId.setTypeface(appTypeface.getPro_News());

            tv_wallet_transaction_bid = cellView.findViewById(R.id.tv_wallet_transaction_bid);
            tv_wallet_transaction_bid.setTypeface(appTypeface.getPro_News());

            tv_wallet_transaction_amount = cellView.findViewById(R.id.tv_wallet_transaction_amount);
            tv_wallet_transaction_amount.setTypeface(appTypeface.getPro_narMedium());

            tv_wallet_transaction_description = cellView.findViewById(R.id.tv_wallet_transaction_description);
            tv_wallet_transaction_description.setTypeface(appTypeface.getPro_News());

            tv_wallet_transaction_date = cellView.findViewById(R.id.tv_wallet_transaction_date);
            tv_wallet_transaction_date.setTypeface(appTypeface.getPro_News());

            iv_wallet_transaction_arrow = cellView.findViewById(R.id.iv_wallet_transaction_arrow);
        }
    }
}
