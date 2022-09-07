package com.karru.landing;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.karru.api.NetworkService;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.ApplicationClass;
import com.karru.dagger.ActivityScoped;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.booking.RxInvoiceDetailsObserver;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.booking_flow.address.model.FavAddressModel;
import com.karru.landing.home.model.InvoiceDetailsModel;
import com.karru.util.Alerts;
import com.karru.util.DataParser;
import com.karru.utility.Constants;
import com.karru.utility.Scaler;
import com.karru.utility.Utility;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.drawerlayout.widget.DrawerLayout;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.INVOICE_DATA;
import static com.karru.utility.Constants.IS_APP_BACKGROUND;
import static com.karru.utility.Constants.IS_INVOICE_OPEN;
import static com.karru.utility.Constants.MAIN;
import static com.karru.utility.Constants.NETWORK_SCREEN_OPEN;
import static com.karru.utility.Constants.switchFlag;

@ActivityScoped
public class MainActivityPresenter implements MainActivityContract.MainActPresenter
{
    private static final String TAG = "MainActivityPresenter";

    @Inject Alerts alerts;
    @Inject Gson gson;
    @Inject com.karru.util.Utility utility;
    @Inject Context mContext;
    @Inject MainActivity mActivity;
    @Inject NetworkStateHolder networkStateHolder;
    @Inject MainActivityContract.MainActView mainActivityView;
    @Inject SQLiteDataSource addressDataSource;
    @Inject NetworkService networkService;
    @Inject @Named(MAIN) CompositeDisposable compositeDisposable;
    @Inject RxInvoiceDetailsObserver rxInvoiceDetailsObserver;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;

    @Inject MainActivityPresenter() {}

