package parkingfacile.hexan.com.Controller;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import parkingfacile.hexan.com.Controller.Http.RennesParkingRestClient;
import parkingfacile.hexan.com.Model.Feature;
import parkingfacile.hexan.com.Model.Park;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Rennes extends AbstractCity {

    public static final LatLng location = new LatLng(48.1119800, -1.6742900);

    private final String mParks = "/parks";
    private final String mStatus = "/status";
    private final String mDetail = "/free";
    private final String mFree = "/";
    private final String mTimePrices = "/timetable-and-prices";
    private final static String ERROR_MSG = "Erreur lors de la récupération des données Rennes";

    private ArrayList<Park> parkList = new ArrayList<>();

    public Rennes(){
        super();
    }

    @Override
    public void getParks() {
        RequestParams params = new RequestParams();
        params.put("crs", "EPSG:4326");

         RennesParkingRestClient.get(this.mParks, params, new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 parkList.clear();
                 ArrayList<Feature> featureList = new ArrayList<Feature>();
                 try {
                     JSONArray parks = response.getJSONArray("parks");
                     for (int i = 0; i < parks.length(); i++) {
                         JSONObject park = parks.getJSONObject(i);
                         if (park != null) {
                             Park parkTmp = Park.parseFromJsonRennes(park);
                             if (parkTmp.getId() != null)
                                 parkList.add(parkTmp);
                         }
                     }
                     JSONObject featuresObj = response.getJSONObject("features");
                     JSONArray features = featuresObj.getJSONArray("features");
                     for (int i = 0; i < features.length(); i++) {
                         JSONObject feature = features.getJSONObject(i);
                         if (feature != null) {
                             Feature featureTmp = Feature.parseFromJsonRennes(feature);
                             if (featureTmp.getId() != null)
                                 featureList.add(featureTmp);
                         }
                     }
                 } catch (JSONException je) {
                     je.printStackTrace();
                 }

                 for (Park p : parkList) {
                     for (Feature f : featureList) {
                         if (f.getId().equals(p.getId())) {
                             p.setFeature(f);
                             break;
                         }
                     }
                 }
                 onParksFoundListener.onParksFound(parkList, OnParksFoundListener.TypeVille.RENNES);
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

    public ArrayList<Park> getParkList(){
        return this.parkList;
    }
}
