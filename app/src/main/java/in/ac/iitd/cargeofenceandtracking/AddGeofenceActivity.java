package in.ac.iitd.cargeofenceandtracking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AddGeofenceActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private int MY_LOCATION_REQUEST_CODE = 1;
    private ArrayList<LatLng> polygonVertices = new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_geofence);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Polyline geofenceLineSegment =  mMap.addPolyline(new PolylineOptions());

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                PolygonOptions polygonOptions = new PolygonOptions();
                int index = 0;

                android.util.Log.i("onMapClick", "Latitude: " + String.valueOf(arg0.latitude) + " Longitude: " + String.valueOf(arg0.longitude));
                if(polygonVertices.size()<3) {
                    polygonVertices.add(arg0);
                    android.util.Log.i("geofence", "Polygon has now " + String.valueOf(polygonVertices.size() + " points."));
                    Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
                    buttonMinus.setEnabled(true);
                } else {
                    //check if adding this point results in a complex polygon
                    //if the resultant polygon is complex, reject the point with a toast
                    //informing the user about the violation
                    //if the resultant polygon is not complex, add the latlng to the list
                    //and draw the resultant polygon
                    //the algorithm is taken from https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
                    polygonVertices.add(arg0);
                    int mainIndex = 0;
                    int comparisonIndex = 0;

                    for(mainIndex = 0; mainIndex <= (polygonVertices.size()-3); mainIndex++) {
                        for(comparisonIndex = (mainIndex+2); comparisonIndex <= (polygonVertices.size()-1); comparisonIndex++) {
                            int nextPoint;
                            nextPoint = comparisonIndex + 1;
                            if(comparisonIndex == polygonVertices.size()-1)
                                nextPoint = 0;
                            if(nextPoint!=mainIndex) {
                                if (doIntersect(polygonVertices.get(mainIndex), polygonVertices.get(mainIndex + 1), polygonVertices.get(comparisonIndex), polygonVertices.get(nextPoint))) {
                                    android.util.Log.i("geofence", "Complex Polygon");
                                    polygonVertices.remove(polygonVertices.size()-1);
                                } else {
                                    android.util.Log.i("geofence", "Simple Polygon");

                                }
                            }
                        }
                    }
                }

                switch (polygonVertices.size())
                {
                    case 0:
                        mMap.clear();
                        break;

                    case 1:
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .position(polygonVertices.get(0))
                        );
                        break;

                    case 2:
                        mMap.clear();
                        do {
                            polygonOptions.add(polygonVertices.get(index));
                            index++;
                        }while(index<polygonVertices.size());

                        polygonOptions.strokeColor(Color.RED);
                        mMap.addPolygon(polygonOptions);
                        break;

                    default:
                        mMap.clear();
                        do {
                            polygonOptions.add(polygonVertices.get(index));
                            index++;
                        }while(index<polygonVertices.size());

                        polygonOptions.strokeColor(Color.RED);
                        mMap.addPolygon(polygonOptions);

                        break;

                }


            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);

            /*mMap.addPolygon(new PolygonOptions()
                    .add(new LatLng(28.544719,77.201159), new LatLng(28.551539,77.182343), new LatLng(28.536360,77.197431), new LatLng(28.544604,77.178260))
                    .strokeColor(Color.RED)
            );*/

        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(this);
                mMap.setOnMyLocationClickListener(this);

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);

                /*mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(28.544719,77.201159), new LatLng(28.551539,77.182343), new LatLng(28.536360,77.197431), new LatLng(28.544604,77.178260))
                        .strokeColor(Color.RED)
                );*/

            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        //map.animateCamera(cameraUpdate);
        //locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    public void removeLastPoint(View view) {
        PolygonOptions polygonOptions = new PolygonOptions();
        int index = 0;

        if(polygonVertices.size()>0) {
            polygonVertices.remove(polygonVertices.size()-1);
            if (polygonVertices.size()==0) {
                Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
                buttonMinus.setEnabled(false);
            }
        }

        switch (polygonVertices.size())
        {
            case 0:
                mMap.clear();
                break;

            case 1:
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(polygonVertices.get(0))
                );
                break;

            case 2:
                mMap.clear();
                do {
                    polygonOptions.add(polygonVertices.get(index));
                    index++;
                }while(index<polygonVertices.size());

                polygonOptions.strokeColor(Color.RED);
                mMap.addPolygon(polygonOptions);
                break;

            default:
                mMap.clear();
                do {
                    polygonOptions.add(polygonVertices.get(index));
                    index++;
                }while(index<polygonVertices.size());

                polygonOptions.strokeColor(Color.RED);
                mMap.addPolygon(polygonOptions);

                break;
        }

        android.util.Log.i("geofence", "Polygon has now " + String.valueOf(polygonVertices.size() + " points."));
    }

    //Given three colinear points p, q, r, the function checks if point q lies on line segment 'pr'
    /*bool onSegment(Point p, Point q, Point r)
    {
        if (q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) &&
                q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y))
            return true;

        return false;
    }*/
    public boolean onSegment(LatLng p, LatLng q, LatLng r) {
        if ((q.longitude <= Math.max(p.longitude, r.longitude)) && (q.longitude >= Math.min(p.longitude, r.longitude))
                && (q.latitude <= Math.max(p.latitude, r.latitude)) && (q.latitude >= Math.min(p.latitude, r.latitude)))
            return true;

        return false;
    }


    //To find orientation of ordered triplet (p, q, r).
    //The function returns following values
    // 0 --> p, q and r are collinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    /*int orientation(Point p, Point q, Point r)
    {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        int val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;  // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }*/
    int orientation(LatLng p, LatLng q, LatLng r) {
        double val = ((q.latitude - p.latitude) * (r.longitude - q.longitude)) - ((q.longitude - p.longitude) * (r.latitude - q.latitude));

        if (val == 0) return 0;

        if (val > 0)
            return 1;
        else
            return 2;
    }


    // The main function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    boolean doIntersect(LatLng p1, LatLng q1, LatLng p2, LatLng q2)
    {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

}
