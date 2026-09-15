package com.renzoleguia.itanes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.renzoleguia.itanes.data.local.database.AppDatabase;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.repository.PlaceRepository;
import com.renzoleguia.itanes.ui.detail.PlaceDetailActivity;

import android.content.Intent;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlacesActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener {

    public static final String EXTRA_PLACE_ID = "com.renzoleguia.itanes.EXTRA_PLACE_ID";
    private PlaceAdapter placeAdapter;
    private PlaceRepository repository;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_places);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_places), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); // Bottom navigation will handle bottom inset
            return insets;
        });

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_places);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_places) {
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Intent intent = new Intent(this, com.renzoleguia.itanes.ui.favorites.FavoritesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Initialize Repository & Executor
        AppDatabase db = AppDatabase.getInstance(this);
        repository = new PlaceRepository(db.placeDao());
        executorService = Executors.newSingleThreadExecutor();

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlaces);
        placeAdapter = new PlaceAdapter(this);
        recyclerView.setAdapter(placeAdapter);

        // Fetch data
        loadPlaces();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlaces();
    }

    private void loadPlaces() {
        executorService.execute(() -> {
            List<PlaceEntity> places = repository.getAllPlaces();
            
            // Update UI on main thread
            runOnUiThread(() -> {
                placeAdapter.setPlaces(places);
            });
        });
    }

    @Override
    public void onPlaceClick(int placeId) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra(EXTRA_PLACE_ID, placeId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}