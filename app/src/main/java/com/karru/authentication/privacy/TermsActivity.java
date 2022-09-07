package com.karru.authentication.privacy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.heride.rider.R;
import com.karru.util.AppTypeface;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.PRIVACY_LINK;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.TERMS_LINK;
import static com.karru.utility.Constants.WEB_LINK;

/**
 * <h1>Terms & Condition Activity</h1>
 * This class is used to provide the Terms & Condition screen, where we can check the Terms & Condition.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class TermsActivity extends AppCompatActivity
{
	private AppTypeface appTypeface;
	private String termsLink="",privacyLink="";

	@BindView(R.id.rlTermsConditions) RelativeLayout rlTermsConditions;
	@BindView(R.id.rlPrivacyPolicy) RelativeLayout rlPrivacyPolicy;
	@BindView(R.id.tvTermsConditions) TextView tvTermsConditions;
	@BindView(R.id.tvPrivacyPolicy) TextView tvPrivacyPolicy;
	@BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_terms_cond);

		appTypeface = AppTypeface.getInstance(this);
		initializeVariables();
	}

	/**
	 * <h2>initializeVariables</h2>
	 * <p>
	 *     here we are initialize all view element of this activity
	 * </p>
	 */
	private void initializeVariables()
	{
		ButterKnife.bind(this);
		termsLink = getIntent().getStringExtra(TERMS_LINK);
		privacyLink = getIntent().getStringExtra(PRIVACY_LINK);
		tvTermsConditions.setTypeface(appTypeface.getPro_News());
		tvPrivacyPolicy.setTypeface(appTypeface.getPro_News());
		tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
		tv_all_tool_bar_title.setText(getString(R.string.legal));
	}

	@OnClick({R.id.rl_back_button,R.id.rlTermsConditions,R.id.rlPrivacyPolicy,R.id.iv_back_button})
	public void clickEvent(View v)
	{
		switch (v.getId())
		{
			case R.id.rl_back_button:
			case R.id.iv_back_button:
				onBackPressed();
				break;

			case R.id.rlTermsConditions:
				Intent intentTerms =new Intent(this,WebViewActivity.class);
				intentTerms.putExtra(WEB_LINK, termsLink);
				intentTerms.putExtra(SCREEN_TITLE, getResources().getString(R.string.terms_and_conditions));
				intentTerms.putExtra(COMING_FROM, "web");
				startActivity(intentTerms);
				overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
				break;

			case R.id.rlPrivacyPolicy:
				Intent intentPrivacy =new Intent(this,WebViewActivity.class);
				intentPrivacy.putExtra(WEB_LINK, privacyLink);
				intentPrivacy.putExtra(SCREEN_TITLE, getResources().getString(R.string.privacy_policy));
				intentPrivacy.putExtra(COMING_FROM, "web");
				startActivity(intentPrivacy);
				overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
				break;
		}
	}

	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
	}
}
