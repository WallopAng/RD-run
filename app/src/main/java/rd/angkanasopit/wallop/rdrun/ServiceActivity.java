package rd.angkanasopit.wallop.rdrun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ServiceActivity extends FragmentActivity implements OnMapReadyCallback {
    //Explicit
    private GoogleMap mMap;
    private String idString, avataString, nameString, surnameString;
    private ImageView imageView;
    private TextView nameTextView, surnameTextView;
    private int[] atataInts;
    private double userLatADouble=13.806814, userLngADouble = 100.574725;  //The conecion
    private LocationManager locationManager; //หาพิกัดบนโลก
    private Criteria criteria;
    private static final String urlPHP = "http://swiftcodingthai.com/rd/edit_location_master.php";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_service);

        //Bind Widget
        imageView = (ImageView) findViewById(R.id.imageView7);
        nameTextView = (TextView) findViewById(R.id.textView8);
        surnameTextView = (TextView) findViewById(R.id.textView9);



        //Get value from Intent
        idString = getIntent().getStringExtra("id");
        avataString = getIntent().getStringExtra("Avata");
        nameString = getIntent().getStringExtra("Name");
        surnameString = getIntent().getStringExtra("Surname");

        //setup Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);


        //show text
        nameTextView.setText(nameString);
        surnameTextView.setText(surnameString);

        //show avata
        MyConstant myConstant = new MyConstant();
        atataInts = myConstant.getAvataInts();
        imageView.setImageResource(atataInts[Integer.parseInt(avataString)]);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }   //main method

    private class SynAllUser extends AsyncTask<Void, Void, String> {
        private Context context;
        private GoogleMap googleMap;
        private static final String urlJSON = "http://swiftcodingthai.com/rd/get_user_master.php";
        private String[] nameStrings, surnameStrings;
        private int[] avataInts;
        private double[] latDoubles, lngDoubles;



        public SynAllUser(Context context, GoogleMap googleMap) {
            this.context = context;
            this.googleMap = googleMap;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("2SepV2", "e doIn ==> " + e.toString());
                return null;


            }



        }   //doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("2SepV2", "JSon ==> "+ s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                nameStrings = new String[jsonArray.length()];
                surnameStrings = new String[jsonArray.length()];
                avataInts = new int[jsonArray.length()];
                latDoubles = new double[jsonArray.length()];
                lngDoubles = new double[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStrings[i] = jsonObject.getString("Name");
                    surnameStrings[i] = jsonObject.getString("Surname");
                    atataInts[i] = Integer.parseInt(jsonObject.getString("Avata"));
                    latDoubles[i] = Double.parseDouble(jsonObject.getString("Lat"));
                    lngDoubles[i] = Double.parseDouble(jsonObject.getString("Lng"));

                    //Creaet Marker
                    MyConstant myConstant = new MyConstant();
                    int[] iconInts = myConstant.getAvataInts();


                    googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latDoubles[i],lngDoubles[i]))
                    .icon(BitmapDescriptorFactory.fromResource(iconInts[atataInts[i]]))
                    .title(nameStrings[i]+" "+surnameStrings[i]));

                    Log.d("2SepV3", "Name (" + i + ") = " + nameStrings[i]);
                    Log.d("2SepV3", "Lat (" + i + ") = " + latDoubles[i]);
                    Log.d("2SepV3", "Lng (" + i + ") = " + lngDoubles[i]);
                    Log.d("2SepV3", "=======================");

                }

            } catch (Exception e) {
                Log.d("2SepV3", "e onPost ==> " + e.toString());
            }
        }   //onPost

    }   //SynAllUser Class




    @Override
    protected void onResume() {
        super.onResume();
        locationManager.removeUpdates(locationListener);
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);

        if (networkLocation != null) {
            userLatADouble = networkLocation.getLatitude();
            userLngADouble = networkLocation.getLongitude();

        }

        Location gpsLocation = myFindLocation(locationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            userLatADouble = gpsLocation.getLatitude();
            userLngADouble = gpsLocation.getLongitude();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {

        Location location = null;
        if (locationManager.isProviderEnabled(strProvider)) {
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("1SepV1", "Cannot find Location");
        }

        return null;
    }


    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            userLatADouble = location.getLatitude();
            userLngADouble = location.getLongitude();


        }   //onLocationChange

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //setup center of map
        LatLng latLng = new LatLng(userLatADouble,userLngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

        //loop
        myLoop();



    }   //onMap

    private void myLoop() {
        //to do
        Log.d("1SepV2", "Lat ==> " + userLatADouble);
        Log.d("1SepV2", "Lng ==> " + userLngADouble);
        editLatLngOnServer();

        createMarker();



        //Post Delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              myLoop();
            }
        },1000);

    }   //myLoop

    private void createMarker() {
        //Clear Marker
        mMap.clear();

        SynAllUser synAllUser = new SynAllUser(this, mMap);
        synAllUser.execute();

    }

    private void editLatLngOnServer() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("id", idString)
                .add("Lat", Double.toString(userLatADouble))
                .add("Lng", Double.toString(userLngADouble))
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(urlPHP).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("2SepV1", "e ==> " + e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("2SepV1", "Result ==> " + response.body().string());

            }
        });

    }   // editLatLng


}   //main class
