package br.edu.ifpb.sgm.projeto_sgm.exception;

public class DisciplinaNotFoundException extends RuntimeException {

    public DisciplinaNotFoundException() {
        super("Disciplina não encontrada.");
    }

    public DisciplinaNotFoundException(String message) {
        super(message);
    }
}
