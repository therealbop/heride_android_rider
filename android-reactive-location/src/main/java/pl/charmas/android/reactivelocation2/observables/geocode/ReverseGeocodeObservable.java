package pl.charmas.android.reactivelocation2.observables.geocode;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import pl.charmas.android.reactivelocation2.observables.ObservableEmitterWrapper;


public class ReverseGeocodeObservable implements ObservableOnSubscribe<List<Address>> {
    private final Context ctx;
    private final Locale locale;
    private final double latitude;
    private final double longitude;
    private final int maxResults;
    private final String serverKey;

    public static Observable<List<Address>> createObservable(Context ctx, Locale locale, double latitude, double longitude,
                                                             int maxResults, String serverKey) {
        return Observable.create(new ReverseGeocodeObservable(ctx, locale, latitude, longitude, maxResults,serverKey));
    }

    private ReverseGeocodeObservable(Context ctx, Locale locale, double latitude, double longitude,
                                     int maxResults,String serverKey) {
        this.ctx = ctx;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxResults = maxResults;
        this.locale = locale;
        this.serverKey = serverKey;
    }

    @Override
    public void subscribe(ObservableEmitter<List<Address>> emitter) {
        Geocoder geocoder = new Geocoder(ctx, locale);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, maxResults);
            if (!emitter.isDisposed()) {
                emitter.onNext(addresses);
                emitter.onComplete();
            }
        } catch (IOException e) {
            // If it's a service not available error try a different approach using google web api
            /*if (!emitter.isDisposed()) {
                Observable
                        .create(new FallbackReverseGeocodeObservable(locale, latitude, longitude, maxResults,
                                serverKey))
                        .subscribeOn(Schedulers.io())
                        .subscribe(new ObservableEmitterWrapper<>(emitter));
            } else {
                emitter.onError(e);
            }*/

            if (e.getMessage().equalsIgnoreCase("Service not Available") || e.getMessage().equalsIgnoreCase("grpc failed")) {
                Observable
                        .create(new FallbackReverseGeocodeObservable(locale, latitude, longitude, maxResults,serverKey))
                        .subscribeOn(Schedulers.io())
                        .subscribe(new ObservableEmitterWrapper<>(emitter));
            } else {
                emitter.onError(e);
            }
        }
    }
}
