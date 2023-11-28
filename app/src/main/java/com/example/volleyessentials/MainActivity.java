package com.example.volleyessentials;

import static com.example.volleyessentials.Rol.Colocador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CardView jugadorSeleccionado;
    private float offsetX, offsetY;
    private RelativeLayout playersContainer;
    private LinearLayout banquillo;
    private List<Jugador> jugadoresEnPista = new ArrayList<>();
    private List<Jugador> jugadoresTotales = new ArrayList<>();
    private int jugadorCount = 0;
    private TextView num_jugadores;
    private RelativeLayout field;
    private GridLayout gridJugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CardView player = findViewById(R.id.card_player);
        field = findViewById(R.id.pista_juego);
        gridJugadores = findViewById(R.id.grid_jugadoresPista);
        num_jugadores = findViewById(R.id.numero_jugadores);
        num_jugadores.setText(String.valueOf(jugadorCount));
        banquillo = findViewById(R.id.banquillo_jugadores);


    }

    public void agregarJugador(View view) {
        if (jugadorCount < 12) {
            final String[] nombreJugador = new String[1];
            final int[] dorsalJugador = new int[1];
            final String[] posicionJugador = new String[1];

            View popupView = getLayoutInflater().inflate(R.layout.agregar_jugador_form, null);

            Dialog dialog = new Dialog(this);
            dialog.setContentView(popupView);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.gravity = Gravity.BOTTOM;
                window.setAttributes(layoutParams);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.show();

            EditText nombreIntroducido = popupView.findViewById(R.id.nombreIntroducido);
            EditText dorsalIntroducido = popupView.findViewById(R.id.dorsalIntroducido);
            RadioGroup posicionElegida = popupView.findViewById(R.id.posiciónElegida);

            Button botonAñadir = popupView.findViewById(R.id.botonAñadir);
            botonAñadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedRadioButtonId = posicionElegida.getCheckedRadioButtonId();
                    RadioButton radioButton = popupView.findViewById(selectedRadioButtonId);

                    if (!nombreIntroducido.getText().toString().isEmpty()) {
                        nombreJugador[0] = nombreIntroducido.getText().toString();
                    } else {
                        Toast.makeText(MainActivity.this, "Añade un nombre", Toast.LENGTH_SHORT).show();
                    }

                    if (!dorsalIntroducido.getText().toString().isEmpty()) {
                        dorsalJugador[0] = Integer.parseInt(dorsalIntroducido.getText().toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Añade un dorsal", Toast.LENGTH_SHORT).show();
                    }

                    if (radioButton != null) {
                        posicionJugador[0] = radioButton.getText().toString();
                    } else {
                        Toast.makeText(MainActivity.this, "Selecciona una posición", Toast.LENGTH_SHORT).show();
                    }

                    if (!nombreIntroducido.getText().toString().isEmpty()
                            && !dorsalIntroducido.getText().toString().isEmpty()
                            && radioButton != null) {
                        jugadorCount++;
                        Jugador jugador = new Jugador(nombreJugador[0], dorsalJugador[0], Rol.valueOf(posicionJugador[0]));
                        jugadoresTotales.add(jugador);
                        jugadoresEnPista.add(jugador);

                        for (Jugador j : jugadoresTotales) {
                            System.out.println(j);
                        }

                        dialog.dismiss();

                        View card_jugador = getLayoutInflater().inflate(R.layout.cardview_player, null);

                        ImageView imagenJugador = card_jugador.findViewById(R.id.imagen_posicion_jugador);
                        TextView nombreTextView = card_jugador.findViewById(R.id.nombre_jugador);
                        TextView dorsalJugador = card_jugador.findViewById(R.id.dorsal_jugador);

                        switch (jugador.getPosicion()){
                            case Colocador:
                                imagenJugador.setImageResource(R.drawable.players_image);
                                break;
                            case Ala:
                                imagenJugador.setImageResource(R.drawable.player_image);
                                break;
                            case Central:
                                imagenJugador.setImageResource(R.drawable.players_image);
                                break;
                            case Líbero:
                                imagenJugador.setImageResource(R.drawable.players_image);
                                break;
                        }

                        nombreTextView.setText(jugador.getNombre());
                        dorsalJugador.setText(String.valueOf(jugador.getDorsal()));


                        if (jugadoresEnPista.size() < 6){
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            gridJugadores.addView(card_jugador, params);
                            card_jugador.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    jugadorSeleccionado = (CardView) v;
                                    onTouchEvent_movimiento(jugadorSeleccionado, event);
                                    return true;
                                }
                            });

                        }
                    }
                }
            });
        }
    }

    private void onTouchEvent_movimiento(View jugadorView, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                jugadorSeleccionado = (CardView) jugadorView;
                offsetX = event.getRawX() - jugadorSeleccionado.getX();
                offsetY = event.getRawY() - jugadorSeleccionado.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (jugadorSeleccionado != null) {
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

}