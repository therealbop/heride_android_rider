package com.karru.landing.home.promo_code;

import android.content.Context;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.home.model.promo_code_model.PromoCodeDataModel;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.PROMO_ACTIVITY;

/**
 * <h1>PromoCodeUtilModule</h1>
 * This class is used to provide the module for promo activity
 * @author 3Embed
 * @since on 25-06-2018.
 */

@Module
public class PromoCodeUtilModule {

    @Provides
    @Named(PROMO_ACTIVITY)
    CompositeDisposable providePromoDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    ArrayList<PromoCodeDataModel> promoCodeDataModelArrayList()
    {
        return new ArrayList<>();
    }

    @Provides
    @ActivityScoped
    PromoCodeAdapter promoCodeAdapter(AppTypeface appTypeface, Context context, ArrayList<PromoCodeDataModel> promoCodeDataModels, DateFormatter dateFormatter,PromoCodeContract.View promocodeActivityView)
    {
        return new PromoCodeAdapter(appTypeface,context, promoCodeDataModels,dateFormatter,promocodeActivityView);
    }
}
