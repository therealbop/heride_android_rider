package com.karru.landing.card_details;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.network.NetworkReachableActivity;
import com.heride.rider.R;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import static com.karru.utility.Constants.CARD_BRAND;
import static com.karru.utility.Constants.CARD_DETAILS;
import static com.stripe.android.model.Card.BRAND_RESOURCE_MAP;

/**
 * <h1>CardDetailsActivity</h1>
 * <p> Activity to delete cards </p>
 */
public class CardDetailsActivity extends DaggerAppCompatActivity implements CardDetailsActivityContract.View
{
	@BindString(R.string.wait) String waitMessage;
	@BindString(R.string.Card_Info) String Card_Info;
	@BindString(R.string.network_problem) String network_problem;
	@BindView(R.id.iv_back_button) ImageView iv_select_address_back;
	@BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
	@BindView(R.id.tv_deleteCard_cardNumber) TextView tv_deleteCard_cardNumber;
	@BindView(R.id.tv_deleteCard_cardName) TextView tv_deleteCard_cardName;
	@BindView(R.id.tv_deleteCard_termsConds) TextView tv_deleteCard_termsConds;
	@BindView(R.id.tv_deleteCard_expiryLabel) TextView tv_deleteCard_expiryLabel;
	@BindView(R.id.tv_deleteCard_expiryDate) TextView tv_deleteCard_expiryDate;
	@BindView(R.id.iv_delete_card) ImageView iv_delete_card;
	@BindView(R.id.cv_default_card_SetAs) CardView cv_default_card_SetAs;
	@BindView(R.id.cbDeleteCard) CheckBox cbDeleteCard;

	@Inject CardDetailsActivityContract.DeleteCardPresenter cardPresenter;
	@Inject AppTypeface appTypeface;

	private ProgressDialog pDialog;
	private String cardId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_card_details);
		ButterKnife.bind(this);
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
		initProgressBar();
		initToolBar();
		initializeViews();
	}


	public void initProgressBar()
	{
		pDialog=new ProgressDialog(this);
		pDialog.setMessage(waitMessage);
	}

	@Override
	protected void onResume() {
		super.onResume();
		cardPresenter.checkRTLConversion();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		cardPresenter.disposeObservable();
	}

	/**
	 * <h2>initToolBar</h2>
	 * <p> custom method to init tool bar </p>
	 */
	private void initToolBar()
	{
		tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
		tv_all_tool_bar_title.setText(Card_Info);
	}


	@OnClick({R.id.rl_back_button})
	public void clickEvent(android.view.View view)
	{
		switch (view.getId())
		{
			case R.id.rl_back_button:
				onBackPressed();
				break;
		}
	}

	@OnCheckedChanged(R.id.cbDeleteCard)
	public void checkListener(CompoundButton buttonView, boolean isChecked)
	{
		cardPresenter.makeCardDefault(cardId);
	}

	/**
	 * <h2>initViews</h2>
	 * <p> Initialize all view element </p>
	 */
	private void initializeViews()
	{
		Bundle bundle=getIntent().getExtras();
		boolean isDefaultCard = bundle.getBoolean("DFLT");
		cardId = bundle.getString("ID");
		String cardBrand = bundle.getString(CARD_BRAND);

		tv_deleteCard_cardNumber.setTypeface(appTypeface.getPro_News());
		tv_deleteCard_cardNumber.setText(bundle.getString("NUM"));
		tv_deleteCard_cardName.setTypeface(appTypeface.getPro_News());
		tv_deleteCard_cardName.setText(bundle.getString("NAM"));
		tv_deleteCard_expiryLabel.setTypeface(appTypeface.getPro_News());
		tv_deleteCard_cardNumber.setTypeface(appTypeface.getPro_News());
		tv_deleteCard_expiryDate.setText(bundle.getString("EXP"));
		tv_deleteCard_termsConds.setTypeface(appTypeface.getPro_News());

		if(cardBrand!=null)
			iv_delete_card.setImageResource(BRAND_RESOURCE_MAP.get(cardBrand));

		if(!isDefaultCard)
			cv_default_card_SetAs.setVisibility(android.view.View.VISIBLE);
	}

	@Override
	public void onError(String message)
	{
		Toast.makeText(this,message,Toast.LENGTH_LONG).show();
	}

	/**
	 * <h2>hideProgressBar</h2>
	 * <p> custom method to hide progress bar if its visibl </p>
	 */
	@Override
	public void hideProgressBar()
	{
		if (pDialog != null)
		{
			pDialog.cancel();
			pDialog.dismiss();
		}
	}

	@Override
	public void showProgressBar() {
		if (pDialog != null)
		{
			pDialog.show();
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
	}

	@Override
	public void onResponse(String message)
	{
		Toast.makeText(this,message,Toast.LENGTH_LONG).show();
		setResult(RESULT_OK);
		this.onBackPressed();
	}
}
