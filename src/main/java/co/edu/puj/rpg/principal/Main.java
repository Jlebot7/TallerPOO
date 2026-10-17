package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

public class Main {
    private static final int CURA_POCION = 30;
    private static final int CURA_POCION_GRANDE = 80;
    private static final double COSTO_POCION_GRANDE = 40.0;
    private static final int EXPERIENCIA_POR_NIVEL_ENEMIGO = 60;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        Enemigo enemigo = new Enemigo("Orco Berserker", 80, 12, 2, 35.0);
        StringBuilder historial = new StringBuilder();
        boolean huyo = false;
        int ronda = 0;

        System.out.printf("=== ARENA DE HÉROES ===%n");
        System.out.printf("%s (Nivel %d) se enfrenta a %s (Nivel %d)%n",
                heroe.getNombre(), heroe.getNivel(), enemigo.getNombre(), enemigo.getNivel());

        do {
            boolean turnoConsumido = false;

            System.out.printf("%n--- Ronda %d ---%n", ronda + 1);
            System.out.printf("%s: %d/%d PV | %s: %d/%d PV%n",
                    heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                    enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
            System.out.printf("1. Atacar%n2. Usar Poción%n3. Ver Estado%n4. Huir%n5. Tienda%n");
            System.out.printf("Seleccione una opción: ");

            int opcion = leerEntero(scanner);

            if (opcion >= 1 && opcion <= 5) {
                switch (opcion) {
                    case 1:
                        int vidaEnemigoAntes = enemigo.getPuntosVida();
                        heroe.atacar(enemigo);
                        int danoInfligido = vidaEnemigoAntes - enemigo.getPuntosVida();
                        if (heroe.isUltimoGolpeCritico()) {
                            historial.append(String.format("¡GOLPE CRÍTICO! %s inflige %d de daño a %s.%n",
                                    heroe.getNombre(), danoInfligido, enemigo.getNombre()));
                        } else {
                            historial.append(String.format("%s inflige %d de daño a %s.%n",
                                    heroe.getNombre(), danoInfligido, enemigo.getNombre()));
                        }
                        if (!enemigo.isEstaVivo()) {
                            historial.append(String.format("%s ha sido derrotado.%n", enemigo.getNombre()));
                        }
                        turnoConsumido = true;
                        break;
                    case 2:
                        System.out.printf("Poción normal (+%d PV): %d disponibles%n", CURA_POCION, heroe.getPociones());
                        System.out.printf("Poción grande (+%d PV): %d disponibles%n", CURA_POCION_GRANDE, heroe.getPocionesGrandes());
                        System.out.printf("Tipo de poción (1 normal, 2 grande): ");
                        int tipo = leerEntero(scanner);
                        if (tipo == 1 || tipo == 2) {
                            boolean grande = tipo == 2;
                            if (heroe.consumirPocion(grande)) {
                                int vidaAntes = heroe.getPuntosVida();
                                heroe.usarPocion(grande ? CURA_POCION_GRANDE : CURA_POCION);
                                historial.append(String.format("%s usa una poción %s y recupera %d puntos de vida.%n",
                                        heroe.getNombre(), grande ? "grande" : "normal",
                                        heroe.getPuntosVida() - vidaAntes));
                                turnoConsumido = true;
                            } else {
                                System.out.printf("No te quedan pociones de ese tipo.%n");
                            }
                        } else {
                            System.out.printf("Tipo de poción no válido.%n");
                        }
                        break;
                    case 3:
                        mostrarEstado(heroe, enemigo);
                        break;
                    case 4:
                        huyo = true;
                        historial.append(String.format("%s huye del combate.%n", heroe.getNombre()));
                        break;
                    case 5:
                        abrirTienda(scanner, heroe, historial);
                        break;
                }
            } else {
                System.out.printf("Opción no válida. Ingrese un número entre 1 y 5.%n");
            }

            if (turnoConsumido && heroe.isEstaVivo() && enemigo.isEstaVivo()) {
                int vidaHeroeAntes = heroe.getPuntosVida();
                heroe.recibirDano(enemigo.getPuntosAtaque());
                historial.append(String.format("%s ataca a %s e inflige %d de daño.%n",
                        enemigo.getNombre(), heroe.getNombre(), vidaHeroeAntes - heroe.getPuntosVida()));
                if (!heroe.isEstaVivo()) {
                    historial.append(String.format("%s ha caído en combate.%n", heroe.getNombre()));
                }
            }

            if (turnoConsumido) {
                ronda++;
                System.out.printf("%n=== HISTORIAL DE BATALLA (Ronda %d) ===%n", ronda);
                System.out.printf("%s", historial.toString());
            }
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        System.out.printf("%n=== FIN DEL COMBATE ===%n");
        if (!heroe.isEstaVivo()) {
            System.out.printf("%s ha sido derrotado por %s.%n", heroe.getNombre(), enemigo.getNombre());
        } else if (!enemigo.isEstaVivo()) {
            heroe.setOro(heroe.getOro() + enemigo.getOro());
            int experienciaGanada = enemigo.getNivel() * EXPERIENCIA_POR_NIVEL_ENEMIGO;
            boolean subioNivel = heroe.ganarExperiencia(experienciaGanada);
            System.out.printf("¡Victoria! Recompensa: %.2f de oro y %d de experiencia.%n",
                    enemigo.getOro(), experienciaGanada);
            if (subioNivel) {
                System.out.printf("¡%s sube al nivel %d!%n", heroe.getNombre(), heroe.getNivel());
            }
            mostrarEstado(heroe, enemigo);
        } else {
            System.out.printf("%s abandonó la arena.%n", heroe.getNombre());
        }

        scanner.close();
    }

    private static int leerEntero(Scanner scanner) {
        int valor = -1;
        if (scanner.hasNextInt()) {
            valor = scanner.nextInt();
        }
        scanner.nextLine();
        return valor;
    }

    private static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        System.out.printf("%n=== ESTADO ===%n");
        System.out.printf("Héroe: %s | Nivel: %d | Vida: %d/%d | Ataque: %d | Oro: %.2f | Vivo: %b%n",
                heroe.getNombre(), heroe.getNivel(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                heroe.getPuntosAtaque(), heroe.getOro(), heroe.isEstaVivo());
        System.out.printf("Experiencia: %d/%d | Pociones: %d | Pociones grandes: %d%n",
                heroe.getExperiencia(), heroe.getExperienciaParaSiguienteNivel(),
                heroe.getPociones(), heroe.getPocionesGrandes());
        System.out.printf("Enemigo: %s | Nivel: %d | Vida: %d/%d | Ataque: %d | Vivo: %b%n",
                enemigo.getNombre(), enemigo.getNivel(), enemigo.getPuntosVida(), enemigo.getVidaMaxima(),
                enemigo.getPuntosAtaque(), enemigo.isEstaVivo());
    }

    private static void abrirTienda(Scanner scanner, Personaje heroe, StringBuilder historial) {
        System.out.printf("%n=== TIENDA ===%n");
        System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
        System.out.printf("1. Poción Grande de Salud (+%d PV) - %.2f de oro%n", CURA_POCION_GRANDE, COSTO_POCION_GRANDE);
        System.out.printf("2. Volver%n");
        System.out.printf("Seleccione una opción: ");

        int opcion = leerEntero(scanner);
        if (opcion == 1) {
            double nuevoSaldo = heroe.getOro() - COSTO_POCION_GRANDE;
            if (heroe.setOro(nuevoSaldo)) {
                heroe.agregarPocionGrande();
                System.out.printf("Compra exitosa. Saldo restante: %.2f%n", heroe.getOro());
                historial.append(String.format("%s compra una Poción Grande de Salud por %.2f de oro.%n",
                        heroe.getNombre(), COSTO_POCION_GRANDE));
            } else {
                System.out.printf("Saldo insuficiente: se requieren %.2f y solo tienes %.2f.%n",
                        COSTO_POCION_GRANDE, heroe.getOro());
                historial.append(String.format("%s intentó comprar una Poción Grande sin saldo suficiente.%n",
                        heroe.getNombre()));
            }
        } else if (opcion == 2) {
            System.out.printf("Saliendo de la tienda.%n");
        } else {
            System.out.printf("Opción de tienda no válida.%n");
        }
    }
}