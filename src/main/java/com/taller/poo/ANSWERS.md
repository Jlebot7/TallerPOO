# Taller POO en Java: respuestas analíticas

Código fuente en `TallerPOO/src/com/taller/poo/`: `Vehiculo`, `Estudiante`, `CuentaBancaria`, `ConversorMoneda`, `Principal` y `SistemaGestionCuentas`. El código no lleva comentarios, por lo que las justificaciones y respuestas que el taller pedía dentro de comentarios están aquí.

## Sección 1

### 1.1 Vehiculo: justificación de tipos

| Atributo | Tipo elegido | Justificación |
|---|---|---|
| `kilometraje` | `double` | Siempre tiene un valor (0.0 en un vehículo nuevo), se opera aritméticamente al acumular recorridos y no requiere representar "dato desconocido". |
| `velocidadMaxima` | `int` | Es un entero positivo pequeño, siempre definido por el fabricante. El primitivo ocupa 4 bytes y no genera objetos en el heap. |
| `encendido` | `boolean` | Solo tiene dos estados reales (encendido o apagado). Un `Boolean` añadiría un tercer estado (`null`) sin significado de negocio. |

**Impacto de usar Wrappers en lugar de primitivos (contenido del comentario de bloque solicitado):**

- **Memoria:** un primitivo se guarda directamente dentro del objeto (4 bytes un `int`, 8 bytes un `double`). Un Wrapper es una referencia (4 u 8 bytes) hacia un objeto en el heap con su propia cabecera (unos 16 bytes en total para un `Integer` o `Double`), más trabajo para el recolector de basura.
- **Valores nulos:** un Wrapper admite `null`, útil para representar "sin dato", pero obliga a validar antes de operar. Un primitivo nunca es nulo y toma un valor por defecto (`0`, `0.0`, `false`).
- **Rendimiento:** operar con Wrappers implica autoboxing y unboxing, más lento que el primitivo.

### 1.3 CuentaBancaria: preguntas de análisis

**1. ¿Qué diferencia ocurre a nivel de memoria y asignación por defecto entre `saldo` (`double`) y `limiteCredito` (`Double`)?**

`saldo` guarda el valor directamente en el objeto, ocupa 8 bytes y por defecto vale `0.0`. `limiteCredito` es una referencia a un objeto `Double` en el heap. Mientras no se le asigne un valor, la referencia vale `null` y no existe ningún objeto `Double`. Cuando se asigna, se crea el objeto (cabecera más el valor), con mayor consumo de memoria.

**2. ¿Por qué una operación aritmética sobre un `Double` no inicializado lanza `NullPointerException`, mientras que sobre un `double` opera con `0.0`?**

Al operar sobre un `Double`, Java aplica unboxing de forma implícita y llama a `limiteCredito.doubleValue()`. Si la referencia es `null`, no hay objeto sobre el cual invocar el método y se lanza `NullPointerException`. El `double` primitivo no es una referencia: siempre contiene un valor (por defecto `0.0`), así que la operación se resuelve sin objetos de por medio. Por eso `retirar()` comprueba `limiteCredito != null` antes de sumarlo.

## Sección 6: Depuración

### Caso 1: referencia nula

**1.** El compilador reporta `variable estudiante1 might not have been initialized`. Como es una variable local, el error ocurre en compilación. Si se hubiera escrito `Estudiante estudiante1 = null;`, el código compilaría y en ejecución lanzaría `java.lang.NullPointerException`.

**2.** `estudiante1` es solo una referencia, y todavía no apunta a ningún objeto. Un método de instancia necesita un objeto concreto con sus atributos (`this`) para ejecutarse, y ese objeto no existe hasta usar `new`.

**3.**

```java
Estudiante estudiante1 = new Estudiante("E001", "Laura Martínez", 4.35, true);
```

### Caso 2: contexto estático e instancia

**1.** `non-static method encenderMotor() cannot be referenced from a static context`.

**2.** Un método `static` pertenece a la clase y se ejecuta sin ningún objeto, por lo que no tiene `this`. Un método de instancia puede leer o modificar el estado de un objeto concreto. Al llamarlo desde `main` no hay objeto que le sirva de contexto, y el compilador lo impide.

**3.** Dos alternativas:

Alternativa A: declarar el método como estático.

```java
public static void encenderMotor() {
    System.out.println("El motor del vehículo ha sido encendido.");
}
```

Alternativa B: crear una instancia y llamar al método sobre ella.

```java
public static void main(String[] args) {
    GestorVehiculos gestor = new GestorVehiculos();
    gestor.encenderMotor();
}
```

### Caso 3: firma incorrecta del constructor

**1.** Un constructor se distingue porque tiene el mismo nombre que la clase y no declara tipo de retorno. Al escribir `void`, Java lo interpreta como un método ordinario que, por casualidad, se llama igual que la clase. Ya no es un constructor y no se ejecuta al usar `new`.

**2.** Al no haber ningún constructor real, el compilador genera el constructor por defecto sin parámetros. La llamada `new CuentaBancaria("A-101", 500.0)` no coincide con esa firma y el compilador indica que el constructor no puede aplicarse a los tipos dados (requiere: sin argumentos; encontrado: `String, double`).

**3.**

```java
public CuentaBancaria(String numeroCuenta, double saldoInicial) {
    this.numeroCuenta = numeroCuenta;
    this.saldo = saldoInicial;
}
```

### Caso 4: sombreado de variables

**1.** Se imprime:

```
Velocidad Máxima: 0 | Encendido: false
```

Dentro del constructor, `velocidadMaxima` y `encendido` se refieren a los parámetros, no a los atributos. Las asignaciones `velocidadMaxima = velocidadMaxima;` y `encendido = encendido;` copian cada parámetro sobre sí mismo. Los atributos quedan con su valor por defecto (`0` para `int`, `false` para `boolean`) y `imprimirEstado()` muestra esos valores.

**2.** El sombreado ocurre cuando una variable local o un parámetro tiene el mismo nombre que un atributo. Dentro de ese ámbito, el nombre simple se resuelve siempre hacia la variable más cercana (el parámetro) y el atributo queda oculto. La asignación no modifica el estado del objeto y el compilador no marca error. Para acceder al atributo se usa `this.`, que referencia el objeto actual.

**3.**

```java
public Vehiculo(int velocidadMaxima, boolean encendido) {
    this.velocidadMaxima = velocidadMaxima;
    this.encendido = encendido;
}
```

## Notas de ejecución

- `Scanner` en `SistemaGestionCuentas` usa `Locale.US`, por lo que los montos decimales se escriben con punto (`100.50`).
- `String.format` y `printf` usan el formato regional del sistema. Con configuración regional de Colombia, los decimales pueden mostrarse con coma. Para forzar el punto, se usa `Locale.US` como primer argumento.