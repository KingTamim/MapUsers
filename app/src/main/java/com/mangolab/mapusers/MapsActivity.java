package com.mangolab.mapusers;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.w3c.dom.Text;

import java.util.Date;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private static final long INTERVAL = 1000 * 9;
    private static final long FASTEST_INTERVAL = 1000 * 3;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Marker locationMarker;
    String mLastUpdateTime;

    private Double latitude ;
    private Double longitude ;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference geoFireRef;
    private DatabaseReference databaseReference3;
    private GeoFire geoFireObject;

    private String name;
    private String info;

    private Button fusedLocation;
    TextView locationUpdate;

    public static final String UID = "marker_uid";
    public static final String USER_NAME ="marker_name";
    public static final String USER_INFO="marker_info";


    protected void createLocationRequest(){

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate FIRED....................................................!");
        //showing error dialog if GooglePlayServices is not available.
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

            setContentView(R.layout.activity_maps);

        locationUpdate = findViewById(R.id.textViewLocationData);
        fusedLocation = findViewById(R.id.buttonShowLocation);
        fusedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),Login_Activity.class));
                finish();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("User_Location").child(firebaseUser.getUid());
        geoFireRef = FirebaseDatabase.getInstance().getReference("User_Location");
        //geoFireRef = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Users");
        geoFireObject = new GeoFire(geoFireRef);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart FIRED............................................................!");
        mGoogleApiClient.connect();

        if(this.mGoogleApiClient != null) {
            this.mGoogleApiClient.connect();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop FIRED.................................................................!");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected......................:" + mGoogleApiClient.isConnected());
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

        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        }

        mMap.setOnMarkerClickListener(this);

    }

    private boolean isGooglePlayServicesAvailable(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status){
            return true;
        }else {
            GooglePlayServicesUtil.getErrorDialog(status,this,0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............! ");
    }




    @RequiresApi(api = Build.VERSION_CODES.N)//This line requires if you are using 25/26 API
    @Override
    public void onLocationChanged(Location location) {
        //Removing previously added markers here!
        mMap.clear();
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        updateUI();
        setLocationToGeoFire();
        getNearbyMarkers();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {

            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            locationUpdate.setText("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    private void setLocationToGeoFire(){

        String lat = String.valueOf(mCurrentLocation.getLatitude());
        String lng = String.valueOf(mCurrentLocation.getLongitude());
        String uid = firebaseUser.getUid();

        LocationData locationData = new LocationData(lat,lng,uid);
        databaseReference2.setValue(locationData);

    }

    private void getNearbyMarkers(){


                geoFireRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()){

                            LocationData locations = locationSnapshot.getValue(LocationData.class);

                            final Double tempLat = Double.parseDouble(locations.getLatitude());
                            final Double tempLng = Double.parseDouble(locations.getLongitude());
                            final String uid = locations.getUid();

                            databaseReference3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child(uid).child("name").getValue().toString();
                                    String info = uid;

                                    LatLng allLatLang = new LatLng(tempLat,tempLng);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(allLatLang);
                                    markerOptions.title(name);
                                    markerOptions.snippet(info);
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));


                                    locationMarker = mMap.addMarker(markerOptions.visible(false));

                                    LatLng yourLatLang = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                                    if (SphericalUtil.computeDistanceBetween(yourLatLang, locationMarker.getPosition()) < 200) {
                                        locationMarker.setVisible(true);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());

    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        locationMarker = marker;
        Intent intent = new Intent(getApplicationContext(),MarkerInfoView.class);
        String uid = marker.getSnippet();
        marker.setSnippet("");
        intent.putExtra(UID,uid);
        startActivity(intent);
        return false;
    }
}
