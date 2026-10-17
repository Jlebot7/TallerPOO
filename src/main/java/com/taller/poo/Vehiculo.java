package com.taller.poo;

public class Vehiculo {
    private String marca;
    private String modelo;
    private double kilometraje;
    private int velocidadMaxima;
    private boolean encendido;

    public Vehiculo() {
        this("Genérico", "Sin Modelo", 0.0, 120, false);
    }

    public Vehiculo(String marca, String modelo, double kilometraje, int velocidadMaxima, boolean encendido) {
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getKilometraje() {
        return kilometraje;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    public boolean isEncendido() {
        return encendido;
    }
}