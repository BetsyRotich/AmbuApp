package com.example.ambuapp.usermaps;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import com.example.ambuapp.UserProfile;
import com.example.ambuapp.databinding.ActivityUserMapsBinding;
import com.example.ambuapp.databinding.NavHeaderMainBinding;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ambuapp.R;
import com.example.ambuapp.RolesActivity;
import com.example.ambuapp.entities.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {
    private static final int LOCATION_PERMISSION_REQUEST = 5495;
    public static final String TAG = "UserMaps";


    private GoogleMap mMap;
    private ActivityUserMapsBinding binding;


    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private Marker destinationMarker;
    private GeoApiContext geoApiContext;
    private Marker startLocationMarker;
    private Polyline polylines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Inflate the nav header layout
        NavHeaderMainBinding navHeaderBinding = NavHeaderMainBinding.inflate(getLayoutInflater());
        // Access the views within the nav header layout
        TextView nameTextView = navHeaderBinding.name;
        TextView emailTextView = navHeaderBinding.email;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST);
            return;
        }

        CancellationToken token = new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        };
        fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, token)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null && mMap != null) {
                            currentLocation = location;

                            setCameraPosition(currentLocation);
                        }
                    }
                });

        geoApiContext = new GeoApiContext.Builder().queryRateLimit(3)
                .apiKey(getString(R.string.google_map_api_key))
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();


        String apiKey = getString(R.string.google_map_api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            new Handler().postDelayed(() -> {
                autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
                ImageView imageView = autocompleteFragment.requireView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_button);
                imageView.setImageResource(R.drawable.outline_menu_black_24);

//                int padding = getResources().getDimensionPixelSize(R.dimen.dimen_main_half);
//                imageView.setPadding(padding, padding, padding, padding);
                imageView.setBackgroundResource(R.drawable.ripple);

                imageView.setOnClickListener(v->{

                    binding.drawerLayout.openDrawer(Gravity.LEFT);

                });



            }, 1000);


            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                    place.getLatLng();
                    if (destinationMarker != null) {
                        destinationMarker.remove();
                    }
                    LatLng latLng = place.getLatLng();

                    destinationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Destination"));

                    drawPolyline();
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }

        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, RolesActivity.class));
            finishAffinity();
        });

        binding.btnProfile.setOnClickListener(v -> startActivity(new Intent(this, UserProfile.class)));

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);

        // Add a marker in Sydney and move the camera
        if (currentLocation != null) {
            setCameraPosition(currentLocation);
        }
    }

    private void setCameraPosition(Location location) {
        LatLng yourLocation = new LatLng(location.getLatitude(), location.getLongitude());
        startLocationMarker = mMap.addMarker(
                new MarkerOptions().
                        position(yourLocation).
                        title("Your Location").
                        draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        );

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(yourLocation, 13, 0, 0)));

    }

    private void drawPolyline() {
        try {
            if (polylines != null)
                polylines.remove();

            if (destinationMarker != null && startLocationMarker != null) {

                LatLng startPosition = startLocationMarker.getPosition();
                DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                        .mode(TravelMode.DRIVING)
                        .origin(startPosition.latitude + "," + startPosition.longitude)
                        .destination(destinationMarker.getPosition().latitude + "," + destinationMarker.getPosition().longitude)
                        .await();

                List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
                polylines = mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    binding.navHeader.name.setText(user.username);
                    binding.navHeader.email.setText(user.email);
                }

            }

        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {


    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        startLocationMarker = marker;


        drawPolyline();
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}

