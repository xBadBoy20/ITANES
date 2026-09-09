package com.renzoleguia.itanes.data.local.seed;

import android.util.Log;

import com.renzoleguia.itanes.data.local.entity.PlaceEntity;
import com.renzoleguia.itanes.data.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaceDataSeeder {

    private static final String TAG = "PlaceDataSeeder";
    private final PlaceRepository repository;
    private final ExecutorService executorService;

    public PlaceDataSeeder(PlaceRepository repository) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void seedData() {
        executorService.execute(() -> {
            try {
                if (repository.getCount() == 0) {
                    Log.d(TAG, "La tabla places está vacía. Iniciando carga de datos...");
                    List<PlaceEntity> places = new ArrayList<>();

                    places.add(new PlaceEntity(
                            1,
                            "Plaza Mayor de Cuzco",
                            "El corazón histórico y punto cero de la ciudad.",
                            "La Plaza Mayor es el punto cero perfecto para tu recorrido en la app, siendo el lugar de reunión urbano por excelencia. Es ideal para iniciar la ruta rodeado de pórticos coloniales, la imponente catedral y sus bellos jardines centrales.",
                            "Centro Histórico, Cuzco, Perú",
                            -13.5167,
                            -71.9787,
                            "https://machupicchuwayna.com/wp-content/uploads/2025/07/plaza-armas-cusco.jpg",
                            1,
                            "2026-09-07"
                    ));

                    places.add(new PlaceEntity(
                            2,
                            "Piedra de los 12 ángulos",
                            "Un elemento icónico de la arquitectura incaica.",
                            "Se ubica a solo un par de cuadras de la plaza y es un elemento súper icónico para destacar como un buen point en el lugar. Se dice que esta enorme piedra, de encaje perfecto, sostiene toda la estructura del antiguo muro de un palacio inca.",
                            "Calle Hatun Rumiyoc, Cuzco, Perú",
                            -13.5158,
                            -71.9765,
                            "https://grouptravelperu.com/wp-content/uploads/Piedra-de-los-12-Angulos.webp",
                            2,
                            "2026-09-07"
                    ));

                    places.add(new PlaceEntity(
                            3,
                            "Qorikancha",
                            "Imponentes ruinas del templo más importante del imperio.",
                            "Representa una parada visualmente clave al seguir bajando por el centro histórico. Son las imponentes ruinas de un importante templo del siglo XV, dedicado al Sol, cuyos muros estaban originalmente cubiertos de oro puro.",
                            "Plazoleta Santo Domingo, Cuzco, Perú",
                            -13.5200,
                            -71.9753,
                            "https://cuscoperu.b-cdn.net/wp-content/uploads/2024/06/Qoricancha-pc.jpg",
                            3,
                            "2026-09-07"
                    ));

                    places.add(new PlaceEntity(
                            4,
                            "Mercado Central de San Pedro",
                            "Mercado techado lleno de color, vida y tradición.",
                            "Le dará mucha vida urbana a la app, al ser un establecimiento techado súper concurrido. Lleno de puestos de frutas, ropa tradicional, recuerdos y jugos recién exprimidos, es una parada clásica que no puede faltar en la ruta.",
                            "Cascaparo s/n, Cuzco, Perú",
                            -13.5208,
                            -71.9824,
                            "https://images.trvl-media.com/place/6254422/d08d3936-ff46-4a36-a0c1-091fe076c593.jpg",
                            4,
                            "2026-09-07"
                    ));

                    places.add(new PlaceEntity(
                            5,
                            "Cristo Blanco",
                            "Estatua monumental con la mejor vista panorámica.",
                            "Funciona como el remate final de la ruta, obligando a tu mapa a hacer un poco de zoom out para mostrar la elevación. Es una estatua de 8 metros instalada alrededor de 1945 en lo alto de un cerro, muy popular por las increíbles vistas de toda la ciudad.",
                            "Cerro Pukamoqo, Cuzco, Perú",
                            -13.509591,
                            -71.978032,
                            "https://machupicchu.cheap/wp-content/uploads/2025/01/WhatsApp-Image-2024-08-12-at-3.22.26-PM-1.jpeg",
                            5,
                            "2026-09-07"
                    ));

                    repository.insertAll(places);
                    Log.d(TAG, "PlaceDataSeeder: 5 lugares disponibles en Room");
                } else {
                    Log.d(TAG, "PlaceDataSeeder: La base de datos ya contiene registros.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al cargar datos iniciales: " + e.getMessage());
            }
        });
    }
}
