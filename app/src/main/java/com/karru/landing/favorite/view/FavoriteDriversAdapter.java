package com.karru.landing.favorite.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.karru.dagger.ActivityScoped;
import com.karru.landing.favorite.FavoriteDriversContract;
import com.karru.landing.favorite.model.FavoritesDriversDetails;
import com.karru.util.AppTypeface;
import com.karru.utility.Utility;
import com.heride.rider.R;
import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

@ActivityScoped
public class FavoriteDriversAdapter extends  RecyclerView.Adapter<FavoriteDriversAdapter.ViewHolder>
{
    private static final String TAG = "FavoriteDriversAdapter";
    private ArrayList<FavoritesDriversDetails> favoritesDriversDetails;
    private AppTypeface appTypeface;
    private Context mContext;
    private FavoriteDriversContract.Presenter presenter;

    @Inject
    public FavoriteDriversAdapter(AppTypeface appTypeface, ArrayList<FavoritesDriversDetails> favoritesDriversDetails,
                                  FavoriteDriversContract.Presenter presenter)
    {
        this.appTypeface=appTypeface;
        this.presenter=presenter;
        this.favoritesDriversDetails=favoritesDriversDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_driver, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Utility.printLog(TAG+" favoriteOnlineDriversDetails name "+favoritesDriversDetails.get(position).getName());
        holder.tv_favorite_driver_name.setText(favoritesDriversDetails.get(position).getName());
        if(!favoritesDriversDetails.get(position).getProfilePic().equals("") &&
                favoritesDriversDetails.get(position).getProfilePic() != null)
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.placeholder(holder.signup_profile_default_image);
            requestOptions = requestOptions.optionalCircleCrop();
            Glide.with(mContext)
                    .load(favoritesDriversDetails.get(position).getProfilePic())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.pb_favorite_driver_progress.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.pb_favorite_driver_progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_favorite_driver_image);
        }
        holder.iv_favorite_driver_icon.setOnClickListener(v ->
                presenter.handleDeleteFavDriver(favoritesDriversDetails.get(position).getDriverId()));
    }

    @Override
    public int getItemCount() {
        return favoritesDriversDetails.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_favorite_driver_name) TextView tv_favorite_driver_name;
        @BindView(R.id.iv_favorite_driver_image) ImageView iv_favorite_driver_image;
        @BindView(R.id.iv_favorite_driver_icon) ImageView iv_favorite_driver_icon;
        @BindView(R.id.pb_favorite_driver_progress) ProgressBar pb_favorite_driver_progress;
        @BindDrawable(R.drawable.signup_profile_default_image) Drawable signup_profile_default_image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            setTypeFace();
        }

        void setTypeFace()
        {
            tv_favorite_driver_name.setTypeface(appTypeface.getPro_narMedium());
        }
    }
}
