package com.karru.network;

import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.utility.Utility;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.karru.util.Utility.RtlConversion;
import static com.karru.utility.Constants.NETWORK;

public class NetworkReachablePresenter implements NetworkReachableContract.Presenter
{
    private static final String TAG = "NetworkReachablePresenter";

    @Inject NetworkReachableContract.View view;
    @Inject NetworkReachableActivity mActivity;
    @Inject RxNetworkObserver rxNetworkObserver;
    @Inject PreferenceHelperDataSource preferenceHelperDataSource;
    @Inject @Named(NETWORK) CompositeDisposable compositeDisposable;

    private Disposable networkDisposable;

    @Inject
    NetworkReachablePresenter() { }

    @Override
    public void checkRTLConversion() {
        RtlConversion(mActivity,preferenceHelperDataSource.getLanguageSettings().getCode());
    }

    @Override
    public void subscribeNetworkObserver()
    {
        rxNetworkObserver.subscribeOn(Schedulers.io());
        rxNetworkObserver.observeOn(AndroidSchedulers.mainThread());
        networkDisposable = rxNetworkObserver.subscribeWith( new DisposableObserver<NetworkStateHolder>()
        {
            @Override
            public void onNext(NetworkStateHolder networkStateHolder)
            {
                Utility.printLog(TAG+" network not available "+networkStateHolder.isConnected());
                if(networkStateHolder.isConnected())
                    view.networkAvailable();
                else
                    view.networkNotAvailable();
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }

            @Override
            public void onComplete()
            {
                compositeDisposable.add(networkDisposable);
            }
        });
    }

    @Override
    public void disposeObservable() {
        compositeDisposable.dispose();
    }
}
