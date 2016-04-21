package edu.wmich.petpatrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask tskSplashScreen = new TimerTask() {       // Creates a Timer Task that closes this Splash Activity and launches the Main Activity
            @Override
            public void run() {
                finish();       // Closes the current Activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));     // Opens the Main Activity

            }
        };
        Timer tmrSplashScreen = new Timer();        // Creates a Timer for the Splash Screen
        tmrSplashScreen.schedule(tskSplashScreen, 4000);         // Sets the Splash Screen Timer to 4 seconds and
        // then launch the Splash Screen Timer Task once time is up
    }
}
