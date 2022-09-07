package com.karru.landing;

import android.content.Context;
import androidx.fragment.app.Fragment;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.emergency_contact.ContactDetails;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.model.DriverPreferenceDataModel;
import com.karru.landing.home.model.DriverPreferenceModel;
import com.karru.landing.home.view.ChooseFavDriverDialog;
import com.karru.landing.home.view.CorporateAccountsAdapter;
import com.karru.landing.home.view.CorporateAccountsDialog;
import com.karru.landing.home.view.CurrentZonePickupsAdapter;
import com.karru.landing.home.view.DriverPreferenceBottomSheet;
import com.karru.landing.home.view.DriverPreferencesAdapter;
import com.karru.landing.home.view.FareBreakDownAdapter;
import com.karru.landing.home.view.FareBreakdownDialog;
import com.karru.landing.home.view.FavDriversListAdapter;
import com.karru.landing.home.view.OnGoingBookingsAdapter;
import com.karru.landing.home.view.OutstandingBalanceBottomSheet;
import com.karru.landing.home.view.PaymentOptionsBottomSheet;
import com.karru.landing.home.view.PromoCodeBottomSheet;
import com.karru.landing.home.view.RideRateCardDialog;
import com.karru.landing.home.view.SomeOneRideBottomSheet;
import com.karru.landing.home.view.TimePickerDialog;
import com.karru.landing.home.view.WalletAlertBottomSheet;
import com.karru.landing.home.view.WalletHardLimitAlert;
import com.karru.managers.location.LocationManager;
import com.karru.util.ActivityUtils;
import com.karru.util.AppTypeface;
import com.karru.util.DateFormatter;
import com.karru.util.Utility;

import java.util.ArrayList;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.HOME_FRAG;
import static com.karru.utility.Constants.MAIN;

/**
 * <h1>AddressSelectionUtilModule</h1>
 * This class is used to provide the module for main activity
 * @author 3Embed
 * @since on 06-01-2018.
 */
@Module
public class MainActivityUtilModule
{

    private static final String FRAGMENT = "BaseFragmentModule.fragment";
    private static final String CHILD_FRAGMENT_MANAGER = "BaseFragmentModule.ChildFragmentManager";

    @Provides
    @ActivityScoped
    ActivityUtils provideActivityUtils(androidx.fragment.app.FragmentManager fragmentManager)
    {
        return new ActivityUtils(fragmentManager);
    }

    @Provides
    @Named(CHILD_FRAGMENT_MANAGER)
    androidx.fragment.app.FragmentManager childFragmentManager(@Named(FRAGMENT) Fragment fragment) {
        return fragment.getChildFragmentManager();
    }

    @Provides
    @ActivityScoped
    @Named(MAIN)
    CompositeDisposable provideCompositeDisposable()
    {
        return new CompositeDisposable();
    }

    /***************************************************************************************************/

    @Provides
    @ActivityScoped
    RideRateCardDialog provideCardDialog(MainActivity context, AppTypeface appTypeface)
    {
        return new RideRateCardDialog(context,appTypeface);
    }

    @Provides
    @ActivityScoped
    FareBreakDownAdapter provideFareBreakDownAdapter(Context context,AppTypeface appTypeface)
    {
        return new FareBreakDownAdapter(context,appTypeface);
    }

    @Provides
    @ActivityScoped
    FareBreakdownDialog fareBreakdownDialog(MainActivity mainActivity, AppTypeface appTypeface,
                                            FareBreakDownAdapter provideFareBreakDownAdapter)
    {
        return new FareBreakdownDialog(mainActivity,appTypeface,provideFareBreakDownAdapter);
    }

    @Provides
    @ActivityScoped
    TimePickerDialog provideTimePickerDialog(MainActivity mainActivity, AppTypeface appTypeface,
                                             HomeFragmentContract.Presenter homeFragmentPresenter,
                                             DateFormatter dateFormatter)
    {
        return new TimePickerDialog(mainActivity,appTypeface,dateFormatter,homeFragmentPresenter);
    }

    @Provides
    @Named(HOME_FRAG)
    CompositeDisposable provideDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @ActivityScoped
    LocationManager provideLocationManager(MainActivity activity){return new LocationManager(activity);}


    @Provides
    @ActivityScoped
    OnGoingBookingsAdapter onGoingBookingsAdapter(AppTypeface appTypeface, Context context)
    {
        return new OnGoingBookingsAdapter(appTypeface,context);
    }

