package com.taller.poo;

public class Estudiante {
    private String identificacion;
    private String nombreCompleto;
    private double promedioCalificaciones;
    private boolean estaMatriculado;

    public Estudiante(String identificacion, String nombreCompleto, double promedioCalificaciones, boolean estaMatriculado) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.promedioCalificaciones = promedioCalificaciones;
        this.estaMatriculado = estaMatriculado;
    }

    public String obtenerReporteAcademicos() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("ID: %s", identificacion));
        reporte.append(String.format(" | Nombre: %s", nombreCompleto));
        reporte.append(String.format(" | Promedio: %.2f", promedioCalificaciones));
        reporte.append(String.format(" | Matriculado: %b", estaMatriculado));
        return reporte.toString();
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public double getPromedioCalificaciones() {
        return promedioCalificaciones;
    }

    public boolean isEstaMatriculado() {
        return estaMatriculado;
    }
}