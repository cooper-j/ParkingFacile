package parkingfacile.hexan.com.Controller;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import parkingfacile.hexan.com.Controller.Http.StrasbourgParkingRestClient;
import parkingfacile.hexan.com.Model.Park;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Strasbourg extends AbstractCity {

    public static final LatLng location = new LatLng(48.58, 7.75);

    private final String mParks = "/Parking.geometry";
    private final String mParksStatus = "/Parking.status";
    private final static String ERROR_MSG = "Erreur lors de la récupération des données Strasbourg";

    private ArrayList<Park> parkList = new ArrayList<Park>();

    public Strasbourg(){
        super();
    }

    @Override
    public void getParks() {

        StrasbourgParkingRestClient.get(this.mParks, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                parkList.clear();
                try {
                    JSONArray parks = response.getJSONArray("s");
                    for (int i = 0; i < parks.length(); i++) {
                        JSONObject park = parks.getJSONObject(i);
                        if (park != null) {
                            Park parkTmp = Park.parseFromJsonStrasbourg(park);
                            if (parkTmp.getId() != null)
                                parkList.add(parkTmp);
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                getParks(parkList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            private void getParks(final ArrayList<Park> parkList) {
                StrasbourgParkingRestClient.get(mParksStatus, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONArray parks = response.getJSONArray("s");
                            HashMap<String, Integer> status = new HashMap<>();
                            status.put("status_1", 0);
                            status.put("status_2", 1);
                            status.put("status_3", 2);
                            status.put("status_4", 3);
                            status.put("status_0", 5);
                            for (Park p : parkList) {
                                for (int i = 0; i < parks.length(); i++) {
                                    JSONObject park = parks.getJSONObject(i);
                                    if (park.getString("id").equals(p.getId())) {
                                        if (park.has("ds"))
                                            p.getparkInformation().setStatus(status.get(park.getString("ds")));
                                        p.getparkInformation().setFree(park.has("df") ? park.getInt("df") : -1);
                                        p.getparkInformation().setMax(park.has("dt") ? park.getInt("dt") : -1);
                                    }
                                }
                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                        onParksFoundListener.onParksFound(parkList, OnParksFoundListener.TypeVille.STRASBOURG);
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
