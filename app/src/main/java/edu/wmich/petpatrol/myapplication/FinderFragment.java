package edu.wmich.petpatrol.myapplication;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//this class is for maps with lost pets and reported pets pinned on them.
public class FinderFragment extends Fragment implements LocationListener {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;

    double latit;
    double longi;
    boolean locationNullFlag = false;

    MapView map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find, container, false);

        //put the map in the page
        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set up options
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        try {
            // get location
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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


            // set marker
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

        } catch (SecurityException se) {
            latit = 0;
            longi = 0;
        }
        //init mapcontroller and give an initial point
        IMapController mapController = map.getController();
        mapController.setZoom(13);

        //Place down the marker riiiight in the center and make it say cool things


        return v;
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
