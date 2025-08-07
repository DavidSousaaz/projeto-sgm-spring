package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

import java.util.List;

@Data
public class MonitoriaRequestDTO {

    private Long disciplinaId;
    private int numeroVaga;
    private int numeroVagaBolsa;
    private int cargaHoraria;
    private Long professorId;
    private List<Long> inscricoesId;
    private Long processoSeletivoId;

}
