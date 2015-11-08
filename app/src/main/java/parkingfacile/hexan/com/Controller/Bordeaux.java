package parkingfacile.hexan.com.Controller;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import parkingfacile.hexan.com.Controller.Http.BordeauxParkingRestClient;
import parkingfacile.hexan.com.Model.Park;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Bordeaux extends AbstractCity {

    public static final LatLng location = new LatLng(44.84, -0.58);

    private final String mParks = "/sigparkpub";
    private final static String ERROR_MSG = "Erreur lors de la récupération des données Bordeaux";

    private ArrayList<Park> parkList = new ArrayList<Park>();

    public Bordeaux(){
        super();
    }

    @Override
    public void getParks() {

        RequestParams params = new RequestParams();
        params.put("format", "json");

         BordeauxParkingRestClient.get(this.mParks, params, new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 parkList.clear();
                 try {
                     JSONArray parks = response.getJSONArray("d");
                     for (int i = 0; i < parks.length(); i++) {
                         JSONObject park = parks.getJSONObject(i);
                         if (park != null) {
                             Park parkTmp = Park.parseFromJsonBordeaux(park);
                             if (parkTmp.getId() != null)
                                 parkList.add(parkTmp);
                         }
                     }
                 } catch (JSONException je) {
                     je.printStackTrace();
                 }

                 onParksFoundListener.onParksFound(parkList, OnParksFoundListener.TypeVille.BORDEAUX);
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
