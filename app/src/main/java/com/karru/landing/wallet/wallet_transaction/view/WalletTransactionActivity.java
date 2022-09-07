package com.karru.landing.wallet.wallet_transaction.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.heride.rider.R;
import com.karru.landing.wallet.wallet_transaction.WalletTransactionContract;
import com.karru.landing.wallet.wallet_transaction.model.CreditDebitTransactionsModel;
import com.karru.util.AppTypeface;
import java.util.ArrayList;

import javax.inject.Inject;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;


/**
 * <h1>WalletFragment</h1>
 * <p> Class to load WalletTransactionActivity and show all transactions list </p>
 */
public class WalletTransactionActivity extends DaggerAppCompatActivity
        implements WalletTransactionContract.View
{
    private ArrayList<CreditDebitTransactionsModel> allTransactionsAL;
    private ArrayList<CreditDebitTransactionsModel> debitTransactionsAL;
    private ArrayList<CreditDebitTransactionsModel> creditTransactionsAL;
    private WalletTransactionsFragment allTransactionsFrag, debitsFrag, creditsFrag;
    private ProgressDialog pDialog;

    @BindView(R.id.tv_all_tool_bar_title) TextView tv_all_tool_bar_title;
    @BindView(R.id.pager)
    ViewPager viewPager;

    @Inject AppTypeface appTypeface;
    @Inject WalletTransactionContract.WalletTransactionPresenter walletTransPresenter;

    @BindString(R.string.recentTransactions) String recentTransactions;
    @BindString(R.string.all) String all;
    @BindString(R.string.debit) String debit;
    @BindString(R.string.credit) String credit;
    @BindString(R.string.wait) String wait;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transactions);
        ButterKnife.bind(this);
        initViews();
    }

    /**
     * <h2>setAllTransactionsAL</h2>
     * provide all transactions list
     * @param allTransactionsAL all transaction data
     */
    public void setAllTransactionsAL(ArrayList<CreditDebitTransactionsModel> allTransactionsAL) {
        this.allTransactionsAL = allTransactionsAL;
    }

    /**
     * <h2>setDebitTransactionsAL</h2>
     * provide debit transactions list
     * @param debitTransactionsAL debitTransactionsAL
     */
    public void setDebitTransactionsAL(ArrayList<CreditDebitTransactionsModel> debitTransactionsAL) {
        this.debitTransactionsAL = debitTransactionsAL;
    }

    /**
     * <h2>setCreditTransactionsAL</h2>
     * provide credit TransactionsAL list
     * @param creditTransactionsAL credit transaction data
     */
    public void setCreditTransactionsAL(ArrayList<CreditDebitTransactionsModel> creditTransactionsAL) {
        this.creditTransactionsAL = creditTransactionsAL;
    }

    @OnClick({R.id.rl_back_button,R.id.iv_back_button})
    public void clickEvent(View view)
    {
        onBackPressed();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        walletTransPresenter.disposeObservable();
    }

    /**
     *<h2>initViews</h2>
     * <P> custom method to initializes all the views of the screen </P>
     */
    private void initViews()
    {
        tv_all_tool_bar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_all_tool_bar_title.setText(recentTransactions);

        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        String tabTitles[]  = new String[]{all, debit, credit};
        WalletViewPagerAdapter viewPagerAdapter = new WalletViewPagerAdapter(getSupportFragmentManager());

        this.allTransactionsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(allTransactionsFrag, tabTitles[0]);

        this.debitsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(debitsFrag, tabTitles[1]);

        this.creditsFrag = WalletTransactionsFragment.getNewInstance();
        viewPagerAdapter.addFragment(creditsFrag, tabTitles[2]);
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        walletTransPresenter.checkRTLConversion();
        loadTransactions();
    }

    /**
     * <h2>loadTransactions</h2>
     * <p> method to init getTransactionsList api </p>
     */
    public void loadTransactions()
    {
        walletTransPresenter.initLoadTransactions();
    }

    @Override
    public void hideProgressDialog()
    {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void showProgressDialog(String msg)
    {
        if(pDialog == null)
            pDialog = new ProgressDialog(this);

        if(!pDialog.isShowing())
        {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(wait);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    public void showToast(String msg, int duration)
    {
        hideProgressDialog();
        Toast.makeText(this, msg, duration).show();
    }

    @Override
    public void showAlert(String title, String msg)
    {
        hideProgressDialog();
    }

    /**
     *<h2>walletTransactionsApiSuccessViewNotifier</h2>
     * <p> method to update fields data on the success response of api </p>
     */
    @Override
    public void walletTransactionsApiSuccessViewNotifier()
    {
        hideProgressDialog();
        String TAG = "WalletTransactionAct";
        Log.d(TAG, "walletTransactionsApiSuccessViewNotifier showToast: ");

        if(this.allTransactionsFrag != null)
            this.allTransactionsFrag.notifyDataSetChangedCustom(allTransactionsAL);

        if(this.debitsFrag != null)
            this.debitsFrag.notifyDataSetChangedCustom(debitTransactionsAL);

        if(this.creditsFrag != null)
            this.creditsFrag.notifyDataSetChangedCustom(creditTransactionsAL);
    }


    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
