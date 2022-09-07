package com.karru.landing.payment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import com.karru.landing.add_card.AddCardActivity;
import com.heride.rider.R;
import com.karru.landing.card_details.CardDetailsActivity;
import com.karru.landing.payment.model.CardDetails;
import com.karru.util.Alerts;
import com.karru.util.AppTypeface;
import com.karru.utility.Constants;
import com.karru.utility.Utility;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.ADD_CARD_REQUEST;
import static com.karru.utility.Constants.CARD_BRAND;
import static com.karru.utility.Constants.CARD_DETAILS_REQUEST;
import static com.karru.utility.Constants.IS_FROM_BOOKING;
import static com.karru.utility.Constants.SCREEN_TITLE;

/**
 * <h1>Payment Screen</h1>
 * This class is used to provide the Payment screen, where we can add our card details.
 */
public class PaymentActivity extends DaggerAppCompatActivity implements PaymentActivityContract.View
{
    private static final String TAG = "PaymentActivity";
    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.tv_payment_add_card) TextView tv_payment_add_card;
    @BindView(R.id.rv_payment_cards) RecyclerView rv_payment_cards;
    @BindString(R.string.select_card) String select_card;
    @BindString(R.string.load) String load;
    @BindString(R.string.load_saved_cards) String load_saved_cards;
    @BindString(R.string.deleting_cards) String deleting_cards;
    @BindString(R.string.bad_gateway) String badGateWay;
    @BindString(R.string.something_went_wrong) String somethingWrongMsg;
    @BindString(R.string.network_problem) String networkProblem;
    @BindString(R.string.alert) String alert;
    @BindString(R.string.delete_alert) String delete_alert;
    @BindString(R.string.confirm) String confirm;
    @BindString(R.string.cancel) String cancel;

    @Inject PaymentActivityContract.Presenter presenter;
    @Inject AppTypeface appTypeface;
    @Inject Alerts alerts;
    @Inject CardsListAdapter cardsListAdapter;

    private List<CardDetails> cardDetailsList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private boolean isFromBooking;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkRTLConversion();
    }

    /**
     * <h2>initViews</h2>
     * <p>Initializing view elements</p>
     */
    private void initViews()
    {
        ButterKnife.bind(this);
        progressDialog = alerts.getProcessDialog(this);
        progressDialog.setMessage(load);
        progressDialog.setCancelable(false);
        isFromBooking =  getIntent().getBooleanExtra(IS_FROM_BOOKING,false);

        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_payment_add_card.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setText(getIntent().getStringExtra(SCREEN_TITLE));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_payment_cards.setLayoutManager(layoutManager);
        rv_payment_cards.setAdapter(cardsListAdapter);

        presenter.extractSavedCards();
    }

    @OnClick({R.id.tv_payment_add_card,R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(android.view.View view)
    {
        switch (view.getId())
        {
            case R.id.tv_payment_add_card:
                Intent intent = new Intent(this, AddCardActivity.class);
                startActivityForResult(intent, ADD_CARD_REQUEST);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.rl_back_button:
            case R.id.iv_back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onSelectOfCard()
    {
        setResult(RESULT_OK);
        onBackPressed();
    }

    @Override
    public void onClickOfItem(CardDetails cardDetails)
    {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtras(createBundle(cardDetails));
        startActivityForResult(intent, CARD_DETAILS_REQUEST);
    }

    /**
     * <h>Bundle Creation</h>
     * <p>this method is using to Create Bundle object </p>
     * @param row_details is using to get the FragmentAdapter values
     * @return bundle object to send to next activity
     */
    public Bundle createBundle(CardDetails row_details)
    {
        String expDate = row_details.getExpMonth();
        if (expDate.length() == 1) {
            expDate = "0" + expDate;
        }
        expDate = expDate + "/" + row_details.getExpYear();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NUMBER, row_details.getLast4());
        bundle.putString(Constants.EXPIRE, expDate);
        bundle.putString(Constants.ID, row_details.getId());
        bundle.putString(Constants.SNAME, row_details.getName());
        bundle.putBoolean(Constants.DEFAULT,row_details.getDefault());
        bundle.putString(CARD_BRAND, row_details.getBrand());
        return bundle;
    }

    @Override
    public void savedCardsDetails(ArrayList<CardDetails> cardDetailsDataModels)
    {
        cardDetailsList = cardDetailsDataModels;
        cardsListAdapter.provideCardsList(this,cardDetailsDataModels,presenter,isFromBooking);
    }

    @Override
    public void errorResponse(String errorMsg)
    {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        presenter.disposeObservables();
    }

    @Override
    public void onClickOfDelete(int position)
    {
        CardDetails row_details = cardDetailsList.get(position);
        String cardId=row_details.getId();

        Dialog deleteDialog = alerts.userPromptWithTwoButtons(this);
        TextView tv_alert_title = deleteDialog.findViewById(R.id.tv_alert_title);
        TextView tv_alert_body = deleteDialog.findViewById(R.id.tv_alert_body);
        TextView tv_alert_yes = deleteDialog.findViewById(R.id.tv_alert_yes);
        TextView tv_alert_no = deleteDialog.findViewById(R.id.tv_alert_no);
        tv_alert_title.setText(alert);
        tv_alert_body.setText(delete_alert);
        tv_alert_yes.setText(confirm);
        tv_alert_no.setText(cancel);

        tv_alert_yes.setOnClickListener(view ->
        {
            progressDialog.setMessage(deleting_cards);
            presenter.deleteCard(cardId,position);
            deleteDialog.dismiss();
        });
        deleteDialog.show();
    }

    /**
     * <h2>deleteItemData</h2>
     * used to deleted the item
     * @param position position of item to be deleted
     */
    public void deleteItemData(int position,ArrayList<CardDetails> cardsDetailsLists)
    {
        cardDetailsList.remove(position);
        this.cardDetailsList = cardsDetailsLists;
        cardsListAdapter.provideCardsList(this, cardDetailsList,presenter,isFromBooking);
    }

    @Override
    public void badGateWayError()
    {
        Toast.makeText(this, badGateWay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismissProgressDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing() && !isFinishing())
            progressDialog.dismiss();
    }

    @Override
    public void showProgressDialog()
    {
        if(progressDialog!=null && !progressDialog.isShowing() && !isFinishing())
            progressDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Utility.printLog(TAG +" onActivityResult " +requestCode+" "+resultCode);
        switch (requestCode)
        {
            case ADD_CARD_REQUEST:
            case CARD_DETAILS_REQUEST:
                presenter.extractSavedCards();
                break;
        }
    }
}
