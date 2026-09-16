package com.renzoleguia.itanes.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.renzoleguia.itanes.MainActivity;
import com.renzoleguia.itanes.PlaceAdapter;
import com.renzoleguia.itanes.PlacesActivity;
import com.renzoleguia.itanes.R;
import com.renzoleguia.itanes.data.local.database.AppDatabase;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.repository.FavoriteRepository;
import com.renzoleguia.itanes.ui.detail.PlaceDetailActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity implements PlaceAdapter.OnPlaceClickListener {

    private PlaceAdapter placeAdapter;
    private FavoriteRepository favoriteRepository;
    private ExecutorService executorService;
    private LinearLayout layoutEmptyFavorites;
    private RecyclerView recyclerViewFavorites;
    private android.widget.ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_favorites), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); // Bottom nav handles bottom inset
            return insets;
        });

        AppDatabase db = AppDatabase.getInstance(this);
        favoriteRepository = new FavoriteRepository(db.favoriteDao());
        executorService = Executors.newSingleThreadExecutor();

        layoutEmptyFavorites = findViewById(R.id.layoutEmptyFavorites);
        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        progressBar = findViewById(R.id.progressBarFavorites);

        placeAdapter = new PlaceAdapter(this);
        recyclerViewFavorites.setAdapter(placeAdapter);
        recyclerViewFavorites.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_places) {
                Intent intent = new Intent(this, PlacesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                return true;
            }
            return false;
        });

        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        if (layoutEmptyFavorites != null) layoutEmptyFavorites.setVisibility(View.GONE);

        executorService.execute(() -> {
            try {
                List<PlaceEntity> favoritePlaces = favoriteRepository.getFavoritePlaces();
                
                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    
                    if (favoritePlaces == null || favoritePlaces.isEmpty()) {
                        if (recyclerViewFavorites != null) recyclerViewFavorites.setVisibility(View.GONE);
                        if (layoutEmptyFavorites != null) layoutEmptyFavorites.setVisibility(View.VISIBLE);
                    } else {
                        if (recyclerViewFavorites != null) recyclerViewFavorites.setVisibility(View.VISIBLE);
                        if (layoutEmptyFavorites != null) layoutEmptyFavorites.setVisibility(View.GONE);
                        placeAdapter.setPlaces(favoritePlaces);
                    }
                });
            } catch (Exception e) {
                android.util.Log.e("ITANES_ROOM", "Error al cargar favoritos: " + e.getMessage());
                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    if (layoutEmptyFavorites != null) layoutEmptyFavorites.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    @Override
    public void onPlaceClick(int placeId) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra(PlacesActivity.EXTRA_PLACE_ID, placeId);
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