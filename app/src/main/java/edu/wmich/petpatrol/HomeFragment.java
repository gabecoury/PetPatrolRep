package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This fragment displays the home screen
* that allows the user to pick a function.
*************************************
*/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {

    private Button mButtonReport;
    private Button mButtonFind;
    private Button mButtonAdopt;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mButtonReport = (Button) v.findViewById(R.id.buttonReport);
        mButtonFind = (Button) v.findViewById(R.id.buttonFind);
        mButtonAdopt = (Button) v.findViewById(R.id.buttonAdopt);

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    /* Opens AddReport Fragment when The Report button is clicked */
                Fragment fragment = new AddReportFragment();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        mButtonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    /* Opens the Finder Fragment when The Find button is clicked */
                Fragment fragment = new FinderFragment();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        mButtonAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    /* Opens Adopt Fragment when The Adopt button is clicked */
                Fragment fragment = new AdoptFragment();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        return v;
    }

}
