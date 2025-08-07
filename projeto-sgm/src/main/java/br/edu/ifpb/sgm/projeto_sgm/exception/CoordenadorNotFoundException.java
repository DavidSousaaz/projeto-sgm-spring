package br.edu.ifpb.sgm.projeto_sgm.exception;

public class CoordenadorNotFoundException extends RuntimeException {

    public CoordenadorNotFoundException() {
        super("Coordenador não encontrado.");
    }

    public CoordenadorNotFoundException(String message) {
        super(message);
    }
}
