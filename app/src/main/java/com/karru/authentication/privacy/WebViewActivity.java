package com.karru.authentication.privacy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.karru.ApplicationClass;
import com.heride.rider.R;
import com.karru.util.AppTypeface;

import static com.karru.utility.Constants.COMING_FROM;
import static com.karru.utility.Constants.SCREEN_TITLE;
import static com.karru.utility.Constants.WEB_LINK;

/**
 * <h>WebView Activity</h>
 * This class is used to provide the WebView screen, where we can
 * show the web view, and show our all data of html document.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class WebViewActivity extends AppCompatActivity
{
	private ProgressBar progressBar;
	private String comingFrom = "";
	private String title = "", url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
		((ApplicationClass) getApplicationContext()).changeLangConfig();
		Intent intent=getIntent();
		if(intent!=null)
		{
			url = getIntent().getStringExtra(WEB_LINK);
			title = getIntent().getStringExtra(SCREEN_TITLE);
			comingFrom = getIntent().getStringExtra(COMING_FROM);
		}

		initToolBar();
		initViews();
	}

	/**
	 * <h2>initToolBar</h2>
	 * <p>
	 *     method to initialize toolbar for this activity
	 * </p>
	 */
	private void initToolBar()
	{
		TextView tvToolBarTitle =  findViewById(R.id.tv_all_tool_bar_title);
		tvToolBarTitle.setTypeface(AppTypeface.getInstance(this).getPro_narMedium());
		tvToolBarTitle.setText(title);

		RelativeLayout rlBackArrow =  findViewById(R.id.rl_back_button);
		rlBackArrow.setOnClickListener(view -> onBackPressed());
	}

	/**
	 * <h2>initViews</h2>
	 */
	private void initViews()
	{
		WebView webView =  findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		progressBar =  findViewById(R.id.pBar_profileFrag);
		if(comingFrom.equals(getString(R.string.comingFrom)))
		{
			webView.loadData(url, "text/html; charset=utf-8", "UTF-8");
			progressBar.setVisibility(View.GONE);
		}
		else
		{
			webView.setWebViewClient(new MyWebViewClient());
			webView.setSaveFromParentEnabled(true);
			progressBar.setVisibility(View.GONE);
			if (validateUrl(url))
			{
				webView.loadUrl(url);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
					webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			}
		}
	}


	/**
	 * This method is used to validate the URL.
	 * @param url URL link
	 * @return flag.
	 */
	private boolean validateUrl(String url)
	{
		return Patterns.WEB_URL.matcher(url).matches();
	}

	/**
	 * <h1>MyWebViewClient</h1>
	 * <p>
	 * This is an inner class, to load the web view data.
	 * </p>
	 */
	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			super.shouldOverrideUrlLoading(view, url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			progressBar.setVisibility(View.GONE);
			progressBar.setProgress(100);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
	}

	/**
	 * <h2>setValue</h2>
	 * <p>
	 *     method to set progress value
	 * </p>
	 * @param progress: progress value
	 */
	public void setValue(int progress)
	{
		this.progressBar.setProgress(progress);
	}

	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.side_slide_out,R.anim.side_slide_in);
	}
}
