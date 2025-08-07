package br.edu.ifpb.sgm.projeto_sgm.exception;

public class MonitorNotFoundException extends RuntimeException {

    public MonitorNotFoundException() {
        super("Monitor não encontrado.");
    }

    public MonitorNotFoundException(String message) {
        super(message);
    }
}
