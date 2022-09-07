package com.karru.landing.add_card;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karru.network.NetworkReachableActivity;
import com.heride.rider.R;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.stripe.android.view.CardInputWidget;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import dagger.android.support.DaggerAppCompatActivity;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static com.karru.utility.Constants.ADD_CARD;
import static com.karru.utility.Constants.PERMISSION_REQUEST;
import static com.karru.utility.Constants.REQUEST_CODE_PERMISSION_MULTIPLE;

/**
 * <h>AddCardActivity</h>
 * <p> Activity to add new card to make payment </p>
 */
public class AddCardActivity extends DaggerAppCompatActivity implements AddCardActivityContract.AddCardView
{
	@BindView(R.id.rl_addCard_poweredBy)	RelativeLayout rl_addCard_poweredBy;
	@BindView(R.id.tv_addCard_scanCard) TextView tv_addCard_scanCard;
	@BindView(R.id.tv_addCard_done) TextView tv_addCard_done;
	@BindView(R.id.rl_addCard_scanCard) RelativeLayout rl_addCard_scanCard;
	@BindView(R.id.rl_addCard_done) RelativeLayout rl_addCard_done;
	@BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
	@BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;
	@BindColor(R.color.white) int white;
	@BindColor(R.color.text_color) int text_color;
	@BindString(R.string.You_did_not_enter_valid_card) String You_did_not_enter_valid_card;
	@BindString(R.string.addcard) String addcard;
	@BindString(R.string.progressMessage) String progressMessage;

	@Inject Alerts alerts;
	@Inject	AppTypeface appTypeface;
	@Inject AppPermissionsRunTime permissionsRunTime;
	@Inject AddCardActivityContract.AddCardPresenter presenter;

	private boolean scanflag = false;
	private CreditCard scanResult;
	private CardInputWidget card_input_widget_card;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
		setContentView(R.layout.activity_add_card);
		ButterKnife.bind(this);

		initializeViews();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		presenter.disposeObservable();
	}

	/**
	 * <h2>initViews</h2>
	 * <p> method to initializes the views of this activit </p>
	 */
	private void initializeViews()
	{
		tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
		tv_all_tool_bar_title.setText(addcard);

		card_input_widget_card=findViewById(R.id.card_input_widget_card);
		tv_addCard_done.setTypeface(appTypeface.getPro_News());
		tv_addCard_scanCard.setTypeface(appTypeface.getPro_News());

		TextView tvPoweredBy_addCard =  findViewById(R.id.tv_addCard_poweredBy);
		tvPoweredBy_addCard.setTypeface(appTypeface.getPro_News());

		EditText et_card_number = card_input_widget_card.findViewById( R.id.et_card_number);
		et_card_number.setTypeface(appTypeface.getPro_News());
		et_card_number.setTextSize(16);

		EditText et_expiry_date = card_input_widget_card.findViewById(R.id.et_expiry_date);
		et_expiry_date.setTypeface(appTypeface.getPro_News());
		et_expiry_date.setTextSize(16);

		EditText et_cvc_number= card_input_widget_card.findViewById(R.id.et_cvc_number);
		et_cvc_number.setTextSize(16);
		et_cvc_number.setTypeface(appTypeface.getPro_News());

		progressDialog = alerts.getProcessDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(progressMessage);
	}

	@OnEditorAction(R.id.tv_addCard_done)
	public boolean onEditEvent(TextView v, int actionId, KeyEvent event)
	{
		if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE)
		{
			presenter.validateCardDetails(card_input_widget_card.getCard());
			return true;
		}
		return false;
	}

	@OnClick({R.id.tv_addCard_done,R.id.tv_addCard_scanCard,R.id.rl_back_button})
	public void clickEvent(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_addCard_done:
				presenter.validateCardDetails(card_input_widget_card.getCard());
				break;

			case R.id.tv_addCard_scanCard:
				if (Build.VERSION.SDK_INT >= 23) {
					ArrayList<AppPermissionsRunTime.Permission> myPermissionConstantsArrayList = new ArrayList<>();
					myPermissionConstantsArrayList.clear();
					myPermissionConstantsArrayList.add(AppPermissionsRunTime.Permission.CAMERA);

					if (permissionsRunTime.getPermission(myPermissionConstantsArrayList, this, true,PERMISSION_REQUEST))
						onScanPress();
				}
				else
					onScanPress();
				break;

			case R.id.rl_back_button:
				onBackPressed();
				break;
			default:
				break;
		}
	}

	/**
	 *<h2>onScanPress</h2>
	 * This method is used to open the scan card activity
	 */
	private void onScanPress()
	{
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
		scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
		scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO,true);
		scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON,false);
		scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME,true );
		int MY_SCAN_REQUEST_CODE = 100;
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}

	/**
	 *<h2>callDoneButton</h2>
	 * <p>This method is used to create the stripe token</p>
	 */
	public void callDoneButton()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tv_addCard_done.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		rl_addCard_poweredBy.setVisibility(View.GONE);
		updateUI(false,colorPrimaryDark,text_color);
		presenter.generateStripeToken(card_input_widget_card.getCard());
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		presenter.checkRTLConversion();
		if(scanflag)
			scanCardResult();
	}

	/**
	 * <h2>scanCardResult</h2>
	 * <p> method to get values from the scanned card </p>
	 */
	private void scanCardResult()
	{
		card_input_widget_card.setCardNumber(scanResult.cardNumber);
		if(scanResult.expiryMonth>=1 && scanResult.expiryMonth<=12)
			card_input_widget_card.setExpiryDate(scanResult.expiryMonth,scanResult.expiryYear);
		card_input_widget_card.setCvcCode(scanResult.cvv);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
		{
			scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
			scanflag = true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		boolean isDenine = false;
		switch (requestCode)
		{
			case REQUEST_CODE_PERMISSION_MULTIPLE:
				for (int grantResult : grantResults) {
					if (grantResult != PackageManager.PERMISSION_GRANTED) {
						isDenine = true;
					}
				}
				if (isDenine)
				{
					Log.i("Permission","Denied ");
				}
				else
				{
					onScanPress();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				break;
		}
	}

	@Override
	public void showProgressDialog()
	{
		if(progressDialog !=null && !progressDialog.isShowing() && !isFinishing())
			progressDialog.show();
	}

	@Override
	public void dismissProgressDialog()
	{
		if(progressDialog !=null && progressDialog.isShowing() && !isFinishing())
			progressDialog.dismiss();
	}

	@Override
	public void updateUI(boolean b, int color, int color1)
	{
		rl_addCard_done.setBackgroundColor(color);
		tv_addCard_done.setTextColor(color1);
		tv_addCard_done.setClickable(b);
		tv_addCard_done.setEnabled(b);

		rl_addCard_scanCard.setBackgroundColor(color);
		tv_addCard_scanCard.setTextColor(color1);
		tv_addCard_scanCard.setClickable(b);
		tv_addCard_scanCard.setEnabled(b);
	}

	@Override
	public void finishActivity()
	{
		finish();
	}

	@Override
	public void onAddCardError(String errorMsg)
	{
		rl_addCard_poweredBy.setVisibility(View.VISIBLE);
		updateUI(true,colorPrimaryDark,white);
		Toast.makeText(AddCardActivity.this, errorMsg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onValidOfCard()
	{
		callDoneButton();
	}

	@Override
	public void onInvalidOfCard() {
		Toast.makeText(this,You_did_not_enter_valid_card,Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCardAdded()
	{
		setResult(RESULT_OK);
		this.onBackPressed();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
		finish();
	}
}
