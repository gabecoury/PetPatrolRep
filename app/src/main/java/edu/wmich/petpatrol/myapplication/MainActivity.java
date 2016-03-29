package edu.wmich.petpatrol.myapplication;
//main activity that holds all the fragments.
import android.support.v4.app.Fragment;

//Moses was here 2016

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new HomeFragment();
    }
}
