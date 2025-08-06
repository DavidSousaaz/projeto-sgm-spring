package br.edu.ifpb.sgm.projeto_sgm.dto;

import br.edu.ifpb.sgm.projeto_sgm.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class PessoaResponseDTO {

    protected Long id;
    protected String cpf;
    protected String nome;
    protected String email;
    protected String emailAcademico;
    protected String matricula;
    protected InstituicaoResponseDTO instituicaoResponseDTO;
    protected List<RoleResponseDTO> roles;

}
