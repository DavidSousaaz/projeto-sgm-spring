package br.edu.ifpb.sgm.projeto_sgm.exception;

public class ProcessoSeletivoNotFoundException extends RuntimeException {

    public ProcessoSeletivoNotFoundException() {
        super("Processo seletivo não encontrado.");
    }

    public ProcessoSeletivoNotFoundException(String message) {
        super(message);
    }
}
