package com.karru.booking_flow.address.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.karru.booking_flow.address.AddressSelectContract;
import com.karru.booking_flow.address.model.PlaceAutoCompleteModel;
import com.karru.util.AppTypeface;
import com.heride.rider.R;
import com.karru.utility.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.karru.utility.Constants.GMT_CURRENT_LAT;
import static com.karru.utility.Constants.GMT_CURRENT_LNG;
import static com.karru.utility.Constants.OUT_JSON;
import static com.karru.utility.Constants.PLACES_API_BASE;
import static com.karru.utility.Constants.TYPE_AUTOCOMPLETE;

/**
 * <h1>PlaceAutoComplete_Adapter</h1>
 * This class is used to provide the PlaceAutoComplete screen, where we can see our Address.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class PlaceAutoCompleteAdapter extends RecyclerView.Adapter<PlaceAutoCompleteAdapter.PlaceViewHolder>
        implements Filterable
{
    private static final String TAG = "PlaceAutocompleteAdapter";
    private Activity mContext;
    private AddressSelectContract.View mListener;
    private ArrayList<PlaceAutoCompleteModel> mResultList;
    private AppTypeface appTypeface;
    private AddressSelectContract.Presenter presenter;

    /**
     * <h2>PlaceAutoCompleteAdapter</h2>
     * This method is constructor
     * @param context context of the activity from which this is called
     */
    PlaceAutoCompleteAdapter(Activity context, AddressSelectContract.View listener,
                             AddressSelectContract.Presenter presenter)
    {
        mContext=context;
        this.presenter=presenter;
        this.mListener = listener;
        appTypeface=AppTypeface.getInstance(context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null)
                {
                    // Query the autocomplete API for the (constraint) search string.
                    Utility.printLog(TAG+ " : "+constraint);
                    mResultList = autocomplete(constraint.toString());
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    mContext.runOnUiThread(() ->
                    {
                        // Stuff that updates the UI
                        notifyDataSetChanged();
                    });
                }
            }
        };
    }

    /**
     * <h2>autocomplete</h2>
     * This method will filter the address with the input given to it
     * @param input filter given to auto complete
     * @return returns the list of addresses filtered
     */
    private ArrayList<PlaceAutoCompleteModel> autocomplete(String input) {
        ArrayList resultList = null;
        ArrayList<PlaceAutoCompleteModel> auto_complete_pojo_list = new ArrayList<PlaceAutoCompleteModel>();
        PlaceAutoCompleteModel auto_complete_pojo;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url= getUrlForGoogleAutoComplete(input);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            Utility.printLog(TAG+" ,address: "+jsonResults);
        } catch (MalformedURLException e) {
            Utility.printLog(TAG+ "Error processing Places API URL"+ e);
            return resultList;
        } catch (IOException e) {
            Utility.printLog(TAG+ "Error connecting to Places API"+ e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            //if status is ok then set all the address in adapter else rotate the keys
            switch (jsonObj.getString("status"))
            {
                case "OK":
                case "ZERO_RESULTS":
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                    Utility.printLog(TAG+" ,address: "+jsonObj+ " -----, "+predsJsonArray);
                    // Extract the Place descriptions from the results
                    resultList = new ArrayList(predsJsonArray.length());
                    for (int i = 0; i < predsJsonArray.length(); i++)
                    {
                        auto_complete_pojo = new PlaceAutoCompleteModel();
                        Utility.printLog(TAG+" response:"+predsJsonArray.getJSONObject(i).getString("description"));
                        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                        auto_complete_pojo.setAddress(predsJsonArray.getJSONObject(i).getString("description"));
                        auto_complete_pojo.setRef_key(predsJsonArray.getJSONObject(i).getString("reference"));
                        auto_complete_pojo_list.add(i, auto_complete_pojo);
                    }
                    break;

                default:
                    presenter.rotateNextKey();
                    break;
            }
        } catch (JSONException e) {
            Utility.printLog(TAG+" ,address exception : "+e);
            e.printStackTrace();
        }
        return auto_complete_pojo_list;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_drop_address, viewGroup, false);
        return new PlaceViewHolder(convertView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, final int i)
    {
        Utility.printLog(TAG+" Address item pos "+i);
        Utility.printLog(TAG+" Address item size "+mResultList.size());
        if(i<mResultList.size())
        {
            mPredictionHolder.tv_select_address.setText(""+mResultList.get(i).getAddress());
            mPredictionHolder.tv_select_address.setTypeface(appTypeface.getPro_News());
            mPredictionHolder.tv_select_address_fav_title.setVisibility(View.GONE);
            //to add the click listener for the layout and notify
            mPredictionHolder.rl_drop_address_layout.setOnClickListener(v -> mListener.onPlaceClick(mResultList,i));
        }
    }
    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    /**
     * <h2>getUrlForGoogleAutoComplete</h2>
     * This method is used to create the link for google API
     * @param input input for the address filter
     * @return returns the URL for the google API
     */
    private URL getUrlForGoogleAutoComplete(String input)
    {
        URL url = null;
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=").append(presenter.getServerKey());
            sb.append("&location=").append(GMT_CURRENT_LAT).append(",").
                    append(GMT_CURRENT_LNG).append("&radius=500&amplanguage=en");
            sb.append("&input=").append(URLEncoder.encode(input, "utf8"));
            Utility.printLog(TAG+ " urL : "+sb.toString());
            url = new URL(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * <h2>PlaceViewHolder</h2>
     * This method is used for defining the android widgets
     */
    class PlaceViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_drop_fav_icon, iv_drop_clock_icon;
        RelativeLayout rl_drop_address_layout;
        TextView tv_select_address, tv_select_address_fav_title;
        PlaceViewHolder(View itemView)
        {
            super(itemView);
            iv_drop_clock_icon = itemView.findViewById(R.id.iv_select_address_left_icon);
            iv_drop_clock_icon.setImageResource(R.drawable.home_location_icon);
            rl_drop_address_layout = itemView.findViewById(R.id.rl_select_address_layout);
            tv_select_address = itemView.findViewById(R.id.tv_select_address);
            tv_select_address_fav_title = itemView.findViewById(R.id.tv_select_address_fav_title);

            iv_drop_fav_icon = itemView.findViewById(R.id.iv_select_address_right_icon);
            iv_drop_fav_icon.setVisibility(View.GONE);
        }
    }
}
