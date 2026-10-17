package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {
    private static final double PROBABILIDAD_CRITICO = 0.15;
    private static final int EXPERIENCIA_BASE_NIVEL = 100;
    private static final int BONO_VIDA_NIVEL = 10;
    private static final int BONO_ATAQUE_NIVEL = 2;

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;
    private int experiencia;
    private int pociones;
    private int pocionesGrandes;
    private boolean ultimoGolpeCritico;

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima > 0 ? vidaMaxima : 1;
        this.puntosAtaque = Math.max(0, puntosAtaque);
        this.nivel = 1;
        this.oro = oro >= 0.0 ? oro : 0.0;
        this.experiencia = 0;
        this.pociones = 2;
        this.pocionesGrandes = 0;
        this.ultimoGolpeCritico = false;
        setPuntosVida(this.vidaMaxima);
    }

    public Personaje(String nombre) {
        this(nombre, 100, 10, 0.0);
    }

    public void atacar(Enemigo enemigo) {
        ultimoGolpeCritico = false;
        if (enemigo != null && estaVivo && enemigo.isEstaVivo()) {
            int dano = puntosAtaque;
            if (Math.random() < PROBABILIDAD_CRITICO) {
                dano *= 2;
                ultimoGolpeCritico = true;
            }
            enemigo.recibirDano(dano);
        }
    }

    public void usarPocion(int cantidadRestaurada) {
        if (estaVivo && cantidadRestaurada > 0) {
            setPuntosVida(puntosVida + cantidadRestaurada);
        }
    }

    public void recibirDano(int cantidadDano) {
        if (estaVivo && cantidadDano > 0) {
            setPuntosVida(puntosVida - cantidadDano);
        }
    }

    public boolean ganarExperiencia(int puntos) {
        if (!estaVivo || puntos <= 0) {
            return false;
        }
        experiencia += puntos;
        boolean subioNivel = false;
        while (experiencia >= getExperienciaParaSiguienteNivel()) {
            experiencia -= getExperienciaParaSiguienteNivel();
            setNivel(nivel + 1);
            vidaMaxima += BONO_VIDA_NIVEL;
            puntosAtaque += BONO_ATAQUE_NIVEL;
            subioNivel = true;
        }
        if (subioNivel) {
            setPuntosVida(vidaMaxima);
        }
        return subioNivel;
    }

    public boolean consumirPocion(boolean grande) {
        if (grande) {
            if (pocionesGrandes > 0) {
                pocionesGrandes--;
                return true;
            }
            return false;
        }
        if (pociones > 0) {
            pociones--;
            return true;
        }
        return false;
    }

    public void agregarPocionGrande() {
        pocionesGrandes++;
    }

    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > vidaMaxima) {
            this.puntosVida = vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    public boolean setOro(double oro) {
        if (oro < 0.0) {
            return false;
        }
        this.oro = oro;
        return true;
    }

    protected void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getExperienciaParaSiguienteNivel() {
        return nivel * EXPERIENCIA_BASE_NIVEL;
    }

    public int getPociones() {
        return pociones;
    }

    public int getPocionesGrandes() {
        return pocionesGrandes;
    }

    public boolean isUltimoGolpeCritico() {
        return ultimoGolpeCritico;
    }
}