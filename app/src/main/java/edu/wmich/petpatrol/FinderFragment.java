package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This fragment controls the map on
* the app, and handles the
* interaction when the data is
* returned from the API.
*************************************
*/

import android.os.AsyncTask;
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
import android.widget.Toast;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//this class is for maps with lost pets and reported pets pinned on them.
public class FinderFragment extends Fragment implements LocationListener{

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
        task.execute("https://petpatrol.herokuapp.com/");

        //put the map in the page
        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set up options
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        try {

            // get the user's current location
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

            //get the pets seperately from the main thread
            FetchItemsTask ushahidi = new FetchItemsTask(this);
            ushahidi.execute(ushahidi.GET_PETS);

            //set the zoom level on the map
            IMapController mapController = map.getController();
            mapController.setZoom(13);

            //set the point that the map focuses
            GeoPoint startPoint = new GeoPoint(latit, longi);
            mapController.setCenter(startPoint);

        } catch (SecurityException se) {
            Log.d("Security Exception", "GPS access not enabled");
            Toast.makeText(getParentFragment().getContext(), "Please allow access to Location.", Toast.LENGTH_LONG).show();
            latit = 0;
            longi = 0;
        }

        return v;
    }

    public void updatePosts(JSONObject posts) {
        Events events = Events.get(getContext());

        //Sample data of the posts
        String[] names = {"Cute Dog", "Ugly Dog", "Short Dog", "The Fattest Cat I've Ever Seen",
                "Kitty"};
        String[] details = {"OMG CUTIE DOG", "The face that only the assumed owner could love",
                "who bred in these qualities", "it's like garfield up in here",
                ":3"};
        for(int i = 0; i < 5; i++){
            Event newEvent = new Event();
            newEvent.setEventName(names[i]);
            newEvent.setDetails(details[i]);
            newEvent.setContactNumber(5555550 + i);
            Location newLocation = new Location("");

            //variety
            String latitude = "42.2" + i + "369117";

            newLocation.setLatitude(Float.parseFloat(latitude));
            newLocation.setLongitude(-85.58898926);
            newEvent.setLocation(newLocation);

            events.addEvent(newEvent);
        }

        //initialize the list of events
        List<Event> listEvents = events.getEvents();

        //for each event, place down a marker
        for (Event event : listEvents){
            Location eventLocation = event.getLocation();

            Marker eventMarker = new Marker(map);

            //set the configuration for the marker
            eventMarker.setPosition(new GeoPoint(eventLocation.getLatitude(),
                    eventLocation.getLongitude()));
            eventMarker.setTitle(event.getEventName());
            eventMarker.setSnippet(event.getDetails());
            eventMarker.setSubDescription(event.getContactNumber() + "");

            //add it to the map
            map.getOverlays().add(eventMarker);

            Log.d("Location", "Adding Location with Lat: " + eventLocation.getLatitude()
                    + " and Long: " + eventLocation.getLongitude());
        }

    }

    //download the webpage in the background
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    URL url = new URL("http://petpatrol.herokuapp.com/views/map");
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

    @Override //run when the location changes
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
