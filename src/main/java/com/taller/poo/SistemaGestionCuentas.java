package com.taller.poo;

import java.util.Locale;
import java.util.Scanner;

public class SistemaGestionCuentas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.0);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Luis Herrera", 300.00, 200.0);

        int opcion;
        do {
            System.out.printf("%n===== SISTEMA DE GESTIÓN DE CUENTAS =====%n");
            System.out.printf("1. Depositar%n");
            System.out.printf("2. Retirar%n");
            System.out.printf("3. Ver reporte consolidado%n");
            System.out.printf("4. Salir%n");
            System.out.printf("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                    System.out.printf("Número de cuenta: ");
                    String numeroDeposito = scanner.nextLine().trim();
                    CuentaBancaria cuentaDeposito = buscarCuenta(cuentas, numeroDeposito);
                    if (cuentaDeposito == null) {
                        System.out.printf("Error: la cuenta %s no existe.%n", numeroDeposito);
                    } else {
                        double montoDeposito = leerMonto(scanner, "Monto a depositar: ");
                        if (montoDeposito <= 0) {
                            System.out.printf("Error: el monto debe ser estrictamente mayor a cero.%n");
                        } else if (cuentaDeposito.depositar(montoDeposito)) {
                            System.out.printf("Depósito exitoso. Nuevo saldo: %.2f%n", cuentaDeposito.getSaldo());
                        } else {
                            System.out.printf("No fue posible realizar el depósito.%n");
                        }
                    }
                    break;
                case 2:
                    System.out.printf("Número de cuenta: ");
                    String numeroRetiro = scanner.nextLine().trim();
                    CuentaBancaria cuentaRetiro = buscarCuenta(cuentas, numeroRetiro);
                    if (cuentaRetiro == null) {
                        System.out.printf("Error: la cuenta %s no existe.%n", numeroRetiro);
                    } else {
                        double montoRetiro = leerMonto(scanner, "Monto a retirar: ");
                        if (montoRetiro <= 0) {
                            System.out.printf("Error: el monto debe ser estrictamente mayor a cero.%n");
                        } else if (cuentaRetiro.retirar(montoRetiro)) {
                            System.out.printf("Retiro exitoso. Nuevo saldo: %.2f%n", cuentaRetiro.getSaldo());
                        } else {
                            System.out.printf("Fondos insuficientes para retirar %.2f%n", montoRetiro);
                        }
                    }
                    break;
                case 3:
                    System.out.printf("%s", construirReporte(cuentas));
                    break;
                case 4:
                    System.out.printf("Sistema finalizado.%n");
                    break;
                default:
                    System.out.printf("Opción %d no válida. Intente de nuevo.%n", opcion);
                    break;
            }
        } while (opcion != 4);

        scanner.close();
    }

    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numeroCuenta) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return cuentas[i];
            }
        }
        return null;
    }

    private static double leerMonto(Scanner scanner, String mensaje) {
        System.out.printf("%s", mensaje);
        if (scanner.hasNextDouble()) {
            double monto = scanner.nextDouble();
            scanner.nextLine();
            return monto;
        }
        scanner.nextLine();
        return -1;
    }

    private static String construirReporte(CuentaBancaria[] cuentas) {
        StringBuilder reporte = new StringBuilder();
        double saldoTotal = 0.0;
        reporte.append(String.format("%n=== REPORTE CONSOLIDADO ===%n"));
        for (int i = 0; i < cuentas.length; i++) {
            CuentaBancaria c = cuentas[i];
            reporte.append(String.format("[%d] Cuenta: %s | Titular: %s | Saldo: %.2f | Crédito: %.2f | Sobregirada: %b%n",
                    i + 1, c.getNumeroCuenta(), c.getTitular(), c.getSaldo(), c.getLimiteCredito(), c.getSaldo() < 0));
            saldoTotal += c.getSaldo();
        }
        reporte.append(String.format("Cuentas registradas: %d%n", cuentas.length));
        reporte.append(String.format("Saldo total acumulado: %.2f%n", saldoTotal));
        return reporte.toString();
    }
}
