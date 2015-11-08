package parkingfacile.hexan.com.Model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by james_000 on 9/25/2015.
 */
public class GooglePark {
    private String mId;
    private String mPlaceId;
    private LatLng mLocation;
    private String mName;
    private boolean mIsOpen;

    /**
     * @return
     */
    public String getId() {
        return mId;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.mId = id;
    }

    /**
     * @return
     */
    public String getPlaceId() {
        return mPlaceId;
    }

    /**
     * @param placeId
     */
    public void setPlaceId(String placeId) {
        this.mPlaceId = placeId;
    }

    /**
     * @return
     */
    public LatLng getLocation() {
        return mLocation;
    }

    /**
     * @param location
     */
    public void setLocation(LatLng location) {
        this.mLocation = location;
    }

    /**
     * @return
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.mName = name;
    }

    /**
     * @return
     */
    public boolean isOpen() {
        return mIsOpen;
    }

    /**
     * @param isOpen
     */
    public void setIsOpen(boolean isOpen) {
        this.mIsOpen = isOpen;
    }

    /**
     *
     * Returns a GooglePark that is parsed from a JSONObject
     *
     * @param obj the JSONObject to parse
     * @return the populated GooglePark
     */
    public static GooglePark parseFromJson(JSONObject obj){
        GooglePark googlePark = new GooglePark();
        try {
            if (obj.has("id"))
                googlePark.setId(obj.getString("id"));
            if (obj.has("place_id"))
                googlePark.setPlaceId(obj.getString("place_id"));
            if (obj.has("name"))
                googlePark.setName(obj.getString("name"));
            if (obj.has("geometry") && obj.getJSONObject("geometry").has("location")
                    && obj.getJSONObject("geometry").getJSONObject("location").has("lat")
                    && obj.getJSONObject("geometry").getJSONObject("location").has("lng"))
                googlePark.setLocation(new LatLng(obj.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                        obj.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
            if (obj.has("opening_hours") && obj.getJSONObject("opening_hours").has("open_now"))
                googlePark.setIsOpen(obj.getJSONObject("opening_hours").getBoolean("open_now"));
        }catch (JSONException je){
            je.printStackTrace();
        }
        return googlePark;
    }
}
