package com.example.iot;

public class ListadoActividades {
    public String lugar;
    public String fecha;
    public String hora;
    public String tipo;
    public String nombreactividad;

    public ListadoActividades(){
        //Constructor sin argumentos
    }
    public ListadoActividades(String lugar, String fecha, String hora, String tipo, String nombreactividad) {
        this.lugar = lugar;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.nombreactividad = nombreactividad;
    }

    public String getNombreactividad() {
        return nombreactividad;
    }

    public void setNombreactividad(String nombreactividad) {
        this.nombreactividad = nombreactividad;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
