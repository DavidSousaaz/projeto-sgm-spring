package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.DisciplinaResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Disciplina;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {CursoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DisciplinaMapper {

    Disciplina toEntity(DisciplinaRequestDTO disciplinaRequestDTO);

    @Mapping(source = "curso", target = "cursoResponseDTO")
    DisciplinaResponseDTO toResponseDTO(Disciplina disciplina);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDisciplinaFromDto(DisciplinaRequestDTO dto, @MappingTarget Disciplina entity);
}
