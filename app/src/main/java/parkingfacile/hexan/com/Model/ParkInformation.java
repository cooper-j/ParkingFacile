package parkingfacile.hexan.com.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by james_000 on 9/20/2015.
 */
public class ParkInformation {
    private String mName;
    private int mStatusId;
    private int mMax;
    private int mFree;

    /**
     * @return
     */
    public String getName(){
        return mName;
    }

    /**
     * @param name
     */
    public void setName(String name){
        this.mName = name;
    }

    /**
     * @return
     */
    public int getStatus(){
        return mStatusId;
    }

    /**
     * @param status
     */
    public void setStatus(int status){
        this.mStatusId = status;
    }

    /**
     * @return
     */
    public int getMax(){
        return mMax;
    }

    /**
     * @param max
     */
    public void setMax(int max){
        this.mMax = max;
    }

    /**
     * @return
     */
    public int getFree(){
        return mFree;
    }

    /**
     * @param free
     */
    public void setFree(int free){
        this.mFree = free;
    }

    /**
     * From Rennes
     * Returns a ParkInformation that is parsed from a JSONObject
     *
     * @param parkInfoObj the JSONObject to parse
     * @return the populated ParkInformation
     */
    public static ParkInformation parseFromJsonRennes(JSONObject parkInfoObj){
        ParkInformation pi = new ParkInformation();


        HashMap<String, Integer> status = new HashMap<>();
        status.put("AVAILABLE", 0);
        status.put("FULL", 1);
        status.put("CLOSED ", 3);

        try {
            pi.setName(parkInfoObj.getString("name"));
            pi.setStatus(status.get(parkInfoObj.getString("status")));
            pi.setMax(parkInfoObj.getInt("max"));
            pi.setFree(parkInfoObj.getInt("free"));
        }
        catch (JSONException je){
            je.printStackTrace();
        }

        return pi;
    }

    /**
     * From Nantes
     * Returns a ParkInformation that is parsed from a JSONObject
     *
     * @param parkInfoObj the JSONObject to parse
     * @return the populated ParkInformation
     */
    public static ParkInformation parseFromJsonNantes(JSONObject parkInfoObj){
        ParkInformation pi = new ParkInformation();
        int[] status = { 2, 3, 4, 0, 0, 1};
        try {
            pi.setName(parkInfoObj.getString("Grp_nom"));
            pi.setMax(parkInfoObj.getInt("Grp_exploitation"));
            pi.setFree(parkInfoObj.getInt("Grp_disponible"));
            int statusNb = pi.getFree() < pi.getMax() && parkInfoObj.getInt("Grp_statut") == 5  ? 4 : parkInfoObj.getInt("Grp_statut");
            pi.setStatus(status[statusNb]);
        }
        catch (JSONException je){
            je.printStackTrace();
        }

        return pi;
    }

    /**
     * From Paris
     * Returns a ParkInformation that is parsed from a JSONObject
     *
     * @param parkInfoObj the JSONObject to parse
     * @return the populated ParkInformation
     */
    public static ParkInformation parseFromJsonParis(JSONObject parkInfoObj){
        ParkInformation pi = new ParkInformation();
        try {
            pi.setName(parkInfoObj.getString("nom_du_parc_de_stationnement"));
            pi.setMax(-1);
            pi.setFree(-1);
            pi.setStatus(0);
        }
        catch (JSONException je){
            je.printStackTrace();
        }

        return pi;
    }

    /**
     * From Bordeaux
     * Returns a ParkInformation that is parsed from a JSONObject
     *
     * @param parkInfoObj the JSONObject to parse
     * @return the populated ParkInformation
     */
    public static ParkInformation parseFromJsonBordeaux(JSONObject parkInfoObj){
        ParkInformation pi = new ParkInformation();
        try {
            pi.setName(parkInfoObj.getString("nom"));
            pi.setMax(-1);
            pi.setFree(parkInfoObj.has("nombre_de_places") ? parkInfoObj.getInt("nombre_de_places") : -1);
            pi.setStatus(0);
        }
        catch (JSONException je){
            je.printStackTrace();
        }

        return pi;
    }

    /**
     * From Strasbourg
     * Returns a ParkInformation that is parsed from a JSONObject
     *
     * @param parkInfoObj the JSONObject to parse
     * @return the populated ParkInformation
     */
    public static ParkInformation parseFromJsonStrasbourg(JSONObject parkInfoObj){
        ParkInformation pi = new ParkInformation();
        try {
            pi.setName(parkInfoObj.getString("ln"));
        }
        catch (JSONException je){
            je.printStackTrace();
        }

        return pi;
    }
}
