package parkingfacile.hexan.com.Controller.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import parkingfacile.hexan.com.Controller.AbstractCity;
import parkingfacile.hexan.com.Controller.Bordeaux;
import parkingfacile.hexan.com.Controller.Google;
import parkingfacile.hexan.com.Controller.Nantes;
import parkingfacile.hexan.com.Controller.Paris;
import parkingfacile.hexan.com.Controller.Rennes;
import parkingfacile.hexan.com.Controller.SelectTownDialogFragment;
import parkingfacile.hexan.com.Controller.Strasbourg;
import parkingfacile.hexan.com.Model.GooglePark;
import parkingfacile.hexan.com.Model.Park;
import parkingfacile.hexan.com.R;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap;

    private ArrayList<Park> mParkArrayList = new ArrayList<>();

    private TextView mCityName;
    private FloatingActionButton changeLocationBtn;
    private SelectTownDialogFragment mSelectTownDialog = new SelectTownDialogFragment();

    private String[] mTown;
    private float[] mTownZoom = {12.5F, 12.5F, 11F, 12F, 12F};
    private LatLng[] mTownLocation = { Rennes.location, Nantes.location, Paris.location, Bordeaux.location, Strasbourg.location };
    private String[] mStatusName = { "Ouvert", "Complet", "Indisponible", "Fermé", "Abonnes", "Inconnu" };
    private LatLng userLocation = Rennes.location;

    public static final String PREFS = "access_parking_prefs";
    public static final String GOOGLE_PARKING = "google_parking";
    public static final String VILLE_ID = "ville_id";

    SharedPreferences settings;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<Marker> googleMarkerList = new ArrayList<>();

    private Rennes mHttpR = new Rennes();
    private Nantes mHttpN=new Nantes();
    private Paris mHttpP = new Paris();
    private Bordeaux mHttpB = new Bordeaux();
    private Strasbourg mHttpS = new Strasbourg();

    private Boolean isRennesReady = false;
    private Boolean isNantesReady = false;
    private Boolean isParisReady = false;
    private Boolean isBordeauxReady = false;
    private Boolean isStrasbourgReady = false;

    private final ReentrantLock googleLock = new ReentrantLock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(PREFS, 0);
        if (!settings.contains(GOOGLE_PARKING)) {
            settings.edit().putBoolean(GOOGLE_PARKING, false).commit();
        }
        if (!settings.contains(VILLE_ID)) {
            settings.edit().putInt(VILLE_ID, 0).commit();
        }


        mTown = new String[]{getString(R.string.rennes), getString(R.string.nantes), getString(R.string.paris), getString(R.string.bordeaux), getString(R.string.strasbourg)};

        setUpMapIfNeeded();

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                userLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
            }
        });

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mCityName = (TextView) findViewById(R.id.city_name);
        changeLocationBtn = (FloatingActionButton)findViewById(R.id.changeLocationBtn);

        mCityName.setText(mTown[settings.getInt(VILLE_ID, 0)]);
        mCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLocation(mTownLocation[settings.getInt(VILLE_ID, 0)], mTownZoom[settings.getInt(VILLE_ID, 0)]);
            }
        });

        changeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectTownDialog.show(getSupportFragmentManager(), "Towns");
            }
        });

        if (isConnectedToInternet()) {
            initHttpParkings();
            getAllParkings();
        } else {
            Toast.makeText(this, "Connection internet insufisante", Toast.LENGTH_LONG).show();
        }
    }

    private void initHttpParkings(){
        AbstractCity.OnParksFoundListener parksFoundListener = new AbstractCity.OnParksFoundListener() {
            @Override
            public void onParksFound(ArrayList<Park> parkArrayList, TypeVille ville) {
                if (ville == TypeVille.RENNES)
                    isRennesReady = true;
                else if (ville == TypeVille.NANTES)
                    isNantesReady = true;
                else if (ville == TypeVille.PARIS)
                    isParisReady = true;
                else if (ville == TypeVille.BORDEAUX)
                    isBordeauxReady = true;
                else if (ville == TypeVille.STRASBOURG)
                    isStrasbourgReady = true;

                if (settings.getBoolean(GOOGLE_PARKING, false) && isRennesReady &&
                        isNantesReady && isParisReady && isBordeauxReady && isStrasbourgReady)
                    getGoogleParkings();

                setParkingMarkers(parkArrayList, BitmapDescriptorFactory.HUE_CYAN);
            }
        };

        AbstractCity.OnHttpErrorListener httpErrorListener = new AbstractCity.OnHttpErrorListener() {
            @Override
            public void onHttpError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        };
        mHttpR.setParksFoundListener(parksFoundListener);
        mHttpR.setHttpErrorListener(httpErrorListener);

        mHttpN.setParksFoundListener(parksFoundListener);
        mHttpN.setHttpErrorListener(httpErrorListener);

        mHttpP.setParksFoundListener(parksFoundListener);
        mHttpP.setHttpErrorListener(httpErrorListener);

        mHttpB.setParksFoundListener(parksFoundListener);
        mHttpB.setHttpErrorListener(httpErrorListener);

        mHttpS.setParksFoundListener(parksFoundListener);
        mHttpS.setHttpErrorListener(httpErrorListener);
    }

    private void getAllParkings() {
        mHttpR.getParks();
        mHttpN.getParks();
        mHttpP.getParks();
        mHttpB.getParks();
        mHttpS.getParks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mTownLocation[settings.getInt(VILLE_ID, 0)])      // Sets the center of the map
                .zoom(mTownZoom[settings.getInt(VILLE_ID, 0)])                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera
                .tilt(0)                   // Sets the tilt of the camera
                .build();                   // Creates a CameraPosition from the builder
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void moveCameraToLocation(LatLng location, float zoom){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location)      // Sets the center of the map
                .zoom(zoom)           // Sets the zoom
                .bearing(0)            // Sets the orientation of the camera
                .tilt(0)               // Sets the tilt of the camera
                .build();              // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setParkingMarkers(ArrayList<Park> parkArrayList,float bdf){
        for (Park park : parkArrayList){
            if (park.getparkInformation() != null && park.getFeature() != null && park.getFeature().getLocation() != null) {
                MarkerOptions ops = new MarkerOptions().position(park.getFeature().getLocation()).title(park.getparkInformation().getName());
                ops.snippet(mStatusName[park.getparkInformation().getStatus()] + " " + (park.getparkInformation().getFree() == -1 ? "?" : park.getparkInformation().getFree()) + "/" + (park.getparkInformation().getMax() == -1 ? "?" : park.getparkInformation().getMax()));
                ops.icon(BitmapDescriptorFactory.defaultMarker(bdf));
                mMap.addMarker(ops);
            }
        }
    }

    private void setParkingMarkers(ArrayList<GooglePark> googleParkArrayList){
        for (GooglePark park : googleParkArrayList){
            if (park.getLocation() != null) {
                MarkerOptions ops = new MarkerOptions().position(park.getLocation()).title(park.getName());
                ops.snippet(mStatusName[park.isOpen() ? 0 : 3]);
                ops.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                googleMarkerList.add(mMap.addMarker(ops));
            }
        }
    }

    public void setParkArrayList(ArrayList<Park> parkArrayList){
        mParkArrayList = parkArrayList;
    }

    /**
     * callback method from SelectTownDialogFragment, returning the value of user
     * input.
     *
     * @param selectedIdx
     */
    public void onUserSelectValue(int selectedIdx, boolean hasGoogle) {
        this.mCityName.setText(mTown[selectedIdx]);
        this.moveCameraToLocation(mTownLocation[selectedIdx], mTownZoom[selectedIdx]);
        settings.edit().putInt(VILLE_ID, selectedIdx).commit();
        settings.edit().putBoolean(GOOGLE_PARKING, hasGoogle).commit();
        if (hasGoogle && isConnectedToInternet()) {
            if (isRennesReady && isNantesReady && isParisReady
                    && isBordeauxReady && isStrasbourgReady)
                getGoogleParkings();
        }else {
            for (Marker m : googleMarkerList)
                m.remove();
            googleMarkerList.clear();
        }
    }

    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
    }

    private void getGoogleParkings(){
        /*Google.OnGoogleParksFoundListener onGoogleParksFoundListener = new Google.OnGoogleParksFoundListener() {
            @Override
            public void onGoogleParksFound(ArrayList<GooglePark> googleParkArrayList) {
                for (GooglePark gp : googleParkArrayList)
                    for (Park p : mHttpR.getParkList()) {
                        if (gp.getName().toLowerCase().contains(p.getparkInformation().getName().toLowerCase())) {
                            googleParkArrayList.remove(gp);
                            break;
                        }
                    }
                for (GooglePark gp : googleParkArrayList)
                    for (Park p : mHttpN.getParkList()) {
                        if (gp.getName().toLowerCase().contains(p.getparkInformation().getName().toLowerCase())) {
                            googleParkArrayList.remove(gp);
                            break;
                        }
                    }
                for (GooglePark gp : googleParkArrayList)
                    for (Park p : mHttpP.getParkList()) {
                        if (gp.getName().toLowerCase().contains(p.getparkInformation().getName().toLowerCase())) {
                            googleParkArrayList.remove(gp);
                            break;
                        }
                    }
                for (GooglePark gp : googleParkArrayList)
                    for (Park p : mHttpB.getParkList()) {
                        if (gp.getName().toLowerCase().contains(p.getparkInformation().getName().toLowerCase())) {
                            googleParkArrayList.remove(gp);
                            break;
                        }
                    }
                for (GooglePark gp : googleParkArrayList)
                    for (Park p : mHttpS.getParkList()) {
                        if (gp.getName().toLowerCase().contains(p.getparkInformation().getName().toLowerCase())) {
                            googleParkArrayList.remove(gp);
                            break;
                        }
                    }
                setParkingMarkers(googleParkArrayList);
            }
        };*/
        Google.OnHttpErrorListener onHttpErrorListener = new AbstractCity.OnHttpErrorListener() {
            @Override
            public void onHttpError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        };
        final AbstractCity[] cities = { mHttpR, mHttpN, mHttpP, mHttpB, mHttpS };

        for (int i = 0; i < mTownLocation.length; i++) {
            final int val = i;
            Google httpG = new Google(this, mTownLocation[i]);
            httpG.setGoogleParksFoundListener(new Google.OnGoogleParksFoundListener() {
                @Override
                public void onGoogleParksFound(ArrayList<GooglePark> googleParkArrayList) {

                    Iterator<GooglePark> i = googleParkArrayList.iterator();
                    while (i.hasNext()) {
                        GooglePark gp = i.next();
                        for (Park p : (cities[val]).getParkList()) {
                            if (gp != null && p.getparkInformation() != null && gp.getName().toLowerCase().contains(p.getparkInformation().getName().toLowerCase())) {
                                i.remove();
                                break;
                            }
                        }
                    }
                    setParkingMarkers(googleParkArrayList);
                }
            });
            httpG.setHttpErrorListener(onHttpErrorListener);
            httpG.getParks();
        }
    }
}
