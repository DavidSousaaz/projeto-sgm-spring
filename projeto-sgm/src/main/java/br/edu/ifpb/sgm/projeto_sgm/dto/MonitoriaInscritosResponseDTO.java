package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.TipoVaga;
import br.edu.ifpb.sgm.projeto_sgm.model.embeddable.MonitoriaInscritoId;
import lombok.Data;

@Data
public class MonitoriaInscritosResponseDTO {

    private MonitoriaInscritoId id;
    private MonitoriaResponseDTO monitoriaResponseDTO;
    private AlunoResponseDTO alunoResponseDTO;
    private boolean selecionado;
    private TipoVaga tipoVaga;
}
