package parkingfacile.hexan.com.Controller;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import parkingfacile.hexan.com.Controller.Http.NantesParkingRestClient;
import parkingfacile.hexan.com.Model.Feature;
import parkingfacile.hexan.com.Model.Park;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Nantes extends AbstractCity {

    public static final LatLng location = new LatLng(47.2181, -1.5528);

    private final String mParks = "/getDisponibiliteParkingsPublics/1.0/";
    private final String mApiId = "4CM9NJZ9187Q0PH";
    private final static String ERROR_MSG = "Erreur lors de la récupération des données Nantes";

    private ArrayList<Park> parkList = new ArrayList<Park>();

    public Nantes(){
        super();
    }

    @Override
    public void getParks() {
        RequestParams params = new RequestParams();
        params.put("output", "json");

        NantesParkingRestClient.get("/publication/24440040400129_NM_NM_00022/LOC_EQUIPUB_MOBILITE_NM_STBL/content", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                ArrayList<Feature> featureList = new ArrayList<Feature>();
                try {
                    JSONArray features = response.getJSONArray("data");
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject feature = features.getJSONObject(i);
                        if (feature != null) {
                            Feature featureTmp = Feature.parseFromJsonNantes(feature);
                            if (featureTmp.getId() != null)
                                featureList.add(featureTmp);
                        }
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
                this.getParks(featureList);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            private void getParks(final ArrayList<Feature> featureList) {
                RequestParams params = new RequestParams();
                params.put("output", "json");
                NantesParkingRestClient.get(mParks + mApiId, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        parkList.clear();
                        try {
                            JSONArray parks = response.getJSONObject("opendata").getJSONObject("answer")
                                    .getJSONObject("data").getJSONObject("Groupes_Parking").getJSONArray("Groupe_Parking");
                            for (int i = 0; i < parks.length(); i++) {
                                JSONObject park = parks.getJSONObject(i);
                                parkList.add(Park.parseFromJsonNantes(park));
                            }
                            for (Park p : parkList) {
                                for (Feature f : featureList) {
                                    if (f.getId().equals(p.getId())) {
                                        p.setFeature(f);
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                        onParksFoundListener.onParksFound(parkList, OnParksFoundListener.TypeVille.NANTES);
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
