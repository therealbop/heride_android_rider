package com.karru.dagger;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.karru.ApplicationVersion;
import com.karru.RxAppVersionObserver;
import com.karru.RxConfigCalled;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChatDataObservable;
import com.karru.countrypic.CountryPicker;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDbHelper;
import com.karru.landing.home.model.HomeActivityModel;
import com.karru.landing.home.model.MQTTResponseDataModel;
import com.karru.landing.home.model.fare_estimate_model.FareEstimateModel;
import com.karru.managers.RxConfirmPickNotifier;
import com.karru.managers.booking.BookingsManager;
import com.karru.managers.booking.RxDriverCancelledObserver;
import com.karru.managers.booking.RxDriverDetailsObserver;
import com.karru.managers.booking.RxInvoiceDetailsObserver;
import com.karru.managers.booking.RxLiveBookingDetailsObserver;
import com.karru.managers.booking.RxOnGoingBookingsDetailsObserver;
import com.karru.managers.booking.RxRequestingDetailsObserver;
import com.karru.managers.location.AddressProviderFromLocation;
import com.karru.managers.location.LocationProvider;
import com.karru.managers.location.RxAddressObserver;
import com.karru.managers.location.RxLocationObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.managers.user_vehicles.RxCurrentZoneObserver;
import com.karru.managers.user_vehicles.RxVehicleDetailsObserver;
import com.karru.splash.first.LanguagesList;
import com.karru.util.Alerts;
import com.karru.util.AppPermissionsRunTime;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;
import com.karru.util.Utility;
import com.karru.util.WaveDrawableRequest;
import com.karru.util.image_upload.AmazonUpload;
import com.karru.util.path_plot.RxRoutePathObserver;
import com.heride.rider.R;

import java.util.ArrayList;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

import static com.karru.utility.Constants.LANGUAGE;

/**
 * <h2>UtilityModule</h2>
 * This class is used to provide the app related instances throwout the app
 * @author 3Embed
 * @since on 08-12-2017.
 */

@Module
class UtilityModule
{
    @Provides
    @Singleton
    Alerts provideAlerts(Context context,AppTypeface appTypeface){return new Alerts(context,appTypeface);}

    @Provides
    @Singleton
    RxNetworkObserver getNetWorkObserver()
    {
        return new RxNetworkObserver();
    }

    @Provides
    @Singleton NetworkStateHolder getNetWorHolder() {return new NetworkStateHolder();}

    @Provides
    @Singleton
    CountryPicker provideCountryPickerInstance() {return CountryPicker.newInstance("");}

    @Provides
    @Singleton
    SQLiteDbHelper providesAddressDbHelper(Context context){return new SQLiteDbHelper(context);}

    @Provides
    @Singleton
    WaveDrawableRequest waveDrawable (Context context)
    {
        return  new WaveDrawableRequest(ContextCompat.getColor(context, R.color.white),300);
    }

    @Provides
    @Singleton
    Gson provideGSON(){return new Gson();}

    @Provides
    @Singleton
    AppTypeface provideAppTypeface(Context context) {return new AppTypeface(context);}

    @Provides
    @Singleton
    FavAddressDataModel provideFavAddressDataModel(){return new FavAddressDataModel();}

    @Provides
    @Singleton
    AmazonUpload provideAmazonCdn()
    {
        return AmazonUpload.getInstance();
    }

    /*************************************MQTT ***************************************************/

    @Provides
    @Singleton
    MQTTResponseDataModel provideMQTTResponseModel(){return new MQTTResponseDataModel();}

    @Provides
    @Singleton
    RxVehicleDetailsObserver provideRxVehicleTypesObserver() {return new RxVehicleDetailsObserver();}

    @Provides
    @Singleton
    RxLiveBookingDetailsObserver provideRxBookingDetailsObserver()
    {
        return new RxLiveBookingDetailsObserver();
    }

    @Provides
    @Singleton
    RxOnGoingBookingsDetailsObserver provideRxOnGoingBookingDetailsObserver()
    {
        return new RxOnGoingBookingsDetailsObserver();
    }

    @Provides
    @Singleton
    RxCurrentZoneObserver proviRxCurrentZoneObserver()
    {
        return new RxCurrentZoneObserver();
    }

    @Provides
    @Singleton
    RxDriverDetailsObserver provideRxDriverDetailsObserver()
    {
        return new RxDriverDetailsObserver();
    }

    @Provides
    @Singleton
    RxInvoiceDetailsObserver rxInvoiceDetailsObserver()
    {
        return new RxInvoiceDetailsObserver();
    }

