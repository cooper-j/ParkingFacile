package parkingfacile.hexan.com.Controller;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import parkingfacile.hexan.com.Controller.Http.ParisParkingRestClient;
import parkingfacile.hexan.com.Model.Park;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Paris extends AbstractCity {

    public static final LatLng location = new LatLng(48.8567, 2.3508);

    private final String mParks = "";
    private final static String ERROR_MSG = "Erreur lors de la récupération des données Paris";

    private ArrayList<Park> parkList = new ArrayList<Park>();

    public Paris(){
        super();
    }

    @Override
    public void getParks() {
        RequestParams params = new RequestParams();
        params.put("dataset", "parcs-de-stationnement-concedes-de-la-ville-de-paris");

         ParisParkingRestClient.get(mParks, params, new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 parkList.clear();
                 try {
                     JSONArray parks = response.getJSONArray("records");
                     for (int i = 0; i < parks.length(); i++) {
                         JSONObject park = parks.getJSONObject(i);
                         if (park != null) {
                             Park parkTmp = Park.parseFromJsonParis(park);
                             if (parkTmp.getId() != null)
                                 parkList.add(parkTmp);
                         }
                     }
                 } catch (JSONException je) {
                     je.printStackTrace();
                 }
                 onParksFoundListener.onParksFound(parkList, OnParksFoundListener.TypeVille.PARIS);
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
