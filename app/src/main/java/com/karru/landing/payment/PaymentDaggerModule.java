package com.karru.landing.payment;

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

/**
 * <h1>PaymentDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public abstract class PaymentDaggerModule
{
    @Binds
    @ActivityScoped
    abstract PaymentActivityContract.Presenter providePaymentFragPresenter(PaymentPresenter presenter);

    @Binds
    @ActivityScoped
    abstract PaymentActivityContract.View provideView(PaymentActivity paymentActivity);
}
