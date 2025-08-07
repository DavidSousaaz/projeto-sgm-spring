package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

import java.util.List;

@Data
public class MonitoriaResponseDTO {

    private Long id;
    private DisciplinaResponseDTO disciplinaResponseDTO;
    private int numeroVaga;
    private int numeroVagaBolsa;
    private int cargaHoraria;
    private ProfessorResponseDTO professorResponseDTO;
    private List<MonitoriaInscritosResponseDTO> monitoriaInscritosResponseDTO;
    private ProcessoSeletivoResponseDTO processoSeletivoResponseDTO;

}
