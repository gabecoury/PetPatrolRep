package edu.wmich.petpatrol.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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