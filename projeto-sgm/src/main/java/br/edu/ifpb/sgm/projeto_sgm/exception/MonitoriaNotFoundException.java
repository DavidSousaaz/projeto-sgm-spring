package br.edu.ifpb.sgm.projeto_sgm.exception;

public class MonitoriaNotFoundException extends RuntimeException {

    public MonitoriaNotFoundException() {
        super("Monitoria não encontrada.");
    }

    public MonitoriaNotFoundException(String message) {
        super(message);
    }
}
