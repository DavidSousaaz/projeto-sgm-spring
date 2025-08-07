package br.edu.ifpb.sgm.projeto_sgm.exception;

public class AtividadeNotFoundException extends RuntimeException {

    public AtividadeNotFoundException() {
        super("Atividade não encontrada.");
    }

    public AtividadeNotFoundException(String message) {
        super(message);
    }
}
