package com.renzoleguia.itanes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.renzoleguia.itanes.data.local.database.AppDatabase;
import com.renzoleguia.itanes.data.local.seed.PlaceDataSeeder;
import com.renzoleguia.itanes.data.repository.PlaceRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Configuración para que el diseño respete las barras del sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // NOTA: El botón 'buttonExplore' está definido en XML pero no requiere lógica
        // de navegación en esta primera etapa.

        // Inicialización de la base de datos y carga inicial de datos (Seeder)
        AppDatabase db = AppDatabase.getInstance(this);
        PlaceRepository repository = new PlaceRepository(db.placeDao());
        PlaceDataSeeder seeder = new PlaceDataSeeder(repository);
        seeder.seedData();
    }
}
