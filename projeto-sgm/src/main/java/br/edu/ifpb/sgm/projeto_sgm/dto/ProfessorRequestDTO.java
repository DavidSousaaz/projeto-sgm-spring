package br.edu.ifpb.sgm.projeto_sgm.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProfessorRequestDTO {

    protected String cpf;
    protected String nome;
    protected String email;
    protected String emailAcademico;
    private String senha;
    protected String matricula;
    protected Long instituicaoId;
    private List<Long> disciplinasId;
    private Set<Long> cursosId;
}
