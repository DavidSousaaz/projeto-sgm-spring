package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.CursoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.CursoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Curso;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {InstituicaoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CursoMapper {

    @Mapping(target = "instituicao", ignore = true)
    Curso toEntity(CursoRequestDTO CursoRequestDTO);

    @Mapping(source = "instituicao", target = "instituicaoResponseDTO")
    CursoResponseDTO toResponseDTO(Curso curso);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCursoFromDto(CursoRequestDTO dto, @MappingTarget Curso entity);
}
