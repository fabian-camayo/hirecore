# Hirecore

Aplicación Spring Boot que modela el cambio de estado de un candidato con los patrones **Command**, **State** y **Observer**, más **Dependency Inversion** para publicar eventos.

Al arrancar, corre una demostración en consola que recorre ese flujo de punta a punta.

## Integrantes

- Alexander Marin Villa
- Leidy Melissa Trejos Pamplona
- Sebastian Vargas Guarin
- Fabian Andres Muñoz Camayo

## Cómo ejecutar la demostración

```bash
./mvnw spring-boot:run
```

La clase `DemostracionArquitectura` se ejecuta sola al iniciar (es un `CommandLineRunner`). Queda activa por defecto; para apagarla:

```bash
./mvnw spring-boot:run -Dhirecore.demo.enabled=false
```

O en `src/main/resources/application.properties`:

```properties
hirecore.demo.enabled=false
```

## Qué hace el ejemplo

Simula a Sofía, una reclutadora, moviendo al candidato `c-001` por el proceso de selección. **No cambia el objeto `Candidato` a mano.** Cada movimiento se convierte en un `CambiarEstadoCommand` y se le entrega al `EjecutorComandos`, que es el único punto del sistema que sabe correr comandos.

El ejecutor hace tres cosas, siempre en este orden:

1. Ejecuta el comando (el estado actual decide si la transición es válida).
2. Lo guarda en el historial, por si hay que deshacerlo.
3. Publica un evento a través de `PublicarEventos` (una interfaz, no el bus concreto).

`BusEventos` implementa esa interfaz, mantiene la lista de interesados y les reparte el evento. Cada observador reacciona por su cuenta:

| Observador | Cuándo reacciona |
|---|---|
| `NotificarReclutador` | En todo cambio o deshacer |
| `ActualizarPortalCandidato` | En todo cambio o deshacer |
| `NotificarGerente` | Solo si el nuevo estado es `OFERTA` o `CONTRATADO` |
| `NotificacionNomina` | Solo si el nuevo estado es `CONTRATADO` |

## Recorrido paso a paso

El candidato empieza en **APLICADO**. El autor de todos los cambios es `reclutador-sofia`.

### 1. Aplicado → Entrevista (cambio válido)

Se crea el comando y el ejecutor lo corre. `EstadoAplicado` sí permite pasar a entrevista.

En el log aparecen el reclutador y el portal. El gerente y nómina no dicen nada: ese cambio no les importa.

```
Candidato inicial: Candidato{id='c-001', estado=APLICADO}
[Portal] Publicar estado ENTREVISTA para el candidato c-001
[Reclutador] Candidato c-001 pasó de APLICADO a ENTREVISTA (autor: reclutador-sofia)
Estado actual: Candidato{id='c-001', estado=ENTREVISTA}
```

### 2. Entrevista → Contratado (salto inválido)

El ejemplo intenta saltarse el proceso. La validación **no** la hace el comando ni el candidato: la hace `EstadoEntrevista`, que no permite ir directo a contratado.

El comando falla, no se guarda en el historial y no se publica evento. El candidato se queda en entrevista.

```
Transición rechazada por el estado: No se puede transicionar de 'ENTREVISTA' a 'CONTRATADO'
```

### 3. Entrevista → Prueba técnica

Cambio válido. Otra vez solo avisan reclutador y portal.

```
[Portal] Publicar estado PRUEBA_TECNICA para el candidato c-001
[Reclutador] Candidato c-001 pasó de ENTREVISTA a PRUEBA_TECNICA (autor: reclutador-sofia)
```

### 4. Prueba técnica → Oferta

Aquí entra el gerente: una oferta es un hito que debe revisar. Nómina sigue en silencio.

```
[Portal] Publicar estado OFERTA para el candidato c-001
[Gerente] Candidato c-001 está en OFERTA y requiere revisión
[Reclutador] Candidato c-001 pasó de PRUEBA_TECNICA a OFERTA (autor: reclutador-sofia)
```

### 5. Oferta → Contratado

Nómina se suma para iniciar contrato y pagos. El gerente también se entera.

```
[Portal] Publicar estado CONTRATADO para el candidato c-001
[Nómina] Alta de c-001 para iniciar contrato y pagos
[Gerente] Candidato c-001 está en CONTRATADO y requiere revisión
[Reclutador] Candidato c-001 pasó de OFERTA a CONTRATADO (autor: reclutador-sofia)
```

### 6. Deshacer el último cambio

El ejecutor saca el último comando del historial, lo deshace y publica un `CambioRevertido`. El candidato vuelve a **OFERTA** sin volver a validar la transición (deshacer no es un movimiento de negocio).

Reclutador y portal actualizan. Gerente y nómina no reaccionan al evento de reversión.

```
Deshaciendo el último cambio...
[Portal] Restaurar estado OFERTA para el candidato c-001
[Reclutador] Se deshizo el cambio de c-001. Estado restaurado: OFERTA
Estado tras deshacer: Candidato{id='c-001', estado=OFERTA}
```

## Qué se está demostrando

- **Command:** cada cambio es un objeto (`CambiarEstadoCommand`) con candidato, estado nuevo, estado anterior y autor. Por eso se puede guardar, auditar y deshacer.
- **State:** las reglas “de qué estado se puede pasar a cuál” viven en cada estado. Agregar un estado nuevo no obliga a tocar un `if/else` central.
- **Observer:** reclutamiento, gerencia, nómina y el portal se enteran sin que el ejecutor sepa quiénes son.
- **Evento de dominio:** `EstadoCambiado` y `CambioRevertido` son el puente. El comando los produce; el bus los consume.
- **Dependency Inversion:** el ejecutor solo conoce `PublicarEventos`. Si mañana el bus en memoria se cambia por un sistema de mensajería, `EjecutorComandos` no se entera.

## Transiciones que usa el ejemplo

```
APLICADO        → ENTREVISTA | RECHAZADO
ENTREVISTA      → PRUEBA_TECNICA | REFERENCIA | OFERTA | RECHAZADO
PRUEBA_TECNICA  → REFERENCIA | OFERTA | RECHAZADO
REFERENCIA      → OFERTA | RECHAZADO
OFERTA          → CONTRATADO | RECHAZADO
CONTRATADO      → (terminal)
RECHAZADO       → (terminal)
```

El salto que la demo intenta a propósito (`ENTREVISTA` → `CONTRATADO`) no está permitido.

## Código del ejemplo

La demostración está en `src/main/java/com/hirecore/hirecore/demostracion/DemostracionArquitectura.java`.
El equivalente de lo que corre al arrancar es:

```java
Candidato candidato = new Candidato("c-001", factory.crear("APLICADO"));

ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "ENTREVISTA", "reclutador-sofia", factory));
ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "CONTRATADO", "reclutador-sofia", factory)); // falla
ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "PRUEBA_TECNICA", "reclutador-sofia", factory));
ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "OFERTA", "reclutador-sofia", factory));
ejecutor.ejecutar(new CambiarEstadoCommand(candidato, "CONTRATADO", "reclutador-sofia", factory));
ejecutor.deshacer();
```
