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

        // Navegación a PlacesActivity
        findViewById(R.id.buttonExplore).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, PlacesActivity.class);
            startActivity(intent);
        });

        // Inicialización de la base de datos y carga inicial de datos (Seeder)
        AppDatabase db = AppDatabase.getInstance(this);
        PlaceRepository repository = new PlaceRepository(db.placeDao());
        PlaceDataSeeder seeder = new PlaceDataSeeder(repository);
        seeder.seedData();

        // Prueba de consumo de API (Mock) para validación en esta Tanda
        testRemoteApi();
    }

    private void testRemoteApi() {
        com.renzoleguia.itanes.data.remote.api.ItanesApiService apiService = 
                com.renzoleguia.itanes.data.remote.retrofit.RetrofitClient.getApiService();
        
        apiService.getPlaces().enqueue(new retrofit2.Callback<java.util.List<com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto>> call, 
                                   retrofit2.Response<java.util.List<com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    java.util.List<com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto> places = response.body();
                    android.util.Log.d("ITANES_API", "Respuesta recibida correctamente");
                    android.util.Log.d("ITANES_API", "Total lugares: " + places.size());
                    
                    for (com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto place : places) {
                        android.util.Log.d("ITANES_API", place.getId() + " - " + place.getName());
                    }
                } else {
                    android.util.Log.e("ITANES_API", "Error HTTP en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.renzoleguia.itanes.data.remote.dto.PlaceRemoteDto>> call, Throwable t) {
                android.util.Log.e("ITANES_API", "Error de red o conexión: " + t.getMessage());
            }
        });
    }
}
