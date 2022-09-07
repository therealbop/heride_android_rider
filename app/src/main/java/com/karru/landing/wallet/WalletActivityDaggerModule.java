package com.karru.landing.wallet;


import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class WalletActivityDaggerModule
{
    @Binds
    @ActivityScoped
    abstract WalletActivityContract.WalletView provideWalletView(WalletActivity activity);

    @Binds
    @ActivityScoped
    abstract WalletActivityContract.WalletPresenter provideWalletPresenter(WalletActivityPresenter presenter);
}
