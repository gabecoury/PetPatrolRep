package edu.wmich.petpatrol;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class PetFinder {

    private static final String TAG = "PetFinderAPI";
    private static final String KEY = "39bb0fd3b14b821c0a97b8c5f7408fa3";
    private static final String CONTENT_TYPE = "application/json";
    private static final String backend_url = "http://api.petfinder.com";

    public static JSONObject getPets(String postal){
        try {

            //build the api string
            String urlString = Uri.parse(backend_url + "/pet.find")
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("key", KEY)
                    .appendQueryParameter("location", postal)
                    .build().toString();

            //get a new connection from the server
            HttpURLConnection connection = buildConnection(urlString);

            //receive the data
            String jsonString = new String(getBytes(connection));

            Log.i(TAG, "Received JSON: " + jsonString);

            //turn into an object
            JSONObject json = new JSONObject(jsonString);

            Log.d(TAG, json.toString());

            return json;
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch pets from petfinder", e);
        }

        return null;
    }

    /*
    //Every call of the rest API requires an access token. This gets that token.
    private static String getAccessToken(){
        try {
            String urlString = Uri.parse(backend_url + "/auth.getToken")
                    .buildUpon()
                    .appendQueryParameter("key", KEY)
                    .build().toString();

            HttpURLConnection connection = buildConnection(urlString);

            //set the properties of the rest api call
            //connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            //connection.setRequestProperty("Authorization", "Basic " + create_authorization());
            //connection.setRequestProperty("Content-Type", CONTENT_TYPE);

            //get the api access token
            String jsonString = new String(getBytes(connection));

            Log.i(TAG, "Received JSON: " + jsonString);

            JSONObject json = new JSONObject(jsonString);

            return json.getString("access_token");
        } catch (Exception e){
            Log.e(TAG, "Unable to fetch Access Token!");
            e.printStackTrace();
        }

        return null;
    }
    */

    //build a new connection using the specified url
    private static HttpURLConnection buildConnection(String urlString){
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            return connection;
        } catch (Exception e){
            Log.e(TAG, "Unable to build a connection!");
        }

        return null;
    }

    //build a new connection with a url, but using the specified api acess token
    private static HttpURLConnection buildConnection(String urlString, String access_token){
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Authorization", "Bearer " + access_token);

            return connection;
        } catch (Exception e){
            Log.e(TAG, "Unable to build a connection!");
        }

        return null;
    }

    //get the data
    private static byte[] getBytes(HttpURLConnection connection) throws IOException {

        try {
            connection.connect();

            InputStream in;

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            //attempt to get the stream
            try {
                in = connection.getInputStream();
            } catch (Exception e){
                in = connection.getErrorStream();
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    //while there's data, write to the output
                    out.write(buffer, 0, bytesRead);
                }

                out.close();
                Log.e(TAG, out.toString());
                throw new IOException(connection.getResponseMessage());
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                //while there's data, write to the ouput
                out.write(buffer, 0, bytesRead);
            }

            out.close();

            //return the output as bytes
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

}
