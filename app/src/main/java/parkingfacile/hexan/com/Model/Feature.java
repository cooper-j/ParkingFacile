package parkingfacile.hexan.com.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Feature {
    private String mId = null;
    private LatLng mLocation = null;

    public Feature(){

    }

    /**
     * @return the feature id
     */
    public String getId(){
        return mId;
    }

    /**
     * @param id the feature id
     */
    public void setId(String id){
        mId = id;
    }

    /**
     * @return the feature location
     */
    public LatLng getLocation(){
        return mLocation;
    }

    /**
     * @param loc the feature location
     */
    public void setLocation(LatLng loc){
        mLocation = loc;
    }

    /**
     * For Rennes
     * Returns a Feature that is parsed from a JSONObject
     *
     * @param featureObj the JSONObject to parse
     * @return the populated Feature
     */
    public static Feature parseFromJsonRennes(JSONObject featureObj){
        Feature feature = new Feature();

        try{
            feature.setId(featureObj.getString("id"));
            if (featureObj.getJSONObject("geometry") != null)
            {
                if (featureObj.getJSONObject("geometry").getString("type").equals("Point")) {


                    String arr = featureObj.getJSONObject("geometry").getString("coordinates");
                    String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").split(",");

                    double[] results = new double[items.length];

                    for (int i = 0; i < items.length; i++) {
                        try {
                            results[i] = Double.parseDouble(items[i]);
                        } catch (NumberFormatException nfe) {};
                    }
                    LatLng location = new LatLng(results[1], results[0]);
                    feature.setLocation(location);
                }
            }
        }catch (JSONException je){
            je.printStackTrace();
        }

        return feature;
    }

    /**
     * For Nantes
     * Returns a Feature that is parsed from a JSONObject
     *
     * @param featureObj the JSONObject to parse
     * @return the populated Feature
     */
    public static Feature parseFromJsonNantes(JSONObject featureObj){
        Feature feature = new Feature();

        try{
            feature.setId(featureObj.getString("_IDOBJ"));

            String arr = featureObj.getString("_l");
            String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").split(",");

            double[] results = new double[items.length];

            for (int i = 0; i < items.length; i++) {
                try {
                    results[i] = Double.parseDouble(items[i]);
                } catch (NumberFormatException nfe) {};
            }
            LatLng location = new LatLng(results[0], results[1]);
            feature.setLocation(location);

        }catch (JSONException je){
            je.printStackTrace();
        }

        return feature;
    }

    /**
     * For Paris
     * Returns a Feature that is parsed from a JSONObject
     *
     * @param featureObj the JSONObject to parse
     * @return the populated Feature
     */
    public static Feature parseFromJsonParis(JSONObject featureObj){
        Feature feature = new Feature();

        try{
            feature.setId("N\\A");
            if (featureObj.getJSONObject("geometry") != null)
            {
                if (featureObj.getJSONObject("geometry").getString("type").equals("Point")) {


                    String arr = featureObj.getJSONObject("geometry").getString("coordinates");
                    String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").split(",");

                    double[] results = new double[items.length];

                    for (int i = 0; i < items.length; i++) {
                        try {
                            results[i] = Double.parseDouble(items[i]);
                        } catch (NumberFormatException nfe) {};
                    }
                    LatLng location = new LatLng(results[1], results[0]);
                    feature.setLocation(location);
                }
            }
        }catch (JSONException je){
            je.printStackTrace();
        }

        return feature;
    }

    /**
     * For Bordeaux
     * Returns a Feature that is parsed from a JSONObject
     *
     * @param featureObj the JSONObject to parse
     * @return the populated Feature
     */
    public static Feature parseFromJsonBordeaux(JSONObject featureObj) {
        Feature feature = new Feature();

        try {
            feature.setId("N\\A");
            feature.setLocation(new LatLng(featureObj.getDouble("y_lat"), featureObj.getDouble("x_long")));
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return feature;
    }

    /**
     * For Strasbourg
     * Returns a Feature that is parsed from a JSONObject
     *
     * @param featureObj the JSONObject to parse
     * @return the populated Feature
     */
    public static Feature parseFromJsonStrasbourg(JSONObject featureObj) {
        Feature feature = new Feature();

        try {
            feature.setId(featureObj.getString("id"));
            if (featureObj.has("go")) {
                JSONObject geo = featureObj.getJSONObject("go");
                feature.setLocation(new LatLng(geo.getDouble("y"), geo.getDouble("x")));
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return feature;
    }
}
