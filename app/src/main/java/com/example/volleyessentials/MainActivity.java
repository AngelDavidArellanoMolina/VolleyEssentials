package com.example.volleyessentials;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CardView jugadorSeleccionado;
    private float offsetX, offsetY;
    private RelativeLayout playersContainer;
    private LinearLayout banquillo;
    private List<CardView> jugadoresEnPista = new ArrayList<>();
    private int jugadorCount = 6;
    private TextView num_jugadores;
    private RelativeLayout field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CardView player1 = findViewById(R.id.card_player1);
        final CardView player2 = findViewById(R.id.card_player2);
        final CardView player3 = findViewById(R.id.card_player3);
        final CardView player4 = findViewById(R.id.card_player4);
        final CardView player5 = findViewById(R.id.card_player5);
        final CardView player6 = findViewById(R.id.card_player6);
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
            jugadoresEnPista.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onTouchEvent(jugadoresEnPista.get(finalI), event);
                    return true;
                }
            });
        }

        Button btnIntercambiar = findViewById(R.id.btnIntercambiar);
        btnIntercambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intercambiarJugadores(v);
            }
        });


    }

    public void agregarJugador(View view) {
        if (jugadorCount < 12) {
            // Crear un nuevo CardView para el jugador
            CardView nuevoJugadorCardView = new CardView(this);
            nuevoJugadorCardView.setLayoutParams(new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.card_width),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            nuevoJugadorCardView.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
            nuevoJugadorCardView.setCardElevation(0);

            // Crear un nuevo LinearLayout para el contenido del CardView
            LinearLayout nuevoJugadorLayout = new LinearLayout(this);
            nuevoJugadorLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            nuevoJugadorLayout.setOrientation(LinearLayout.VERTICAL);
            nuevoJugadorLayout.setGravity(Gravity.CENTER);

            // Crear un nuevo ImageView para la imagen del jugador
            ImageView imagenJugador = new ImageView(this);
            imagenJugador.setLayoutParams(new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.player_width),
                    getResources().getDimensionPixelSize(R.dimen.player_height)
            ));
            imagenJugador.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imagenJugador.setImageResource(R.drawable.players_image);

            // Crear un nuevo TextView para el nombre del jugador
            TextView nombreJugador = new TextView(this);
            LinearLayout.LayoutParams nombreParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            nombreParams.gravity = Gravity.CENTER;
            nombreParams.topMargin = getResources().getDimensionPixelSize(R.dimen.text_margin_top);
            nombreJugador.setLayoutParams(nombreParams);
            nombreJugador.setText("Jugador " + (jugadorCount + 1));
            nombreJugador.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            nombreJugador.setTypeface(null, Typeface.BOLD);

            // Agregar la imagen y el nombre al LinearLayout del CardView
            nuevoJugadorLayout.addView(imagenJugador);
            nuevoJugadorLayout.addView(nombreJugador);

            // Agregar el LinearLayout al CardView
            nuevoJugadorCardView.addView(nuevoJugadorLayout);

            // Asignar un nuevo ID único al jugador
            nuevoJugadorCardView.setId(View.generateViewId());

            // Agregar el jugador al contenedor
            banquillo.addView(nuevoJugadorCardView);

            // Agregar el nuevo jugador a la lista de jugadores en la pista
            jugadoresEnPista.add(nuevoJugadorCardView);

            // Incrementar el contador de jugadores
            jugadorCount++;
            num_jugadores.setText(String.valueOf(jugadorCount));
        }
    }

    private void onTouchEvent(View jugadorView, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                jugadorSeleccionado = (CardView) jugadorView;
                offsetX = event.getRawX() - jugadorSeleccionado.getX();
                offsetY = event.getRawY() - jugadorSeleccionado.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (jugadorSeleccionado != null && jugadoresEnPista.contains(jugadorSeleccionado)) {
                    float x = event.getRawX() - offsetX;
                    float y = event.getRawY() - offsetY;

                    // Ajustar la posición del jugadorCardView
                    x = Math.max(0, Math.min(x, field.getWidth() - jugadorSeleccionado.getWidth()));
                    y = Math.max(0, Math.min(y, field.getHeight() - jugadorSeleccionado.getHeight()));

                    // Establecer las nuevas coordenadas del jugadorCardView
                    jugadorSeleccionado.setX(x);
                    jugadorSeleccionado.setY(y);
                }
                break;
            case MotionEvent.ACTION_UP:
                jugadorSeleccionado = null;
                break;
        }
    }

    public void intercambiarJugadores(View view) {
        // Verifica si hay jugadores en el banquillo y en la pista
        if (!jugadoresEnPista.isEmpty() && banquillo.getChildCount() > 0) {
            // Obtiene el jugador seleccionado en la pista
            CardView jugadorEnPista = jugadoresEnPista.get(0);

            // Obtiene el jugador del banquillo (el último agregado)
            View jugadorEnBanquillo = banquillo.getChildAt(banquillo.getChildCount() - 1);

            // Remueve el jugador del banquillo
            banquillo.removeView(jugadorEnBanquillo);

            // Añade el jugador del banquillo a la pista
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            jugadorEnPista.setLayoutParams(params);
            playersContainer.addView(jugadorEnBanquillo);

            // Actualiza la lista de jugadores en la pista
            jugadoresEnPista.set(0, (CardView) jugadorEnBanquillo);
        }
    }


}