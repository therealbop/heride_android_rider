package com.karru.landing.my_vehicles;

import android.content.Context;

import com.karru.dagger.ActivityScoped;
import com.karru.util.AppTypeface;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.MY_VEHICLES;

/**
 * <h1>AddCorporateProfileUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class MyVehiclesUtilModule
{
    @Provides
    @Named(MY_VEHICLES)
    @ActivityScoped
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    ArrayList<MyVehiclesDataModel> myVehiclesDataModels(){return new ArrayList<>();}

    @Provides
    @ActivityScoped
    MyVehiclesAdapter myVehiclesAdapter(AppTypeface appTypeface, Context context,MyVehiclesPresenter myVehiclesPresenter,
                                        ArrayList<MyVehiclesDataModel> myVehiclesDataModels,MyVehiclesActivity myVehiclesActivity){
        return new MyVehiclesAdapter(appTypeface,context,myVehiclesPresenter,myVehiclesActivity,myVehiclesDataModels);}
}
