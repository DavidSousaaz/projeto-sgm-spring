package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.StatusAtividade;
import lombok.Data;

@Data
public class AtualizacaoStatusDTO {
    private StatusAtividade status;
}