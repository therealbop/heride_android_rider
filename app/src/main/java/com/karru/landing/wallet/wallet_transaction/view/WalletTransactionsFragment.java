package com.karru.landing.wallet.wallet_transaction.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.heride.rider.R;
import com.karru.dagger.ActivityScoped;
import com.karru.landing.wallet.wallet_transaction.model.CreditDebitTransactionsModel;
import com.karru.util.DateFormatter;
import com.karru.util.Utility;

import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * <h1>WalletTransactionsFragment</h1>
 * <p> Fragment to show wallet all transactions list according to fragment instances for the view pager of WalletTransActivity </p>
 */
@ActivityScoped
public class WalletTransactionsFragment extends DaggerFragment
{
    private ArrayList<CreditDebitTransactionsModel> transactionsAL;

    @BindView(R.id.rvTransactions) RecyclerView rvTransactions;
    @BindView(R.id.llNoTransactions) LinearLayout llNoTransactions;
    private WalletTransactionActivity walletTransActivity;
    private WalletTransactionsAdapter walletTransactionsRVA;

    @Inject DateFormatter dateFormatter;
    @Inject Utility utility;

    @Inject
    public WalletTransactionsFragment()
    {
    }

    /**
     * <h2>getNewInstance</h2>
     * <p> method to return the instance of this fragment </p>
     */
    public static WalletTransactionsFragment getNewInstance()
    {
        WalletTransactionsFragment walletTransactionsFragment = new WalletTransactionsFragment();
        Bundle args = new Bundle();
        walletTransactionsFragment.setArguments(args);
        return walletTransactionsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        walletTransActivity = (WalletTransactionActivity) getActivity();
        transactionsAL = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View  view = inflater.inflate(R.layout.fragment_wallet_tansactions_list, container, false);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }

    /**
     * <h2>initViews</h2>
     * <p> method to notify adapter or update views if the transactions list size changed </p>
     */
    private void initViews()
    {
        rvTransactions.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvTransactions.setLayoutManager(llm);
        walletTransactionsRVA = new WalletTransactionsAdapter(getActivity(), transactionsAL,utility,
                dateFormatter);
        rvTransactions.setAdapter(walletTransactionsRVA);
        updateView();
    }

    /**
     * <h2>notifyDataSetChangedCustom</h2>
     * <p> method to notify adapter or update views if the transactions list size changed </p>
     */
    public void notifyDataSetChangedCustom(ArrayList<CreditDebitTransactionsModel> _transactionsAL)
    {
        transactionsAL.clear();
        transactionsAL.addAll(_transactionsAL);
        updateView();
    }

    /**
     * <h2>updateView</h2>
     * <p> method to show or hide the list and notItems views according to the size of the list </p>
     */
    private void updateView()
    {
        if(transactionsAL.size() > 0)
        {
            llNoTransactions.setVisibility(View.GONE);
            rvTransactions.setVisibility(View.VISIBLE);
        }
        else
        {
            rvTransactions.setVisibility(View.GONE);
            llNoTransactions.setVisibility(View.VISIBLE);
        }
        walletTransactionsRVA.notifyDataSetChanged();
    }
}
