package parkingfacile.hexan.com.Controller;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cz.msebera.android.httpclient.Header;
import parkingfacile.hexan.com.Controller.Activity.MainActivity;
import parkingfacile.hexan.com.Controller.Http.GoogleMapApiRestClient;
import parkingfacile.hexan.com.Controller.Http.RennesParkingRestClient;
import parkingfacile.hexan.com.Model.Feature;
import parkingfacile.hexan.com.Model.GooglePark;
import parkingfacile.hexan.com.Model.Park;
import parkingfacile.hexan.com.R;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Google extends AbstractCity {

    private LatLng mLocation;

    private MainActivity mActivity;
    private final String mParks = "/place/nearbysearch/json";
    private final static String ERROR_MSG = "Erreur lors de la récupération des données Google";

    private ArrayList<GooglePark> googleParkList = new ArrayList<>();

    protected OnGoogleParksFoundListener onGoogleParksFoundListener;

    public Google(MainActivity act, LatLng location){
        super();
        mActivity = act;
        mLocation = location;
    }

    @Override
    public void getParks() {

        RequestParams params = new RequestParams();
        params.put("location", mLocation.latitude + "," + mLocation.longitude);
        params.put("radius", "10000");
        params.put("types", "parking");
        params.put("key", mActivity.getString(R.string.google_places_key));

        GoogleMapApiRestClient.get(this.mParks, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray parks = response.getJSONArray("results");
                    for (int i = 0; i < parks.length(); i++) {
                        googleParkList.add(GooglePark.parseFromJson(parks.getJSONObject(i)));
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                onGoogleParksFoundListener.onGoogleParksFound(googleParkList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onHttpErrorListener.onHttpError(ERROR_MSG);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onHttpErrorListener.onHttpError(ERROR_MSG);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onHttpErrorListener.onHttpError(ERROR_MSG);
            }
        });
    }

    @Override
    public void getParksDetail() {

    }

    @Override
    public void getParksFree() {

    }

    @Override
    public void getParksStatus() {

    }

    @Override
    public void getParksTimeAndPrice() {

    }

    public interface OnGoogleParksFoundListener {
        void onGoogleParksFound(ArrayList<GooglePark> parkArrayList);
    }

    public void setGoogleParksFoundListener(OnGoogleParksFoundListener listener) {
        onGoogleParksFoundListener = listener;
    }
}
