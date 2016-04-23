package edu.wmich.petpatrol;

import android.os.AsyncTask;

import org.json.JSONObject;

public class FetchItemsTask extends AsyncTask<String,Void,Void> {

    final String GET_PETS = "GET_PETS";
    final String GET_PETFINDER_PETS = "GET_PETFINDER_PETS";

    FinderFragment finderFragment;
    AdoptFragment adoptFragment;

    JSONObject pets = null;

    String action;
    String parameter;

    public FetchItemsTask(FinderFragment finderFragment){
        this.finderFragment = finderFragment;
    }
    public FetchItemsTask(AdoptFragment adoptFragment) { this.adoptFragment = adoptFragment; }


    @Override //do specific task in background. Called on .execute
    protected Void doInBackground(String... params) {
        action = params[0];
        if(params.length > 1) {
            parameter = params[1];
        }

        //do a different action depending on the parameter passed
        switch(action){
            case GET_PETS:
                pets = Ushahidi.getPets();
                break;
            case GET_PETFINDER_PETS:
                pets = PetFinder.getPets(parameter);
                break;
            default:
                break;
        }

        return null;
    }

    @Override
    //run after the background task is finished
    protected void onPostExecute(Void result){
        //do a different action depending on the parameter passed
        switch(action){
            case GET_PETS:
                finderFragment.updatePosts(pets);
                break;
            case GET_PETFINDER_PETS:
                adoptFragment.updatePosts(pets);
            default:
                break;
        }
    }

}