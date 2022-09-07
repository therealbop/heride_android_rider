package com.karru.landing.wallet.wallet_transaction;

import com.karru.dagger.ActivityScoped;
import com.karru.dagger.FragmentScoped;
import com.karru.landing.wallet.wallet_transaction.view.WalletTransactionActivity;
import com.karru.landing.wallet.wallet_transaction.view.WalletTransactionsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WalletTransactionDaggerModule
{
    @Binds
    @ActivityScoped
    abstract WalletTransactionContract.View provideWalletTransactionView(WalletTransactionActivity transActivity);

    @Binds
    @ActivityScoped
    abstract WalletTransactionContract.WalletTransactionPresenter provideWalletTransPresnter(WalletTransactionPresenter transactionActivityPresenter);

    @ContributesAndroidInjector
    @FragmentScoped
    abstract WalletTransactionsFragment provideWalletListFragment();
}
