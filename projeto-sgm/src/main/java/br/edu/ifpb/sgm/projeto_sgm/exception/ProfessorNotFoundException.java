package br.edu.ifpb.sgm.projeto_sgm.exception;

public class ProfessorNotFoundException extends RuntimeException {

    public ProfessorNotFoundException() {
        super("Professor não encontrado.");
    }

    public ProfessorNotFoundException(String message) {
        super(message);
    }
}
