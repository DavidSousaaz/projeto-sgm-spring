package br.edu.ifpb.sgm.projeto_sgm.exception;

public class CursoNotFoundException extends RuntimeException {

    public CursoNotFoundException() {
        super("Curso não encontrado.");
    }

    public CursoNotFoundException(String message) {
        super(message);
    }
}
