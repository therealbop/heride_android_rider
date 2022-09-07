package com.karru.landing.favorite;

import com.karru.dagger.ActivityScoped;
import com.karru.landing.favorite.model.FavoritesDriversDetails;
import com.karru.landing.favorite.view.FavoriteDriversAdapter;
import com.karru.util.AppTypeface;

import java.util.ArrayList;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

import static com.karru.utility.Constants.FAVORITE_DRIVER;
import static com.karru.utility.Constants.FAVORITE_OFFLINE_DRIVER;
import static com.karru.utility.Constants.FAVORITE_ONLINE_DRIVER;

/**
 * <h1>AddCorporateProfileUtilModule</h1>
 * used to provide models to dagger
 * @author 3Embed
 * @since on 21-03-2018.
 */
@Module
public class FavoriteDriverUtilModule
{
    @Provides
    @Named(FAVORITE_DRIVER)
    @ActivityScoped
    CompositeDisposable compositeDisposable()
    {
        return new CompositeDisposable();
    }

    @Provides
    @Named(FAVORITE_ONLINE_DRIVER)
    @ActivityScoped
    ArrayList<FavoritesDriversDetails> favoritesDriversDetails()
    {
        return new ArrayList<>();
    }

    @Provides
    @Named(FAVORITE_OFFLINE_DRIVER)
    @ActivityScoped
    ArrayList<FavoritesDriversDetails> offlineDriversList()
    {
        return new ArrayList<>();
    }

    @Provides
    @Named(FAVORITE_ONLINE_DRIVER)
    @ActivityScoped
    FavoriteDriversAdapter favoriteDriversAdapter(AppTypeface appTypeface,FavoriteDriversContract.Presenter presenter,
                                                  @Named(FAVORITE_ONLINE_DRIVER) ArrayList<FavoritesDriversDetails> favoritesDriversDetails)
    {
        return new FavoriteDriversAdapter(appTypeface,favoritesDriversDetails,presenter);
    }

    @Provides
    @Named(FAVORITE_OFFLINE_DRIVER)
    @ActivityScoped
    FavoriteDriversAdapter offlineAdapter(AppTypeface appTypeface,FavoriteDriversContract.Presenter presenter,
                                                  @Named(FAVORITE_OFFLINE_DRIVER) ArrayList<FavoritesDriversDetails> favoritesDriversDetails)
    {
        return new FavoriteDriversAdapter(appTypeface,favoritesDriversDetails,presenter);
    }
}
