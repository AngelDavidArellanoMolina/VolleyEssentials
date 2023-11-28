package com.example.volleyessentials;

public class Jugador {
    private String nombre;
    private int dorsal;
    private Rol posicion;

    public Jugador(String nombre, int dorsal, Rol posicion) {
        this.nombre = nombre;
        this.dorsal = dorsal;
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public Rol getPosicion() {
        return posicion;
    }

    public void setPosicion(Rol posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", dorsal=" + dorsal +
                ", posicion=" + posicion +
                '}';
    }
}