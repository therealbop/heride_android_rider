package com.karru.landing.home.promo_code;
/**
 * <h1>PromoCodeDaggerModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 15-06-2018.
 */

import com.karru.dagger.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module(includes = PromoCodeUtilModule.class)
public  interface  PromoCodeDaggerModule {


    @Binds
    @ActivityScoped
     PromoCodeContract.Presenter providePromoCodePresenter(PromoCodePresenter promoCodePresenter);

    @Binds
    @ActivityScoped
    PromoCodeContract.View providePromoCodeView(PromoCodeActivity promoCodeActivity);

}
