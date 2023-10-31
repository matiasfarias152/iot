package com.example.iot;

public class listaintegrantes {

    public String nombreintegrante;

    public listaintegrantes() {

        //Constructor vacio firebase
    }

    public listaintegrantes(String nombreintegrante) {
        this.nombreintegrante = nombreintegrante;
    }

    public String getNombreintegrante() {
        return nombreintegrante;
    }

    public void setNombreintegrante(String nombreintegrante) {
        this.nombreintegrante = nombreintegrante;
    }
}
