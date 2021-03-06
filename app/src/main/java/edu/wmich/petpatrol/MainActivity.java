package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This Activity hosts every fragment
*************************************
*/

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends SingleFragmentActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    // Check permissions onResume in case app was shelved by pressing home button but not
    // killed in memory, after permissions were denied
    @Override
    protected void onResume()
    {
        int currentAPI = android.os.Build.VERSION.SDK_INT;
        if (currentAPI >= android.os.Build.VERSION_CODES.M){
            checkForPermissions();
        } else{
            // API version less than 23
            System.out.println("API less than 23");
        }
        super.onResume();
    }

    @Override
    protected Fragment createFragment(){

        //Ask for permissions, as done by the OSMDroid sample APP
        int currentAPI = android.os.Build.VERSION.SDK_INT;
        if (currentAPI >= android.os.Build.VERSION_CODES.M){
            checkForPermissions();
        } else{
            // API version less than 23
            System.out.println("API less than 23");
        }
        return new HomeFragment();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermissions(){
        List<String> permissions = new ArrayList<>();
        String message = "Pet Patrol requires these permissions:";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            message += "\nStorage access to store map tiles.";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            message += "\nLocation to show user location.";
        }
        if (!permissions.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            String[] params = permissions.toArray(new String[permissions.size()]);
            requestPermissions(params, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }
}
