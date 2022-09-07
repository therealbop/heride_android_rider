package com.karru.managers.location;

import android.app.Activity;
import com.karru.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * <h2>LocationModule</h2>
 * This class is used to provide the location manager class
 * @author 3Embed
 * @since on 09-12-2017.
 */
@Module
public class LocationModule
{
    @Provides
    @ActivityScoped
    LocationManager getLocationManger(Activity activity)
    {
        return new LocationManager(activity);
    }
}
