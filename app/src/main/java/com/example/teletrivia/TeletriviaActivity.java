package com.example.teletrivia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.teletrivia.databinding.ActivityTeletriviaBinding;
import com.example.teletrivia.model.Pregunta;
import com.example.teletrivia.model.RespuestaTrivia;
import com.example.teletrivia.network.ApiService;
import com.example.teletrivia.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeletriviaActivity extends AppCompatActivity {

    private ActivityTeletriviaBinding binding;
    private TriviaViewModel triviaViewModel;

    private String categoriaSeleccionada;
    private int cantidadPreguntas;
    private String dificultadSeleccionada;

    private List<Pregunta> preguntas = new ArrayList<>();
    private int indicePreguntaActual = 0;
    private int respuestasCorrectas = 0;
    private int respuestasIncorrectas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeletriviaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Capturar datos de la actividad anterior
        categoriaSeleccionada = getIntent().getStringExtra("categoria");
        cantidadPreguntas = getIntent().getIntExtra("cantidad", 1);
        dificultadSeleccionada = getIntent().getStringExtra("dificultad");

        // Mostrar la categoría seleccionada
        binding.tvCategoria.setText(categoriaSeleccionada);

        // Inicializar Timer
        triviaViewModel = new ViewModelProvider(this).get(TriviaViewModel.class);
        iniciarTimer();

        // Cargar preguntas desde API
        cargarPreguntas();

        // Botón Siguiente
        binding.btnSiguiente.setOnClickListener(view -> validarYAvanzar());
    }

    private void iniciarTimer() {
        int tiempoPorPregunta;
        switch (dificultadSeleccionada.toLowerCase()) {
            case "fácil":
                tiempoPorPregunta = 5;
                break;
            case "medio":
                tiempoPorPregunta = 7;
                break;
            case "difícil":
                tiempoPorPregunta = 10;
                break;
            default:
                tiempoPorPregunta = 5;
        }
        int tiempoTotal = cantidadPreguntas * tiempoPorPregunta;
        triviaViewModel.iniciarContador(tiempoTotal);

        triviaViewModel.getTiempoRestante().observe(this, tiempo -> {
            binding.tvContador.setText(String.format("00:%02d", tiempo));
            if (tiempo == 0) {
                irAEstadisticas();
            }
        });
    }

    private void cargarPreguntas() {
        int idCategoria = obtenerIdCategoria(categoriaSeleccionada);
        String dificultadIngles = traducirDificultad(dificultadSeleccionada);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.obtenerPreguntas(
                cantidadPreguntas,
                idCategoria,
                dificultadIngles,
                "boolean"
        ).enqueue(new Callback<RespuestaTrivia>() {
            @Override
            public void onResponse(Call<RespuestaTrivia> call, Response<RespuestaTrivia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponseCode() != 0) {
                        Toast.makeText(TeletriviaActivity.this, "No se encontraron preguntas disponibles para esa categoría y dificultad.", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }

                    preguntas = response.body().getResults();

                    if (preguntas == null || preguntas.isEmpty()) {
                        Toast.makeText(TeletriviaActivity.this, "No se encontraron preguntas.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        mostrarPregunta();
                    }
                } else {
                    Toast.makeText(TeletriviaActivity.this, "Error en la respuesta de la API.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RespuestaTrivia> call, Throwable t) {
                Toast.makeText(TeletriviaActivity.this, "Fallo al conectar: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                finish();
            }
        });
    }

    private void mostrarPregunta() {
        if (indicePreguntaActual < preguntas.size()) {
            Pregunta pregunta = preguntas.get(indicePreguntaActual);

            binding.tvPreguntaNumero.setText("Pregunta " + (indicePreguntaActual + 1) + "/" + cantidadPreguntas);
            binding.tvPreguntaTexto.setText(android.text.Html.fromHtml(pregunta.getQuestion()).toString());
            binding.rgOpciones.clearCheck();
        } else {
            irAEstadisticas();
        }
    }

    private void validarYAvanzar() {
        int idSeleccionado = binding.rgOpciones.getCheckedRadioButtonId();

        if (idSeleccionado == -1) {
            Toast.makeText(this, "Seleccione una opción", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radioButton = findViewById(idSeleccionado);
        String respuestaSeleccionada = radioButton.getText().toString().toLowerCase();

        Pregunta preguntaActual = preguntas.get(indicePreguntaActual);
        String respuestaCorrecta = preguntaActual.getCorrectAnswer().toLowerCase();

        if (respuestaSeleccionada.equals(respuestaCorrecta)) {
            respuestasCorrectas++;
        } else {
            respuestasIncorrectas++;
        }

        indicePreguntaActual++;
        mostrarPregunta();
    }

    private void irAEstadisticas() {
        Intent intent = new Intent(this, EstadisticasActivity.class);
        intent.putExtra("correctas", respuestasCorrectas);
        intent.putExtra("incorrectas", respuestasIncorrectas);
        intent.putExtra("totales", cantidadPreguntas);
        startActivity(intent);
        finish();
    }

    private int obtenerIdCategoria(String nombreCategoria) {
        switch (nombreCategoria) {
            case "Cultura General":
                return 9;
            case "Libros":
                return 10;
            case "Películas":
                return 11;
            case "Música":
                return 12;
            case "Computación":
                return 18;
            case "Matemática":
                return 19;
            case "Deportes":
                return 21;
            case "Historia":
                return 23;
            default:
                return 9;
        }
    }

    private String traducirDificultad(String dificultadEsp) {
        switch (dificultadEsp.toLowerCase()) {
            case "fácil":
                return "easy";
            case "medio":
                return "medium";
            case "difícil":
                return "hard";
            default:
                return "easy";
        }
    }
}
