package edu.wmich.petpatrol.myapplication;

import android.os.AsyncTask;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

        DownloadWebpageTask task = new DownloadWebpageTask();
        task.execute("http://testpetpatrol.herokuapp.com");

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
            Log.d("Security Exception", "GPS access not enabled");
            Toast.makeText(getParentFragment().getContext(), "Please allow access to Location.", Toast.LENGTH_LONG).show();
            latit = 0;
            longi = 0;
        }
        //init mapcontroller and give an initial point
        IMapController mapController = map.getController();
        mapController.setZoom(13);
        GeoPoint startPoint = new GeoPoint(42.6549, -86.2032);
        mapController.setCenter(startPoint);

        //Place down the marker riiiight in the center and make it say cool things
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Saugatuck");
        startMarker.setSnippet("Coordinates: ");
        GeoPoint location = startMarker.getPosition();
        startMarker.setSubDescription("Lat: " + location.getLatitude() + ", Long: " + location.getLongitude());

        map.getOverlays().add(startMarker);

        return v;
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    URL url = new URL("http://testpetpatrol.herokuapp.com/views/map");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        InputStream in = connection.getInputStream();
                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            throw new IOException(connection.getResponseMessage());
                        }
                        int bytesRead;
                        byte[] buffer = new byte[1024];
                        while ((bytesRead = in.read(buffer)) > 0) {
                            out.write(buffer, 0, bytesRead);
                        }
                        out.close();
                        Log.d("url", out.toString());
                        return out.toString();
                    } finally {
                        connection.disconnect();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                //return downloadUrl(urls[0]);
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

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
