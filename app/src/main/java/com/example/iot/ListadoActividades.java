package com.example.iot;

public class ListadoActividades {
    public String lugar;
    public String fecha;
    public String hora;
    public String tipoactividad;
    public String nombreactividad;
    public String codigo;
    public Integer cantidad;

    public ListadoActividades(){
        //Constructor sin argumentos
    }
    public ListadoActividades(String lugar, String fecha, String hora, String tipoactividad, String nombreactividad, String codigo, Integer cantidad) {
        this.lugar = lugar;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoactividad = tipoactividad;
        this.nombreactividad = nombreactividad;
        this.codigo = codigo;
        this.cantidad = cantidad;
    }

    public String getNombreactividad() {
        return nombreactividad;
    }

    public void setNombreactividad(String nombreactividad) {
        this.nombreactividad = nombreactividad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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

    public String getTipoactividad() {
        return tipoactividad;
    }

    public void setTipoactividad(String tipoactividad) {
        this.tipoactividad = tipoactividad;
    }
}
