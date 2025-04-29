package com.example.teletrivia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.teletrivia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean conexionVerificada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar botón "Comenzar"
        binding.btnComenzar.setEnabled(false);
        binding.btnComenzar.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));

        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_array,
                android.R.layout.simple_spinner_item
        );
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapterCategoria);

        ArrayAdapter<CharSequence> adapterDificultad = ArrayAdapter.createFromResource(
                this,
                R.array.dificultad_array,
                android.R.layout.simple_spinner_item
        );
        adapterDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDificultad.setAdapter(adapterDificultad);

        // Acción Botón "Comprobar Conexión"
        binding.btnComprobarConexion.setOnClickListener(view -> {
            if (hayConexionInternet()) {
                Toast.makeText(this, "¡Conexión exitosa!", Toast.LENGTH_SHORT).show();
                binding.btnComenzar.setEnabled(true);
                binding.btnComenzar.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorBotonPrimario)); // Azul
                conexionVerificada = true;
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                binding.btnComenzar.setEnabled(false);
                binding.btnComenzar.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray)); // Gris
            }
        });

        // Acción Botón "Comenzar"
        binding.btnComenzar.setOnClickListener(view -> {
            if (!conexionVerificada) {
                Toast.makeText(this, "Primero verifica tu conexión", Toast.LENGTH_SHORT).show();
                return;
            }

            String categoria = binding.spinnerCategoria.getSelectedItem().toString();
            String dificultad = binding.spinnerDificultad.getSelectedItem().toString();
            String cantidadStr = binding.etCantidad.getText().toString();

            if (categoria.isEmpty() || dificultad.isEmpty() || cantidadStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cantidad <= 0) {
                Toast.makeText(this, "La cantidad debe ser positiva", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pasar datos a la siguiente Activity
            Intent intent = new Intent(this, TeletriviaActivity.class);
            intent.putExtra("categoria", categoria);
            intent.putExtra("dificultad", dificultad);
            intent.putExtra("cantidad", cantidad);
            startActivity(intent);
        });
    }

    private boolean hayConexionInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
