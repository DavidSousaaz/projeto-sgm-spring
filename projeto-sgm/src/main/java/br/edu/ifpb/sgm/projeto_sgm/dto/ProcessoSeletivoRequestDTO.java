package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessoSeletivoRequestDTO {

    private LocalDate inicio;
    private LocalDate fim;
    private String numero;
    private Long instituicaoId;

}
