package com.renzoleguia.itanes.ui.detail;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.renzoleguia.itanes.PlacesActivity;
import com.renzoleguia.itanes.R;
import com.renzoleguia.itanes.data.local.database.AppDatabase;
import com.renzoleguia.itanes.data.local.entity.FavoriteEntity;
import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.repository.FavoriteRepository;
import com.renzoleguia.itanes.data.repository.PlaceRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaceDetailActivity extends AppCompatActivity {

    private static final String TAG = "PlaceDetailActivity";
    
    private PlaceRepository repository;
    private FavoriteRepository favoriteRepository;
    private ExecutorService executorService;
    
    private ImageView imageDetail;
    private TextView textDetailName;
    private TextView textDetailShortDesc;
    private TextView textDetailDescription;
    private TextView textDetailAddress;
    private TextView textDetailCoordinates;
    
    private Button buttonFavorite;
    private Button buttonShare;
    private Button buttonMap;

    private int placeId;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        
        AppDatabase db = AppDatabase.getInstance(this);
        repository = new PlaceRepository(db.placeDao());
        favoriteRepository = new FavoriteRepository(db.favoriteDao());
        executorService = Executors.newSingleThreadExecutor();

        placeId = getIntent().getIntExtra(PlacesActivity.EXTRA_PLACE_ID, -1);
        
        if (placeId == -1) {
            Toast.makeText(this, R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        checkFavoriteStatus();
        loadPlaceDetail(placeId);
    }

    private void initViews() {
        imageDetail = findViewById(R.id.imageDetail);
        textDetailName = findViewById(R.id.textDetailName);
        textDetailShortDesc = findViewById(R.id.textDetailShortDesc);
        textDetailDescription = findViewById(R.id.textDetailDescription);
        textDetailAddress = findViewById(R.id.textDetailAddress);
        textDetailCoordinates = findViewById(R.id.textDetailCoordinates);
        
        buttonFavorite = findViewById(R.id.buttonFavorite);
        buttonShare = findViewById(R.id.buttonShare);
        buttonMap = findViewById(R.id.buttonMap);
        
        buttonFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void checkFavoriteStatus() {
        executorService.execute(() -> {
            isFavorite = favoriteRepository.isFavorite(placeId);
            runOnUiThread(this::updateFavoriteButton);
        });
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            buttonFavorite.setText(R.string.button_unfavorite);
        } else {
            buttonFavorite.setText(R.string.button_favorite);
        }
    }

    private void toggleFavorite() {
        executorService.execute(() -> {
            if (isFavorite) {
                favoriteRepository.removeFavorite(placeId);
                isFavorite = false;
            } else {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                favoriteRepository.addFavorite(new FavoriteEntity(placeId, date));
                isFavorite = true;
            }
            runOnUiThread(this::updateFavoriteButton);
        });
    }

    private void loadPlaceDetail(int placeId) {
        executorService.execute(() -> {
            PlaceEntity place = repository.getPlaceById(placeId);
            
            runOnUiThread(() -> {
                if (place != null) {
                    displayPlace(place);
                } else {
                    Toast.makeText(PlaceDetailActivity.this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void displayPlace(PlaceEntity place) {
        textDetailName.setText(place.getName());
        textDetailShortDesc.setText(place.getShortDescription());
        textDetailDescription.setText(place.getDescription());
        textDetailAddress.setText(place.getAddress());
        
        String coords = getString(R.string.coordinates_format, place.getLatitude(), place.getLongitude());
        textDetailCoordinates.setText(coords);

        Glide.with(this)
                .load(place.getImageUrl())
                .placeholder(R.drawable.ic_place_placeholder)
                .error(R.drawable.ic_place_placeholder)
                .centerCrop()
                .into(imageDetail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
