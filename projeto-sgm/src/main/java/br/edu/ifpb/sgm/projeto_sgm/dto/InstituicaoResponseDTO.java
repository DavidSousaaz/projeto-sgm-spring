package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

@Data
public class InstituicaoResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String email;

}
