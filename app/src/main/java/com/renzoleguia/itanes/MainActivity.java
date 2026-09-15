package com.renzoleguia.itanes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.renzoleguia.itanes.data.local.database.AppDatabase;
import com.renzoleguia.itanes.data.local.seed.PlaceDataSeeder;
import com.renzoleguia.itanes.data.repository.PlaceRepository;
import com.renzoleguia.itanes.ui.favorites.FavoritesActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configuración para que el diseño respete las barras del sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_places) {
                Intent intent = new Intent(this, PlacesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Intent intent = new Intent(this, FavoritesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Navegación a PlacesActivity
        findViewById(R.id.buttonExplore).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Inicialización de la base de datos y carga inicial de datos (Seeder)
        AppDatabase db = AppDatabase.getInstance(this);
        PlaceRepository repository = new PlaceRepository(db.placeDao());
        PlaceDataSeeder seeder = new PlaceDataSeeder(repository);
        seeder.seedData();

        // Iniciar sincronización de lugares desde la API a Room
        repository.syncPlaces();
    }
}
