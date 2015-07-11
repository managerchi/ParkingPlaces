package com.example.android.parkingplaces;

import android.app.ActionBar;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements
//public class MainActivity extends Fragment implements
        OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener {


    private static final String TAG = MainActivity.class.getSimpleName();

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

    static MarkerOptions station;


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

    //static ClassicSingleton CS = new ClassicSingleton().getInstance();
    //Stations stations = new Stations().getInstance();
    //Chains chains = new Chains().getInstance();

    ArrayList<Stations> chains = new ArrayList<Stations>();
    static final String CHAINS = "chains";

    public Integer numberOfChains = 0;
    static final String NumberOfChains = "numberOfChains";
    public Integer currentChain = -1;
    static final String CURRENT_CHAIN = "currentChain";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        supportInvalidateOptionsMenu();

        //setHasOptionsMenu(true);


        //if (savedInstanceState == null) {
        //    getSupportFragmentManager().beginTransaction()
        //           .add(R.id.container, new ForecastFragment())
        //            .commit();
        //}

        //mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        //mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        buildGoogleApiClient();


        if (savedInstanceState != null) {
            // Restore value of members from saved state
//            numberOfChains = savedInstanceState.getInt(NumberOfChains);
            Log.e("onCreate", "numberOfChains=" + numberOfChains);
        } else {
            // Probably initialize members with default values for a new instance
            chains.clear();
            //Stations stations = new Stations();


            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("https://dl.dropboxusercontent.com/u/46823822/24TPS.json", "24TPS");

            Toast.makeText(this, "Loading data...", Toast.LENGTH_LONG).show();

        }

        //chains.add(stations);
        //chains.get(numberOfChains).company = "24TPS";
        //weatherTask.execute("https://dl.dropboxusercontent.com/u/46823822/24TPS.json", "24TPS");
        //numberOfChains++;

        //FetchWeatherTask weatherTask = new FetchWeatherTask();
        //weatherTask.execute("https://dl.dropboxusercontent.com/u/46823822/DoDoHome.json", "DoDoHome");
        //chains.StationsAL().get(numberOfChains).company = "DoDoHome";
        //numberOfChains++;

        //FetchWeatherTask weatherTask = new FetchWeatherTask();
        //weatherTask.execute("https://dl.dropboxusercontent.com/u/46823822/TaiwanParking.json", "TaiwanParking");
        //chains.StationsAL().get(numberOfChains).company = "TaiwanParking";
        //numberOfChains++;

        Log.e("onCreate", "numberOfChains=" + numberOfChains);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(NumberOfChains, numberOfChains);
        savedInstanceState.putInt(CURRENT_CHAIN, currentChain);
        savedInstanceState.putParcelableArrayList(CHAINS, (ArrayList<? extends Parcelable>) chains);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        numberOfChains = savedInstanceState.getInt(NumberOfChains);
        currentChain = savedInstanceState.getInt(CURRENT_CHAIN);
        chains = savedInstanceState.getParcelableArrayList(CHAINS);

        Log.e("onRestoreInstanceState", "numberOfChains=" + numberOfChains);

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

        if (id == R.id.action_24TPS) {

            addMarkersToMap(0);
            //return true;
        } else if (id == R.id.action_DoDoHome) {

            addMarkersToMap(1);
            //return true;
        } else if (id == R.id.action_TaiwanParking) {

            addMarkersToMap(2);
            //return true;
        }

        return super.onOptionsItemSelected(item);
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

        Log.e("onConnected", "(" + latitude + "," + longitude + ")");
        if (currentChain >= 0) {
            return;
        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if ((mLastLocation != null) &&
                (m_map != null)) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));


            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            CameraPosition LAST = CameraPosition.builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(14)
                    .bearing(0)
                    .tilt(0)
                    .build();

            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(LAST));

            last = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

            m_map.addMarker(last);

            lastLL = new LatLng(latitude, longitude);

            Integer i;


            //for (i = 0; i < CS.stations; i++) {
            //    Log.e(TAG, (i+1)+"<"+CS.Name().get(i)+"><"+CS.Address().get(i)+">("+CS.Latitude().get(i)+","+CS.Longitude().get(i)+")");
            //}


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
    public void onMapReady(GoogleMap map) {
        mapReady = true;
        m_map = map;

        Integer i;

        if (currentChain >= 0) {

            addMarkersToMap(currentChain);
        }

        //m_map.addMarker(last);

        //Stations stations = new Stations().getInstance();

        //Log.e("onMapReady", "CS.stations=" + CS.stations);
        //Log.e("onMapReady", "stations.numberOfStations=" + stations.numberOfStations);


        //addMarkersToMap(0);

        //map.moveCamera(CameraUpdateFactory.newCameraPosition(NEWYORK));

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

    private boolean checkReady() {
        if (m_map == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addPersonalMarkersToMap() {

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

        m_map.addMarker(home);
        m_map.addMarker(oldHome);
        m_map.addMarker(building101);
        m_map.addMarker(trueYoga);
        m_map.addMarker(taishinBank);
        m_map.addMarker(homeTom);
    }


    public void addMarkersToMap(Integer chain) {
        Integer i;
        BitmapDescriptor m_icon;

        m_map.clear();

        if (numberOfChains == 0) {
            Log.e("addMarkersToMap", "no markers to add");
            return;
        }

        //Stations stations = new Stations().getInstance();
        Stations stations = chains.get(chain);
        //Station station = new Station();

        if (stations.company.equals("24TPS")) {
            m_icon = BitmapDescriptorFactory.fromResource(R.mipmap.tps24);
        } else if (stations.company.equals("DoDoHome")) {
            m_icon = BitmapDescriptorFactory.fromResource(R.mipmap.do_do_home);
        } else if (stations.company.equals("TaiwanParking")) {
            m_icon = BitmapDescriptorFactory.fromResource(R.mipmap.taiwan_parking);
        } else {
            m_icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        }

        Log.e("addMarkersToMap", "stations.numberOfStations=" + stations.numberOfStations);

        for (i = 0; i < stations.numberOfStations; i++) {
            Station station = stations.StationAL().get(i);

            //Log.e("addMarkersToMap", (i + 1) + "<" + CS.Name().get(i) + "><" + CS.Address().get(i) + ">(" + CS.Latitude().get(i) + "," + CS.Longitude().get(i) + ")");
            Log.e("addMarkersToMap", (i + 1) + "<" + station.name + "><" + station.address + ">(" + station.latidude + "," + station.longitude + ")");

            //station = new MarkerOptions()
            //        .position(new LatLng(CS.Latitude().get(i), CS.Longitude().get(i)))
            //        .title(CS.Name().get(i))
            //        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
            MarkerOptions stationMO = new MarkerOptions()
                    .position(new LatLng(station.latidude, station.longitude))
                    .title(station.name)
                    .snippet(station.address)
                    .icon(m_icon);
//            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

            m_map.addMarker(stationMO);
        }

        last = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

        m_map.addMarker(last);

        currentChain = chain;
    }


    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {


        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */
        private String getReadableDateString(long time) {
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }


        private String[] getWeatherDataFromJson(String companyJsonStr, String chainName)
                throws JSONException {

            String[] resultStrs = new String[1000];
            // These are the names of the JSON objects that need to be extracted.
            final String NAME = "name";
            final String ADDRESS = "address";
            final String LATITUDE = "latitude";
            final String LONGITUDE = "longitude";
            final String STATIONS = "stations";
            Integer i;

            Log.e("getWeatherDataFromJson", companyJsonStr);

            //Stations stations = new Stations().getInstance();
            Stations stations = new Stations();

            Log.e("getWeatherDataFromJson", "numberOfChains=" + numberOfChains);

            //if (numberOfChains == 0) {
            //    return resultStrs;
            //}

            //Stations stations = chains.get(numberOfChains);

            stations.numberOfStations = 0;
            stations.StationAL().clear();


            JSONObject stationsJson = new JSONObject(companyJsonStr);
            JSONArray stationArray = stationsJson.getJSONArray(STATIONS);

            for (i = 0; i < stationArray.length(); i++) {
                JSONObject stationJson = stationArray.getJSONObject(i);
                String name = stationJson.getString(NAME);
                String address = stationJson.getString(ADDRESS);
                double latitude = stationJson.getDouble(LATITUDE);
                double longitude = stationJson.getDouble(LONGITUDE);

                if (latitude != 0.0 &&
                        longitude != 0.0) {

                    Station station = new Station(name, address, latitude, longitude);
                    //station.name = name;
                    //station.address = address;
                    //station.latidude = latitude;
                    //station.longitude = longitude;

                    stations.StationAL().add(station);

                    stations.numberOfStations++;
                }

                resultStrs[i] = (i + 1) + "<" + name + "><" + address + ">(" + latitude + "," + longitude + ")";
                Log.e("getWeatherDataFromJson", resultStrs[i]);
            } // i

            //Log.e("getWeatherDataFromJson", "CS.stations=" + CS.stations);
            //Log.e("getWeatherDataFromJson", "stations.numberOfStations=" + stations.numberOfStations);

            //for (String s : resultStrs) {
            //    Log.e(LOG_TAG, "Station: " + s);
            //}

            //
            stations.company = chainName;
            chains.add(stations);
            numberOfChains++;

            return resultStrs;

        }

        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            httpCall("https://dl.dropboxusercontent.com/u/46823822/24TPS.json", "24TPS");
            httpCall("https://dl.dropboxusercontent.com/u/46823822/DoDoHome.json", "DoDoHome");
            httpCall("https://dl.dropboxusercontent.com/u/46823822/TaiwanParking.json", "TaiwanParking");

//            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            Log.e("<onPostExecute>", "Done");
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Loading data...", Toast.LENGTH_LONG).show();
            Log.e("<onPreExecute>", "Begin downloading...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(getApplicationContext(), "Loading data...", Toast.LENGTH_LONG).show();
            Log.e("<onProgressUpdate>", "Downloading...");
        }

        protected String[] httpCall(String urlStr, String chainName) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, urlStr)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .build();

                //URL url = new URL(builtUri.toString());

                //URL url = new URL("https://dl.dropboxusercontent.com/u/46823822/24TPS.json");
                URL url = new URL(urlStr);

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr, chainName);


            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }


    }


}
