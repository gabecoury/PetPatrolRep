package edu.wmich.petpatrol;

import android.os.AsyncTask;

import org.json.JSONObject;

public class FetchItemsTask extends AsyncTask<String,Void,Void> {

    final String GET_PETS = "GET_PETS";

    FinderFragment finderFragment;

    JSONObject pets = null;

    String action;
    String parameter;

    public FetchItemsTask(FinderFragment finderFragment){
        this.finderFragment = finderFragment;
    }


    @Override //do specific task in background. Called on .execute
    protected Void doInBackground(String... params) {
        action = params[0];
        //parameter = params[1];

        //do a different action depending on the parameter passed
        switch(action){
            case GET_PETS:
                pets = Ushahidi.getPets();
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
            default:
                break;
        }
    }

}