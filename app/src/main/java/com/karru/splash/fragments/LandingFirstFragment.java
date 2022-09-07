package com.karru.splash.fragments;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karru.splash.first.SplashContract;
import com.heride.rider.R;
import com.karru.util.AppTypeface;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public class LandingFirstFragment extends DaggerFragment
{
    @BindView(R.id.iv_landing_tireBack) ImageView iv_landing_tireBack;
    @BindView(R.id.iv_landing_tireFront) ImageView iv_landing_tireFront;
    @BindView(R.id.rv_landing_car) RelativeLayout rv_landing_car;
    @BindView(R.id.tv_landing_label1) TextView tv_landing_label1;

    private Unbinder unbinder;
    private SplashContract.Presenter presenter;

    @Inject AppTypeface appTypeface;
    @Inject public LandingFirstFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_landing_first, container, false);
        unbinder = ButterKnife.bind(this,view);
        tv_landing_label1.setTypeface(appTypeface.getPro_News());
        move();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * <h2>getPresenter</h2>
     * used to get the presenter
     * @param presenter presenter
     */
    public void getPresenter(SplashContract.Presenter presenter)
    {
        this.presenter = presenter;
    }

    /**
     * <h>Car Animator</h>
     * <p>this method is using to Create Animation for car</p>
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void move()
    {
        Animation anim = null;
        if(presenter != null)
        {
            switch (presenter.checkForDirection())
            {
                case LayoutDirection.LTR:
                    rv_landing_car.setScaleX(1);
                    anim=new TranslateAnimation(Animation.ABSOLUTE,350,Animation.ABSOLUTE,Animation.ABSOLUTE);
                    break;

                case LayoutDirection.RTL:
                    rv_landing_car.setScaleX(-1);
                    anim=new TranslateAnimation(Animation.ABSOLUTE,-350,Animation.ABSOLUTE,Animation.ABSOLUTE);
                    break;
            }
        }
        else
        {
            rv_landing_car.setScaleX(1);
            anim=new TranslateAnimation(Animation.ABSOLUTE,350,Animation.ABSOLUTE,Animation.ABSOLUTE);
        }

        ObjectAnimator animator=ObjectAnimator.ofFloat(iv_landing_tireBack,"rotation",0f,360f);
        ObjectAnimator animator2=ObjectAnimator.ofFloat(iv_landing_tireFront,"rotation",0f,360f);
        anim.setDuration(1000);
        animator.setDuration(1000);
        animator2.setDuration(1000);
        animator.start();
        animator2.start();
        anim.setFillAfter(true);
        rv_landing_car.setAnimation(anim);
    }
}
