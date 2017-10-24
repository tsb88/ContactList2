package com.example.tejasbhoir.contactlist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements contactListView.OnFragmentInteractionListener, AddFragment.OnFragmentInteractionListener, contactDetails.OnFragmentInteractionListener {

    public static boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDualPane = findViewById(R.id.dualView) != null;

        if (mDualPane) {
            FragmentManager fmL = getFragmentManager();
            FragmentTransaction ftL = fmL.beginTransaction();
            contactListView viewScreen = new contactListView();
            contactDetails detailsScreen = new contactDetails();

            ftL.replace(R.id.ListView, viewScreen);
            ftL.replace(R.id.addFragment, detailsScreen);
            ftL.commit();
        }
        else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            contactListView viewScreen = new contactListView();
            ft.add(R.id.ListView, viewScreen);
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
