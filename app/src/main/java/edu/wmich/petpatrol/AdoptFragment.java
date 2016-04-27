package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This class uses the PetFinder api
* to display a list of pets that are
* available for adoption around the
* user. The user has the option to
* enter in an alternate zip code and
* view the options there. A
* RecyclerView is used to display the
* pets.
*************************************
*/

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdoptFragment extends Fragment implements LocationListener {

    private RecyclerView locationRecyclerView;
    private PetAdapter adapter;
    private List petList;
    private String zipCode;

    double latit;
    double longi;
    boolean locationNullFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize the list of pets
        petList = new ArrayList();

        //do you want a menu? that's how you get a menu
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
            //get the user's current location
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            //no need to be super specific
            Criteria cri = new Criteria();
            cri.setAccuracy(Criteria.ACCURACY_COARSE);

            String bestprov = locationManager.getBestProvider(cri, true);
            Location loc = locationManager.getLastKnownLocation(bestprov);

            if (loc != null) {
                latit = loc.getLatitude();
                longi = loc.getLongitude();
            } else {
                //could not find the location
                latit = 0;
                longi = 0;
                locationNullFlag = true;
            }

        } catch (SecurityException se) { //Please let us find where you are so we can show you pets
            Log.d("Security Exception", "GPS access not enabled");
            Toast.makeText(getParentFragment().getContext(), "Please allow access to Location.",
                    Toast.LENGTH_LONG).show();
            latit = 0;
            longi = 0;
        }

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String postalCode = "49008"; //default zip code in case location cannot be found

        try { //need to find the zip code using the given location
            Log.d("location", "lat " + latit + " long " + longi);
            if (geocoder.isPresent()){ //make sure we have geocoder ready
                List<Address> addresses = geocoder.getFromLocation(latit, longi, 1);

                Log.d("geocoder address", addresses.toString());

                postalCode = addresses.get(0).getPostalCode();
            }
        } catch (Exception e){
            Log.e("Address", "Error finding current address");
            e.printStackTrace();
        }

        //Do the background task to get the list of pets, using the postal code
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

                //create a dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Zip Code");

                // Set up the input
                final EditText input = new EditText(getActivity());

                //make it so it only accepts numbers
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        zipCode = input.getText().toString();
                        runFromDialog(); //get the zipcode and then execute the background task
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // get this dialog out of my face
                    }
                });

                //pop this up
                builder.create();
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //runs the background task to get the available pets
    public void runFromDialog(){
        FetchItemsTask petfinder = new FetchItemsTask(this);
        petfinder.execute(petfinder.GET_PETFINDER_PETS, zipCode);
    }

    //update the pets that are displayed
    public void updatePosts(JSONObject pets){

        //initialize the list of pets
        petList = new ArrayList();

        try {

            //get the actual pets from the return
            //while I understand this from a standpoint of making an api, this is very buried
            JSONArray petListJSON = pets.getJSONObject("petfinder").getJSONObject("pets").getJSONArray("pet");

            //put in each pet
            for(int i = 0; i < petListJSON.length(); i++){
                JSONObject pet = petListJSON.getJSONObject(i);

                //start building the pet object to use
                Pet newPet = new Pet();
                newPet.setPetName(pet.getJSONObject("name").getString("$t"));

                //adopt id used for the webpage
                newPet.setAdoptID(Integer.parseInt(pet.getJSONObject("id").getString("$t")));

                String breedString;
                try { //the pet can have multiple breeds, so this brings them all together

                    JSONArray breed = pet.getJSONObject("breeds").getJSONArray("breed");
                    breedString = pet.getJSONObject("animal").getString("$t") + " - ";

                    //combine them
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

                //add to the list
                petList.add(newPet);
            }


        } catch (Exception e){
            Log.e("AdoptFragment", "Unable to process array of pets!");
            e.printStackTrace();
        }

        //if there are no pets available, or the zip code entered is wrong
        if(petList.size() == 0){
            Pet noPet = new Pet(); //dummy pet display
            noPet.setPetName("No Pets Found.");
            noPet.setAdoptID(-1);

            petList.add(noPet);
        }

        locationRecyclerView.invalidate();

        //put in the new list of pets
        adapter = new PetAdapter(petList);
        locationRecyclerView.setAdapter(adapter);
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
        public void onClick(View v) { //when clicking on a pet, open the pet's adoption page
            if(pet.getAdoptID() == -1){ //for the dummy pet
                return;
            }

            //open the page
            String url = "http://m.petfinder.com/petdetail/" + pet.getAdoptID();

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

        @Override //set the text on the pet when it is bound
        public void onBindViewHolder(PetHolder holder, int position) {
            Pet pet = pets.get(position);
            holder.bindPet(pet);
        }

        @Override //get how many pets there are
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


