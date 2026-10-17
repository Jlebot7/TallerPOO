# Taller Encapsulamiento: Simulador de Batalla RPG

Código fuente en `TallerRPG/src/co/edu/puj/rpg/`: `modelo/Personaje.java`, `modelo/enemigo/Enemigo.java` y `principal/Main.java`. El código no lleva comentarios, por lo que las explicaciones están en este documento.

## Decisiones de diseño

- **Segundo constructor:** el documento pide dos constructores pero solo especifica el completo `Personaje(String, int, int, double)`. El segundo, `Personaje(String nombre)`, delega con `this(...)` en el completo usando valores por defecto (100 de vida máxima, 10 de ataque, 0.0 de oro).
- **Los constructores usan el setter:** `puntosVida` nunca se asigna directamente, sino con `setPuntosVida(vidaMaxima)`. Así `estaVivo` queda siempre coherente (ver Caso B).
- **`setOro` devuelve `boolean`:** la tienda necesita saber si la operación fue rechazada. Devuelve `false` y no altera el atributo si el monto sería negativo.
- **`setNivel` es `protected`:** el taller lo pide así en el Reto 2. `Main` está en otro paquete y no es subclase de `Personaje`, por lo que no puede invocarlo. La subida de nivel ocurre dentro de `Personaje`, en `ganarExperiencia()`, y `Main` solo llama a ese método público.
- **Visibilidad entre paquetes:** `Personaje` (paquete `modelo`) importa `Enemigo` (paquete `modelo.enemigo`). Como están en paquetes distintos, `Personaje` solo puede usar los miembros `public` de `Enemigo`.
- **Atributos adicionales de `Personaje`:** `experiencia`, `pociones`, `pocionesGrandes` y `ultimoGolpeCritico`, todos privados. Los pide el inventario de la tienda, el sistema de experiencia y el registro de golpes críticos en el historial. `atacar()` conserva la firma `void` de la plantilla.
- **Menú:** además de las cuatro opciones obligatorias, se añadió la opción 5 (Tienda) para el Reto 1. La tienda y "Ver Estado" no consumen turno.
- **Condición del `do-while`:** evalúa que `Personaje` y `Enemigo` sigan vivos, y añade `!huyo` para que la opción "Huir" termine el bucle.
- **Experiencia:** el umbral para subir de nivel es `nivel * 100`. Derrotar al enemigo (nivel 2) da 120 puntos, así que el héroe sube a nivel 2. Al subir, gana +10 de vida máxima, +2 de ataque y recupera toda la vida.
- **Golpe crítico:** `Math.random() < 0.15` duplica el daño de `atacar()`.

## Sección 5: Depuración

### Caso A: acceso directo a un atributo privado

**Error del compilador:** `puntosVida has private access in co.edu.puj.rpg.modelo.Personaje` (aparece en las dos líneas que usan `heroe.puntosVida`).

**Causa raíz:** `puntosVida` es `private`, así que solo se puede usar dentro de `Personaje`. `Main` está en otro paquete y no tiene ningún acceso a ese atributo.

**Corrección:** pasar por la API pública.

```java
Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
heroe.setPuntosVida(200);
System.out.println("Vida: " + heroe.getPuntosVida());
```

Imprime `Vida: 100`, porque el setter limita el valor a `vidaMaxima`. Además, `Main` necesita `import co.edu.puj.rpg.modelo.Personaje;` y `Personaje` debe ser `public`.

### Caso B: constructor que se salta las validaciones

**Causa raíz:** el constructor asigna `puntosVida = -50` y `estaVivo = true` directamente. Quedan una vida negativa y un personaje "vivo", una combinación que las reglas de negocio no permiten. Solo el setter conoce y aplica esas reglas.

**Corrección:**

```java
public class Personaje {
    private int puntosVida;
    private int vidaMaxima;
    private boolean estaVivo;

    public Personaje(int vidaMaxima, int puntosVida) {
        this.vidaMaxima = vidaMaxima > 0 ? vidaMaxima : 1;
        setPuntosVida(puntosVida);
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
}
```

Con `-50`, el resultado es `puntosVida = 0` y `estaVivo = false`. Primero se asigna `vidaMaxima` porque `setPuntosVida` la usa como tope.

### Caso C: fuga de estado y asignación fuera de límites

**Qué pasa:** el código compila, porque `setPuntosVida` es público. Lo que decide si el estado queda coherente es la lógica del setter.

| Llamada | Sin validación | Con el setter validado |
|---|---|---|
| `setPuntosVida(999999)` | `puntosVida = 999999`, supera `vidaMaxima` | `puntosVida = vidaMaxima` (100) |
| `setPuntosVida(-100)` | `puntosVida = -100` y `estaVivo` sigue en `true` | `puntosVida = 0` y `estaVivo = false` |

**Corrección:** implementar el setter con el mismo bloque `if / else if / else` mostrado en el Caso B. El setter es el único punto de escritura de `puntosVida` y `estaVivo`.

## 6.2 Guía de verificación en IntelliJ IDEA

1. **Autocompletado de `heroe.` en `Main`:** muestra solo los miembros `public`: `atacar`, `usarPocion`, `recibirDano`, `ganarExperiencia`, `consumirPocion`, `agregarPocionGrande`, `setPuntosVida`, `setOro` y todos los getters. No aparece ningún campo (`nombre`, `puntosVida`, etc.), ni `setNivel`, que es `protected` y desde `principal` no es visible.
2. **Acceso directo a un miembro privado:** IntelliJ subraya la línea en rojo con `'puntosVida' has private access in 'co.edu.puj.rpg.modelo.Personaje'`.
3. **Valores límite** (héroe con `vidaMaxima = 100`):

| Setter | `-10` | `0` | `100000` |
|---|---|---|---|
| `setPuntosVida` | vida 0, `estaVivo = false` | vida 0, `estaVivo = false` | vida 100, `estaVivo = true` |
| `setOro` | rechazado (`false`), oro sin cambios | aceptado, oro 0.0 | aceptado, oro 100000.0 |
| `setNivel` (desde dentro de `Personaje`) | se conserva el nivel actual | se conserva el nivel actual | se asigna 100000, sin tope máximo en la especificación |

Con cualquiera de estos valores, `puntosVida` queda entre 0 y `vidaMaxima` y `estaVivo` coincide con `puntosVida > 0`.