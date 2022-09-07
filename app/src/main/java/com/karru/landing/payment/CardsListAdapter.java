package com.karru.landing.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.landing.payment.model.CardDetails;
import com.karru.util.AppTypeface;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

/**
 * This is an inner class of cardsListAdapter type, to integrate the list on screen.
 */
public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.ViewHolder>
{
    private Context context;
    private AppTypeface appTypeface;
    private PaymentActivityContract.View paymentFragmentView;
    private List<CardDetails> cards = new ArrayList<>();
    private boolean isFromBooking;
    private PaymentActivityContract.Presenter presenter;

    public CardsListAdapter(Context context)
    {
        this.context = context;
        appTypeface=AppTypeface.getInstance(context);
    }

    void provideCardsList(PaymentActivityContract.View paymentView, List<CardDetails> cards,
                          PaymentActivityContract.Presenter presenter,boolean isFromBooking)
    {
        this.isFromBooking = isFromBooking;
        this.paymentFragmentView = paymentView;
        this.presenter = presenter;
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        android.view.View view = LayoutInflater.from(context).inflate(R.layout.item_card_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        CardDetails cardDetailsDataModel = cards.get(position);
        holder.iv_payment_card.setImageResource(BRAND_RESOURCE_MAP.get(cardDetailsDataModel.getBrand()));
        holder.tv_payment_card_number.setText(context.getString(R.string.card_ending_with) + " " + cardDetailsDataModel.getLast4());
        holder.tv_payment_card_number.setTypeface(appTypeface.getPro_News());

        if (cardDetailsDataModel.getDefault())
        {
            holder.iv_payment_tick.setVisibility(android.view.View.VISIBLE);
            presenter.saveDefaultCard(cards.get(position));
        }
        else
            holder.iv_payment_tick.setVisibility(android.view.View.GONE);

        holder.iv_payment_delete.setOnClickListener(v -> paymentFragmentView.onClickOfDelete(position));

        holder.rl_payment_layout.setOnClickListener(v ->
                presenter.checkForCardSelect(cards.get(position),isFromBooking,cardDetailsDataModel.getDefault()));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_payment_card) ImageView iv_payment_card;
        @BindView(R.id.iv_payment_tick) ImageView iv_payment_tick;
        @BindView(R.id.iv_payment_delete) ImageView iv_payment_delete;
        @BindView(R.id.tv_payment_card_number) TextView tv_payment_card_number;
        @BindView(R.id.rl_payment_layout) RelativeLayout rl_payment_layout;

        ViewHolder(android.view.View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
