package com.taller.poo;

public class CuentaBancaria {
    private String numeroCuenta;
    private String titular;
    private double saldo;
    private Double limiteCredito;

    public CuentaBancaria() {
        this("000000", "Sin Nombre", 0.0, 0.0);
    }

    public CuentaBancaria(String numeroCuenta, String titular) {
        this(numeroCuenta, titular, 0.0, 100.0);
    }

    public CuentaBancaria(String numeroCuenta, String titular, double saldo, Double limiteCredito) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldo;
        this.limiteCredito = limiteCredito;
    }

    public boolean depositar(double monto) {
        if (monto > 0) {
            saldo += monto;
            return true;
        } else {
            return false;
        }
    }

    public boolean retirar(double monto) {
        if (monto > 0) {
            double disponible = saldo;
            if (limiteCredito != null) {
                disponible += limiteCredito;
            }
            if (disponible >= monto) {
                saldo -= monto;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }
}