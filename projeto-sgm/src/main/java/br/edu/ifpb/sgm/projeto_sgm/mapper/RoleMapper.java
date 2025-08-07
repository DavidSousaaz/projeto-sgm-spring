package br.edu.ifpb.sgm.projeto_sgm.mapper;

import br.edu.ifpb.sgm.projeto_sgm.dto.RoleRequestDTO;
import br.edu.ifpb.sgm.projeto_sgm.dto.RoleResponseDTO;
import br.edu.ifpb.sgm.projeto_sgm.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleRequestDTO dto);

    RoleResponseDTO toDTO(Role role);

    List<RoleResponseDTO> toDTOList(List<Role> roles);

    void updateEntityFromDTO(RoleRequestDTO dto, @MappingTarget Role role);

}