    @Provides
    @Singleton
    RxDriverCancelledObserver rxDriverCancelledObserver()
    {
        return new RxDriverCancelledObserver();
    }

    @Provides
    @Singleton
    BookingsManager provideBookingsManager(RxDriverDetailsObserver rxDriverDetailsObserver,
                                           RxLiveBookingDetailsObserver rxBookingDetailsObserver,
                                           RxOnGoingBookingsDetailsObserver rxOnGoingBookingDetailsObserver,
                                           Gson gson, RxInvoiceDetailsObserver rxInvoiceDetailsObserver,
                                           RxDriverCancelledObserver rxDriverCancelledObserver,
                                           RxRequestingDetailsObserver rxRequestingDetailsObserver)
    {
        return new BookingsManager(rxDriverDetailsObserver,rxBookingDetailsObserver,rxRequestingDetailsObserver,
                rxOnGoingBookingDetailsObserver,gson,rxInvoiceDetailsObserver,rxDriverCancelledObserver);
    }

    @Provides
    @Singleton
    MQTTManager  mqttManager(Context context, RxVehicleDetailsObserver rxVehicleDetailsObserver,
                             PreferenceHelperDataSource preferenceHelperDataSource, Gson gson,
                             MQTTResponseDataModel mqttResponseDataModel, BookingsManager bookingsManager,
                             RxCurrentZoneObserver rxCurrentZoneObserver, NetworkStateHolder networkStateHolder,
                             RxNetworkObserver rxNetworkObserver,ChatDataObservable chatDataObservable)
    {
        return new MQTTManager(context,rxVehicleDetailsObserver, preferenceHelperDataSource,gson,
                mqttResponseDataModel, bookingsManager,rxCurrentZoneObserver,networkStateHolder,
                rxNetworkObserver,chatDataObservable);
    }

    /********************************* Location Provider ***************************************/

    @Provides
    @Singleton
    ReactiveLocationProvider locationProvider(Context context) {return new ReactiveLocationProvider(context);}

    @Provides
    @Singleton
    RxLocationObserver provideRxLocationObserver(){return new RxLocationObserver();}

    @Provides
    @Singleton
    LocationProvider provideLocation(Context context,RxLocationObserver rxLocationObserver)
    {
        return new LocationProvider(context,rxLocationObserver);
    }

    /**********************************Address Provider **********************************************/
    @Provides
    @Singleton
    RxAddressObserver provideRxAddressObserver(){return new RxAddressObserver();}

    @Provides
    @Singleton
    AddressProviderFromLocation providerFromLocation(ReactiveLocationProvider locationProvider,
                                                     RxAddressObserver rxAddressObserver)
    {
        return new AddressProviderFromLocation(locationProvider,rxAddressObserver);
    }

    /****************************Utility**********************************************************/

    @Provides
    @Singleton
    Utility provideUtility()
    {
        return new Utility();
    }

    @Provides
    @Singleton
    DateFormatter provideDateFormatter()
    {
        return new DateFormatter();
    }

    @Provides
    @Singleton
    RxConfirmPickNotifier provideRxConfirmPickNotifier()
    {
        return new RxConfirmPickNotifier();
    }

    @Provides
    @Singleton
    RxRoutePathObserver provideRxRoutePathObserver()
    {
        return new RxRoutePathObserver();
    }

    @Provides
    @Singleton
    RxRequestingDetailsObserver rxRequestingDetailsObserver()
    {
        return new RxRequestingDetailsObserver();
    }

    @Provides
    @Singleton
    ChatDataObservable chatDataObservable()
    {
        return new ChatDataObservable();
    }

    @Provides
    @Singleton
    ApplicationVersion applicationVersion()
    {
        return new ApplicationVersion();
    }

    @Provides
    @Singleton
    RxAppVersionObserver rxApplicationVersionObserver()
    {
        return new RxAppVersionObserver();
    }

    @Provides
    @Named(LANGUAGE)
    @Singleton
    ArrayList<LanguagesList> languagesLists(){return new ArrayList<>();}

    @Provides
    @Singleton
    com.karru.util.AppPermissionsRunTime appPermissionsRunTime(Alerts alerts)
    {
        return new AppPermissionsRunTime(alerts);
    }

    @Provides
    @Singleton
    HomeActivityModel provideHomeActivityModel() { return new HomeActivityModel();}

    @Provides
    @Singleton
    RxConfigCalled rxConfigCalled() { return new RxConfigCalled();}

    @Provides
    @Singleton
    FareEstimateModel provideFareEstimateModel() { return new FareEstimateModel();}

}
