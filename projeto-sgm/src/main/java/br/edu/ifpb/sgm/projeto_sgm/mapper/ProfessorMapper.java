package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProfessorResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Professor;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {DisciplinaMapper.class, CursoMapper.class, InstituicaoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProfessorMapper {

    @Mapping(target = "pessoa", ignore = true)
    Professor toEntity(ProfessorRequestDTO professorRequestDTO);

    @Mapping(source = "pessoa.id", target = "id")
    @Mapping(source = "pessoa.cpf", target = "cpf")
    @Mapping(source = "pessoa.nome", target = "nome")
    @Mapping(source = "pessoa.email", target = "email")
    @Mapping(source = "pessoa.emailAcademico", target = "emailAcademico")
    @Mapping(source = "pessoa.matricula", target = "matricula")
    @Mapping(source = "pessoa.instituicao", target = "instituicaoResponseDTO")
    @Mapping(source = "disciplinas", target = "disciplinasResponseDTO")
    @Mapping(source = "cursos", target = "cursosResponseDTO")
    ProfessorResponseDTO toResponseDTO(Professor professor);

    @Mapping(target = "pessoa", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProfessorFromDto(ProfessorRequestDTO dto, @MappingTarget Professor entity);
}