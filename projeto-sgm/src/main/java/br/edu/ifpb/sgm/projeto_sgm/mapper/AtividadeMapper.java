package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.AtividadeResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Atividade;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring",
        uses = {MonitoriaMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AtividadeMapper {

    Atividade toEntity(AtividadeRequestDTO atividadeRequestDTO);

    @Mapping(source = "monitoria", target = "monitoriaResponseDTO")
    AtividadeResponseDTO toResponseDTO(Atividade atividade);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAtividadeFromDto(AtividadeRequestDTO dto, @MappingTarget Atividade entity);
}
