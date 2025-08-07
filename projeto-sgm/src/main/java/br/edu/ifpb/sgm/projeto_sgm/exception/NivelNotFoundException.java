package br.edu.ifpb.sgm.projeto_sgm.exception;

public class NivelNotFoundException extends RuntimeException {

    public NivelNotFoundException() {
        super("Nivel não encontrado.");
    }

    public NivelNotFoundException(String message) {
        super(message);
    }
}
