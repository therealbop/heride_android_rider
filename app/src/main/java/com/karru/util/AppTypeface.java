package com.karru.util;

import android.content.Context;
import android.graphics.Typeface;
import javax.inject.Inject;

/**
 * <h2>AppTypeface</h2>
 * This class contains several methods that are used for setting and getting methods for typeFace.
 */
public class AppTypeface
{
    private Typeface pro_narMedium;
    private Typeface pro_News;
    private static AppTypeface setTypeface = null;

    @Inject
    public AppTypeface(Context context)
    {
        initTypefaces(context);
    }

    /**
     * <h2>AppTypeface</h2>
     * @param context: calling activity reference
     * @return: Single instance of this class
     */
    public static AppTypeface getInstance(Context context)
    {
        if (setTypeface == null)
        {
            setTypeface = new AppTypeface(context.getApplicationContext());
        }
        return setTypeface;
    }

    /**
     * <h2>initTypefaces</h2>
     * <p>
     *     method to initializes the typefaces of the app
     * </p>
     * @param context Context of the activity from where it is called
     */
    private void initTypefaces(Context context)
    {
        this.pro_narMedium = Typeface.createFromAsset(context.getAssets(),"fonts/ClanPro-NarrMedium.otf");
        this.pro_News = Typeface.createFromAsset(context.getAssets(),"fonts/ClanPro-NarrNews.otf");
    }

    public Typeface getPro_narMedium() {
        return pro_narMedium;
    }

    public Typeface getPro_News() {
        return pro_News;
    }
}
