package com.karru.rental;

import android.content.Context;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.MainActivity;
import com.karru.landing.home.HomeFragmentContract;
import com.karru.landing.home.model.DriverPreferenceDataModel;
import com.karru.landing.home.model.DriverPreferenceModel;
import com.karru.landing.home.view.CorporateAccountsAdapter;
import com.karru.landing.home.view.CorporateAccountsDialog;
import com.karru.landing.home.view.DriverPreferenceBottomSheet;
import com.karru.landing.home.view.DriverPreferencesAdapter;
import com.karru.landing.home.view.FareBreakDownAdapter;
import com.karru.landing.home.view.FareBreakdownDialog;
import com.karru.landing.home.view.OutstandingBalanceBottomSheet;
import com.karru.landing.home.view.PaymentOptionsBottomSheet;
import com.karru.landing.home.view.WalletAlertBottomSheet;
import com.karru.landing.home.view.WalletHardLimitAlert;
import com.karru.rental.model.CarDataModel;
import com.karru.rental.model.DataModel;
import com.karru.rental.model.RentalModel;
import com.karru.rental.view.RentalActivity;
import com.karru.rental.view.RentalPackageAdapter;
import com.karru.rental.view.RentalVehicleTypesAdapter;
import com.karru.util.AppTypeface;
import com.karru.util.Utility;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class RentalUtilModule {
    @Provides
    @ActivityScoped
    List<DataModel> rentalPackages() { return new ArrayList<>(); }

    @Provides
    @ActivityScoped
    RentalPackageAdapter rentalPackageAdapter(List<DataModel> rentalPackages, RentalActivity rentalActivity,
                                              RentCarContract.Presenter presenter, AppTypeface appTypeface)
    {
        return new RentalPackageAdapter(rentalPackages,rentalActivity,presenter,appTypeface);
    }

    @Provides
    @ActivityScoped
    List<CarDataModel> carDataModels() { return new ArrayList<>(); }

    @Provides
    @ActivityScoped
    RentalVehicleTypesAdapter rentalVehicleTypesAdapter(List<CarDataModel> carDataModels, RentalActivity rentalActivity,
                                                        RentCarContract.Presenter presenter, AppTypeface appTypeface, Utility utility)
    {
        return new RentalVehicleTypesAdapter(carDataModels,rentalActivity,utility,appTypeface,presenter);
    }

    @Provides
    @ActivityScoped
    RentalModel rentalModel (){return new RentalModel();}

    @Provides
    @ActivityScoped
    PaymentOptionsBottomSheet providePaymentOptionsBottomSheet(AppTypeface appTypeface,
                                                               RentCarContract.Presenter presenter)
    {
        return new PaymentOptionsBottomSheet(appTypeface,null,presenter);
    }

    @Provides
    @ActivityScoped
    WalletHardLimitAlert walletHardLimitAlert(AppTypeface appTypeface)
    {
        return new WalletHardLimitAlert(appTypeface);
    }

    @Provides
    @ActivityScoped
    WalletAlertBottomSheet walletAlertBottomSheet(AppTypeface appTypeface,
                                                  RentCarContract.Presenter rentalPresenter)
    {
        return new WalletAlertBottomSheet(appTypeface,null,rentalPresenter);
    }

    @Provides
    @ActivityScoped
    CorporateAccountsAdapter corporateAccountsAdapter(AppTypeface appTypeface, Context context,
                                                      RentCarContract.Presenter presenter)
    {
        return new CorporateAccountsAdapter(appTypeface,context,null,presenter);
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
    DriverPreferenceBottomSheet provideDriverPreferenceBottomSheet(AppTypeface appTypeface,
                                                                   DriverPreferencesAdapter driverPreferencesAdapter,
                                                                   RentCarContract.Presenter presenter)
    {
        return new DriverPreferenceBottomSheet(appTypeface,driverPreferencesAdapter,null,presenter);
    }

    @Provides
    @ActivityScoped
    OutstandingBalanceBottomSheet provideLastDuesBottomSheet(AppTypeface appTypeface)
    {
        return new OutstandingBalanceBottomSheet(appTypeface);
    }

    @Provides
    @ActivityScoped
    FareBreakdownDialog fareBreakdownDialog(RentalActivity rentalActivity, AppTypeface appTypeface,
                                            FareBreakDownAdapter provideFareBreakDownAdapter)
    {
        return new FareBreakdownDialog(rentalActivity,appTypeface,provideFareBreakDownAdapter);
    }

    @Provides
    @ActivityScoped
    FareBreakDownAdapter provideFareBreakDownAdapter(Context context,AppTypeface appTypeface)
    {
        return new FareBreakDownAdapter(context,appTypeface);
    }
}
