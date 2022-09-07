/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karru.util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.heride.rider.R;

import static androidx.core.util.Preconditions.checkNotNull;

/**
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    private FragmentManager fragmentManager;

    public ActivityUtils(FragmentManager fragmentManager) {
        this.fragmentManager=fragmentManager;
    }

    /**
     * <h2>hideSoftKeyBoard</h2>
     * <p>
     * This method is used for hiding the soft keyboard.
     * </p>
     *
     * @param v view instance.
     */
    public static void hideSoftKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    @SuppressLint("RestrictedApi")
    public void addFragmentToActivity (@NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        fragmentManager.beginTransaction().replace(frameId, fragment).commit();
    }

    /**
     * <h2>getBitmapFromShape</h2>
     * used to get bitmap from shape drawable
     * @param context context
     * @param shape shape from drawable
     * @return returns bitmap
     */
    public static Bitmap getBitmapFromShape(Context context,Drawable shape)
    {
        int px = context.getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
        shape.draw(canvas);
        return mDotMarkerBitmap;
    }

    /**
     * <h2>changeTextColor</h2>
     * This method is used to set the color to same text view with different string
     * @param mContext context
     * @param text1 text string 1
     * @param text2 text string 2
     * @param textView text view to be set
     */
    public static void changeTextColor(Context mContext, String text1, String text2, TextView textView)
    {
        Spannable word = new SpannableString(text1);
        word.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.confirm_pick_address)), 0, word.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(word);
        Spannable word1 = new SpannableString(" "+text2);
        word1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.vehicle_unselect_color)), 0, word1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(word1);
    }

    /**
     * <h2>timeAdjustment</h2>
     * used to adjust the amount with currency symbol
     * @param timeMetrics time metrics
     * @param time time
     * @return returns time adjusted
     */
    public static String timeAdjustment(String timeMetrics,String time)
    {
       return time+" "+timeMetrics;
    }

    /**
     * <h2>timeAdjustment</h2>
     * used to adjust the amount with currency symbol
     * @param distanceMetrics distance metrics
     * @param distance distance
     * @return returns distance adjusted
     */
    public static String distanceAdjustment(String distanceMetrics,String distance)
    {
       return distance+" "+distanceMetrics;
    }
}
