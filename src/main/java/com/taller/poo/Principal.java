package com.taller.poo;

public class Principal {

    public static void main(String[] args) {
        Vehiculo vehiculo1 = new Vehiculo("Toyota", "Corolla", 15000.50, 180, true);
        Vehiculo vehiculo2 = new Vehiculo();
        Vehiculo vehiculo3 = new Vehiculo("Mazda", "CX-5", 42000.75, 200, false);
        Vehiculo[] vehiculos = {vehiculo1, vehiculo2, vehiculo3};

        Estudiante estudiante1 = new Estudiante("E001", "Laura Martínez", 4.35, true);
        Estudiante estudiante2 = new Estudiante("E002", "Andrés Rojas", 3.80, true);
        Estudiante estudiante3 = new Estudiante("E003", "Camila Torres", 2.95, false);
        Estudiante[] estudiantes = {estudiante1, estudiante2, estudiante3};

        CuentaBancaria cuenta1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.0);
        CuentaBancaria cuenta2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria cuenta3 = new CuentaBancaria();
        CuentaBancaria[] cuentas = {cuenta1, cuenta2, cuenta3};

        boolean deposito1 = cuenta1.depositar(250.75);
        boolean deposito2 = cuenta2.depositar(100.00);
        boolean retiro2 = cuenta2.retirar(50.00);
        boolean deposito3 = cuenta3.depositar(-20.00);
        boolean retiro3 = cuenta3.retirar(300.00);

        System.out.printf("=== REPORTE DE VEHÍCULOS ===%n");
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo v = vehiculos[i];
            System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                    i + 1, v.getMarca(), v.getModelo(), v.getKilometraje(), v.getVelocidadMaxima(), v.isEncendido());
        }

        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        for (int i = 0; i < estudiantes.length; i++) {
            System.out.printf("[%d] %s%n", i + 1, estudiantes[i].obtenerReporteAcademicos());
        }

        System.out.printf("%n=== OPERACIONES BANCARIAS ===%n");
        System.out.printf("Depósito en 1001: %b%n", deposito1);
        System.out.printf("Depósito en 1002: %b%n", deposito2);
        System.out.printf("Retiro en 1002: %b%n", retiro2);
        System.out.printf("Depósito inválido en 000000: %b%n", deposito3);
        System.out.printf("Retiro excedido en 000000: %b%n", retiro3);

        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        for (CuentaBancaria c : cuentas) {
            System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: %s | Crédito: %s%n",
                    c.getNumeroCuenta(), c.getTitular(),
                    ConversorMoneda.formatearMoneda(c.getSaldo()),
                    ConversorMoneda.formatearMoneda(c.getLimiteCredito()));
        }

        System.out.printf("%n=== CONVERSOR DE MONEDA ===%n");
        double euros = ConversorMoneda.convertirDolaresAEuros(250.00, 0.92);
        double eurosInvalido = ConversorMoneda.convertirDolaresAEuros(-10.00, 0.92);
        System.out.printf("250.00 USD a EUR: %s%n", ConversorMoneda.formatearMoneda(euros));
        System.out.printf("Conversión inválida: %s%n", ConversorMoneda.formatearMoneda(eurosInvalido));
    }
}