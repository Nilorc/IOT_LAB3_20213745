package com.example.teletrivia.network;

import com.example.teletrivia.model.RespuestaTrivia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api.php")
    Call<RespuestaTrivia> obtenerPreguntas(
            @Query("amount") int cantidad,
            @Query("category") int idCategoria,
            @Query("difficulty") String dificultad,
            @Query("type") String tipo
    );
}
