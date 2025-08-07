package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.ProcessoSeletivoResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.ProcessoSeletivo;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {InstituicaoMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProcessoSeletivoMapper {

    ProcessoSeletivo toEntity(ProcessoSeletivoRequestDTO processoSeletivoRequestDTO);

    @Mapping(source = "instituicao", target = "instituicaoResponseDTO")
    ProcessoSeletivoResponseDTO toResponseDTO(ProcessoSeletivo processoSeletivo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProcessoSeletivoFromDto(ProcessoSeletivoRequestDTO dto, @MappingTarget ProcessoSeletivo entity);
}
