package com.example.android.parkingplaces;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;



public class MainActivity extends ActionBarActivity implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener {


    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;


    public Location mLastLocation;

    /**
     * Represents a geographical location.
     */

    protected TextView mLatitudeText;
    protected TextView mLongitudeText;


    static public GoogleMap m_map;
    boolean mapReady = false;


    MarkerOptions last;
    MarkerOptions home;
    MarkerOptions oldHome;
    MarkerOptions building101;
    MarkerOptions trueYoga;
    MarkerOptions taishinBank;
    MarkerOptions homeTom;

    MarkerOptions station;


    LatLng homeLL = new LatLng(24.938486, 121.503394);
    LatLng oldHomeLL = new LatLng(25.028104, 121.499944);
    LatLng building101LL = new LatLng(25.033720, 121.564811);
    LatLng trueYogaLL = new LatLng(25.041626, 121.564047);
    LatLng taishinBankLL = new LatLng(25.037573, 121.550077);
    LatLng homeTomLL = new LatLng(25.073208, 121.469505);



    static public double latitude;
    static public double longitude;
    //LatLng lastLL = new LatLng(latitude, longitude);
    LatLng lastLL;
    CameraPosition LAST;

    static final CameraPosition NEWYORK = CameraPosition.builder()
            .target(new LatLng(40.710670, -74.016663))
            .zoom(11)
            .bearing(0)
            .tilt(90)
            .build();

    static final CameraPosition SEATTLE = CameraPosition.builder()
            .target(new LatLng(47.6204, -122.2491))
            .zoom(10)
            .bearing(0)
            .tilt(45)
            .build();

    ClassicSingleton CS= new ClassicSingleton().getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }

        //mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        //mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        buildGoogleApiClient();



        home = new MarkerOptions()
                .position(new LatLng(24.938486, 121.503394))
                .title("Home")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        oldHome = new MarkerOptions()
                .position(new LatLng(25.028104, 121.499944))
                .title("Old Home")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        building101 = new MarkerOptions()
                .position(new LatLng(25.033720, 121.564811))
                .title("101")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        trueYoga = new MarkerOptions()
                .position(new LatLng(25.041626, 121.564047))
                .title("True Yoga")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        taishinBank = new MarkerOptions()
                .position(new LatLng(25.037573, 121.550077))
                .title("Taishin Bank")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        homeTom = new MarkerOptions()
                .position(new LatLng(25.073208, 121.469505))
                .title("Hometom")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if ((mLastLocation != null) &&
                (m_map != null)) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));


            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            CameraPosition LAST = CameraPosition.builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(11)
                    .bearing(0)
                    .tilt(90)
                    .build();
            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(LAST));

            last = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Home")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

            m_map.addMarker(last);

            lastLL = new LatLng(latitude, longitude);

            Integer i;


            for (i = 0; i < CS.stations; i++) {
                Log.e(TAG, (i+1)+"<"+CS.Name().get(i)+"><"+CS.Address().get(i)+">("+CS.Latitude().get(i)+CS.Longitude().get(i)+")");
            }


//            m_map.addPolyline(new PolylineOptions().geodesic(true)
//                            .add(lastLL)
//                            .add(homeLL)
//                        .add(oldHomeLL)
//                        .add(homeTomLL)
//                            .add(taishinBankLL)
//                            .add(trueYogaLL)
//                            .add(homeLL)
//            );

        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onMapReady(GoogleMap map) {
        mapReady = true;
        m_map = map;

        Integer i;

        //ClassicSingleton CS= new ClassicSingleton();
        //CS.getInstance();

        Log.e(TAG, "CS.stations=" + CS.stations);

//        m_map.addMarker(last);
                m_map.addMarker(home);
        m_map.addMarker(oldHome);
        m_map.addMarker(building101);
        m_map.addMarker(trueYoga);
        m_map.addMarker(taishinBank);
        m_map.addMarker(homeTom);



        for (i = 0; i < CS.stations; i++) {
            Log.e(TAG, (i + 1) + "<" + CS.Name().get(i) + "><" + CS.Address().get(i) + ">(" + CS.Latitude().get(i) + CS.Longitude().get(i) + ")");

            station = new MarkerOptions()
                    .position(new LatLng(CS.Latitude().get(i), CS.Longitude().get(i)))
                    .title(CS.Name().get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

            m_map.addMarker(station);
        }



        map.moveCamera(CameraUpdateFactory.newCameraPosition(NEWYORK));

//        map.addPolyline(new PolylineOptions().geodesic(true)
//                        .add(lastLL)
//                .add(homeLL)
//                        .add(oldHomeLL)
//                        .add(homeTomLL)
//                .add(taishinBankLL)
//                .add(trueYogaLL)
//                .add(homeLL)
//        );

                //map.addPolygon(new PolygonOptions().add(homeLL, oldHomeLL, trueYogaLL, homeLL).fillColor(Color.GREEN));

                //map.addCircle(new CircleOptions()
                //.center(homeLL)
                //.radius(5000)
                //.strokeColor(Color.GREEN)
                //.fillColor(Color.argb(64,0,255,0)));

    }


}
