package com.karru.landing.payment;

import com.karru.dagger.ActivityScoped;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.PAYMENT;

/**
 * <h1>HistoryDaggerUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class PaymentDaggerUtilModule
{
    @Provides
    @Named(PAYMENT)
    @ActivityScoped
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    CardsListAdapter provideCardsListAdapter(PaymentActivity paymentActivity){
        return new CardsListAdapter(paymentActivity);
    }
}

