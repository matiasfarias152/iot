package com.example.iot;

public class ListComunidades {
    public String nombrecomunidad;
    public String tipoactividadcomunidad;
    public String descripcioncomunidad;


    public ListComunidades(){
        //Constructor sin argumentos
    }
    public ListComunidades(String nombrecomunidad, String tipoactividadcomunidad, String descripcioncomunidad) {
        this.nombrecomunidad = nombrecomunidad;
        this.tipoactividadcomunidad = tipoactividadcomunidad;
        this.descripcioncomunidad = descripcioncomunidad;
    }

    public String getNombrecomunidad() {
        return nombrecomunidad;
    }

    public void setNombrecomunidad(String nombrecomunidad) {
        this.nombrecomunidad = nombrecomunidad;
    }

    public String getTipoactividadcomunidad() {
        return tipoactividadcomunidad;
    }

    public void setTipoactividadcomunidad(String tipoactividadcomunidad) {
        this.tipoactividadcomunidad = tipoactividadcomunidad;
    }

    public String getDescripcioncomunidad() {
        return descripcioncomunidad;
    }

    public void setDescripcioncomunidad(String descripcioncomunidad) {
        this.descripcioncomunidad = descripcioncomunidad;
    }
}