    @Provides
    @ActivityScoped
    CurrentZonePickupsAdapter currentZonePickupsAdapter(AppTypeface appTypeface, Context context)
    {
        return new CurrentZonePickupsAdapter(appTypeface,context);
    }


    @Provides
    @ActivityScoped
    PaymentOptionsBottomSheet providePaymentOptionsBottomSheet(AppTypeface appTypeface,
                                                               HomeFragmentContract.Presenter presenter)
    {
        return new PaymentOptionsBottomSheet(appTypeface,presenter,null);
    }

    @Provides
    @ActivityScoped
    OutstandingBalanceBottomSheet provideLastDuesBottomSheet(AppTypeface appTypeface)
    {
        return new OutstandingBalanceBottomSheet(appTypeface);
    }

    @Provides
    @ActivityScoped
    SomeOneRideBottomSheet provideSomeOneRideBottomSheet(AppTypeface appTypeface,
                                                         HomeFragmentContract.Presenter presenter)
    {
        return new SomeOneRideBottomSheet(appTypeface,presenter);
    }

    @Provides
    @ActivityScoped
    ChooseFavDriverDialog chooseFavDriverDialog(AppTypeface appTypeface,FavDriversListAdapter favDriversListAdapter,
                                                HomeFragmentContract.Presenter presenter)
    {
        return new ChooseFavDriverDialog(appTypeface,presenter,favDriversListAdapter);
    }

    @Provides
    @ActivityScoped
    DriverPreferencesAdapter driverPreferencesAdapter(AppTypeface appTypeface, Context context,
                                                      DriverPreferenceModel driverPreferenceModel,Utility utility,
                                                      ArrayList<DriverPreferenceDataModel> tempPreferenceDataModels)
    {
        return new DriverPreferencesAdapter(appTypeface,context,driverPreferenceModel,utility,tempPreferenceDataModels);
    }

    @Provides
    @ActivityScoped
    ArrayList<ContactDetails> contactDetailsArrayList()
    {
        return new ArrayList<>();
    }

    @Provides
    @ActivityScoped
    FavDriversListAdapter favDriversListAdapter(AppTypeface appTypeface, MainActivity context,
                                                ArrayList<ContactDetails> contactDetails)
    {
        return new FavDriversListAdapter(context,contactDetails,appTypeface);
    }

    @Provides
    @ActivityScoped
    DriverPreferenceBottomSheet provideDriverPreferenceBottomSheet(AppTypeface appTypeface,
                                                                   DriverPreferencesAdapter driverPreferencesAdapter,
                                                                   HomeFragmentContract.Presenter homeFragmentPresenter)
    {
        return new DriverPreferenceBottomSheet(appTypeface,driverPreferencesAdapter,homeFragmentPresenter,null);
    }

    @Provides
    @ActivityScoped
    WalletAlertBottomSheet walletAlertBottomSheet(AppTypeface appTypeface,
                                                  HomeFragmentContract.Presenter homeFragmentPresenter)
    {
        return new WalletAlertBottomSheet(appTypeface,homeFragmentPresenter,null);
    }

    @Provides
    @ActivityScoped
    WalletHardLimitAlert walletHardLimitAlert(AppTypeface appTypeface)
    {
        return new WalletHardLimitAlert(appTypeface);
    }

    @Provides
    @ActivityScoped
    CorporateAccountsAdapter corporateAccountsAdapter(AppTypeface appTypeface,Context context,
                                                      HomeFragmentContract.Presenter homeFragmentPresenter)
    {
        return new CorporateAccountsAdapter(appTypeface,context,homeFragmentPresenter,null);
    }

    @Provides
    @ActivityScoped
    CorporateAccountsDialog corporateAccountsDialog(AppTypeface appTypeface,
                                                    CorporateAccountsAdapter corporateAccountsAdapter)
    {
        return new CorporateAccountsDialog(appTypeface,corporateAccountsAdapter);
    }

    @Provides
    @ActivityScoped
    PromoCodeBottomSheet promoCodeBottomSheet(AppTypeface appTypeface,
                                              HomeFragmentContract.Presenter presenter)
    {
        return new PromoCodeBottomSheet(appTypeface,presenter);
    }

    @Provides
    @ActivityScoped
    DriverPreferenceModel driverPreferenceModel()
    {
        return new DriverPreferenceModel();
    }

    @Provides
    @ActivityScoped
    ArrayList<DriverPreferenceDataModel> tempPreferenceDataModels()
    {
        return new ArrayList<>();
    }
}
