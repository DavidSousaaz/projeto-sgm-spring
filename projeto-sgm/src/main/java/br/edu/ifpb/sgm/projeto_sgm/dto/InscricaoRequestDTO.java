package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.TipoVaga;
import lombok.Data;

@Data
public class InscricaoRequestDTO {
    private TipoVaga tipoVaga;
}