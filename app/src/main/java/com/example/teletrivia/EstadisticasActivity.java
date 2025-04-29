package com.example.teletrivia;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teletrivia.databinding.ActivityEstadisticasBinding;

public class EstadisticasActivity extends AppCompatActivity {

    private ActivityEstadisticasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstadisticasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recibir los datos desde TeletriviaActivity
        int correctas = getIntent().getIntExtra("correctas", 0);
        int incorrectas = getIntent().getIntExtra("incorrectas", 0);
        int totales = getIntent().getIntExtra("totales", 0);

        int noRespondidas = totales - (correctas + incorrectas);

        // Mostrar resultados
        binding.tvCorrectas.setText("Correctas: " + correctas);
        binding.tvIncorrectas.setText("Incorrectas: " + incorrectas);
        binding.tvNoRespondidas.setText("No respondidas: " + noRespondidas);

        // Acción botón volver
        binding.btnVolverInicio.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
