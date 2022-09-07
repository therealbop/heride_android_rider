package com.karru.landing.my_vehicles.add_new_vehicle;

import com.karru.dagger.ActivityScoped;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.ADD_NEW_VEHICLE;

/**
 * <h1>PromoCodeUtilModule</h1>
 * This class is used to provide the module for promo activity
 * @author 3Embed
 * @since on 25-06-2018.
 */

@Module
public class AddNewVehicleUtilModule {

    @Provides
    @Named(ADD_NEW_VEHICLE)
    CompositeDisposable providePromoDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    ArrayList<String> yearDataModelArrayList()
    {
        return new ArrayList<>();
    }

    /*@Provides
    @ActivityScoped
    PromoCodeAdapter promoCodeAdapter(AppTypeface appTypeface, Context context, ArrayList<PromoCodeDataModel> promoCodeDataModels, DateFormatter dateFormatter,PromoCodeContract.View promocodeActivityView)
    {
        return new PromoCodeAdapter(appTypeface,context, promoCodeDataModels,dateFormatter,promocodeActivityView);
    }*/


}
