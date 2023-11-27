package com.example.volleyessentials;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView jugadorSeleccionado;
    private float offsetX, offsetY;
    private RelativeLayout playersContainer;
    private LinearLayout banquillo;
    private List<ImageView> jugadoresEnPista = new ArrayList<>();
    private int jugadorCount = 6;
    private TextView num_jugadores;
    private RelativeLayout field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView player1 = findViewById(R.id.player1);
        final ImageView player2 = findViewById(R.id.player2);
        final ImageView player3 = findViewById(R.id.player3);
        final ImageView player4 = findViewById(R.id.player4);
        final ImageView player5 = findViewById(R.id.player5);
        final ImageView player6 = findViewById(R.id.player6);
        field = findViewById(R.id.pista_juego);
        num_jugadores = findViewById(R.id.numero_jugadores);
        num_jugadores.setText(String.valueOf(jugadorCount));
        banquillo = findViewById(R.id.banquillo_jugadores);

        jugadoresEnPista.add(player1);
        jugadoresEnPista.add(player2);
        jugadoresEnPista.add(player3);
        jugadoresEnPista.add(player4);
        jugadoresEnPista.add(player5);
        jugadoresEnPista.add(player6);

        for (int i = 0; i < jugadoresEnPista.size(); i++) {
            final int finalI = i;
            int finalI1 = i;
            jugadoresEnPista.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(jugadoresEnPista.get(finalI1), event);
                    return true;
                }
            });
        }
    }

    public void agregarJugador(View view) {
        if (jugadorCount < 12) {
            // Crear un nuevo ImageView para el jugador
            ImageView nuevoJugador = new ImageView(this);
            nuevoJugador.setLayoutParams(new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.player_width),
                    getResources().getDimensionPixelSize(R.dimen.player_height)
            ));
            nuevoJugador.setImageResource(R.drawable.player_image);

            // Asignar un nuevo ID único al jugador
            nuevoJugador.setId(View.generateViewId());

            // Agregar el jugador al contenedor
            banquillo.addView(nuevoJugador);

            // Agregar el nuevo jugador a la lista de jugadores en la pista
            jugadoresEnPista.add(nuevoJugador);

            // Incrementar el contador de jugadores
            jugadorCount++;
            num_jugadores.setText(String.valueOf(jugadorCount));

            // Asignar el evento onTouch solo para los jugadores en la pista
            nuevoJugador.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(nuevoJugador, event);
                    return true;
                }
            });
        }
    }

    private void onTouchEvent(ImageView jugador, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                jugadorSeleccionado = jugador;
                offsetX = event.getRawX() - jugador.getX();
                offsetY = event.getRawY() - jugador.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (jugadorSeleccionado != null && jugadoresEnPista.contains(jugadorSeleccionado)) {
                    float x = event.getRawX() - offsetX;
                    float y = event.getRawY() - offsetY;

                    // Ajustar la posición del jugador
                    x = Math.max(0, Math.min(x, field.getWidth() - jugador.getWidth()));
                    y = Math.max(0, Math.min(y, field.getHeight() - jugador.getHeight()));

                    // Establecer las nuevas coordenadas del jugador
                    jugadorSeleccionado.setX(x);
                    jugadorSeleccionado.setY(y);
                }
                break;
            case MotionEvent.ACTION_UP:
                jugadorSeleccionado = null;
                break;
        }
    }
}