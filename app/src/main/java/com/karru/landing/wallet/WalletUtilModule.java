package com.karru.landing.wallet;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.WALLET;

@Module
public class WalletUtilModule
{
    @Named(WALLET)
    @Provides
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }
}
