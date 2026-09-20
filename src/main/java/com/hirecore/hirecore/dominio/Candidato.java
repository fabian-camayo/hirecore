package com.hirecore.hirecore.dominio;

import com.hirecore.hirecore.dominio.estado.EstadoCandidato;

import java.util.Objects;

/**
 * Entidad cuyo estado no se muta “a mano” desde el resto del sistema:
 * el cambio llega a través de un comando, y las reglas las impone el estado actual.
 */
public class Candidato {

    private final String id;
    private EstadoCandidato estado;

    public Candidato(String id, EstadoCandidato estadoInicial) {
        this.id = Objects.requireNonNull(id, "id");
        this.estado = Objects.requireNonNull(estadoInicial, "estadoInicial");
    }

    public String getId() {
        return id;
    }

    public EstadoCandidato obtenerEstado() {
        return estado;
    }

    /**
     * Asigna el estado. No valida transiciones: eso lo hace {@link EstadoCandidato#avanzar}.
     * El deshacer del comando también usa este método para restaurar sin revalidar.
     */
    public void cambiarEstado(EstadoCandidato nuevoEstado) {
        this.estado = Objects.requireNonNull(nuevoEstado, "nuevoEstado");
    }

    @Override
    public String toString() {
        return "Candidato{id='%s', estado=%s}".formatted(id, estado.nombre());
    }
}
