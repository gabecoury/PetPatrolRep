package edu.wmich.petpatrol;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class SelectLocationActivity extends AppCompatActivity implements LocationListener {

    MapView map;
    double latit;
    double longi;
    boolean locationNullFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        //put the map in the page
        map = (MapView) findViewById(R.id.chooselocationmap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        try {

            // get the user's current location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            Criteria cri = new Criteria();
            cri.setAccuracy(Criteria.ACCURACY_COARSE);

            String bestprov = locationManager.getBestProvider(cri, true);
            Location loc = locationManager.getLastKnownLocation(bestprov);

            if (loc != null) {
                latit = loc.getLatitude();
                longi = loc.getLongitude();
            } else {
                latit = 0;
                longi = 0;
                locationNullFlag = true;
            }

            //set the zoom level on the map
            IMapController mapController = map.getController();
            mapController.setZoom(13);

            //set the point that the map focuses
            GeoPoint startPoint = new GeoPoint(latit, longi);
            mapController.setCenter(startPoint);

        } catch (SecurityException se) {
            Log.d("Security Exception", "GPS access not enabled");
            Toast.makeText(SelectLocationActivity.this, "Please allow access to Location.", Toast.LENGTH_LONG).show();
            latit = 0;
            longi = 0;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latit = (location.getLatitude());
        longi = (location.getLongitude());

        if (locationNullFlag) {
            latit = (location.getLatitude());
            longi = (location.getLongitude());
            Log.d("Current Location", "Lat: " + latit + ", Long: " + longi);

            IMapController mapController = map.getController();
            mapController.setZoom(13);

            GeoPoint startPoint = new GeoPoint(latit, longi);
            Marker startMarker = new Marker(map);

            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapController.setCenter(startPoint);

            startMarker.setTitle("Current Location");
            startMarker.setSnippet("Coordinates: ");
            startMarker.setSubDescription("Lat: " + startPoint.getLatitude() + ", Long: " + startPoint.getLongitude());

            map.getOverlays().add(startMarker);

            locationNullFlag = false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
