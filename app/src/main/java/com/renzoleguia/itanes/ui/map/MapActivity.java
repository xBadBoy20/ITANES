package com.renzoleguia.itanes.ui.map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.renzoleguia.itanes.PlacesActivity;
import com.renzoleguia.itanes.R;
import com.renzoleguia.itanes.data.local.database.AppDatabase;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.repository.PlaceRepository;

import org.maplibre.android.MapLibre;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private TextView textMapPlaceName;
    private TextView textMapPlaceAddress;

    private PlaceRepository repository;
    private ExecutorService executorService;
    private int placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar MapLibre antes de setContentView
        MapLibre.getInstance(this);
        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textMapPlaceName = findViewById(R.id.textMapPlaceName);
        textMapPlaceAddress = findViewById(R.id.textMapPlaceAddress);
        mapView = findViewById(R.id.mapView);
        
        android.widget.ImageButton buttonBack = findViewById(R.id.buttonBack);
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }

        mapView.onCreate(savedInstanceState);

        AppDatabase db = AppDatabase.getInstance(this);
        repository = new PlaceRepository(db.placeDao());
        executorService = Executors.newSingleThreadExecutor();

        placeId = getIntent().getIntExtra(PlacesActivity.EXTRA_PLACE_ID, -1);
        if (placeId == -1) {
            Toast.makeText(this, R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        checkInternetConnection();
        loadPlaceData();
    }

    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
                Toast.makeText(this, R.string.error_no_internet_map, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadPlaceData() {
        executorService.execute(() -> {
            PlaceEntity place = repository.getPlaceById(placeId);
            
            runOnUiThread(() -> {
                if (place != null) {
                    setupMapForPlace(place);
                } else {
                    Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void setupMapForPlace(PlaceEntity place) {
        textMapPlaceName.setText(place.getName());
        textMapPlaceAddress.setText(place.getAddress());

        double lat = place.getLatitude();
        double lng = place.getLongitude();

        // Validar coordenadas
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            Toast.makeText(this, R.string.error_invalid_coordinates, Toast.LENGTH_LONG).show();
            return;
        }

        android.widget.ImageButton buttonZoomIn = findViewById(R.id.buttonZoomIn);
        android.widget.ImageButton buttonZoomOut = findViewById(R.id.buttonZoomOut);

        mapView.getMapAsync(mapLibreMap -> {
            mapLibreMap.setMinZoomPreference(3.0);
            mapLibreMap.setMaxZoomPreference(19.0);

            if (buttonZoomIn != null) {
                buttonZoomIn.setOnClickListener(v -> {
                    mapLibreMap.easeCamera(org.maplibre.android.camera.CameraUpdateFactory.zoomIn());
                });
            }

            if (buttonZoomOut != null) {
                buttonZoomOut.setOnClickListener(v -> {
                    mapLibreMap.easeCamera(org.maplibre.android.camera.CameraUpdateFactory.zoomOut());
                });
            }

            mapLibreMap.setStyle("https://tiles.openfreemap.org/styles/liberty", style -> {
                // Centrar cámara
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng))
                        .zoom(14.0)
                        .build();
                mapLibreMap.setCameraPosition(position);

                // Agregar marcador
                mapLibreMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(place.getName()));
            });
        });
    }

    // --- Ciclo de vida de MapView ---

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
