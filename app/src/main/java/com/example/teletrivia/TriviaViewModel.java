package com.example.teletrivia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class TriviaViewModel extends ViewModel {

    private final MutableLiveData<Integer> tiempoRestante = new MutableLiveData<>();
    private Timer timer;

    public LiveData<Integer> getTiempoRestante() {
        return tiempoRestante;
    }

    public void iniciarContador(int segundosTotales) {
        tiempoRestante.setValue(segundosTotales);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Integer valorActual = tiempoRestante.getValue();
                if (valorActual != null) {
                    if (valorActual > 0) {
                        tiempoRestante.postValue(valorActual - 1);
                    } else {
                        timer.cancel();
                    }
                }
            }
        }, 1000, 1000); // inicial delay 1s, luego cada 1s
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null) {
            timer.cancel();
        }
    }
}
