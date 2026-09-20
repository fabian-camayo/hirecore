# Supuestos

La conversación con RRHH confirma la intención, no el detalle. Aquí se declara qué se asumió donde no hay certeza, y qué pieza de la arquitectura absorbe esa incertidumbre para no tener que reescribir el flujo principal cuando el negocio concrete la regla.

## 1. Alcance de “deshacer”

**Qué no se sabe.** RRHH pide revertir el último cambio (un rechazo aplicado al candidato equivocado). Desarrollo pregunta si es solo el último o cualquier punto del historial. La respuesta: *“con el último nos basta por ahora, pero no me sorprendería que más adelante pidamos poder ir más atrás.”*

**Qué se asumió.** El requisito vigente es deshacer **solo el último cambio**. El diseño, sin embargo, no implementa un “deshacer de un solo nivel”: cada cambio se convierte en un comando con el estado anterior, y el ejecutor los apila en un historial.

**Qué resuelve la arquitectura.** Command + historial separan *usar* deshacer de *cómo se guarda*. Hoy `EjecutorComandos.deshacer()` saca el último comando y lo revierte. Si RRHH pide ir más atrás, se desapilan más comandos sobre la misma pila: no hay que rediseñar `Candidato`, ni los estados, ni las notificaciones. El evento `CambioRevertido` ya existe para avisar cuando algo se restaura.

## 2. Cuándo le interesa un cambio al gerente

**Qué no se sabe.** RRHH dice que el gerente *“solo le importa cuando ya hay algo concreto que decidir, no los pasos intermedios”*. No define qué cuenta como “concreto”.

**Qué se asumió.** Hay algo concreto que decidir en dos momentos:

- **OFERTA** — hay que aprobar o no enviar una oferta.
- **CONTRATADO** — hay que confirmar el cierre de la vacante.

Los pasos intermedios (aplicado, entrevista, prueba técnica, referencias) son operativos del reclutador, no del gerente.

**Qué resuelve la arquitectura.** Observer: `NotificarGerente` filtra solo esos dos eventos. El ejecutor no conoce al gerente ni esa regla. Si “concreto” pasa a incluir, por ejemplo, el resultado de referencias, se cambia únicamente ese observador.

## 3. El proceso de selección todavía no está cerrado

**Qué no se sabe.** RRHH pide prueba técnica y verificación de referencias, y avisa que *“seguramente en unos meses agreguemos alguna más — todavía estamos definiendo el proceso completo.”* No hay lista cerrada de etapas ni de transiciones futuras.

**Qué se asumió.** El conjunto actual es:

```
APLICADO → ENTREVISTA | RECHAZADO
ENTREVISTA → PRUEBA_TECNICA | REFERENCIA | OFERTA | RECHAZADO
PRUEBA_TECNICA → REFERENCIA | OFERTA | RECHAZADO
REFERENCIA → OFERTA | RECHAZADO
OFERTA → CONTRATADO | RECHAZADO
CONTRATADO / RECHAZADO → (terminales)
```

Se asume además que se puede ir de entrevista a oferta (omitir prueba o referencias) porque RRHH no prohibió atajos entre etapas operativas; lo que sí queda prohibido es saltar a un cierre (`CONTRATADO`) sin pasar por `OFERTA`.

**Qué resuelve la arquitectura.** State: cada estado declara a cuáles otros puede pasar. El ejecutor y el comando no nombran etapas. Una etapa nueva es una clase nueva + su alta en `EstadoFactory` + las transiciones de los estados vecinos. No se reabre un `if/else` central como el de `GestorDeCandidato`.

## 4. Qué ve el candidato en el portal

**Qué no se sabe.** El candidato *“debería ver su propio progreso en el portal, aunque no todos los cambios — hay notas internas que no queremos que vea.”* No se describe el modelo de notas ni qué otros cambios serían invisibles.

**Qué se asumió.** El portal solo publica **cambios de estado** (`EstadoCambiado` y `CambioRevertido`). Las notas internas, si existieran, no viajarían en esos eventos y por tanto no llegarían a `ActualizarPortalCandidato`.

**Qué resuelve la arquitectura.** El portal es un observador más, no un acoplamiento dentro del cambio de estado. Ampliar o recortar lo visible (ocultar un rechazo interno, mostrar solo el estado vigente, etc.) se decide en ese observador, sin tocar el comando ni el ejecutor.

## 5. Quién se entera de qué

**Qué está confirmado.** El reclutador se entera de todo. Nómina, solo al confirmar la contratación.

**Qué se asumió.** “Todo” para el reclutador incluye tanto el cambio como el deshacer (necesita saber si se revirtió un rechazo por error). Nómina reacciona únicamente a `CONTRATADO`; no a una reversión, porque una oferta restaurada no es una alta.

**Qué resuelve la arquitectura.** Cada interesado es un `AvisarObserver`. El ejecutor publica un evento y no conoce destinatarios. Agregar o quitar un equipo (por ejemplo, Seguridad o Legal) no modifica el flujo de cambio de estado.

## 6. Auditoría: quién hizo qué y cuándo

**Qué no se sabe.** RRHH pide saber *quién hizo qué y cuándo* para el error de la semana pasada y para auditoría en general. No pide un módulo de auditoría, ni retención, ni un reporte.

**Qué se asumió.** Basta con que cada comando y cada evento carguen **autor**, **estado anterior**, **estado nuevo** y **momento** (`ocurridoEn`). El historial de comandos es la traza reversible; el evento es la traza publicable.

**Qué resuelve la arquitectura.** Esa información vive en `CambiarEstadoCommand` y en `EstadoCambiado` / `CambioRevertido`, no en el candidato. Un auditor futuro puede suscribirse al bus como un observador más, o leer el historial, sin cambiar cómo se transiciona.

## 7. Cómo se notifican realmente los equipos

**Qué no se sabe.** El código en producción manda correos con `EmailService` hardcodeado. RRHH habla de “enterarse”, no de canal (correo, Slack, bandeja interna).

**Qué se asumió.** El mecanismo de entrega es un detalle de cada observador. Hoy registran el aviso; mañana pueden enviar correo o mensaje sin que el ejecutor lo sepa.

**Qué resuelve la arquitectura.** `EjecutorComandos` depende de `PublicarEventos`, no de `BusEventos` ni de un servicio de correo. Cambiar el canal, o sustituir el bus en memoria por mensajería externa, no altera el comando, el estado ni el historial.
