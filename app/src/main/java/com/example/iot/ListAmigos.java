package com.example.iot;

public class ListAmigos {
    public String nombre;
    public String estado;

    public ListAmigos(){

    }

    public ListAmigos(String nombre, String estado){
        this.nombre = nombre;
        this.estado = estado;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

