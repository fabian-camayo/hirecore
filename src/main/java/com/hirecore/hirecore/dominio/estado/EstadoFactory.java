package com.hirecore.hirecore.dominio.estado;

import org.springframework.stereotype.Component;

@Component
public class EstadoFactory {

    public EstadoCandidato crear(String nombre) {
        return crear(NombreEstado.desde(nombre));
    }

    public EstadoCandidato crear(NombreEstado nombre) {
        return switch (nombre) {
            case APLICADO -> new EstadoAplicado();
            case ENTREVISTA -> new EstadoEntrevista();
            case REFERENCIA -> new EstadoReferencia();
            case PRUEBA_TECNICA -> new EstadoPruebaTecnica();
            case OFERTA -> new EstadoOferta();
            case CONTRATADO -> new EstadoContratado();
            case RECHAZADO -> new EstadoRechazado();
        };
    }
}
