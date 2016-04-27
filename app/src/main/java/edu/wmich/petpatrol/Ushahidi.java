package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This controls the Ushahidi API.
* Asks the server for an
* authorization key for every call to
* the API. Builds the necessary JSON
* for the calls.
*************************************
*/

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Ushahidi {

    private static final String TAG = "UshahidiAPI";
    private static final String CLIENT_ID = "ushahidiui";
    private static final String CLIENT_SECRET = "35e7f0bca957836d05ca0492211b0ac707671261";
    private static final String CONTENT_TYPE = "application/json";
    private static final String backend_url = "https://petpatrolback.herokuapp.com";

    public static JSONObject getPets(){
        try {
            //build the api string
            String urlString = Uri.parse(backend_url + "/api/v3/posts?limit=10&offset=0&order=desc&orderby=created&status=all")
                    .buildUpon()
                    .build().toString();
            Log.d("urlString", urlString);
            //get a new connection from the server
            HttpURLConnection connection = buildConnection(urlString, getAccessToken());

            //receive the data
            String jsonString = new String(getBytes(connection));

            Log.i(TAG, "Received JSON: " + jsonString);

            //turn into an object
            JSONObject json = new JSONObject(jsonString);

            Log.d(TAG, json.toString());

            return json;
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch pet posts", e);
        }

        return null;
    }

    //post a pet to the server
    //At the moment, this will return with 204 NO CONTENT
    //This is fine, this is an issue with Ushahidi not acepting public posts. This should
    //work with minimal effort if that becomes fixed.
    public static boolean submitPet(Pet pet){
        try {
            //build the api string
            String urlString = Uri.parse(backend_url + "/api/v3/posts")
                    .buildUpon()
                    .build().toString();

            //get a new connection from the server
            HttpURLConnection connection = buildConnection(urlString, getAccessToken());

            //set the properties of the rest api call
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            String petTitle = "Lost Pet";
            if(pet.isFound()){
                petTitle = "Found Pet";
            }

            String petContent = "";

            if(pet.getPetName().length() > 0){
                petContent += "Name: " + pet.getPetName() + ". ";
            }

            petContent += "Type: " + pet.getPetType() + ". " +
                    "Description: " +
                    pet.getPetDescription() + ". ";

            if(pet.getDetails().length() > 0){
                petContent += "Details: " + pet.getDetails() + ". ";
            }

            if(pet.getContactNumber() != 0){
                petContent += " Contact at " + pet.getContactNumber();
            }

            Log.d("PetContent", petContent);

            // TODO Fix location here
            OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
            wr.write(

                    "{" +
                        "\"title\":\"" + petTitle + "\"," +
                        "\"content\":\"" + petContent + "\"," +
                        "\"locale\":\"en_US\"," +
                        "\"status\":\"draft\"," +
                        "\"values\":{" +
                            "\"location_default\":[" +
                                "{" +
                                    "\"lat\":" + pet.getLatit() + "," + // This will need to updated when pets can store location info
                                    "\"lon\":" + pet.getLongi() +
                                "}" +
                            "]" +
                        "}," +
                        "\"completed_stages\":[]," +
                        "\"allowed_privileges\":[" +
                            "\"read\",\"create\",\"search\"" +
                        "]," +
                        "\"form\":{" +
                            "\"id\":2" +
                        "}" +
                    "}"


            );
            wr.close();

            //get the result
            String resultString = new String(getBytes(connection));

            Log.d("PostPet", resultString);

        } catch (Exception e) {
            Log.e(TAG, "Failed to post pet", e);
            return false;
        }


        return true;
    }

    //post an event to the server
    //At the moment, this will return with 204 NO CONTENT
    //This is fine, this is an issue with Ushahidi not acepting public posts. This should
    //work with minimal effort if that becomes fixed.
    public static boolean submitEvent(Event event){
        try {
            //build the api string
            String urlString = Uri.parse(backend_url + "/api/v3/posts")
                    .buildUpon()
                    .build().toString();

            //get a new connection from the server
            HttpURLConnection connection = buildConnection(urlString, getAccessToken());

            //set the properties of the rest api call
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            String eventContent = "Starts on " + event.getEventStartDateTime().getTime().toString() +
                    " and ends on " + event.getEventEndDateTime().getTime().toString() + ". ";


            if(event.getDetails().length() > 0){
                eventContent += event.getDetails();
            }

            if(event.getContactNumber() != 0){
               eventContent += " \nContact at " + event.getContactNumber() + ".";
            }


            Log.d("EventContent", eventContent);

            // TODO fix location here too
            OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
            wr.write(
                "{" +
                    "\"title\":\"" + event.getEventName() + "\"," +
                    "\"content\":\"" + " " + "\"," +
                    "\"locale\":\"en_US\"," +
                    "\"status\":\"draft\"," +
                    "\"values\":{" +
                        "\"location_default\":[" +
                            "{" +
                                "\"lat\":" + event.getLatit() + "," + // This will need to updated when pets can store location info
                                "\"lon\":" + event.getLongi() +
                            "}" +
                        "]" +
                    "}," +
                    "\"completed_stages\":[]," +
                    "\"allowed_privileges\":[" +
                        "\"read\",\"create\",\"search\"" +
                    "]," +
                    "\"form\":{" +
                        "\"id\":2" +
                    "}" +
                "}"


            );
            wr.close();

            //get the result
            String resultString = new String(getBytes(connection));

            Log.d("PostPet", resultString);

        } catch (Exception e) {
            Log.e(TAG, "Failed to post pet", e);
            return false;
        }


        return true;
    }

    //Every call of the rest API requires an access token. This gets that token.
    private static String getAccessToken(){
        try {
            String urlString = Uri.parse(backend_url + "/oauth/token")
                    .buildUpon()
                    .build().toString();

            HttpURLConnection connection = buildConnection(urlString);

            //set the properties of the rest api call
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            //connection.setRequestProperty("Authorization", "Basic " + create_authorization());
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);

            OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
            wr.write("{" +
                    "\"username\": \"troyerms@gmail.com\"," +
                    "\"password\": \"youshallnotpass\"," +
                    "\"grant_type\": \"password\"," +
                    "\"client_id\": \"" + CLIENT_ID + "\"," +
                    "\"client_secret\": \"" + CLIENT_SECRET + "\"," +
                    "\"scope\": \"posts\"}");
            wr.close();

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

    //build a new connection with a url, but using the specified api access token
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
