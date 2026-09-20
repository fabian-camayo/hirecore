package com.hirecore.hirecore.comando;

import com.hirecore.hirecore.dominio.excepcion.HistorialVacioException;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Pila de comandos ya ejecutados, para poder deshacerlos en orden inverso.
 */
@Component
public class HistorialComandos {

    private final Deque<ComandoCandidato> comandos = new ArrayDeque<>();

    public void registrar(ComandoCandidato comando) {
        comandos.push(comando);
    }

    public ComandoCandidato deshacer() {
        if (vacio()) {
            throw new HistorialVacioException();
        }
        return comandos.pop();
    }

    public boolean vacio() {
        return comandos.isEmpty();
    }

    public int tamanio() {
        return comandos.size();
    }
}
