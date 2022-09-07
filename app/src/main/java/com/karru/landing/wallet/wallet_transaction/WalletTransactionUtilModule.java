package com.karru.landing.wallet.wallet_transaction;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.WALLET_TRANSACTION;

/**
 * @author  3Embed
 * @since on 28-03-2018.
 */
@Module
public class WalletTransactionUtilModule
{
    @Named(WALLET_TRANSACTION)
    @Provides
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }
}
