package edu.wmich.petpatrol.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//this class is for maps with lost pets and reported pets pinned on them.
public class FinderFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find, container, false);

        //put the map in the page
        MapView map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //set up options
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

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

}
