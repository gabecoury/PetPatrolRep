package edu.wmich.petpatrol;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//fragment to access the petfinder api.. with recyclerview for the information
public class AdoptFragment extends Fragment implements LocationListener {

    private RecyclerView locationRecyclerView;
    private PetAdapter adapter;
    private List petList;
    private SQLiteDatabase database;
    private String zipCode;

    double latit;
    double longi;
    boolean locationNullFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        petList = new ArrayList();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_adopt, container, false);

        //create a recycler view for the list of pets and return it
        locationRecyclerView = (RecyclerView) v
                .findViewById(R.id.adopt_recycler_view);
        //required or else will crash
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        } catch (SecurityException se) {
            Log.d("Security Exception", "GPS access not enabled");
            Toast.makeText(getParentFragment().getContext(), "Please allow access to Location.", Toast.LENGTH_LONG).show();
            latit = 0;
            longi = 0;
        }

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String postalCode = "49008";
        try {
            Log.d("location", "lat " + latit + " long " + longi);
            if (geocoder.isPresent()){
                List<Address> addresses = geocoder.getFromLocation(latit, longi, 1);

                Log.d("geocoder address", addresses.toString());

                postalCode = addresses.get(0).getPostalCode();
            }
        } catch (Exception e){
            Log.e("Address", "Error finding current address");
            e.printStackTrace();
        }

        FetchItemsTask petfinder = new FetchItemsTask(this);
        petfinder.execute(petfinder.GET_PETFINDER_PETS, postalCode);

        return v;
    }

    //create the menu and add items to the bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_petfinder, menu);
    }

    @Override //when the user touches the menu
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_alternate_zip:
                Log.d("zip", "zip");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Zip Code");

                // Set up the input
                final EditText input = new EditText(getActivity());

                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        zipCode = input.getText().toString();
                        runFromDialog();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.create();

                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void runFromDialog(){
        FetchItemsTask petfinder = new FetchItemsTask(this);
        petfinder.execute(petfinder.GET_PETFINDER_PETS, zipCode);
    }

    public void updatePosts(JSONObject pets){

        petList = new ArrayList();

        try {

            JSONArray petListJSON = pets.getJSONObject("petfinder").getJSONObject("pets").getJSONArray("pet");

            for(int i = 0; i < petListJSON.length(); i++){
                JSONObject pet = petListJSON.getJSONObject(i);

                //Log.d("PET", pet.getJSONObject("name").getString("$t"));
                Pet newPet = new Pet();
                newPet.setPetName(pet.getJSONObject("name").getString("$t"));
                newPet.setAdoptID(Integer.parseInt(pet.getJSONObject("id").getString("$t")));
                //newPet.setDetails(pet.getJSONObject("description").getString("$t"));
                String breedString;
                try {
                    JSONArray breed = pet.getJSONObject("breeds").getJSONArray("breed");
                    breedString = pet.getJSONObject("animal").getString("$t") + " - ";

                    for (int breedPlace = 0; breedPlace < breed.length(); breedPlace++) {
                        breedString += breed.getJSONObject(breedPlace).getString("$t");

                        if (breedPlace + 1 != breed.length()) {
                            breedString += ", ";
                        }
                    }
                } catch (JSONException e ){ //The api returns either an object or array. Lovely.
                    JSONObject breed = pet.getJSONObject("breeds").getJSONObject("breed");
                    breedString = pet.getJSONObject("animal").getString("$t") + " - " +
                            breed.getString("$t");

                }

                newPet.setPetType(breedString);

                petList.add(newPet);
            }


        } catch (Exception e){
            Log.e("AdoptFragment", "Unable to process array of pets!");
            e.printStackTrace();
        }

        if(petList.size() == 0){
            Pet noPet = new Pet();
            noPet.setPetName("No Pets Found.");
            noPet.setAdoptID(-1);

            petList.add(noPet);
        }

        locationRecyclerView.invalidate();

        //if(adapter == null) {
            adapter = new PetAdapter(petList);
            locationRecyclerView.setAdapter(adapter);
        //} else {
        //    adapter.notifyDataSetChanged();
        //}

    }

    //this viewholder will store references to the displayed variables
    private class PetHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Pet pet;

        private TextView textViewPetName;
        private TextView textViewPetBreed;

        public PetHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            textViewPetName = (TextView) itemView.findViewById(R.id.list_item_pet_name);
            textViewPetBreed = (TextView) itemView.findViewById(R.id.list_item_pet_breed);
        }


        public void bindPet(Pet pet){
            this.pet = pet;
            textViewPetName.setText(this.pet.getPetName());
            textViewPetBreed.setText(this.pet.getPetType());
        }

        @Override
        public void onClick(View v) {
            if(pet.getAdoptID() == -1){
                return;
            }

            String url = "https://m.petfinder.com/petdetail/" + pet.getAdoptID();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    //recyclerview will talk to this when a viewholder needs to be created or connected
    private class PetAdapter extends RecyclerView.Adapter<PetHolder>{
        private List<Pet> pets;

        public PetAdapter(List<Pet> pets){
            this.pets = pets;
        }

        @Override //inflate a new view to hold the pet
        public PetHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_pet, parent, false);
            return new PetHolder(view);
        }

        @Override //set the text on the tweet when it is bound
        public void onBindViewHolder(PetHolder holder, int position) {
            Pet pet = pets.get(position);
            holder.bindPet(pet);
        }

        @Override //get how many tweets there are
        public int getItemCount() {
            return pets.size();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latit = (location.getLatitude());
        longi = (location.getLongitude());
        if (locationNullFlag) {
            latit = (location.getLatitude());
            longi = (location.getLongitude());
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


