package br.edu.ifpb.sgm.projeto_sgm.exception;

public class InstituicaoNotFoundException extends RuntimeException {

    public InstituicaoNotFoundException() {
        super("Instituicao não encontrada.");
    }

    public InstituicaoNotFoundException(String message) {
        super(message);
    }
}