    @Override
    public void disposeObservable()
    {
        compositeDisposable.clear();
    }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }


    @Override
    public void getFavAddressApi()
    {
        if(networkStateHolder.isConnected())
        {
            Observable<Response<ResponseBody>> request = networkService.getFavAddresses(
                    ((ApplicationClass) mContext.getApplicationContext()).getAuthToken(preferenceHelperDataSource.getSid()),
                    preferenceHelperDataSource.getLanguageSettings().getCode());

            request.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<ResponseBody>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(Response<ResponseBody> value)
                        {
                            Utility.printLog(TAG+ " getFavAddressApi onNext: "+value.code());
                            switch (value.code())
                            {
                                case 200:
                                    FavAddressModel favDropAddressDataPOJO = gson.fromJson(DataParser.fetchSuccessResponse(value),
                                            FavAddressModel.class);
                                    addressDataSource.insertAllFavAddresses(favDropAddressDataPOJO.getData());
                                    break;

                                case 502:
                                    mainActivityView.showToast(mContext.getString(R.string.bad_gateway));
                                    break;

                                default:
                                    mainActivityView.showToast(DataParser.fetchErrorMessage(value));
                                    break;
                            }
                        }
                        @Override
                        public void onError(Throwable e)
                        {
                            Utility.printLog(TAG+ " getFavAddressApi error: "+e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                        }
                    });
        }
    }

    @Override
    public void checkFlagConstant()
    {
        if (!switchFlag && !Constants.bookingFlag && !Constants.cardFlag) {
            mainActivityView.displayView(1);
        }
    }

    /**
     * <h2>workResume</h2>
     * <p>
     * This method is used to perform all the task, which we wants to do on our onResume() method.
     * </p>
     */
    @Override
    public void workResume()
    {
        checkForNetwork(networkStateHolder.isConnected());
        mainActivityView.checkForHelpCenter(preferenceHelperDataSource.isHelpModuleEnable());
        if (switchFlag)
        {
            switchFlag = false;
            mainActivityView.displayView(4);
        }
        else if (Constants.bookingFlag)
        {
            Constants.bookingFlag = false;
            mainActivityView.displayView(2);
        }
        else if (Constants.cardFlag)
        {
            mainActivityView.displayView(3);
        }
        mainActivityView.checkForEmergencyContact(preferenceHelperDataSource.isEmergencyContactEnable());
        mainActivityView.checkForReferral(preferenceHelperDataSource.isReferralCodeEnabled());
        if(preferenceHelperDataSource.getWalletSettings()!=null)
        {
            int currAbbr = preferenceHelperDataSource.getWalletSettings().getCurrencyAbbr();
            String currSymbol = preferenceHelperDataSource.getWalletSettings().getCurrencySymbol();
            if((preferenceHelperDataSource.getLoginType() == 0 || (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))
                    && preferenceHelperDataSource.getWalletSettings().isWalletEnable())
                mainActivityView.checkForWallet(true,
                        utility.currencyAdjustment(currAbbr,currSymbol,preferenceHelperDataSource.getWalletSettings().getWalletBalance()+"") );
            else
                mainActivityView.checkForWallet(false,
                        utility.currencyAdjustment(currAbbr,currSymbol,preferenceHelperDataSource.getWalletSettings().getWalletBalance()+"") );
        }
        mainActivityView.checkForFavorite(preferenceHelperDataSource.isFavDriverEnable());
        if((preferenceHelperDataSource.getLoginType() == 0 || (preferenceHelperDataSource.getLoginType() == 1 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1))
                && preferenceHelperDataSource.isCardEnable())
            mainActivityView.checkForCard(true);
        else
            mainActivityView.checkForCard(false);
        if(preferenceHelperDataSource.getLoginType() == 0 && preferenceHelperDataSource.isCorporateEnable())
            mainActivityView.checkForCorporate(true);
        else
            mainActivityView.checkForCorporate(false);

        if(preferenceHelperDataSource.getLoginType() != 0)
        {
            mainActivityView.checkForTowing(false);
            mainActivityView.checkForEmergencyContact(false);
        }
        else {
            mainActivityView.checkForTowing(true);
            mainActivityView.checkForEmergencyContact(true);
        }
        if(preferenceHelperDataSource.getLoginType() != 0 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 2)
        {
            mainActivityView.checkForFavorite(false);
            mainActivityView.checkForReferral(false);
            mainActivityView.checkForMyBooking(false);
            mainActivityView.checkForLiveChat(false);
            mainActivityView.checkForHelpCenter(preferenceHelperDataSource.isHelpModuleEnable());
        }

        if(preferenceHelperDataSource.getLoginType() != 0 && preferenceHelperDataSource.getHotelDataModel().getHotelUserType() == 1)
        {
           /* mainActivityView.checkForFavorite(false);
            mainActivityView.checkForReferral(false);*/
        }


    }

    @Override
    public void subscribeForInvoiceDetails()
    {
        Observer<InvoiceDetailsModel> observer = new Observer<InvoiceDetailsModel>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
            }

            @Override
            public void onNext(InvoiceDetailsModel invoiceDetailsModel)
            {
                Utility.printLog(TAG+" invoice details observed  "+IS_INVOICE_OPEN+" ");
                if(!IS_INVOICE_OPEN)
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(INVOICE_DATA,invoiceDetailsModel);
                    mainActivityView.openInvoiceScreen(bundle,preferenceHelperDataSource.getLoginType());
                }
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
                Utility.printLog(TAG+" invoice details onAddCardError  "+e);
            }

            @Override
            public void onComplete()
            {}
        };
        rxInvoiceDetailsObserver.subscribeOn(Schedulers.io());
        rxInvoiceDetailsObserver.observeOn(AndroidSchedulers.mainThread());
        rxInvoiceDetailsObserver.subscribe(observer);
    }

    @Override
    public void getLiveChatDetails()
    {
        mainActivityView.openLiveChatScreen(preferenceHelperDataSource.getUsername());
    }

    @Override
    public void checkForNetwork(boolean isConnected)
    {
        networkStateHolder.setConnected(isConnected);
        rxNetworkObserver.publishData(networkStateHolder);
        if(!isConnected && !IS_APP_BACKGROUND && !NETWORK_SCREEN_OPEN)
            mainActivityView.openNetworkScreen();
    }

    @Override
    public void isDrawerOpen(DrawerLayout mDrawerLayout)
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mainActivityView.closeDrawer();
        else
            mainActivityView.openDrawer();
    }

    @Override
    public void menuItemCheck(MenuItem item, DrawerLayout mDrawerLayout, NavigationView mDrawerList)
    {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList))
                mainActivityView.closeDrawer();
            else
                mainActivityView.openDrawer();
        }
    }

    @Override
    public void getHeader()
    {
        String url = preferenceHelperDataSource.getImageUrl().replace(" ", "%20");
        double size[] = Scaler.getScalingFactor(mContext);
        double height = (120) * size[1];
        double width = (120) * size[0];

        mainActivityView.setHeader(url,width,height,preferenceHelperDataSource.getUsername());
    }
}
