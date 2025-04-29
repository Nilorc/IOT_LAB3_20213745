package com.example.teletrivia.model;

import java.util.List;

public class RespuestaTrivia {

    private int response_code;
    private List<Pregunta> results;

    // Getters
    public int getResponseCode() {
        return response_code;
    }

    public List<Pregunta> getResults() {
        return results;
    }
}
