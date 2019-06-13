package in.ac.iitd.cargeofenceandtracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //Initialize fragments
    public FragmentCars fragmentCars = new FragmentCars();
    public FragmentGeofences fragmentGeofences = new FragmentGeofences();
    public FragmentStats fragmentStats = new FragmentStats();
    public FragmentCommCar fragmentCommCar = new FragmentCommCar();
    public FragmentCommCloud fragmentCommCloud = new FragmentCommCloud();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the tool bar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing the floating action bar and setting a click listener for it
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), AddGeofenceActivity.class);
                startActivity(intent);

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        //Initializing the navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        drawerView.bringToFront();
                        drawerView.requestLayout();
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                }
        );


        FragmentTransaction fragmentCarsTransaction = getSupportFragmentManager().beginTransaction();
        fragmentCarsTransaction.replace(R.id.list_fragment_container, fragmentCars);
        fragmentCarsTransaction.commit();

        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("@string/TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.i("@string/TAG"+"FCM Token = ", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END retrieve_current_token]

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Log.i("Drawer","Home clicked");
            FragmentTransaction fragmentCarsTransaction = getSupportFragmentManager().beginTransaction();
            fragmentCarsTransaction.replace(R.id.list_fragment_container, fragmentCars);
            fragmentCarsTransaction.commit();


        } else if (id == R.id.nav_geofences) {
            Log.i("Drawer","Geofences clicked");
            FragmentTransaction fragmentCarsTransaction = getSupportFragmentManager().beginTransaction();
            fragmentCarsTransaction.replace(R.id.list_fragment_container, fragmentGeofences);
            fragmentCarsTransaction.commit();

            //Intent intent = new Intent(this, ImageTextListBaseAdapterActivity.class);
            //startActivity(intent);

        } else if (id == R.id.nav_statistics) {
            Log.i("Drawer","Statistics clicked");
            FragmentTransaction fragmentCarsTransaction = getSupportFragmentManager().beginTransaction();
            fragmentCarsTransaction.replace(R.id.list_fragment_container, fragmentStats);
            fragmentCarsTransaction.commit();

        } else if (id == R.id.nav_cloud) {
            Log.i("Drawer","Cloud clicked");
            FragmentTransaction fragmentCarsTransaction = getSupportFragmentManager().beginTransaction();
            fragmentCarsTransaction.replace(R.id.list_fragment_container, fragmentCommCloud);
            fragmentCarsTransaction.commit();

        } else if (id == R.id.nav_car) {
            Log.i("Drawer","Car clicked");
            FragmentTransaction fragmentCarsTransaction = getSupportFragmentManager().beginTransaction();
            fragmentCarsTransaction.replace(R.id.list_fragment_container, fragmentCommCar);
            fragmentCarsTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
