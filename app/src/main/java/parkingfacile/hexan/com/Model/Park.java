package parkingfacile.hexan.com.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james_000 on 9/20/2015.
 */
public class Park {
    private String mId = null;

    private ParkInformation mParkInformation = null;

    private Feature mFeature = null;

    /**
     * @return
     */
    public String getId(){
        return mId;
    }

    /**
     * @param id
     */
    public void setId(String id){
        mId = id;
    }

    /**
     * @return
     */
    public ParkInformation getparkInformation(){
        return mParkInformation;
    }

    /**
     * @param pi
     */
    public void setParkInformation(ParkInformation pi){
        mParkInformation = pi;
    }

    /**
     * @return
     */
    public Feature getFeature(){
        return mFeature;
    }

    /**
     * @param feature
     */
    public void setFeature(Feature feature){
        this.mFeature = feature;
    }

    /**
     * From Rennes
     * Returns a Park that is parsed from a JSONObject
     *
     * @param parkObj the JSONObject to parse
     * @return the populated Park
     */
    public static Park parseFromJsonRennes(JSONObject parkObj){
        Park park = new Park();
        try{
            park.setId(parkObj.getString("id"));
            park.setParkInformation(ParkInformation.parseFromJsonRennes(parkObj.getJSONObject("parkInformation")));
        }catch(JSONException je){
            je.printStackTrace();
        }
        return park;
    }

    /**
     * From Nantes
     * Returns a Park that is parsed from a JSONObject
     *
     * @param parkObj the JSONObject to parse
     * @return the populated Park
     */
    public static Park parseFromJsonNantes(JSONObject parkObj){
        Park park = new Park();
        try{
            park.setId(parkObj.getString("IdObj"));
            park.setParkInformation(ParkInformation.parseFromJsonNantes(parkObj));
        }catch(JSONException je){
            je.printStackTrace();
        }
        return park;
    }

    /**
     * From Paris
     * Returns a Park that is parsed from a JSONObject
     *
     * @param parkObj the JSONObject to parse
     * @return the populated Park
     */
    public static Park parseFromJsonParis(JSONObject parkObj){
        Park park = new Park();
        try{
            park.setId(parkObj.getString("recordid"));
            park.setParkInformation(ParkInformation.parseFromJsonParis(parkObj.getJSONObject("fields")));
            park.setFeature(Feature.parseFromJsonParis(parkObj));
        }catch(JSONException je){
            je.printStackTrace();
        }
        return park;
    }

    /**
     * From Bordeaux
     * Returns a Park that is parsed from a JSONObject
     *
     * @param parkObj the JSONObject to parse
     * @return the populated Park
     */
    public static Park parseFromJsonBordeaux(JSONObject parkObj){
        Park park = new Park();
        try{
            park.setId(parkObj.getString("cle"));
            park.setParkInformation(ParkInformation.parseFromJsonBordeaux(parkObj));
            park.setFeature(Feature.parseFromJsonBordeaux(parkObj));
        }catch(JSONException je){
            je.printStackTrace();
        }
        return park;
    }

    /**
     * From Strasbourg
     * Returns a Park that is parsed from a JSONObject
     *
     * @param parkObj the JSONObject to parse
     * @return the populated Park
     */
    public static Park parseFromJsonStrasbourg(JSONObject parkObj){
        Park park = new Park();
        try{
            park.setId(parkObj.getString("id"));
            park.setParkInformation(ParkInformation.parseFromJsonStrasbourg(parkObj));
            park.setFeature(Feature.parseFromJsonStrasbourg(parkObj));
        }catch(JSONException je){
            je.printStackTrace();
        }
        return park;
    }
}
